package com.rfbsoft.game.engine.entites.initializers.g2d.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rfbsoft.game.engine.components.g2d.Box2dBodyComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.utils.ObjectAllocator;

public class Box2dBodyInitializer extends Box2DInitializer {
    public Box2dBodyComponent component;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    protected Body body;
    String TAG = "com.rfbsoft.game.engine.entites.initializers.g2d.box2d.Box2dBodyInitializer";

    public Box2dBodyInitializer(Matrix3 transform, Shape shape) {
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;


        Vector2 vector2 = ObjectAllocator.getVector2();
        vector2 = transform.getTranslation(vector2);
        bodyDef.position.set(vector2);
        bodyDef.angle = transform.getRotation();
    }

    public void initBody() {
        component = new Box2dBodyComponent(bodyDef);
        body = component.body;
    }

    public Fixture initFixture() {
        if (body == null) {
            Gdx.app.error(TAG, "Body null");
            initBody();
        }

        Fixture fixture = body.createFixture(fixtureDef);
        fixtureDef = new FixtureDef();
        return fixture;
    }

    public void init() {
        if (body == null)
            initBody();
        initFixture();

    }

    @Override
    public void initialize(GameEntity object) {
        init();
        body.setUserData(object);
        object.add(component);


    }
}
