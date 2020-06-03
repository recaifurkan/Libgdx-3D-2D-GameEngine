package com.rfbsoft.game.engine.systems.g3d.debug;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.rfbsoft.game.engine.components.g3d.PathFindComponent;
import org.recast4j.detour.NavMesh;

import static com.rfbsoft.utils.g3d.DetourDebugRender.renderNavMesh;

public class DebugDetourSystem extends EntitySystem {

    private final ComponentMapper<PathFindComponent> modelMapper = ComponentMapper.getFor(PathFindComponent.class);
    private ImmutableArray<Entity> entities;
    // init primitive renderer


    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PathFindComponent.class).get());
        this.setProcessing(false);

    }

    @Override
    public void update(float deltaTime) {
//        renderer.setProjectionMatrix(GameFields.cam.combined);
        for (Entity entity : entities) {
            PathFindComponent pathFindComponent = modelMapper.get(entity);
            NavMesh mesh = pathFindComponent.navMesh;
            renderNavMesh(mesh, new Color(0x37C1B1));
        }


    }
}
