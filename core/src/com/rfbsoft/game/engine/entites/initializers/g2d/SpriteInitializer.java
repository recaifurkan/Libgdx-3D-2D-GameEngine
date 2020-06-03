package com.rfbsoft.game.engine.entites.initializers.g2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.rfbsoft.game.engine.components.g2d.SpriteComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;

public class SpriteInitializer implements IEntityInitializer {
    SpriteComponent spriteComponent;

    public SpriteInitializer() {
        Sprite sprite = new Sprite();

        spriteComponent = new SpriteComponent(sprite);
    }

    public SpriteInitializer(Sprite sprite) {
        spriteComponent = new SpriteComponent(sprite);

    }

    @Override
    public void initialize(GameEntity object) {
        object.add(spriteComponent);

    }
}
