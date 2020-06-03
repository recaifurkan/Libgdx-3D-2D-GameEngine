package com.rfbsoft.assets.g3d;

import com.badlogic.gdx.graphics.g3d.Model;
import com.rfbsoft.assets.GameAsset;

public class GameModel<T extends Model> extends GameAsset<Model> {

    public GameModel(String fileName) {
        super(fileName, Model.class);

    }

    @Override
    public Model get() {
        return super.get();
    }
}