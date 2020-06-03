package com.rfbsoft.factories.g3d.character;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.AnimatedInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.ModelInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.ModelledBulletDynamicInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.SteeringBulletInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletRigidInitializer;
import com.rfbsoft.utils.ObjectAllocator;

public class Character extends GameEntity {
    // An instance of the state machine class

    static float mass = 4f;
    AnimatedInitializer animatedInitializer;
    ModelInitializer modelInitializer;
    BulletRigidInitializer bulletCollisionInitializer;
    SteeringBulletInitializer steeringBulletInitializer;


    public Character(ModelInstance instance, btCollisionShape shape) {
        animatedInitializer = new AnimatedInitializer(instance,
                new DefaultStateMachine(this));
        ModelledBulletDynamicInitializer modelledBulletDynamicObject =
                new ModelledBulletDynamicInitializer(instance, mass, shape);

        IEntityInitializer.initialize(this, animatedInitializer, modelledBulletDynamicObject);
        modelInitializer = modelledBulletDynamicObject.model;
        bulletCollisionInitializer = modelledBulletDynamicObject.physics;
//        Vector3 vector3 = ObjectAllocator.getObject(Vector3.class);
        btRigidBody rigidBody = bulletCollisionInitializer.rigidBody;
//        rigidBody.setAngularFactor(vector3.set(0, 1, 0));

        rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        steeringBulletInitializer = new SteeringBulletInitializer(rigidBody
        );
        steeringBulletInitializer.initialize(this);
        ObjectAllocator.freeAllInPool();


        animatedInitializer.animationStateComponent.stateMachine.changeState(CharacterAnimationState.WALK);


    }

}
