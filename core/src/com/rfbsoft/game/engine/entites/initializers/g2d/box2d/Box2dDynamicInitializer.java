package com.rfbsoft.game.engine.entites.initializers.g2d.box2d;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class Box2dDynamicInitializer extends Box2dBodyInitializer {

    public Box2dDynamicInitializer(Matrix3 transform, Shape shape) {
        super(transform, shape);

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;


// Create a circle shape and set its radius to 6


// Create a fixture definition to apply our shape to


// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
    }


}
