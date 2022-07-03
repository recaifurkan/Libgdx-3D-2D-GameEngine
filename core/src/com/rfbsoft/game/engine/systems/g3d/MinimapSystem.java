package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.utils.ObjectAllocator;

public class MinimapSystem extends EntitySystem {


    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidBodyMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private ImmutableArray<Entity> entities;


    private final ShapeRenderer shape;
    private final Stage stage;

    public MinimapSystem() {
        shape = new ShapeRenderer();
        stage = new Stage();
        stage.setDebugAll(true);
    }


    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class, BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        stage.draw();

        shape.setProjectionMatrix(stage.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : entities) {
            BulletRigidBodyComponent bulletRigidBodyComponent = rigidBodyMapper.get(entity);

            Matrix4 worldTransform = bulletRigidBodyComponent.rigidBody.getWorldTransform();
            Vector3 position = ObjectAllocator.getVector3();
            position = worldTransform.getTranslation(position);
            if (G3dEntites.car.getComponent(BulletRigidBodyComponent.class).equals(bulletRigidBodyComponent)) {
                shape.setColor(Color.CYAN);
            } else {
                shape.setColor(Color.RED);
            }
            shape.rect(100 + position.x, 100 + position.z, 5, 5);
        }

        shape.end();

        shape.setProjectionMatrix(stage.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.YELLOW);
        shape.rect(0, 0, 200, 200);
        shape.end();


    }


}
