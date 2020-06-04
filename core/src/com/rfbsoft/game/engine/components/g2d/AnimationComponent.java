package com.rfbsoft.game.engine.components.g2d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AnimationComponent implements Component {
    public static class AnimationController{
        Array<Animation<TextureRegion>> animations = new Array<>();
        public Animation<TextureRegion> currentAnimation;

        public AnimationController(Animation<TextureRegion>... animations) {
            for(Animation animation : animations){
                this.animations.add(animation);
            }
            currentAnimation = this.setAnimation(0);


        }

        public Animation<TextureRegion> setAnimation(String name){
            for(Animation animation : animations){
                if(animation.name.equals(name)){
                    animation.elapsed = 0;
                    this.currentAnimation = animation;
                    return animation;
                }
            }
            throw new GdxRuntimeException("Unknown animation: " + name);



        }
        public Animation<TextureRegion> setAnimation(int index){
            if(index > animations.size - 1)
                throw new GdxRuntimeException("Unknown animation index: " + index);
            Animation<TextureRegion> textureRegionAnimation = animations.get(index);
            textureRegionAnimation.elapsed = 0;
            this.currentAnimation = textureRegionAnimation;
            return textureRegionAnimation;


        }


    }

    public static class Animation<T> extends com.badlogic.gdx.graphics.g2d.Animation<T>{
        public String name;
        public float elapsed = 0;

        public Animation(String name ,float fps, Array<? extends T> keyFrames) {
            super(1/fps, keyFrames);
            this.name = name;

        }

        public Animation(String name ,float frameDuration, Array<? extends T> keyFrames, PlayMode playMode) {
            super(frameDuration, keyFrames, playMode);
            this.name = name;
        }

        public Animation(String name ,float fps, T... keyFrames) {
            super(1/fps, keyFrames);
            this.name = name;
        }

    }


    public AnimationController animationController;


//    public AnimationComponent(float fps, TextureAtlas atlas) {
//        Animation<TextureRegion> animation = new Animation(1f / fps, atlas.getRegions());
//        animationController = new AnimationController(animation);
//        currentAnimation = animation;
//
//    }

    public AnimationComponent(Animation... animations) {
        animationController = new AnimationController(animations);




    }
}
