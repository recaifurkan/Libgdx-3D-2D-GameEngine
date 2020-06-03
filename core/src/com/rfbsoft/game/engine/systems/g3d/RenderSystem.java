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
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.common.Init;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;

public class RenderSystem extends EntitySystem implements Disposable {

    public static ModelBatch modelBatch;
    public static Environment environment;
    public final Color backgroundColor = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1f);
    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);
    private ImmutableArray<Entity> entities;

    public RenderSystem() {

        modelBatch = new ModelBatch();

        RenderSystem.environment = new Environment();
        RenderSystem.environment.set(
                new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        RenderSystem.environment.add(
                new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        Camera cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.position.set(0f, 12f, 35f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 3000f;
        cam.update();

        Init.initCamController(cam);
        GameFields.cam = cam;

    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        GameFields.camController.update();
        modelBatch.begin(GameFields.cam);
        for (Entity entity : entities) {
            ModelComponent modelComponent = modelMapper.get(entity);
            modelBatch.render(modelComponent.instances, environment);

        }
        modelBatch.end();


    }

    @Override
    public void dispose() {
        modelBatch.dispose();

    }
}
