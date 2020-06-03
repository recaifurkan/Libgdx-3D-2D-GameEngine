package com.rfbsoft.assets;

import com.badlogic.gdx.Gdx;
import com.rfbsoft.assets.g3d.GameModel;
import com.rfbsoft.assets.g3d.GameNavMesh;
import com.rfbsoft.assets.loaders.GameText;
import com.rfbsoft.game.GameFields;

/**
 * Created by scanevaro on 01/08/2015.
 */


public class Assets {
    private static final String TAG = "com.rfbsoft.assets.Assets";
    public static GameModel terrainModel;
    public static GameModel characterModel;
    public static GameNavMesh navMesh;
    public static GameModel carModel;
    public static GameModel wheelModel;
    public static GameText gameText;
    public static GameText gameSettings;
    public static GameBundle gameBundle;

    static {
        terrainModel = new GameModel("models/obj/terrain.obj");
        characterModel = new GameModel("models/g3dj/character.g3dj");
        navMesh = new GameNavMesh("navmeshs/terrain.nav");
        carModel = new GameModel("models/g3dj/car.g3dj");
        wheelModel = new GameModel("models/obj/wheel.obj");
        gameText = new GameText("deneme.text");
        gameBundle = new GameBundle("i18n/i18n");

        String s = gameText.get();
        Gdx.app.debug(TAG, String.format("Dosyadan Gelen = %s", s));
        gameSettings = new GameText("settings");
    }

    public static void load() {


    }

    public static void dispose() {

        GameFields.assetManager.dispose();
    }
}
