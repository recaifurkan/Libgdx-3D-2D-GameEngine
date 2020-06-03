package com.rfbsoft.game;

import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.engine.entites.G2dEntites;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.game.engine.systems.IGameSystemsCreator;
import com.rfbsoft.game.engine.systems.g2d.G2dGameSystems;
import com.rfbsoft.game.engine.systems.g3d.G3dGameSystems;


public class GameInitializer implements Disposable {
    IGameSystemsCreator gameSystem;

    public GameInitializer() {
        if (GameFields.systemType == GameType.G2D) {
            gameSystem = new G2dGameSystems();
            new G2dEntites();
        } else {
            gameSystem = new G3dGameSystems();
            new G3dEntites();
        }


    }

    public void dispose() {

        gameSystem.dispose();
    }


    public enum GameType {
        G2D,
        G3D


    }

}
