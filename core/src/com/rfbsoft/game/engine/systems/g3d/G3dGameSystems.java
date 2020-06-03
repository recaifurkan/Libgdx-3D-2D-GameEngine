package com.rfbsoft.game.engine.systems.g3d;

import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.systems.AiMessageSystem;
import com.rfbsoft.game.engine.systems.IGameSystemsCreator;
import com.rfbsoft.game.engine.systems.PlayerAddSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugBulletSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugDetourSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugInfoSystem;
import com.rfbsoft.main.Root;

public class G3dGameSystems implements IGameSystemsCreator {

    RenderSystem renderSystem = new RenderSystem();
    BulletSystem bulletSystem = new BulletSystem();
    DebugInfoSystem debugTextSystem = new DebugInfoSystem();
    PlayerAddSystem playerAddSystem = new PlayerAddSystem();
    AnimationSystem animationSystem = new AnimationSystem();
    AnimationSystem.AnimationStateSystem animationStateSystem = new AnimationSystem.AnimationStateSystem();
    CharacterAnimationSystem characterAnimationSystem = new CharacterAnimationSystem();
    SteeringSystem steeringSystem = new SteeringSystem();
    DebugDetourSystem detourDebugSystem = new DebugDetourSystem();
    BulletCarSystem bulletCarSystem = new BulletCarSystem();
    DebugBulletSystem bulletDebugSystem = new DebugBulletSystem();
    AiMessageSystem aiMessageSystem = new AiMessageSystem();

    public G3dGameSystems() {
        init();
    }

    @Override
    public void init() {
        GameFields.gameEngine.addSystems(
                renderSystem,
                bulletSystem,

                playerAddSystem,
                animationSystem,
                animationStateSystem,
                characterAnimationSystem,
                steeringSystem,

                bulletCarSystem,

                aiMessageSystem
        );
        if (Root.DEBUG)
            GameFields.gameEngine.addSystems(
                    debugTextSystem,
                    bulletDebugSystem,
                    detourDebugSystem
            );

    }

    @Override
    public void dispose() {
        bulletSystem.dispose();
        renderSystem.dispose();
        debugTextSystem.dispose();
        bulletDebugSystem.dispose();

    }
}
