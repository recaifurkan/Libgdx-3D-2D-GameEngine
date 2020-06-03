package com.rfbsoft.game.engine.components.g2d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class SpriteComponent implements Component {
    public Array<Sprite> instances = new Array<>();

    public SpriteComponent(Sprite instance) {
        addInstance(instance);
    }

    public SpriteComponent(Sprite... instances) {
        addInstances(instances);
    }

    public void addInstance(Sprite instance) {
//
        this.instances.add(instance);
    }

    public void addInstances(Sprite... instances) {
        for (Sprite instance : instances) {
            addInstance(instance);

        }
    }
}
