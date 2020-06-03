package com.rfbsoft.game.engine.entites.initializers.g3d;


import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.rfbsoft.game.engine.components.g3d.AnimationComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;


public class AnimatedInitializer implements IEntityInitializer {


    public AnimationComponent animationComponent;
    public AnimationComponent.AnimationStateComponent animationStateComponent;


    public AnimatedInitializer(ModelInstance instance, StateMachine stateMachine) {


        animationComponent = new AnimationComponent(new AnimationController(instance));
        animationStateComponent = new AnimationComponent.AnimationStateComponent(
                stateMachine
        );
    }


    @Override
    public void initialize(GameEntity object) {
        object.add(animationComponent);
        object.add(animationStateComponent);

    }
}
