package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameInput;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.components.g3d.BulletCollisionComponent;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.systems.RayRequest;
import com.rfbsoft.utils.ObjectAllocator;

/**
 * Created by scanevaro on 22/09/2015.
 */
public class BulletSystem extends EntitySystem implements EntityListener, Telegraph, Disposable {
    private static final String TAG = "com.rfbsoft.systems.BulletSystem";
    public static RayRequest rayRequest = RayRequest.IDLE;
    public static ClosestRayResultCallback rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
    public static btDiscreteDynamicsWorld collisionWorld;
    public final btCollisionConfiguration collisionConfig;
    public final btCollisionDispatcher dispatcher;
    public final btBroadphaseInterface broadphase;
    public final btConstraintSolver solver;

    private final ComponentMapper<BulletCollisionComponent> collisionMapper = ComponentMapper.getFor(BulletCollisionComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidMapper =
            ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private final int maxSubSteps = 5;
    private final float fixedTimeStep = 1f / 60f;
    GameContactListener contactListener;
    private ImmutableArray<Entity> entities;

    public BulletSystem() {


        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new GameCollisionDispatcher(collisionConfig);
        broadphase = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
        collisionWorld.setGravity(new Vector3(0, -9.8f, 0));

        contactListener = new GameContactListener();


        MessageManager.getInstance().addListener(this, GameMessages.MOUSEEVENT);


    }

    public static ClosestRayResultCallback ray(int x, int y) {
        rayRequest = RayRequest.PROCESSING;
        Vector3 rayFrom = ObjectAllocator.getObject(Vector3.class);
        Vector3 rayTo = ObjectAllocator.getObject(Vector3.class);


        Ray ray = GameFields.cam.getPickRay(x, y);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(1000f).add(rayFrom); // 50 meters max from the origin

        // Because we reuse the ClosestRayResultCallback, we need reset it's values
        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);

        collisionWorld.rayTest(rayFrom, rayTo, rayTestCB);
        ObjectAllocator.freeAllInPool();

        rayRequest = RayRequest.IDLE;
        if (rayTestCB.hasHit()) {
            return rayTestCB;
        }
        return null;

    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.one(BulletCollisionComponent.class,
                BulletRigidBodyComponent.class).get();
        entities = engine.getEntitiesFor(family);
        engine.addEntityListener(family, this);
    }

    @Override
    public boolean handleMessage(Telegram msg) {

        if (msg.message == GameMessages.MOUSEEVENT) {
            GameInput.MouseEventObject mouseEvent = (GameInput.MouseEventObject) msg.extraInfo;
            Gdx.app.debug(TAG,String.format("Clicked Button = %d" , mouseEvent.button));
            if (mouseEvent.button == Input.Buttons.RIGHT) rayRequest = RayRequest.REQUESTED;
            if (rayRequest == RayRequest.REQUESTED) {
                if (ray(mouseEvent.screenX, mouseEvent.screenY) != null)
                    MessageManager.getInstance().dispatchMessage(GameMessages.RAYRESULT, rayTestCB);

            }

            return true;
        }

        return false;
    }

    @Override
    public void update(float deltaTime) {

        collisionWorld.stepSimulation(deltaTime, maxSubSteps, fixedTimeStep);

    }

    public void dispose() {

        collisionConfig.dispose();
        dispatcher.dispose();
        broadphase.dispose();
        solver.dispose();

        collisionWorld.dispose();
        contactListener.dispose();

    }

    @Override
    public void entityAdded(Entity entity) {
//    	RigidBodyComponent  rigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
        BulletRigidBodyComponent bulletComponent = rigidMapper.get(entity);

        if (bulletComponent != null) {
            if (bulletComponent.rigidBody == null) {
                Gdx.app.error(TAG, "RigidBody Null");
                return;
            }
            collisionWorld.addRigidBody(bulletComponent.rigidBody);
            return;
        }
        BulletCollisionComponent bulletCollisionComponent = collisionMapper.get(entity);
        if (bulletCollisionComponent.collisionObject == null) {
            Gdx.app.error(TAG, "CollisionObject Null");
            return;
        }
        collisionWorld.addCollisionObject(bulletCollisionComponent.collisionObject);


    }

    @Override
    public void entityRemoved(Entity entity) {
//    	RigidBodyComponent  rigidBodyComponent = entity.getComponent(RigidBodyComponent.class);

        BulletRigidBodyComponent bulletComponent = rigidMapper.get(entity);

        if (bulletComponent != null) {
            if (bulletComponent.rigidBody == null) {
                Gdx.app.error(TAG, "RigidBody Null");
                return;
            }
            collisionWorld.removeRigidBody(bulletComponent.rigidBody);
            return;
        }
        BulletCollisionComponent bulletCollisionComponent = collisionMapper.get(entity);
        if (bulletCollisionComponent.collisionObject == null) {
            Gdx.app.error(TAG, "CollisionObject Null");
            return;
        }
        collisionWorld.addCollisionObject(bulletCollisionComponent.collisionObject);


    }


    public static class GameCollisionDispatcher extends CustomCollisionDispatcher {
        public GameCollisionDispatcher(btCollisionConfiguration collisionConfiguration) {
            super(collisionConfiguration);
        }

        @Override
        public boolean needsCollision(btCollisionObject colObj0, btCollisionObject colObj1) {

            if (colObj0.userData instanceof GameEntity && colObj1.userData instanceof GameEntity) {

                return super.needsCollision(colObj0, colObj1);

//                logger.debug("Collisipn Started");


            }

            return false;
        }

        @Override
        public boolean needsResponse(btCollisionObject colObj0, btCollisionObject colObj1) {
            if (colObj0.userData instanceof GameEntity && colObj1.userData instanceof GameEntity) {

                return super.needsResponse(colObj0, colObj1);

//                logger.debug("Collisipn Started");


            }

            return false;
        }
    }

    public class GameContactListener extends ContactListener {

        @Override
        public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
            GameEntity entity0 = (GameEntity) colObj0.userData;
            GameEntity entity1 = (GameEntity) colObj1.userData;


            entity0.getPhysics().onCollisionStart(entity1);
            entity1.getPhysics().onCollisionStart(entity0);


        }

        @Override
        public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1) {
            if (colObj0.userData instanceof GameEntity && colObj1.userData instanceof GameEntity) {
                GameEntity entity0 = (GameEntity) colObj0.userData;
                GameEntity entity1 = (GameEntity) colObj1.userData;

                entity0.getPhysics().onCollisionEnd(entity1);
                entity1.getPhysics().onCollisionEnd(entity0);


//                logger.debug("Collisipn Ended");


            }
        }


    }

}