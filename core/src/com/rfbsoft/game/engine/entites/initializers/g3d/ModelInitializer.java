package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;

public class ModelInitializer implements IEntityInitializer {
    public ModelComponent modelComponent;

    public ModelInitializer(ModelInstance instance) {
        modelComponent = new ModelComponent(instance);

    }

    public ModelInitializer(ModelInstance... instance) {
        modelComponent = new ModelComponent(instance);

    }


    @Override
    public void initialize(GameEntity object) {
        object.add(modelComponent);

    }
}
