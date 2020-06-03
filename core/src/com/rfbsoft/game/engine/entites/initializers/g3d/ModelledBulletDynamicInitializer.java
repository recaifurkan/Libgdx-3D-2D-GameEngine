package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletDynamicInitializer;

public class ModelledBulletDynamicInitializer extends ModelledBulletInitializer {


    public ModelledBulletDynamicInitializer(ModelInstance instance, float mass, btCollisionShape collisionShape) {
        model = new ModelInitializer(instance);
        physics = new BulletDynamicInitializer(instance.transform, mass, collisionShape);


    }


}
