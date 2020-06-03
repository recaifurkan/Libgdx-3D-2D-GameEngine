package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;
import com.rfbsoft.game.engine.entites.initializers.g3d.bullet.BulletRigidInitializer;

public class ModelledBulletInitializer implements IEntityInitializer {
    public ModelInitializer model;
    public BulletRigidInitializer physics;

    @Override
    public void initialize(GameEntity object) {
        model.initialize(object);
        physics.initialize(object);

    }
}
