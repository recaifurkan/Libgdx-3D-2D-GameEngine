package com.rfbsoft.game.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.engine.common.Init;
import com.rfbsoft.game.engine.components.g3d.BulletRigidBodyComponent;
import com.rfbsoft.game.engine.entites.G3dEntites;
import com.rfbsoft.game.engine.systems.g3d.G3dGameSystems;
import com.rfbsoft.utils.ObjectAllocator;

public class CameraInputSystem extends EntitySystem {

    public CameraInputSystem() {

        Init.initCamController(GameFields.cam);
    }


   float distanceX = 0;
   float distanceZ = 20;
   float height = 5;



    Matrix4 carTemp = ObjectAllocator.getMatrix4();
    Vector3 carPosition = ObjectAllocator.getVector3();
    Vector3 tempVec = ObjectAllocator.getVector3();
    @Override
    public void update(float deltaTime) {
        if(GameFields.camController != null)
            GameFields.camController.update();
        //TODO  cameranın odak alacağı obje seçimi yapılacak.
        BulletRigidBodyComponent component = G3dEntites.car.getComponent(BulletRigidBodyComponent.class);
        Matrix4 carTransform = component.rigidBody.getWorldTransform();

        carTransform.getTranslation(carPosition);


        Camera cam = GameFields.cam;
        Vector3 camPosition = cam.position;

        carTemp.set(carTransform);
        carTemp.translate(distanceX,height,-distanceZ);
        carTemp.getTranslation(tempVec);

        // Assign value to Camera position
        camPosition.set(tempVec);


        cam.lookAt(tempVec.set(carPosition.x,camPosition.y,carPosition.z));



        cam.update();
    }
}
