package com.rfbsoft.game.engine.entites.entitytypes;

import com.rfbsoft.game.engine.entites.GameEntity;

public interface IPhysics {
    void onCollisionStart(GameEntity entity);


    void onCollisionEnd(GameEntity entity);
}
