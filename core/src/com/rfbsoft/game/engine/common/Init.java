package com.rfbsoft.game.engine.common;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.rfbsoft.game.GameFields;

public class Init {
    public static void initCamController(Camera cam) {
        CameraInputController camController = new CameraInputController(cam);
        camController.scrollFactor = -1f;
        camController.translateUnits = 30;

        GameFields.camController = camController;
    }
}
