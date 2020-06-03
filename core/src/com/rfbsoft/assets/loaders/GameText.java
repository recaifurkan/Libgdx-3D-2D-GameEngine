package com.rfbsoft.assets.loaders;

import com.rfbsoft.assets.GameAsset;

public class GameText extends GameAsset<String> {
    public GameText(String fileName) {
        super(fileName, String.class);
    }

}
