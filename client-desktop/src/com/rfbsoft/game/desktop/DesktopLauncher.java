package com.rfbsoft.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rfbsoft.GLTFQuickStartExample;
import com.rfbsoft.main.Root;

public class DesktopLauncher {
    public static void main(String[] arg) {
//        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 960;
        config.height = 540;
        GLTFQuickStartExample gltfQuickStartExample = new GLTFQuickStartExample();
        new LwjglApplication(new Root(), config);
    }
}
