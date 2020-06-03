package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;

public class CarComponent implements Component {

    public btVehicleRaycaster raycaster;
    public btRaycastVehicle vehicle;
    public btRaycastVehicle.btVehicleTuning tuning;


}
