package com.rfbsoft.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;


public class TextFileLoader extends AsynchronousAssetLoader<String, TextFileLoader.TextParameter> {


    public TextFileLoader(FileHandleResolver resolver) {

        super(resolver);

    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {


    }

    @Override
    public String loadSync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {


        return file.readString();

    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextParameter parameter) {

        return null;

    }

    public static class TextParameter extends AssetLoaderParameters<String> {

    }

}