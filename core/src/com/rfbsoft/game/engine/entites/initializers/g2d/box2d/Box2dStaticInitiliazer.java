package com.rfbsoft.game.engine.entites.initializers.g2d.box2d;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.physics.box2d.Shape;

public class Box2dStaticInitiliazer extends Box2dBodyInitializer {
//    Matrix4 transform, btCollisionShape collisionShape


    public Box2dStaticInitiliazer(Matrix3 transform, Shape shape) {
        super(transform, shape);
        fixtureDef.density = 0;
    }


}
