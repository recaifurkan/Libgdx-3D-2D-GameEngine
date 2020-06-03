package com.rfbsoft.game.engine.entites;

import com.rfbsoft.factories.g2d.G2dEntityFactory;
import com.rfbsoft.game.GameFields;

public class G2dEntites {


    public static GameEntity character;
    public static GameEntity terrain;

    public G2dEntites() {
        character = G2dEntityFactory.createCharacter();
        terrain = G2dEntityFactory.createTerrain();

        GameFields.gameEngine.addEntites(
                character,
                terrain
        );
    }
}
