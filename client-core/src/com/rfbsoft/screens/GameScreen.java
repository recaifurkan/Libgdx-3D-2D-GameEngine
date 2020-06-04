package com.rfbsoft.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.rfbsoft.Settings;
import com.rfbsoft.assets.Assets;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.ClientGameInitializer;
import com.rfbsoft.game.GameInput;

public class GameScreen implements Screen {
    private static final String TAG = "com.rfbsoft.factories.EntityFactory";


    ClientGameInitializer systems;

    @Override
    public void show() {

        Bullet.init();
        Settings.load();
        Assets.load();


        systems = new ClientGameInitializer();


        GameInput gameInput = new GameInput();
        Gdx.input.setInputProcessor(new InputMultiplexer(gameInput

//                , GameFields.camController
        ));


    }
    public final Color backgroundColor = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1f);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        GameFields.gameEngine.update(delta);


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Assets.dispose();
        Settings.save();
        systems.dispose();


    }


}
