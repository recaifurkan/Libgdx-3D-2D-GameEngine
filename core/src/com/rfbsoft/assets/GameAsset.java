package com.rfbsoft.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.rfbsoft.game.GameFields;

public class GameAsset<T> {
    public String fileName;
    public Class<T> clazz;

    public GameAsset(String fileName, Class<T> clazz) {
        this.fileName = fileName;
        this.clazz = clazz;
        AssetManager manager = GameFields.assetManager;
        manager.load(fileName, clazz);
        manager.finishLoading();
    }

    public T get() {
        T asset = GameFields.assetManager.get(fileName);
        return asset;
    }
}
