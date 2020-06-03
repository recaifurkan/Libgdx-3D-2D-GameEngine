package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimationComponent implements Component {
    public AnimationController animationController;

    public AnimationComponent(AnimationController animationController) {
        this.animationController = animationController;
    }

    public static class AnimationStateComponent implements Component {
        public StateMachine stateMachine;

        public AnimationStateComponent(StateMachine stateMachine) {
            this.stateMachine = stateMachine;
        }
    }
}
