package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.common.Init;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;

public class RenderSystem extends EntitySystem implements Disposable {


    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);

    private ImmutableArray<Entity> entities;

    public RenderSystem() {


        Camera cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.position.set(0f, 12f, 35f);
//        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 3000f;
        cam.update();


        GameFields.cam = cam;

    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {


        GameFields.modelBatch.begin(GameFields.cam);
        for (Entity entity : entities) {
            ModelComponent modelComponent = modelMapper.get(entity);

            GameFields.modelBatch.render(modelComponent.instances, GameFields.environment);

        }
        GameFields.modelBatch.end();


    }

    @Override
    public void dispose() {
        GameFields.modelBatch.dispose();

    }
}
