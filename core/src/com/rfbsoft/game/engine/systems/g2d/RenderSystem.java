package com.rfbsoft.game.engine.systems.g2d;


import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.components.g2d.SpriteComponent;

public class RenderSystem extends EntitySystem implements Disposable {

    public final Color backgroundColor = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1f);
    final float CAMERAMOVEFACTOR = 10;
    private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    SpriteBatch batch = new SpriteBatch();
    private ImmutableArray<Entity> entities;
    private float elapsedTime = 0f;

    public RenderSystem() {

        OrthographicCamera cam = new OrthographicCamera(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());


        GameFields.cam = cam;

        cam.position.set(0, 0, 100);
        cam.lookAt(0,
                0, 0);
        cam.near = 1f;
        cam.far = 3000f;
        cam.update();
//        Init.initCamController(cam);


    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();
        if (GameFields.camController != null)
            GameFields.camController.update();
        batch.begin();

        batch.setProjectionMatrix(GameFields.cam.combined);


        for (Entity entity : entities) {
            SpriteComponent spriteComponent = spriteMapper.get(entity);
            for (Sprite sprite : spriteComponent.instances) {
                batch.draw(
                        sprite,
                        sprite.getX(),
                        sprite.getY(),
                        sprite.getOriginX(),
                        sprite.getOriginY(),
                        sprite.getWidth(),
                        sprite.getHeight(),
                        sprite.getScaleX(),
                        sprite.getScaleY(),
                        sprite.getRotation()
                );
//                batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getRotation());
            }
        }
        batch.end();

        processCam(deltaTime);


    }

    private void processCam(float deltaTime) {
        Camera cam = GameFields.cam;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.position.add(0, CAMERAMOVEFACTOR, 0);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.position.add(0, -CAMERAMOVEFACTOR, 0);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.position.add(-CAMERAMOVEFACTOR, 0, 0);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cam.position.add(CAMERAMOVEFACTOR, 0, 0);
        }
        cam.update();
    }

    @Override
    public void dispose() {
//        GameFields.modelBatch.dispose();

    }
}
