package com.rfbsoft.game.engine.entites.initializers;

import com.rfbsoft.game.engine.entites.GameEntity;

public interface IEntityInitializer {
    static void initialize(GameEntity object, IEntityInitializer... initializors) {
        for (IEntityInitializer initializor : initializors) {
            initializor.initialize(object);
        }
    }

    void initialize(GameEntity object);

}
