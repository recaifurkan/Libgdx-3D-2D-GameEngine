package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.game.engine.entites.GameEntity;

public class BulletStaticInitializer extends BulletRigidInitializer {
    public BulletStaticInitializer(Matrix4 transform, btCollisionShape collisionShape) {

        super(transform, 0, collisionShape);

        rigidBody.setCollisionFlags(rigidBody.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);


    }

    @Override
    public void initialize(GameEntity object) {
        super.initialize(object);

    }
}
