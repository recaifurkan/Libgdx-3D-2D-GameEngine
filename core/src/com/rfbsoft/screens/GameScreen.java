package com.rfbsoft.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.rfbsoft.Settings;
import com.rfbsoft.assets.Assets;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameInitializer;
import com.rfbsoft.game.GameInput;

public class GameScreen implements Screen {
    private static final String TAG = "com.rfbsoft.factories.EntityFactory";


    GameInitializer systems;

    @Override
    public void show() {

        Bullet.init();
        Settings.load();
        Assets.load();


        systems = new GameInitializer();


        GameInput gameInput = new GameInput();
        Gdx.input.setInputProcessor(new InputMultiplexer(gameInput

//                , GameFields.camController
        ));


    }


    @Override
    public void render(float delta) {

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
