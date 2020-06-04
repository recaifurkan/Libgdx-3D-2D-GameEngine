package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.rfbsoft.game.GameFields;
import org.recast4j.detour.NavMesh;

public class DetourLoadTest extends ApplicationAdapter {
    @Override
    public void create() {
        AssetManager manager = GameFields.assetManager;
        NavMesh navMesh = manager.get("nav.nav", NavMesh.class);
        Gdx.app.debug("", navMesh.toString());
    }
}
