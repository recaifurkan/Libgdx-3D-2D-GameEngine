package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.game.engine.entites.GameEntity;

public class BulletSensorInitializer extends BulletCollisionInitializer {

    btCollisionObject collisionObject;

    public BulletSensorInitializer(Matrix4 transform, btCollisionShape shape) {

        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(shape);


        collisionObject.setWorldTransform(transform);
        collisionObject.setCollisionFlags(collisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);

        collisionComponent.collisionObject = collisionObject;
    }

    @Override
    public void initialize(GameEntity object) {
        super.initialize(object);
        collisionObject.userData = object;

    }
}
