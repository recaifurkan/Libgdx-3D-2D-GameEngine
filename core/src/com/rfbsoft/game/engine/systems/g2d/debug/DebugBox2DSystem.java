package com.rfbsoft.game.engine.systems.g2d.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.systems.g2d.Box2DSystem;

public class DebugBox2DSystem extends EntitySystem {
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(Box2DSystem.world, GameFields.cam.combined);
    }
}
