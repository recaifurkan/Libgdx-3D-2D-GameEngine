package com.rfbsoft.game.engine.entites;

import com.rfbsoft.factories.g3d.G3dEntityFactory;
import com.rfbsoft.game.GameFields;

public class G3dEntites {

    public static GameEntity terrain;
    public static GameEntity player;
    public static GameEntity sensor;
    public static GameEntity character;
    public static GameEntity car;

    public G3dEntites() {
        terrain = G3dEntityFactory.createTerrain();
        player = G3dEntityFactory.createPlayer();
        sensor = G3dEntityFactory.createSensor();
        character = G3dEntityFactory.createCharacter();
        car = G3dEntityFactory.createCar();
        GameFields.gameEngine.addEntites(
                terrain
                , player
                , sensor
                , character
                , car
        );
    }
}
