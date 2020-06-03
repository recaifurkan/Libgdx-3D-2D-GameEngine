package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.rfbsoft.game.engine.entites.initializers.g3d.SteeringBulletEntity;

public class SteeringComponent implements Component {
    public SteeringBulletEntity steeringBulletEntity;

    public SteeringComponent(SteeringBulletEntity steeringBulletEntity) {
        this.steeringBulletEntity = steeringBulletEntity;
    }
}
