package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class BulletRigidBodyComponent implements Component {
    public btRigidBody rigidBody;

    public BulletRigidBodyComponent(btRigidBody rigidBody) {
        this.rigidBody = rigidBody;
    }
}
