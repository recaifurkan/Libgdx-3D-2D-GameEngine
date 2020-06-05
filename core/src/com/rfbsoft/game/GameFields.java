package com.rfbsoft.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.rfbsoft.assets.loaders.NavMeshLoader;
import com.rfbsoft.assets.loaders.TextFileLoader;
import com.rfbsoft.game.engine.GameEngine;
import org.recast4j.detour.NavMesh;

public class GameFields {
    private static final String TAG = "com.rfbsoft.game.GameFields";
    public static CameraInputController camController;
    public static Camera cam;
    public static SpriteBatch debugBatch;
    public static GameEngine gameEngine;
    public static AssetManager assetManager;
    public static boolean DEBUG = true;

    public static ModelBatch modelBatch;
    public static Environment environment;
    public static GameType systemType = GameType.G3D;

    static {

        GameFields.modelBatch = new ModelBatch();

        GameFields.environment = new Environment();
        GameFields.environment.set(
                new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        GameFields.environment.add(
                new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


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

    public enum GameType {
        G2D,
        G3D
    }
}
