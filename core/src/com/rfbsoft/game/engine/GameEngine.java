package com.rfbsoft.game.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

public class GameEngine extends Engine {

    public void addSystems(EntitySystem... systems) {
        for (EntitySystem system : systems)
            this.addSystem(system);
    }

    public void addEntites(Entity... entities) {
        for (Entity entity : entities)
            this.addEntity(entity);
    }
}
