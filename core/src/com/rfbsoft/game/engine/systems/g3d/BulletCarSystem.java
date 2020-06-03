package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.rfbsoft.game.engine.components.g3d.CarComponent;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;

import static com.rfbsoft.game.engine.systems.g3d.BulletSystem.collisionWorld;

public class BulletCarSystem extends EntitySystem implements EntityListener {
    private final ComponentMapper<CarComponent> carMapper =
            ComponentMapper.getFor(CarComponent.class);
    private final ComponentMapper<ModelComponent> modelMapper =
            ComponentMapper.getFor(ModelComponent.class);
    float maxForce = 200f;
    float currentForce = 0f;
    float acceleration = 50f; // force/second
    float maxAngle = 40f;
    float currentAngle = 0f;
    float steerSpeed = 30f; // angle/second
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {

        Family family = Family.all(CarComponent.class).get();
        entities = engine.getEntitiesFor(family);
        engine.addEntityListener(family, this);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            CarComponent carComponent = carMapper.get(entity);
            ModelComponent modelComponent = modelMapper.get(entity);
            for (int i = 0; i < carComponent.vehicle.getNumWheels(); i++) {
                carUpdate(deltaTime, carComponent.vehicle);
//                btWheelInfo wheelInfo = carComponent.vehicle.getWheelInfo(i);


                carComponent.vehicle.getWheelInfo(i).getWorldTransform().
                        getOpenGLMatrix(modelComponent.instances.get(i + 1).transform.val);
                /*

                ilk eklenen tekerlek arabanın sol ön tekerleğidir
                Sağ ve sol tekerleklerin instancesini çevirmemiz gerekiyor
                çünkü tek tön olursa terstir.
                 */
                if (i == 0 || i == 2)
                    modelComponent.instances.get(i + 1).transform.rotate(Vector3.Y, 180);
//                carComponent.wheelInstances[i].transform.rotate(Vector3.Y,90);

            }

//            GameFields.modelBatch.begin(GameFields.cam);
//            for(int i = 0 ; i < carComponent.vehicle.getNumWheels() ; i++){
//
//                GameFields.modelBatch.render(modelComponent.instances.get(i+1),GameFields.environment);
//            }
//            GameFields.modelBatch.end();


        }


    }

    private void carUpdate(float delta, btRaycastVehicle vehicle) {

        float angle = currentAngle;
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            if (angle > 0f) angle = 0f;
            angle = MathUtils.clamp(angle - steerSpeed * delta, -maxAngle, 0f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if (angle < 0f) angle = 0f;
            angle = MathUtils.clamp(angle + steerSpeed * delta, 0f, maxAngle);
        } else
            angle = 0f;
        if (angle != currentAngle) {
            currentAngle = angle;
            vehicle.setSteeringValue(angle * MathUtils.degreesToRadians, 0);
            vehicle.setSteeringValue(angle * MathUtils.degreesToRadians, 1);
        }

        float force = currentForce;
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            if (force < 0f) force = 0f;
            force = MathUtils.clamp(force + acceleration * delta, 0f, maxForce);
        } else if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            if (force > 0f) force = 0f;
            force = MathUtils.clamp(force - acceleration * delta, -maxForce, 0f);
        } else
            force = 0f;
        if (force != currentForce) {
            currentForce = force;
            vehicle.applyEngineForce(force, 0);
            vehicle.applyEngineForce(force, 1);
        }

    }

    @Override
    public void entityAdded(Entity entity) {
        CarComponent carComponent = carMapper.get(entity);
        collisionWorld.addVehicle(carComponent.vehicle);

    }

    @Override
    public void entityRemoved(Entity entity) {
        CarComponent carComponent = carMapper.get(entity);
        collisionWorld.removeVehicle(carComponent.vehicle);

    }
}


