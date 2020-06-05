package com.rfbsoft.game.engine.systems.g3d;

public class ServerG3dGameSystems extends G3dGameSystems{
    public ServerG3dGameSystems() {
        renderSystem.setProcessing(false);
    }
}