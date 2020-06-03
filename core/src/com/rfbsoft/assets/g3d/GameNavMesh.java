package com.rfbsoft.assets.g3d;

import com.rfbsoft.assets.GameAsset;
import org.recast4j.detour.NavMesh;

public class GameNavMesh extends GameAsset<NavMesh> {
    public GameNavMesh(String fileName) {
        super(fileName, NavMesh.class);
    }

    @Override
    public NavMesh get() {
        return super.get();
    }
}
