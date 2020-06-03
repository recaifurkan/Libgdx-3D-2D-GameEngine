package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletStaticInitializer;


public class ModelledBulletStaticInitializer extends ModelledBulletInitializer {

    public ModelledBulletStaticInitializer(
            ModelInstance instance, btCollisionShape collisionShape, Matrix4 collisionTransform) {
        model = new ModelInitializer(instance);
        physics = new BulletStaticInitializer(collisionTransform, collisionShape);

    }

    @Override
    public void initialize(GameEntity object) {
        model.initialize(object);
        physics.initialize(object);
    }
}
