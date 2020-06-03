package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.rfbsoft.game.engine.components.g3d.PathFindComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.entites.initializers.IEntityInitializer;
import org.recast4j.detour.NavMesh;

public class PathFindInitiliazer implements IEntityInitializer {
    PathFindComponent navMeshComponent;

    public PathFindInitiliazer(NavMesh navMesh) {
        navMeshComponent = new PathFindComponent(navMesh);
    }

    @Override
    public void initialize(GameEntity object) {
        object.add(navMeshComponent);

    }
}
