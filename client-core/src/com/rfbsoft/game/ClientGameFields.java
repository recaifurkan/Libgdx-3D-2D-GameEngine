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

public class ClientGameFields {
    private static final String TAG = "com.rfbsoft.game.GameFields";


    static {




        Gdx.app
                .debug(TAG, "GameFields initialized");

    }
}
