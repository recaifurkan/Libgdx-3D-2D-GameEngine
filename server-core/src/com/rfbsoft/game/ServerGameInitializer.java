package com.rfbsoft.game;

import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.engine.entites.G2dEntites;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.game.engine.systems.IGameSystemsCreator;
import com.rfbsoft.game.engine.systems.g2d.ServerG2dGameSystems;
import com.rfbsoft.game.engine.systems.g3d.ServerG3dGameSystems;


public class ServerGameInitializer implements Disposable {
    IGameSystemsCreator gameSystem;

    public ServerGameInitializer() {
        if (GameFields.systemType == GameFields.GameType.G2D) {
            gameSystem = new ServerG2dGameSystems();
            new G2dEntites();
        } else {
            gameSystem = new ServerG3dGameSystems();
            new G3dEntites();
        }


    }

    public void dispose() {

        gameSystem.dispose();
    }


}
