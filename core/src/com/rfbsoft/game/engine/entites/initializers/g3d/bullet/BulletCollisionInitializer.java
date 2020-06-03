package com.rfbsoft.game.engine.entites.initializers.g3d.bullet;

import com.rfbsoft.game.engine.components.g3d.BulletCollisionComponent;
import com.rfbsoft.game.engine.entites.GameEntity;

public class BulletCollisionInitializer extends BulletInitilizer {

    public BulletCollisionComponent collisionComponent;

    public BulletCollisionInitializer() {

        collisionComponent = new BulletCollisionComponent();
    }


    @Override
    public void initialize(GameEntity object) {
        object.add(collisionComponent);
    }
}
