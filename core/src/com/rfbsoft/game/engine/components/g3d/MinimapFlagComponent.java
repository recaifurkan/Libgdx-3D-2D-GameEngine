package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MinimapFlagComponent implements Component {
    public final Image flag;
    public boolean isRegistered;

    public MinimapFlagComponent(Image flag) {
        this.flag = flag;
    }
}
