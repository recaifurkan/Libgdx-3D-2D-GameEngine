package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.game.engine.components.g3d.BulletCollisionComponent;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.utils.ObjectAllocator;

public class BulletRigidInitializer extends BulletInitilizer {

    public btRigidBody rigidBody;
    protected BulletRigidBodyComponent bulletRigidBodyComponent;

    public BulletRigidInitializer(Matrix4 transform, float mass, btCollisionShape collisionShape) {
        Vector3 localInertia = ObjectAllocator.getObject(Vector3.class);

        if (mass > 0f) {
            collisionShape.calculateLocalInertia(mass, localInertia);
        } else {
            localInertia.set(0, 0, 0);
        }
//        BulletCollisionComponent.MotionState motionState = new BulletCollisionComponent.MotionState(transform);
        btRigidBody.btRigidBodyConstructionInfo bodyInfo = new
                btRigidBody.btRigidBodyConstructionInfo(mass, null, collisionShape, localInertia);
        rigidBody = new btRigidBody(bodyInfo);
        rigidBody.setWorldTransform(transform);
        bulletRigidBodyComponent = new BulletRigidBodyComponent(rigidBody);
    }

    @Override
    public void initialize(GameEntity object) {
        rigidBody.userData = object;
        object.add(bulletRigidBodyComponent);
    }
}
