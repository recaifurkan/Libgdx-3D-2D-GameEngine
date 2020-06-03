package com.rfbsoft.game.engine.systems.g3d;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.factories.g3d.character.Character;
import com.rfbsoft.factories.g3d.character.CharacterAnimationState;
import com.rfbsoft.game.engine.components.g3d.AnimationComponent;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;

import static com.rfbsoft.utils.g3d.BulletUtils.getZeroLinearSpeedThreshold;

public class CharacterAnimationSystem extends EntitySystem {
    final float animationTime = 5;
    private final ComponentMapper<AnimationComponent.AnimationStateComponent> modelMapper =
            ComponentMapper.getFor(AnimationComponent.AnimationStateComponent.class);

    private final ComponentMapper<BulletRigidBodyComponent> collisionMapper =
            ComponentMapper.getFor(BulletRigidBodyComponent.class);
    float timeElapsed;
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AnimationComponent.AnimationStateComponent.class,
                BulletRigidBodyComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
//        timeElapsed += deltaTime;
//        if(timeElapsed < animationTime)return;
//        timeElapsed = 0;
        for (Entity entity : entities) {
            if (entity instanceof Character) {
                AnimationComponent.AnimationStateComponent animationStateComponent = modelMapper.get(entity);
                BulletRigidBodyComponent bulletCollisionComponent = collisionMapper.get(entity);
                btRigidBody rigidBody = bulletCollisionComponent.rigidBody;
                /*
                Aşağıda bakıyoruz
                hızı sıfır mı diye
                hızı sıfır değilse ve durur vaziyetteyse animasyon durumunu yürüyor yapıyoruz
                hızı sıfırsa ve yürüyorsa durur vaziyete getiriyoruz
                 */
                Vector3 linVel = rigidBody.getLinearVelocity();
                if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                    if (animationStateComponent.stateMachine.getCurrentState() == CharacterAnimationState.IDLE)
                        animationStateComponent.stateMachine.changeState(CharacterAnimationState.WALK);
                } else {
                    if (animationStateComponent.stateMachine.getCurrentState() == CharacterAnimationState.WALK)
                        animationStateComponent.stateMachine.changeState(CharacterAnimationState.IDLE);

                }


            }
        }
    }
}
