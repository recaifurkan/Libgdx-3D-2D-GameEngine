package com.rfbsoft.game.engine.systems.g2d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.rfbsoft.game.engine.components.g2d.Box2dBodyComponent;
import com.rfbsoft.game.engine.components.g2d.SpriteComponent;
import com.rfbsoft.utils.ObjectAllocator;

public class Box2dRenderSystem extends EntitySystem {
    private final ComponentMapper<Box2dBodyComponent>
            bodyMapper = ComponentMapper.getFor(Box2dBodyComponent.class);
    private final ComponentMapper<SpriteComponent>
            spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    float MAX_VELOCITY = 200;
    float forceX = 300;
    float forceY = 0;
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(Box2dBodyComponent.class, SpriteComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {

            Box2dBodyComponent box2dBodyComponent = bodyMapper.get(entity);
            update(box2dBodyComponent);
            SpriteComponent spriteComponent = spriteMapper.get(entity);
            Transform transform = box2dBodyComponent.body.getTransform();
            Vector2 position = transform.getPosition();

            float rotation = transform.getRotation();
            Sprite sprite = spriteComponent.instances.get(0);

            sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
            sprite.setRotation(MathUtils.radiansToDegrees * rotation);

        }
    }

    private void update(Box2dBodyComponent box2dBodyComponent) {
        Body body = box2dBodyComponent.body;
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        body.setLinearVelocity(ObjectAllocator.getVector2(100,0));

// apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.F) && vel.x > -MAX_VELOCITY) {
//            body.applyForceToCenter(-forceX,forceY,true);
            body.applyLinearImpulse(-forceX, 0, pos.x, pos.y, true);
        }
//
//// apply right impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.H) && vel.x < MAX_VELOCITY) {
//            body.applyForceToCenter(forceX,forceY,true);
            body.applyLinearImpulse(forceX, 0, pos.x, pos.y, true);
        }
    }


}
