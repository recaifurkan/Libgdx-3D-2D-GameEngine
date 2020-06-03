package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import org.recast4j.detour.NavMesh;
import org.recast4j.detour.NavMeshQuery;

public class PathFindComponent implements Component {
    public NavMeshQuery query;
    public NavMesh navMesh;

    public PathFindComponent(NavMesh navMesh) {
        this.navMesh = navMesh;
        query = new NavMeshQuery(navMesh);

    }
}
