package com.rfbsoft.game.engine.systems.g2d.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.systems.g2d.Box2DSystem;

public class G2dDebugInfoSystem extends EntitySystem implements Telegraph, Disposable {

    BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    GameEntity selectedEntity;

    public G2dDebugInfoSystem() {
        MessageManager.getInstance().addListener(this, GameMessages.RAYRESULT);
    }

    @Override
    public void update(float deltaTime) {

        Camera camera = GameFields.cam;
        int bodyCount = Box2DSystem.world.getBodyCount();
//        batch.setProjectionMatrix(camera.combined); //or your matrix to draw GAME WORLD, not UI
//draw background, objects, etc.
        String text = String.format(
                "Camera coord =  %s \n" +
                        "Hit Entity = %s \n" +
                        "Collision Object Size %d \n" +
                        "Fps = %s",
                camera.position.toString(),
                selectedEntity == null ? "Not Hit" : selectedEntity.getName(),
                bodyCount,
                Gdx.graphics.getFramesPerSecond()
        );

        SpriteBatch debugBatch = GameFields.debugBatch;
        debugBatch.begin();
        font.draw(debugBatch, text, 10, Gdx.graphics.getHeight() - 10);
        debugBatch.end();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == GameMessages.RAYRESULT) {
            selectedEntity = (GameEntity) msg.extraInfo;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}


