package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.components.g3d.MinimapFlagComponent;
import com.rfbsoft.utils.ObjectAllocator;

public class Scene2DMinimapSystem extends EntitySystem {

    private final ComponentMapper<BulletRigidBodyComponent> rigidBodyMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private final ComponentMapper<MinimapFlagComponent> minimapFlagComponentComponentMapper = ComponentMapper.getFor(MinimapFlagComponent.class);
    private ImmutableArray<Entity> entities;


    private Table table;
    private Image background;

    private Stack stack;


    private Table overlay;


    public static Texture createTexture(int width, int height, Color col,
                                        float alfa) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color color = col;
        pixmap.setColor(color.r, color.g, color.b, alfa);
        pixmap.fillRectangle(0, 0, width, height);

        Texture pixmaptexture = new Texture(pixmap);
        return pixmaptexture;
    }

    private Table buildControlsWindowLayer() {

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Window window = new Window("Deneme", skin);

        table = new Table();


        stack = new Stack();


        background = new Image(createTexture(200, 200, Color.WHITE, 1));


        stack.add(background);
        stack.setDebug(false, true);


        overlay = new Table();
        stack.add(overlay);


        this.table.add(stack);


        window.add(this.table);


        window.setKeepWithinStage(true);
        window.setMovable(true);
        window.setResizable(true);
        window.pack();
        return window;
    }


    public Scene2DMinimapSystem() {
        GameFields.stage.setDebugAll(false);
        GameFields.stage.addActor(buildControlsWindowLayer());

    }


    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(MinimapFlagComponent.class, BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {


        GameFields.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        GameFields.stage.draw();


        for (Entity entity : entities) {
            BulletRigidBodyComponent bulletRigidBodyComponent = rigidBodyMapper.get(entity);
            MinimapFlagComponent minimapFlagComponent = minimapFlagComponentComponentMapper.get(entity);

            Matrix4 worldTransform = bulletRigidBodyComponent.rigidBody.getWorldTransform();
            Vector3 position = ObjectAllocator.getVector3();
            position = worldTransform.getTranslation(position);
            final Image flag = minimapFlagComponent.flag;
            if (!minimapFlagComponent.isRegistered) {
                overlay.add(flag);
                minimapFlagComponent.isRegistered = true;
            }
            Vector2 snapShot = new Vector2(flag.getParent().getX() + position.x + 100, flag.getParent().getY() + position.z + 100);
            flag.setPosition(snapShot.x, snapShot.y);


        }

    }


}
