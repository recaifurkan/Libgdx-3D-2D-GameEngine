package com.rfbsoft.game.engine.systems.g3d;

import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.systems.AiMessageSystem;
import com.rfbsoft.game.engine.systems.CameraInputSystem;
import com.rfbsoft.game.engine.systems.IGameSystemsCreator;
import com.rfbsoft.game.engine.systems.PlayerAddSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugBulletSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugDetourSystem;
import com.rfbsoft.game.engine.systems.g3d.debug.DebugInfoSystem;

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
    BulletPositionSystem bulletPositionSystem = new BulletPositionSystem();
    CameraInputSystem cameraInputSystem = new CameraInputSystem();
    MinimapSystem minimapSystem = new MinimapSystem();
    Scene2DMinimapSystem scene2DMinimapSystem = new Scene2DMinimapSystem();

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

                aiMessageSystem,
                bulletPositionSystem,
                cameraInputSystem,
                minimapSystem,
                scene2DMinimapSystem

        );
        if (GameFields.DEBUG)
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
