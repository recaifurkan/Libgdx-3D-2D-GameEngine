package com.rfbsoft.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import org.recast4j.detour.NavMesh;
import org.recast4j.detour.io.MeshSetReader;

import java.io.IOException;
import java.io.InputStream;

public class NavMeshLoader extends AsynchronousAssetLoader<NavMesh, NavMeshLoader.NavMeshParameters> {


    public NavMeshLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, NavMeshParameters parameter) {
    }

    @Override
    public NavMesh loadSync(AssetManager manager, String fileName, FileHandle file, NavMeshParameters parameter) {
        InputStream reader = file.read();
        MeshSetReader meshReader = new MeshSetReader();
        NavMesh navMesh = null;
        try {

            navMesh = meshReader.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return navMesh;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, NavMeshParameters parameter) {
        return null;
    }

    public static class NavMeshParameters extends AssetLoaderParameters<NavMesh> {

    }
}
