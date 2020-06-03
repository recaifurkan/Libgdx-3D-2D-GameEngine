package com.rfbsoft.game.engine.components.g2d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.rfbsoft.game.engine.systems.g2d.Box2DSystem;

public class Box2dBodyComponent implements Component {
    public Body body;

    public Box2dBodyComponent(BodyDef bodyDef) {
        body = Box2DSystem.world.createBody(bodyDef);
    }
}
