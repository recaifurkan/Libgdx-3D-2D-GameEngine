package com.rfbsoft.game.engine.entites;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.rfbsoft.game.engine.entites.entitytypes.IPhysics;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;

public class GameEntity extends Entity implements IPhysics {
    private static long gameObjectSize = 0;
    IPhysics physics;
    private String name = "Game Object -" + gameObjectSize;


    public GameEntity() {
        gameObjectSize++;
        this.physics = this;
    }

    public GameEntity(String name) {
        this();
        this.name = name;

    }

    public GameEntity(String name, IEntityInitializer... initializor) {
        this();
        this.name = name;
        for (IEntityInitializer iEntityInitializer : initializor)
            iEntityInitializer.initialize(this);

    }

    public void addComponents(ImmutableArray<Component> components) {
        for (Component component : components) {
            this.add(component);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPhysics getPhysics() {
        return physics;
    }

    public void setPhysics(IPhysics physics) {
        this.physics = physics;
    }

    @Override
    public void onCollisionStart(GameEntity entity) {

    }

    @Override
    public void onCollisionEnd(GameEntity entity) {

    }
}
