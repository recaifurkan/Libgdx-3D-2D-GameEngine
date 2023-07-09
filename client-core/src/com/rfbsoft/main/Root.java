package com.rfbsoft.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rfbsoft.ads.AdsProcessor;
import com.rfbsoft.screens.GameScreen;

public class Root extends Game {
    final static String TAG = "com.rfbsoft.main.Root";

    public AdsProcessor adsProcessor;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new GameScreen(adsProcessor));
        Gdx.app.debug("", "Deneme");
//        System.out.println("Deneme");


        String versionString, vendorString, rendererString;
        vendorString = Gdx.graphics.getGL20().glGetString(GL20.GL_VENDOR);
        versionString = Gdx.graphics.getGL20().glGetString(GL20.GL_VERSION);
        rendererString = Gdx.graphics.getGL20().glGetString(GL20.GL_RENDERER);

        Gdx.app.debug(TAG, String.format(
                "\n" +
                        "Version = %s \n" +
                        "Vendor = %s \n" +
                        "Renderer = %s \n",
                versionString, vendorString, rendererString
        ));
    }
}
