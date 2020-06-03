package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.rfbsoft.game.engine.components.g3d.AnimationComponent;

public class AnimationSystem extends EntitySystem {
    private final ComponentMapper<AnimationComponent> mapper = ComponentMapper.getFor(AnimationComponent.class);
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AnimationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            AnimationComponent animationComponent = mapper.get(entity);
            animationComponent.animationController.update(deltaTime);

        }
    }

    public static class AnimationStateSystem extends EntitySystem {
        private final ComponentMapper<AnimationComponent.AnimationStateComponent> mapper = ComponentMapper.getFor(AnimationComponent.AnimationStateComponent.class);
        private ImmutableArray<Entity> entities;

        @Override
        public void addedToEngine(Engine engine) {
            entities = engine.getEntitiesFor(Family.all(AnimationComponent.AnimationStateComponent.class).get());

        }

        @Override
        public void update(float deltaTime) {
            for (Entity entity : entities) {
                AnimationComponent.AnimationStateComponent stateMachineComponent = mapper.get(entity);
                stateMachineComponent.stateMachine.update();
                //            stateMachineComponent.state.enter((GameEntity) entity);
            }
        }
    }
}
