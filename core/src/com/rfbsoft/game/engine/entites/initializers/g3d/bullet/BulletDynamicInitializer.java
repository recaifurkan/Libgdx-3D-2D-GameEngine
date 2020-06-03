package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.game.engine.entites.GameEntity;

public class BulletDynamicInitializer extends BulletRigidInitializer {
    public BulletDynamicInitializer(Matrix4 transform,
                                    float mass,
                                    btCollisionShape collisionShape) {
        super(transform, mass, collisionShape);
        rigidBody.setCollisionFlags(rigidBody.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);


    }

    @Override
    public void initialize(GameEntity object) {
        super.initialize(object);
    }
}
