package com.rfbsoft.utils.g3d;

import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.rfbsoft.game.engine.components.g3d.PathFindComponent;
import com.rfbsoft.game.engine.entites.initializers.g3d.SteeringBulletEntity;
import org.recast4j.detour.DefaultQueryFilter;
import org.recast4j.detour.NavMeshQuery;
import org.recast4j.detour.Result;
import org.recast4j.detour.StraightPathItem;

import java.util.List;

public class PathFindUtils {
    private static final float[] m_polyPickExt = new float[]{2, 4, 2};
    private static final DefaultQueryFilter m_filter = new DefaultQueryFilter();
    private static final Matrix3 tmpMatrix3 = new Matrix3();
    private static final Vector2 tmpVector2 = new Vector2();
    private static final int MAX_STEER_POINTS = Integer.MAX_VALUE;
    public static float timeToTarget = 0.1f;
    public static float arrivalTolerance = 0.1f;
    public static float decelerationRadius = 0.5f;
    public static float predictionTime = 0f;
    public static float crouchMultiplier = 0.5f;
    static boolean openPath = true;
    private static long m_startRef;
    private static long m_endRef;
    private static List<Long> m_polys;

    public static Array<Vector3> findPath(PathFindComponent component, Vector3 startPos, Vector3 endPos) {
        Array<Vector3> pathCoords = new Array<>();
        NavMeshQuery m_navQuery = component.query;
        float[] start = vec3ToF3(startPos);
        float[] end = vec3ToF3(endPos);

        m_startRef = m_navQuery.findNearestPoly(start, m_polyPickExt, m_filter).result.getNearestRef();
        m_endRef = m_navQuery.findNearestPoly(end, m_polyPickExt, m_filter).result.getNearestRef();

        Result<List<Long>> path = m_navQuery.findPath(m_startRef, m_endRef, start, end, m_filter);

        m_polys = path.result;


        Result<List<StraightPathItem>> result = m_navQuery.findStraightPath(start, end, m_polys, MAX_STEER_POINTS, 0);
        if (result.succeeded()) {
            List<StraightPathItem> straightPath = result.result;
            for (int i = 0; i < straightPath.size(); i++) {
                Vector3 coord = new Vector3();
                coord.x = straightPath.get(i).getPos()[0];
                coord.y = straightPath.get(i).getPos()[1];
                coord.z = straightPath.get(i).getPos()[2];
                pathCoords.add(coord);

            }

        }
        return pathCoords;


    }

    public static void goPath(SteeringBulletEntity steeringBulletEntity, Array<Vector3> pathList) {

        LinePath<Vector3> linePath = new LinePath<Vector3>(pathList, openPath);


        final LookWhereYouAreGoing<Vector3> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector3>(steeringBulletEntity) //
                .setAlignTolerance(.001f) //
                .setDecelerationRadius(7) //
                .setTimeToTarget(timeToTarget / 10);
        FollowPath<Vector3, LinePath.LinePathParam> followPathSB
                = new FollowPath<Vector3, LinePath.LinePathParam>(steeringBulletEntity, linePath, 3) //
                // Setters below are only useful to arrive at the end of an open path
                .setTimeToTarget(timeToTarget * crouchMultiplier) //
                .setArrivalTolerance(arrivalTolerance * crouchMultiplier) //
                .setDecelerationRadius(decelerationRadius * crouchMultiplier)
                .setPredictionTime(predictionTime * crouchMultiplier);
//        float mass = rigidBody.getInvMass();
//        mass = 1 / mass;
        BlendedSteering<Vector3> blendedSteering = new BlendedSteering<Vector3>(steeringBulletEntity) //
                .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
                .add(followPathSB, 1) //
                .add(lookWhereYouAreGoingSB, 4);

        steeringBulletEntity.setSteeringBehavior(blendedSteering);
//        steeringBulletEntity.setSteeringBehavior(followPathSB);
    }

    /**
     * Creates a random path which is bound by rectangle described by the min/max values
     */
    private static Array<Vector3> createRandomPath(int numWaypoints, float minX, float minY, float maxX, float maxY, float height) {
        Array<Vector3> wayPoints = new Array<Vector3>();

        float midX = (maxX + minX) / 2f;
        float midY = (maxY + minY) / 2f;

        float smaller = Math.min(midX, midY);

        float spacing = MathUtils.PI2 / numWaypoints;

        for (int i = 0; i < numWaypoints; i++) {
            float radialDist = MathUtils.random(smaller * 0.2f, smaller);
            tmpVector2.set(radialDist, 0.0f);

            // rotates the specified vector angle rads around the origin
            // init and rotate the transformation matrix
            tmpMatrix3.idt().rotateRad(i * spacing);
            // now transform the object's vertices
            tmpVector2.mul(tmpMatrix3);

            wayPoints.add(new Vector3(tmpVector2.x, height, tmpVector2.y));
        }

        return wayPoints;
    }

    public static float[] vec3ToF3(Vector3 vector3) {
        float[] ret = new float[3];
        ret[0] = vector3.x;
        ret[1] = vector3.y;
        ret[2] = vector3.z;
        return ret;
    }
}
