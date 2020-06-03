package com.rfbsoft.utils;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;

public class ObjectAllocator {
    static ArrayList<Object> objects = new ArrayList<>();

    public static <T> T getObject(Class<T> clazz) {
        T obtain = Pools.get(clazz).obtain();
        objects.add(obtain);
        return obtain;

    }

    public static void free(Object... object) {
        for (Object obj : object) {
            objects.remove(obj);
            Pools.free(obj);
        }
    }

    public static void freeAllInPool() {
        for (Object obj : objects) {
            Pools.free(obj);
        }
        objects.clear();

    }

    public static Vector2 getVector2() {
        return getObject(Vector2.class);
    }

    public static Vector2 getVector2(float x, float y) {
        return getObject(Vector2.class).set(x, y);
    }

    public static Matrix3 getMatrix3() {
        return getObject(Matrix3.class);
    }

    public static Vector3 getVector3() {
        return getObject(Vector3.class);
    }

    public static Vector3 getVector3(float x, float y, float z) {
        return getObject(Vector3.class).set(x, y, z);
    }

    public static Matrix4 getMatrix4() {
        return getObject(Matrix4.class);
    }
}
