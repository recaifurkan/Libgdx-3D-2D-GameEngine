package com.rfbsoft.factories.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.rfbsoft.assets.Assets;
import com.rfbsoft.factories.IEntityFactory;
import com.rfbsoft.factories.g3d.character.Character;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.g3d.ModelledBulletDynamicInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.ModelledBulletStaticInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.PathFindInitiliazer;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletCarInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletSensorInitializer;
import com.rfbsoft.utils.ObjectAllocator;
import com.rfbsoft.utils.g3d.BulletUtils;
import org.recast4j.detour.NavMesh;

public class G3dEntityFactory implements IEntityFactory {
    private static final String TAG = "com.rfbsoft.factories.EntityFactory";
    static ModelBuilder modelBuilder = new ModelBuilder();
    private static int playerIndex = 0;
    private static int characterIndex = 0;

    public static GameEntity createTerrain() {

        Model model = Assets.terrainModel.get();
        NavMesh navMesh = Assets.navMesh.get();
        ModelInstance instance = new ModelInstance(model);
        Vector3 vector = ObjectAllocator.getObject(Vector3.class);
        vector.set(0, 0, 0);
        instance.transform.setTranslation(vector);
        btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(model.nodes);
//        btStaticPlaneShape btStaticPlaneShape = new btStaticPlaneShape(Vector3.Y,1);
//        btCompoundShape compoundShape = new btCompoundShape();
//        compoundShape.addChildShape(instance.transform,btStaticPlaneShape);
//        compoundShape.addChildShape(instance.transform,collisionShape);


        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();
        collisionShapeTransform.set(instance.transform);
        ModelledBulletStaticInitializer modelledBulletStaticInitializer = new ModelledBulletStaticInitializer(instance, collisionShape, collisionShapeTransform);
//        collisionShapeTransform.rotate(Vector3.X, -90);
        GameEntity terrainObject = new GameEntity("Terrain"
                ,modelledBulletStaticInitializer,
                new PathFindInitiliazer(navMesh)
        );


//

        ObjectAllocator.freeAllInPool();

        return terrainObject;


    }

    public static GameEntity createPlayer() {

        float half = 2.5f;


        Model model = modelBuilder.
                createSphere(half * 2, half * 2, half * 2, 5, 5,
                        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance instance = new ModelInstance(model);
        instance.transform.setTranslation(0, 10, 0);

        btSphereShape btSphereShape = BulletUtils.modelToSphereShape(model);
        GameEntity player = new GameEntity("Player  - " + playerIndex++,
                new ModelledBulletDynamicInitializer(
                        instance,
                        0.1f,
                        btSphereShape)) {
            @Override
            public void onCollisionStart(GameEntity entity) {
                if (entity == G3dEntites.terrain) {
                    BulletRigidBodyComponent component = this.getComponent(BulletRigidBodyComponent.class);
                    double random, random1;
                    random = Math.random();
                    random1 = Math.random();

                    random -= 0.5;
                    random *= 20;

                    random1 -= 0.5;
                    random1 *= 20;

                    Vector3 impulse = ObjectAllocator.getObject(Vector3.class);
                    impulse.set((float) random, 0.1f, (float) random1);

                    component.rigidBody.applyCentralImpulse(impulse);
                    ObjectAllocator.freeAllInPool();

                }
            }
        };
        return player;
    }

    public static GameEntity createSensor() {


        Vector3 coord = ObjectAllocator.getObject(Vector3.class);
        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(coord.set(0, 2, 20), quaternion, scale.set(1, 1, 1));
        Vector3 boxVector = ObjectAllocator.getObject(Vector3.class);
        btBoxShape boxShape = new btBoxShape(boxVector.set(2, 2, 2));
        GameEntity sensor = new GameEntity("Sensor",
                new BulletSensorInitializer(tranform, boxShape)) {
            @Override
            public void onCollisionStart(GameEntity entity) {
                Gdx.app.debug(TAG, String.format(
                        "Collision From Sensor With = %s", entity.getName()
                ));

            }
        };


        ObjectAllocator.freeAllInPool();

        return sensor;


    }

    public static GameEntity createCharacter() {
        Model model = Assets.characterModel.get();
        ModelInstance instance = new ModelInstance(model);
        double random = Math.random();
        random -= 0.5;
        random *= 10;
        instance.transform.setTranslation(30, 5, 0);
        btCollisionShape shape = BulletUtils.modelToCapsuleShape(model);
        Character character = new Character(instance, shape);
        character.setName("Animated - " + characterIndex++);

        return character;
    }

    public static GameEntity createCharacter(Vector3 coord) {
        Model model = Assets.characterModel.get();
        ModelInstance instance = new ModelInstance(model);
        coord.y = coord.y + 3.5f;
        instance.transform.setTranslation(coord);
        btCollisionShape shape = BulletUtils.modelToCapsuleShape(model);
        Character character = new Character(instance, shape);
        character.setName("Animated - " + characterIndex++);

        return character;
    }

    public static GameEntity createCar() {
        Model carModel = Assets.carModel.get();
        Model wheelModel = Assets.wheelModel.get();
        BulletCarInitializer carInitializer = new BulletCarInitializer(carModel, wheelModel);
        GameEntity car = new GameEntity("Car", carInitializer);
        return car;


    }
}
