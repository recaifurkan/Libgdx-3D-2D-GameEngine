package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.rfbsoft.game.GameInput;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.components.g3d.ModelComponent;
import com.rfbsoft.game.engine.components.g3d.PathFindComponent;
import com.rfbsoft.game.engine.components.g3d.SteeringComponent;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.game.engine.entites.initializers.g3d.SteeringBulletEntity;
import com.rfbsoft.utils.ObjectAllocator;
import com.rfbsoft.utils.g3d.BulletUtils;
import com.rfbsoft.utils.g3d.PathFindUtils;

public class SteeringSystem extends EntitySystem implements Telegraph {
    private static final ComponentMapper<SteeringComponent> steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
    private static final ComponentMapper<BulletRigidBodyComponent> collisionMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private static final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);
    private static final String TAG = "com.rfbsoft.game.engine.systems.g3d.SteeringSystem";
    private ImmutableArray<Entity> entities;

    public SteeringSystem() {
        MessageManager.getInstance().addListener(this, GameMessages.MOUSEEVENT);

    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SteeringComponent.class,
                BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            SteeringComponent steeringComponent = steeringMapper.get(entity);
            steeringComponent.steeringBulletEntity.update(deltaTime);


        }
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == GameMessages.MOUSEEVENT) {
            GameInput.MouseEventObject mouseEventObject = (GameInput.MouseEventObject) msg.extraInfo;
            if (mouseEventObject.button == Input.Buttons.MIDDLE) {
                Gdx.app.debug(TAG, "Middle Mouse");
                ClosestRayResultCallback ray = BulletSystem.ray(mouseEventObject.screenX, mouseEventObject.screenY);
                if (ray == null) return true;
                Gdx.app.debug(TAG, "Ray True");
                for (Entity entity : entities) {
//                    logger.debug("Entity {}" , entity.toString());
                    SteeringComponent steeringComponent = steeringMapper.get(entity);
//                    ModelComponent modelComponent = modelMapper.get(entity);
                    BulletRigidBodyComponent bulletRigidBodyComponent = collisionMapper.get(entity);


                    PathFindComponent component = G3dEntites.terrain.getComponent(PathFindComponent.class);
                    Vector3 startCoord = ObjectAllocator.getObject(Vector3.class);
                    Vector3 endCoord = ObjectAllocator.getObject(Vector3.class);
                    Matrix4 worldTransform = bulletRigidBodyComponent.rigidBody.getWorldTransform();
                    Vector3 vector3 = ObjectAllocator.getVector3();
                    vector3 = worldTransform.getTranslation(vector3);

                    startCoord = vector3;
//                    steeringBulletEntity.body.getWorldTransform().getTranslation(startCoord);
                    ray.getHitPointWorld(endCoord);
                    Array<Vector3> path = PathFindUtils.findPath(component,
                            startCoord, endCoord);
//                    setArrive(entity, steeringComponent);
                    if (path != null && !path.isEmpty())
                        PathFindUtils.goPath(steeringComponent.steeringBulletEntity, path);

//                    steeringBulletEntity.setSteeringBehavior(faceSB);

                }

            }
        }
        return false;
    }

    private void setArrive(Entity entity, SteeringComponent steeringComponent) {
        BulletRigidBodyComponent bulletCollisionComponent = collisionMapper.get(entity);
        btRigidBody rigidBody = bulletCollisionComponent.rigidBody;
        SteeringBulletEntity steeringBulletEntity = steeringComponent.steeringBulletEntity;
        Vector3 vector3 = ObjectAllocator.getObject(Vector3.class);
        BulletSystem.rayTestCB.getHitPointWorld(vector3);
//                    final Face<Vector3> faceSB = new Face<Vector3>(steeringBulletEntity,
//                            new BulletUtils.BulletLocation(vector3)) //
//                            .setAlignTolerance(.01f) //
//                            .setDecelerationRadius(MathUtils.PI) //
//                            .setTimeToTarget(.18f);

        float mass = rigidBody.getInvMass();
        final LookWhereYouAreGoing<Vector3> lookWhereYouAreGoingSB = new LookWhereYouAreGoing<Vector3>(steeringBulletEntity) //
                .setAlignTolerance(.005f) //
                .setDecelerationRadius(MathUtils.PI) //
                .setTimeToTarget(mass);


        Arrive<Vector3> arriveSB = new Arrive<Vector3>(steeringBulletEntity, new BulletUtils.BulletLocation(vector3)) //
                .setTimeToTarget(0.1f) //
                .setArrivalTolerance(0.0002f) //
                .setDecelerationRadius(3);


        BlendedSteering<Vector3> blendedSteering = new BlendedSteering<Vector3>(steeringBulletEntity) //
                .setLimiter(NullLimiter.NEUTRAL_LIMITER) //
                .add(arriveSB, 1) //
                .add(lookWhereYouAreGoingSB, 1);

        steeringBulletEntity.setSteeringBehavior(blendedSteering);
    }
}
