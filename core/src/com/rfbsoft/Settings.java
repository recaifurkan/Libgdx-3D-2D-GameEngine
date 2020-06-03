package com.rfbsoft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.rfbsoft.assets.Assets;

public class Settings {

    public static final String settingFile = "settings";

    public static void load() {
        String[] strings = Assets.gameSettings.get().split("\n");
    }

    public static void save() {
        try {
            FileHandle filehandle = Gdx.files.internal(settingFile);
//            filehandle.writeString(Boolean.toString(soundEnabled) + "\n", false);
        } catch (Throwable e) {
        }
    }
}
