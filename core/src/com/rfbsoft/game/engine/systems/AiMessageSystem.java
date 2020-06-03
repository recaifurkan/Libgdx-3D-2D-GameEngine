package com.rfbsoft.game.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageManager;

public class AiMessageSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        MessageManager.getInstance().update();
    }
}
