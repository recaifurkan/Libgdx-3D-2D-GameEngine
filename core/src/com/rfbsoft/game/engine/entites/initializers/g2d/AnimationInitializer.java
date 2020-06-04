package com.rfbsoft.game.engine.entites.initializers.g2d;

import com.rfbsoft.game.engine.components.g2d.AnimationComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;

public class AnimationInitializer implements IEntityInitializer {
    AnimationComponent controller;

    public AnimationInitializer(AnimationComponent.Animation... animations) {
        controller = new AnimationComponent(animations);
    }

    @Override
    public void initialize(GameEntity object) {
        object.add(controller);


    }
}
