package com.rfbsoft.game.engine.systems.g3d.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.systems.g3d.BulletSystem;

public class DebugBulletSystem extends EntitySystem implements Disposable {

    private final DebugDrawer debugDrawer;

    public DebugBulletSystem() {
        debugDrawer = new DebugDrawer();
        BulletSystem.collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
    }

    @Override
    public void update(float deltaTime) {
        debugDrawer.begin(GameFields.cam);
        BulletSystem.collisionWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        debugDrawer.dispose();
    }
}
