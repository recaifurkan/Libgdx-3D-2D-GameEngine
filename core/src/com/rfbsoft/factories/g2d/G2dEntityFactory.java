package com.rfbsoft.factories.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rfbsoft.factories.IEntityFactory;
import com.rfbsoft.game.engine.components.g2d.AnimationComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.g2d.AnimationInitializer;
import com.rfbsoft.game.engine.entites.initializers.g2d.SpriteInitializer;
import com.rfbsoft.game.engine.entites.initializers.g2d.box2d.Box2dDynamicInitializer;
import com.rfbsoft.game.engine.entites.initializers.g2d.box2d.Box2dStaticInitiliazer;
import com.rfbsoft.utils.ObjectAllocator;

public class G2dEntityFactory implements IEntityFactory {
    private static final String TAG = "com.rfbsoft.factories.g2d.G2dEntityFactory";

    public static GameEntity createCharacter() {


        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("g2d/char/idle/idle.atlas"));
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal("g2d/char/walk/walk.atlas"));
        Sprite name = idleAtlas.createSprite("0001");
        SpriteInitializer spriteInitializer =
                new SpriteInitializer(name);

        PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
        Rectangle boundingRectangle = name.getBoundingRectangle();

        groundBox.setAsBox(boundingRectangle.width / 2, boundingRectangle.height / 2);

//        CircleShape circle = new CircleShape();
//        circle.setRadius(6f);
        Matrix3 matrix3 = ObjectAllocator.getMatrix3();
        matrix3.setToTranslation(0, 0);
        Box2dDynamicInitializer box2dDynamicInitializer = new
                Box2dDynamicInitializer(matrix3, groundBox);

        FixtureDef fixtureDef = box2dDynamicInitializer.fixtureDef;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 10f;

        fixtureDef.restitution = 0.001f; // Make it bounce a little bit

        AnimationComponent.Animation<TextureRegion> idleAnimation
                = new AnimationComponent.Animation<TextureRegion>("idle", 24f,
                idleAtlas.getRegions());
        AnimationComponent.Animation<TextureRegion> walkAnimation
                = new AnimationComponent.Animation<TextureRegion>("walk", 24f,
                walkAtlas.getRegions());

        AnimationInitializer animationInitializer = new AnimationInitializer(
                idleAnimation,
                walkAnimation

        );
        GameEntity gameEntity = new GameEntity("Character",
                spriteInitializer,
                animationInitializer,
                box2dDynamicInitializer
        );

        return gameEntity;


    }

    public static GameEntity createTerrain() {


        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(Gdx.graphics.getWidth() * 100, 10);
// Create a fixture from our polygon shape and add it to our ground body

        Box2dStaticInitiliazer box2dStaticInitiliazer = new Box2dStaticInitiliazer(
                ObjectAllocator.getMatrix3().setToTranslation(0, -100),
                groundBox

        );


        GameEntity gameEntity = new GameEntity("Terrain",
                box2dStaticInitiliazer) {
            @Override
            public void onCollisionStart(GameEntity entity) {

                Gdx.app.debug(TAG, "Collision");
            }
        };

        return gameEntity;


    }
}
