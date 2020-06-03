package com.rfbsoft.utils.g3d;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.rfbsoft.utils.ObjectAllocator;

public class BulletUtils {

    public static btConvexHullShape createConvexHullShape(final Mesh mesh, boolean optimize) {
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // now optimize the shape
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);
        // delete the temporary shape
        shape.dispose();
        hull.dispose();
        return result;
    }

    public static btBvhTriangleMeshShape createBvhTriangleMeshShape(Array<MeshPart> meshParts) {

        btTriangleIndexVertexArray vertexArray = new btTriangleIndexVertexArray(meshParts);
//        vertexArray.setScaling(new Vector3(100, 100, 100));
        btBvhTriangleMeshShape btBvhTriangleMeshShape = new btBvhTriangleMeshShape(vertexArray, false);
//        vertexArray.dispose();

        return btBvhTriangleMeshShape;

    }

    public static btBoxShape modelToBoxShape(Model model) {
        BoundingBox boundingBox = ObjectAllocator.getObject(BoundingBox.class);
        Vector3 dimensions = ObjectAllocator.getObject(Vector3.class);
        dimensions = model.calculateBoundingBox(boundingBox).getDimensions(dimensions);
        dimensions.x = dimensions.x / 2;
        dimensions.y = dimensions.y / 2;
        dimensions.z = dimensions.z / 2;
        btBoxShape shape = new btBoxShape(dimensions);
        ObjectAllocator.freeAllInPool();
        return shape;
    }

    public static btSphereShape modelToSphereShape(Model model) {
        BoundingBox boundingBox = ObjectAllocator.getObject(BoundingBox.class);
        Vector3 dimensions = ObjectAllocator.getObject(Vector3.class);
        dimensions = model.calculateBoundingBox(boundingBox).getDimensions(dimensions);
        dimensions.x = dimensions.x / 2;
        btSphereShape btSphereShape = new btSphereShape(dimensions.x);
        ObjectAllocator.freeAllInPool();
        return btSphereShape;

    }

    public static btCapsuleShape modelToCapsuleShape(Model model) {
        BoundingBox boundingBox = ObjectAllocator.getObject(BoundingBox.class);
        Vector3 dimensions = ObjectAllocator.getObject(Vector3.class);
        dimensions = model.calculateBoundingBox(boundingBox).getDimensions(dimensions);


        float radius = Math.max(dimensions.x, dimensions.z);
        btCapsuleShape shape = new btCapsuleShape(radius / 3, dimensions.y / 6);
        return shape;

    }

//    public static float vectorToAngle (Vector3 vector) {
//// return (float)Math.atan2(vector.z, vector.x);
//        return (float)Math.atan2(-vector.z, vector.x);
//    }
//
//    public static Vector3 angleToVector (Vector3 outVector, float angle) {
//// outVector.set(MathUtils.cos(angle), 0f, MathUtils.sin(angle));
//        outVector.z = -(float)Math.sin(angle);
//        outVector.y = 0;
//        outVector.x = (float)Math.cos(angle);
//        return outVector;
//    }

    public static float getZeroLinearSpeedThreshold() {
        return 0.1f;
    }

    public static float vectorToAngle(Vector3 vector) {
// return (float)Math.atan2(vector.z, vector.x);
        return (float) Math.atan2(vector.x, vector.z);
    }

    public static Vector3 angleToVector(Vector3 outVector, float angle) {
// outVector.set(MathUtils.cos(angle), 0f, MathUtils.sin(angle));
        outVector.x = -(float) Math.sin(angle);
        outVector.y = 0;
        outVector.z = (float) Math.cos(angle);
        return outVector;
    }

    public static class BulletLocation implements Location<Vector3> {

        Vector3 position;
        float orientation;

        public BulletLocation() {
            this(new Vector3());
            this.orientation = 0;
        }

        public BulletLocation(Vector3 position) {
            this.position = position;
        }

        @Override
        public Vector3 getPosition() {
            return position;
        }

        @Override
        public float getOrientation() {
            return orientation;
        }

        @Override
        public void setOrientation(float orientation) {
            this.orientation = orientation;
        }

        @Override
        public Location<Vector3> newLocation() {
            return new BulletLocation();
        }

        @Override
        public float vectorToAngle(Vector3 vector) {
            return BulletUtils.vectorToAngle(vector);
        }

        @Override
        public Vector3 angleToVector(Vector3 outVector, float angle) {
            return BulletUtils.angleToVector(outVector, angle);
        }

    }
}
