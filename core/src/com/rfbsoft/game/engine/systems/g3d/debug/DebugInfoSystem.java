package com.rfbsoft.game.engine.systems.g3d.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectArray;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.systems.g3d.BulletSystem;

public class DebugInfoSystem extends EntitySystem implements Telegraph, Disposable {

    BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    GameEntity selectedEntity;

    public DebugInfoSystem() {
        MessageManager.getInstance().addListener(this, GameMessages.RAYRESULT);
    }

    @Override
    public void update(float deltaTime) {
        SpriteBatch batch = GameFields.debugBatch;
        Camera camera = GameFields.cam;
        btCollisionObjectArray collisionObjectArray = BulletSystem.collisionWorld.getCollisionObjectArray();
        int size = collisionObjectArray.size();
//        batch.setProjectionMatrix(camera.combined); //or your matrix to draw GAME WORLD, not UI
//draw background, objects, etc.
        String text = String.format(
                "Camera coord =  %s \n" +
                        "Hit Entity = %s \n" +
                        "Collision Object Size %d \n" +
                        "Fps = %s",
                camera.position.toString(),
                selectedEntity == null ? "Not Hit" : selectedEntity.getName(),
                size,
                Gdx.graphics.getFramesPerSecond()
        );
        batch.begin();
        font.draw(batch, text, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == GameMessages.RAYRESULT) {
            ClosestRayResultCallback callback = (ClosestRayResultCallback) msg.extraInfo;
            selectedEntity = (GameEntity) callback.getCollisionObject().userData;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}


