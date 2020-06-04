package com.rfbsoft.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.rfbsoft.assets.loaders.NavMeshLoader;
import com.rfbsoft.assets.loaders.TextFileLoader;
import com.rfbsoft.game.engine.GameEngine;
import org.recast4j.detour.NavMesh;

public class ServerGameFields {
    private static final String TAG = "com.rfbsoft.game.GameFields";
    public static CameraInputController camController;
    public static Camera cam;
    public static SpriteBatch debugBatch;
    public static GameEngine gameEngine;
    public static AssetManager assetManager;
    public static ServerGameInitializer.GameType systemType = ServerGameInitializer.GameType.G2D;
    public static boolean DEBUG = true;

    static {


        debugBatch = new SpriteBatch();
        gameEngine = new GameEngine();
        assetManager = new AssetManager();
        assetManager.setLoader(
                String.class,
                new TextFileLoader(
                        new InternalFileHandleResolver()
                )
        );
        assetManager.setLoader(
                NavMesh.class,
                new NavMeshLoader(
                        new InternalFileHandleResolver()
                )
        );

        Gdx.app
                .debug(TAG, "GameFields initialized");

    }
}
