package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.game.engine.components.g3d.SteeringComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;

public class SteeringBulletInitializer implements IEntityInitializer {
    SteeringComponent steeringComponent;

    public SteeringBulletInitializer(btRigidBody collisionObject) {
        SteeringBulletEntity steeringBulletEntity = new SteeringBulletEntity(collisionObject);

        steeringBulletEntity.setMaxLinearAcceleration(50);
        steeringBulletEntity.setMaxLinearSpeed(5);
        steeringBulletEntity.setMaxAngularAcceleration(50);

        steeringBulletEntity.setMaxAngularSpeed(20);
        steeringBulletEntity.setIndependentFacing(true);


        steeringComponent = new SteeringComponent(steeringBulletEntity);
    }

    @Override
    public void initialize(GameEntity object) {
        object.add(steeringComponent);

    }
}
