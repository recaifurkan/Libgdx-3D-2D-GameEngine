package com.rfbsoft.game.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.rfbsoft.factories.g3d.G3dEntityFactory;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameInput;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.systems.g3d.BulletSystem;
import com.rfbsoft.utils.ObjectAllocator;

public class PlayerAddSystem extends EntitySystem implements Telegraph {
    private static final String TAG = "com.rfbsoft.game.engine.systems.PlayerAddSystem";


    final int SPAWNLIMIT = 0;
    float spawnTime = 0.1f;
    float currentTime = 0;
    int index = 1;

    public PlayerAddSystem() {
        MessageManager.getInstance().addListener(this, GameMessages.MOUSEEVENT);
    }

    @Override
    public void update(float deltaTime) {
        currentTime += deltaTime;
        if (currentTime > spawnTime) {
//            GameFields.gameEngine.addEntity(EntityFactory.createPlayer());
            GameFields.gameEngine.addEntity(G3dEntityFactory.createCharacter());
            currentTime = 0;
            index++;
        }
        if (index > SPAWNLIMIT)
            this.setProcessing(false);

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == GameMessages.MOUSEEVENT) {
            GameInput.MouseEventObject mouseEventObject = (GameInput.MouseEventObject) msg.extraInfo;
            if (mouseEventObject.button == Input.Buttons.MIDDLE) {
                Gdx.app.debug(TAG, "Middle Mouse");
                ClosestRayResultCallback ray = BulletSystem.ray(mouseEventObject.screenX, mouseEventObject.screenY);
                if (ray == null) return true;
                Gdx.app.debug(TAG, "Ray True");
                Vector3 vector3 = ObjectAllocator.getObject(Vector3.class);
                ray.getHitPointWorld(vector3);
                GameFields.gameEngine.addEntity(G3dEntityFactory.createCharacter(vector3));
                return true;
            }
        }
        return false;
    }
}
