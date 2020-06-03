package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.rfbsoft.game.engine.components.g3d.CarComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.g3d.ModelInitializer;
import com.rfbsoft.game.engine.systems.g3d.BulletSystem;
import com.rfbsoft.utils.ObjectAllocator;

public class BulletCarInitializer extends BulletInitilizer {
    CarComponent carComponent;
    btDiscreteDynamicsWorld collisionWorld = BulletSystem.collisionWorld;
    //    ModelInitializer rigidInitializer;
    ModelInitializer modelInitializer;
    BulletRigidInitializer rigidInitializer;

    ModelInstance chasisInstance;
    ModelInstance[] wheelInstances = new ModelInstance[4];

    public BulletCarInitializer(Model chasisModel, Model wheelModel) {
        carComponent = new CarComponent();
        chasisInstance = new ModelInstance(chasisModel);

        wheelInstances[0] = new ModelInstance(wheelModel);
        wheelInstances[1] = new ModelInstance(wheelModel);
        wheelInstances[2] = new ModelInstance(wheelModel);
        wheelInstances[3] = new ModelInstance(wheelModel);
//
//        Matrix4 matrix4 = ObjectAllocator.getMatrix4();
//        matrix4.translate(10,2,10);

        BoundingBox bounds = ObjectAllocator.getObject(BoundingBox.class);
        Vector3 chassisHalfExtents = chasisModel.calculateBoundingBox(bounds).getDimensions(
                ObjectAllocator.getObject(Vector3.class)
        ).scl(0.5f);
        Vector3 wheelHalfExtents = wheelModel.calculateBoundingBox(bounds).getDimensions(
                ObjectAllocator.getObject(Vector3.class)
        ).scl(0.5f);
        /*
        İlk önce chase eklemen lazım
        çünkü 0. index dışındakiler Car systemde tekerlek olarak kullanılmakta

         */
        modelInitializer = new ModelInitializer(chasisInstance);
        modelInitializer.modelComponent.addInstances(wheelInstances);
        chasisInstance.transform.translate(0, 10, 0);
        rigidInitializer = new BulletRigidInitializer(chasisInstance.transform,
                40,
                new btBoxShape(chassisHalfExtents));


        btRigidBody body = rigidInitializer.rigidBody;
        btVehicleRaycaster raycaster = getRaycaster();
        btRaycastVehicle.btVehicleTuning tuning = new btRaycastVehicle.btVehicleTuning();
        btRaycastVehicle vehicle = new btRaycastVehicle(
                tuning,
                body,
                raycaster);
        carComponent.vehicle = vehicle;
        carComponent.tuning = tuning;
        carComponent.raycaster = raycaster;
        body.setActivationState(Collision.DISABLE_DEACTIVATION);

        carComponent.vehicle.setCoordinateSystem(0, 1, 2);


        btWheelInfo wheelInfo;
        Vector3 point = ObjectAllocator.getObject(Vector3.class);
        Vector3 direction = ObjectAllocator.getObject(Vector3.class).set(0, -1, 0);

        Vector3 axis = ObjectAllocator.getObject(Vector3.class).set(-1, 0, 0);

         /*

                ilk eklenen tekerlek arabanın sol ön tekerleğidir
                 */

        float measure = 0.3f;

        float vx = 0.9f;
        float vy = 0.8f;
        float vz = 0.7f;

        wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(vx, -vy, vz), direction, axis,
                wheelHalfExtents.z * measure, wheelHalfExtents.z, tuning, true);
        wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(-vx, -vy, vz), direction, axis,
                wheelHalfExtents.z * measure, wheelHalfExtents.z, tuning, true);
        wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(vx, -vy, -vz), direction, axis,
                wheelHalfExtents.z * measure, wheelHalfExtents.z, tuning, false);
        wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(-vx, -vy, -vz), direction, axis,
                wheelHalfExtents.z * measure, wheelHalfExtents.z, tuning, false);
    }

    protected btVehicleRaycaster getRaycaster() {

        return new btDefaultVehicleRaycaster(collisionWorld);
    }

    @Override
    public void initialize(GameEntity object) {
        modelInitializer.initialize(object);
        rigidInitializer.initialize(object);
        object.add(carComponent);


    }
}
