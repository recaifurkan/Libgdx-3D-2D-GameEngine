package com.rfbsoft.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;

public class GameInput implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MouseEventObject object = new MouseEventObject(screenX, screenY, pointer, button);

        MessageManager.getInstance().dispatchMessage(GameMessages.MOUSEEVENT, object);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    public static class MouseEventObject {
        public int screenX, screenY, pointer, button;

        public MouseEventObject(int screenX, int screenY, int pointer, int button) {
            this.screenX = screenX;
            this.screenY = screenY;
            this.pointer = pointer;
            this.button = button;
        }
    }
}
