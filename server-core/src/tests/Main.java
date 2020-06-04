package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.rfbsoft.assets.Assets;

public class Main extends ApplicationAdapter {
    public btCollisionConfiguration collisionConfig;
    public btCollisionDispatcher dispatcher;
    public btBroadphaseInterface broadphase;
    public btConstraintSolver solver;
    // position of moving object
    float x = 0;
    float y = 500;

    // world and its drawer
    float z = 0;
    // used to roll the camera and see collisions better
    float camRoll = 1000;
    // moving object
    btCollisionObject moveableObject;
    DebugDrawer debugDrawer;
    PerspectiveCamera cam; // 3D camera
    Matrix4 transform; // used to position the object
    btDiscreteDynamicsWorld btWorld;
    btCollisionShape mazeShape;
    btCollisionObject levelModel;
    btCollisionShape shape;
    ContactListener myContactListener;
    CameraInputController cameraInputController;

    @Override
    public void create() {
        // SETUP
        Bullet.init();
        new Assets();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 3000f;
        cam.update();

        cameraInputController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(cameraInputController);

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        btWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);

        // MAKE AND REGISTER RENDERER
        debugDrawer = new DebugDrawer();
        btWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        // IMPORT THE 3D MODEL, ADD TO PHYSICS WORLD
        mazeShape = Bullet.obtainStaticNodeShape(Assets.terrainModel.get().nodes);
        levelModel = new btCollisionObject();
        levelModel.setCollisionShape(mazeShape);
        btWorld.addCollisionObject(levelModel);

        // CREATE BALL
        shape = new btSphereShape(200f);
        moveableObject = new btCollisionObject();
        moveableObject.setCollisionShape(shape);

        // set up its transform
        transform = new Matrix4(
                new Vector3(x, y, z),
                new Quaternion(),
                new Vector3(10, 10, 10));

        // add ball to world
        levelModel.setWorldTransform(transform);
        moveableObject.setWorldTransform(transform);
        btWorld.addCollisionObject(moveableObject);

        // setup contact listener
        myContactListener = new MyContactListener();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_POLYGON_OFFSET_FILL);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glPolygonOffset(1.0f, 1.0f);

        // INPUTS
        float speed = 30;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            z -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            z += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += speed;

        cameraInputController.update();
        //apply movement
        transform.set(new Vector3(x, y, z), new Quaternion());
        moveableObject.setWorldTransform(transform);

        // make camera follow object
        cam.position.set(x, y + camRoll, z + 1000);
        cam.lookAt(x, y, z);
        cam.update();

        // COLLISION CHECKS
        btWorld.stepSimulation(Gdx.graphics.getDeltaTime());

        //RENDER
        debugDrawer.begin(cam);
        btWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        moveableObject.dispose();
        debugDrawer.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();

        btWorld.dispose();
        mazeShape.dispose();
        levelModel.dispose();
        shape.dispose();
        myContactListener.dispose();

    }

    public static class MyContactListener extends ContactListener {

        @Override
        public void onContactStarted(
                btCollisionObject obj1,
                btCollisionObject obj2) {
            System.out.println("COLLIDE");
            System.out.println(obj1.getWorldTransform().getTranslation(new Vector3(0, 0, 0)));
            System.out.println(obj2.getWorldTransform().getTranslation(new Vector3(0, 0, 0)));
        }
    }
}