package tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class LoadModelsTest implements ApplicationListener {
    public final Color backgroundColor = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1f);
    private final String fileName = "models/g3dj/terrain.g3dj";
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        doneLoading();


    }

    private void doneLoading() {


        Model ship = loadModel(fileName);
        ModelInstance shipInstance = new ModelInstance(ship);
        float alphaTest = -1;
        for (Material mat : shipInstance.materials) {
            mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            mat.set(new FloatAttribute(FloatAttribute.AlphaTest, alphaTest));
            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
        }


        shipInstance.transform.setTranslation(0, 0, 0);
        Vector3 dimensions = shipInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3());
        float largest = dimensions.x;
        if (dimensions.y > largest) largest = dimensions.y;
        if (dimensions.z > largest) largest = dimensions.z;
        if (largest > 25) {
            float s = 25f / largest;
            shipInstance.transform.setToScaling(s, s, s);
        } else if (largest < 0.1f) {
            float s = 5 / largest;

            shipInstance.transform.setToScaling(s, s, s);
        }
        instances.add(shipInstance);
    }


    private Model loadModel(String f) {


        if (f == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            return modelBuilder.createBox(5f, 5f, 5f,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        } else {
            AssetManager manager = new AssetManager();
            manager.load(f, Model.class);
            manager.finishLoading();
            return manager.get(f);

        }
    }

    @Override
    public void render() {


        camController.update();


        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }
}
