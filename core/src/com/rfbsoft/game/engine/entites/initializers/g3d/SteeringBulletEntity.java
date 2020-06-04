package com.rfbsoft.game.engine.entites.initializers.g3d;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.utils.g3d.BulletUtils;

/**
 * @author Daniel Holderbaum
 */
public class SteeringBulletEntity implements Steerable<Vector3> {

    private static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<Vector3>(new Vector3());
    private static final Quaternion tmpQuaternion = new Quaternion();
    private static final Matrix4 tmpMatrix4 = new Matrix4();
    private static final Vector3 ANGULAR_LOCK = new Vector3(0, 1, 0);
    private final Vector3 tmpVector3 = new Vector3();
    public btRigidBody body;
    protected SteeringBehavior<Vector3> steeringBehavior;
    float maxLinearSpeed;
    float maxLinearAcceleration;
    float maxAngularSpeed;
    float maxAngularAcceleration;
    float boundingRadius;
    boolean tagged;
    boolean independentFacing;

    public SteeringBulletEntity(btRigidBody collisionObject) {
        this(collisionObject, false);
    }

    public SteeringBulletEntity(btRigidBody collisionObject, boolean independentFacing) {


        if (!(collisionObject instanceof btRigidBody)) {
            throw new IllegalArgumentException("Body must be a rigid body.");
        }
// if (copyEntity.body.isStaticOrKinematicObject()) {
// throw new IllegalArgumentException("Body must be a dynamic body.");
// }

        this.body = collisionObject;
        body.setAngularFactor(ANGULAR_LOCK);

        this.independentFacing = independentFacing;
    }

    public SteeringBehavior<Vector3> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector3> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

//	float oldOrientation = 0;

    public void update(float deltaTime) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

            /*
             * Here you might want to add a motor control layer filtering steering accelerations.
             *
             * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
             * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
             * accelerate; and it only moves in the direction it is facing (ignoring power slides).
             */

            // Apply steering acceleration
            applySteering(steeringOutput, deltaTime);
        }
    }

    protected void applySteering(SteeringAcceleration<Vector3> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyCentralForce(steeringOutput.linear);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(tmpVector3.set(0, steeringOutput.angular, 0));
                anyAccelerations = true;
            }
        } else {
            // If we haven't got any velocity, then we can do nothing.
            Vector3 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                //
                // TODO: Commented out!!!
                // Looks like the code below creates troubles in combination with the applyCentralForce above
                // Maybe we should be more consistent by only applying forces or setting velocities.
                //
//				float newOrientation = vectorToAngle(linVel);
//				Vector3 angVel = body.getAngularVelocity();
//				angVel.y = (newOrientation - oldOrientation) % MathUtils.PI2;
//				if (angVel.y > MathUtils.PI) angVel.y -= MathUtils.PI2;
//				angVel.y /= deltaTime;
//				body.setAngularVelocity(angVel);
//				anyAccelerations = true;
//				oldOrientation = newOrientation;
            }
        }
        if (anyAccelerations) {
            body.activate();

            // TODO:
            // Looks like truncating speeds here after applying forces doesn't work as expected.
            // We should likely cap speeds form inside an InternalTickCallback, see
            // http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

            // Cap the linear speed
            Vector3 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }

            // Cap the angular speed
            Vector3 angVelocity = body.getAngularVelocity();
            if (angVelocity.y > getMaxAngularSpeed()) {
                angVelocity.y = getMaxAngularSpeed();
                body.setAngularVelocity(angVelocity);
            }
        }
    }

    /**
     * @return True if linear velocity of body is not within threshold of zero
     */
    public boolean isMoving() {
        return !body.getLinearVelocity().isZero(BulletUtils.getZeroLinearSpeedThreshold());
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    @Override
    public float getOrientation() {
        Matrix4 transform = body.getWorldTransform();
        transform.getRotation(tmpQuaternion, true);
        return tmpQuaternion.getYawRad();
    }

    @Override
    public void setOrientation(float orientation) {
        Matrix4 transform = body.getWorldTransform();
        transform.setToRotationRad(0, 1, 0, orientation);
        body.setWorldTransform(transform);
    }

    @Override
    public Vector3 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        Vector3 angularVelocity = body.getAngularVelocity();
        return angularVelocity.y;
    }

    @Override
    public float getBoundingRadius() {
        // TODO: this should be calculated via the actual btShape
        return .5f;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector3> newLocation() {
        return new BulletUtils.BulletLocation();
    }

    @Override
    public float vectorToAngle(Vector3 vector) {
        return BulletUtils.vectorToAngle(vector);
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        return BulletUtils.angleToVector(outVector, angle);
    }

    @Override
    public Vector3 getPosition() {
        Matrix4 tmpMatrix4=body.getWorldTransform();
        return tmpMatrix4.getTranslation(tmpVector3);
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return BulletUtils.getZeroLinearSpeedThreshold();
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

}