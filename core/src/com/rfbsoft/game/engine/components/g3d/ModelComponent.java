package com.rfbsoft.game.engine.components.g3d;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.utils.Array;


public class ModelComponent implements Component {
    //    public ModelInstance instance;
    private final float alphaTest = -1;
    public Array<ModelInstance> instances = new Array<>();

    public ModelComponent(ModelInstance instance) {
        addInstance(instance);
    }

    public ModelComponent(ModelInstance... instances) {
        addInstances(instances);
    }

    public void addInstance(ModelInstance instance) {
        for (Material mat : instance.materials) {
            mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
//            mat.set(new FloatAttribute(FloatAttribute.AlphaTest, alphaTest));
//            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
        }
        this.instances.add(instance);
    }

    public void addInstances(ModelInstance... instances) {
        for (ModelInstance instance : instances) {
            addInstance(instance);

        }
    }


}
