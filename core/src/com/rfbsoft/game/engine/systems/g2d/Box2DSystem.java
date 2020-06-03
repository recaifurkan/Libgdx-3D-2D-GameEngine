package com.rfbsoft.game.engine.systems.g2d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.GameInput;
import com.rfbsoft.game.GameMessages;
import com.rfbsoft.game.engine.components.g2d.Box2dBodyComponent;
import com.rfbsoft.game.engine.entites.GameEntity;
import com.rfbsoft.game.engine.systems.RayRequest;
import com.rfbsoft.utils.ObjectAllocator;


public class Box2DSystem extends EntitySystem implements EntityListener, Telegraph {
    public static World world;
    final String TAG = "com.rfbsoft.game.engine.systems.g2d.Box2DSystem";
    final float TIME_STEP = 1 / 60f;
    final int VELOCITY_ITERATIONS = 6;
    final int POSITION_ITERATIONS = 2;
    private final ComponentMapper<Box2dBodyComponent> bodyMapper = ComponentMapper.getFor(Box2dBodyComponent.class);
    Vector2 rayFrom = ObjectAllocator.getVector2();
    Vector2 rayTo = ObjectAllocator.getVector2();
    RayCastCallback rayCastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            Object userData = fixture.getBody().getUserData();
            GameEntity entity = (GameEntity) userData;
            MessageManager.getInstance().dispatchMessage(GameMessages.RAYRESULT, entity);
            Gdx.app
                    .debug(TAG, "Ray Test - " + entity.getName());

            return 0;
        }
    };
    private ImmutableArray<Entity> entities;
    private float accumulator = 0;
    private RayRequest rayRequest;


    public Box2DSystem() {

        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new ListenerClass());
        world.setGravity(ObjectAllocator.getVector2(0, -9.8f));
        MessageManager.getInstance().addListener(this, GameMessages.MOUSEEVENT);

    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.one(Box2dBodyComponent.class).get();
        entities = engine.getEntitiesFor(family);
        engine.addEntityListener(family, this);
    }

    @Override
    public void update(float deltaTime) {
        doPhysicsStep(deltaTime);

    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    @Override
    public boolean handleMessage(Telegram msg) {

        if (msg.message == GameMessages.MOUSEEVENT) {
            GameInput.MouseEventObject mouseEvent = (GameInput.MouseEventObject) msg.extraInfo;

            if (mouseEvent.button == Input.Buttons.LEFT) {
                rayRequest = RayRequest.REQUESTED;

                Vector3 unproject = GameFields.cam.unproject(new Vector3(mouseEvent.screenX, mouseEvent.screenY, 0));
                pointRay(unproject.x, unproject.y);
                rayFrom.set(unproject.x, unproject.y);
            }

            if (mouseEvent.button == Input.Buttons.RIGHT) {

                Vector3 unproject = GameFields.cam.unproject(new Vector3(mouseEvent.screenX, mouseEvent.screenY, 0));
                rayTo.set(unproject.x, unproject.y);
                if (rayRequest == RayRequest.REQUESTED) {
                    if (ray(mouseEvent.screenX, mouseEvent.screenY))
                        Gdx.app
                                .debug(TAG, "Debug");


                }
                rayRequest = RayRequest.IDLE;

            }


            return true;
        }

        return false;
    }

    private boolean pointRay(float x, float y) {
        return pointRayI((int) x, (int) y);
    }

    private boolean pointRayI(int x, int y) {
        Array<Body> bodyArray = new Array();
        world.getBodies(bodyArray);
        for (Body body : bodyArray) {
            Array<Fixture> fixtureList = body.getFixtureList();
            for (Fixture fixture : fixtureList) {
                boolean b = fixture.testPoint(x, y);
                if (b) {
                    return bodyPressed(body);
                }
            }
        }
        return false;
    }

    private boolean bodyPressed(Body body) {
        Object userData = body.getUserData();
        GameEntity entity = (GameEntity) userData;
        MessageManager.getInstance().dispatchMessage(GameMessages.RAYRESULT, entity);
        Gdx.app
                .debug(TAG, "Body pressed " + entity.getName());
        return true;
    }

    private boolean ray(int x, int y) {

        world.rayCast(rayCastCallback, rayFrom, rayTo);
        return true;
    }

    @Override
    public void entityAdded(Entity entity) {
//        Box2dBodyComponent box2dBodyComponent = bodyMapper.get(entity);
//        Body body = new Body();


    }

    @Override
    public void entityRemoved(Entity entity) {
        Box2dBodyComponent box2dBodyComponent = bodyMapper.get(entity);
        world.destroyBody(box2dBodyComponent.body);


    }

    public static class ListenerClass implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            Object colObject1 = contact.getFixtureA().getBody().getUserData();
            Object colObject2 = contact.getFixtureB().getBody().getUserData();
            if (colObject1 instanceof GameEntity &&
                    colObject2 instanceof GameEntity) {
                GameEntity entity0 = (GameEntity) colObject1;
                GameEntity entity1 = (GameEntity) colObject2;
                entity0.getPhysics().onCollisionStart(entity1);
                entity1.getPhysics().onCollisionStart(entity0);


            }

        }

        @Override
        public void endContact(Contact contact) {

            Object colObject1 = contact.getFixtureA().getBody().getUserData();
            Object colObject2 = contact.getFixtureB().getBody().getUserData();
            if (colObject1 instanceof GameEntity &&
                    colObject2 instanceof GameEntity) {
                GameEntity entity0 = (GameEntity) colObject1;
                GameEntity entity1 = (GameEntity) colObject2;
                entity0.getPhysics().onCollisionEnd(entity1);
                entity1.getPhysics().onCollisionEnd(entity0);


            }

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }


    }

}
