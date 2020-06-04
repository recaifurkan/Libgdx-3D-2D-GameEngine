package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.rfbsoft.assets.Assets;

public class BundleTest extends ApplicationAdapter {
    I18NBundle bundle;

    @Override
    public void create() {

        bundle = Assets.gameBundle.get();
        String deneme = bundle.get("deneme");
        Gdx.app.debug("", deneme);

    }
}
