package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.common.Init;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;
import com.rfbsoft.utils.ObjectAllocator;

public class BulletPositionSystem extends EntitySystem  {


    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidBodyMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private ImmutableArray<Entity> entities;



    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelComponent.class,BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {



        for (Entity entity : entities) {
            ModelComponent modelComponent = modelMapper.get(entity);
            BulletRigidBodyComponent bulletRigidBodyComponent = rigidBodyMapper.get(entity);
            Matrix4 worldTransform = bulletRigidBodyComponent.rigidBody.getWorldTransform();
            Vector3 position = ObjectAllocator.getVector3();
            position = worldTransform.getTranslation(position);
            Matrix4 tmp = worldTransform.cpy();
            tmp.setTranslation(position);
            modelComponent.instances.get(0).transform = tmp;

        }



    }


}
