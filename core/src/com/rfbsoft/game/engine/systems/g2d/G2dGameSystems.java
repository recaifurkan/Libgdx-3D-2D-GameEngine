package com.rfbsoft.game.engine.systems.g2d;

import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.systems.IGameSystemsCreator;
import com.rfbsoft.game.engine.systems.g2d.debug.DebugBox2DSystem;
import com.rfbsoft.game.engine.systems.g2d.debug.G2dDebugInfoSystem;

public class G2dGameSystems implements IGameSystemsCreator {

    RenderSystem renderSystem = new RenderSystem();
    AnimationSystem animationSystem = new AnimationSystem();
    Box2DSystem box2DSystem = new Box2DSystem();
    Box2dRenderSystem box2dRenderSystem = new Box2dRenderSystem();
    DebugBox2DSystem debugBox2DSystem = new DebugBox2DSystem();
    G2dDebugInfoSystem g2dDebugInfoSystem = new G2dDebugInfoSystem();

    public G2dGameSystems() {
        init();
    }

    @Override
    public void init() {

        GameFields.gameEngine.addSystems(
                renderSystem
                , animationSystem
                , box2DSystem
        );
        if (GameFields.DEBUG)
            GameFields.gameEngine.addSystems(
                    debugBox2DSystem,
                    g2dDebugInfoSystem,
                    box2dRenderSystem

            );

    }

    @Override
    public void dispose() {
        renderSystem.dispose();

    }
}
