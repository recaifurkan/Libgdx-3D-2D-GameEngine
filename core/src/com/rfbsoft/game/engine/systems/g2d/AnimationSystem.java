package com.rfbsoft.game.engine.systems.g2d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rfbsoft.game.engine.components.g2d.AnimationComponent;
import com.rfbsoft.game.engine.components.g2d.SpriteComponent;

public class AnimationSystem extends EntitySystem {

    private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, AnimationComponent.class).get());
    }
    enum CharAnims{
        IDLE("idle"),
        WALK("walk");
        public String name;

        CharAnims(String name) {
            this.name = name;
        }
    }
    CharAnims anim = CharAnims.IDLE;

    float elapsed;
    float waitTime = 5;
    @Override
    public void update(float deltaTime) {
        elapsed+=deltaTime;

        for (Entity entity : entities) {
            AnimationComponent animationComponent = animationMapper.get(entity);

            if(elapsed > waitTime){
                elapsed = 0;
                if(anim == CharAnims.IDLE){
                    animationComponent.animationController.setAnimation(CharAnims.WALK.name);
                    anim = CharAnims.WALK;
                }

                else{
                    animationComponent.animationController.setAnimation(CharAnims.IDLE.name);
                    anim = CharAnims.IDLE;
                }


            }

            AnimationComponent.Animation<TextureRegion> currentAnimation = animationComponent.animationController.currentAnimation;
            currentAnimation.elapsed+=deltaTime;

            TextureRegion keyFrame = currentAnimation.getKeyFrame(currentAnimation.elapsed, true);
            SpriteComponent spriteComponent = spriteMapper.get(entity);

            spriteComponent.instances.get(0).setRegion(keyFrame);

        }
//        TextureRegion keyFrame = animation
//
//        Sprite sprite = new Sprite(keyFrame);
//        sprite.setRegion(keyFrame);
    }
}
