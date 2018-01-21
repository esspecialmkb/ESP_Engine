/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devCamera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author esspe
 */
public class AnimDev_CamState extends AbstractAppState implements AnalogListener, ActionListener {

    private boolean follow;
    private boolean freeLook = false;
    private boolean rotateLook = false;
    private float camXAngle;
    private float camYAngle;
    
    public enum InputMapping{
        MouseLeft,
        MouseRight,
        MouseUp,
        MouseDown,
        SelectJoint,
        CameraLook,
        CameraRotate,
        MoveLeft,
        MoveRight,
        MoveUp,
        MoveDown,
        RotateLeft,
        RotateRight,
        RotateUp,
        RotateDown;
    }
    
    private Camera cam;
    private Node translateNode;
    private Node rotateNode;
    private Node camPosTarget;
    private InputManager inputManager;
    
    private Vector3f camLocation = new Vector3f(0, 10f, 40);
    private Vector3f lookAtDirection = new Vector3f(0, 0, -0.2f);
    private float camDistance = 10.0f;
    private float moveSpeed = 50.0f;
    float rotateSpeed;
    private Vector2f cursorPosition;
    private Vector2f lastCursorPosition;

    protected boolean moveUp;
    protected boolean moveDown;
    protected boolean moveLeft;
    protected boolean moveRight;
    protected boolean rotateRight;
    protected boolean rotateLeft;
    protected boolean rotateUp;
    protected boolean rotateDown;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.cam = app.getCamera();
        
        translateNode = new Node();
        rotateNode = new Node();
        camPosTarget = new Node();
        
        camXAngle = 0;
        camYAngle = 0;
        
        translateNode.setLocalTranslation(0,0,0);
        rotateNode.setLocalTranslation(0, 14f, 0);
        camPosTarget.setLocalTranslation(0,0,40);
        translateNode.attachChild(rotateNode);
        rotateNode.attachChild(camPosTarget);
        
        //Vector3f lookAtDirection = new Vector3f(0, 0f, -1f);
        cam.lookAtDirection(lookAtDirection, Vector3f.UNIT_Y);
        //camLocation.set(cam.getDirection().mult(-camDistance));
        cam.setLocation(camLocation);
        this.inputManager = app.getInputManager();
        addInputMappings();
    }
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.MoveLeft.name(), new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputMapping.MoveRight.name(), new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(InputMapping.MoveUp.name(), new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputMapping.MoveDown.name(), new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputManager.addMapping(InputMapping.CameraLook.name(), new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping(InputMapping.CameraRotate.name(), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        //inputManager.addMapping(InputMapping.SelectJoint.name(), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addMapping(InputMapping.MouseLeft.name(), new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(InputMapping.MouseRight.name(), new MouseAxisTrigger(MouseInput.AXIS_X, false));
        
        inputManager.addMapping(InputMapping.MouseUp.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(InputMapping.MouseDown.name(), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        //inputManager.addMapping(InputMapping.RotateLeft.name(), new KeyTrigger(KeyInput.KEY_E));
        //inputManager.addMapping(InputMapping.RotateRight.name(), new KeyTrigger(KeyInput.KEY_Q));
        //inputManager.addMapping(InputMapping.RotateUp.name(), new KeyTrigger(KeyInput.KEY_R));
        //inputManager.addMapping(InputMapping.RotateDown.name(), new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, new String[]{InputMapping.MoveLeft.name(), InputMapping.MoveRight.name(), InputMapping.MoveUp.name(), InputMapping.MoveDown.name(), 
                                                    InputMapping.CameraRotate.name(), InputMapping.CameraLook.name(),
                                                    InputMapping.MouseDown.name(), InputMapping.MouseUp.name(), InputMapping.MouseLeft.name(), InputMapping.MouseRight.name()});
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        for (InputMapping i : InputMapping.values()) {
            if (inputManager.hasMapping(i.name())) {
                inputManager.deleteMapping(i.name());
            }
        }
        inputManager.removeListener(this);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        camLocation = cam.getLocation();
        Vector3f tempVector = new Vector3f();
        
        checkMousePosition(tempVector);
        
        if(freeLook == true){
            // If free look is turned on, allow the camera to move as such
            if(moveUp){
                tempVector.addLocal(0, 0, 1f);
            } else if(moveDown){
                tempVector.addLocal(0, 0, -1f);
            }
            if(moveLeft){
                tempVector.addLocal(1f, 0, 0);
            } else if (moveRight){
                tempVector.addLocal(-1f, 0, 0);
            }
        }if(rotateLook == true){
            //System.out.println("rotateLook " + camYAngle);
            // Rotate the camera with the mouse when RMB is down
            cursorPosition = inputManager.getCursorPosition();
            rotateY(camYAngle);
            rotateX(camXAngle);
            camLocation = camPosTarget.getWorldTranslation();
            
            cameraLookAt(new Vector3f(0,10f,0));
            
            //System.out.println(lastCursorPosition);
        }
        
        cam.setLocation(camLocation);
    }
    
    private void checkMousePosition(Vector3f tempVector){
        Vector2f mousePos2D = inputManager.getCursorPosition();
        if(mousePos2D.x > 0 && mousePos2D.x < cam.getWidth() / 10f){
            tempVector.addLocal(1f, 0, 0);
        } else if(mousePos2D.x < cam.getWidth() && mousePos2D.x > cam.getWidth() - cam.getWidth() / 10f){
            tempVector.addLocal(-1f, 0, 0);
        }
        if(mousePos2D.y > 0 && mousePos2D.y < cam.getHeight() / 10f){
            tempVector.addLocal(0, 0, -1f);
        } else if(mousePos2D.y < cam.getHeight() && mousePos2D.y > cam.getHeight() - cam.getHeight() / 10f){
            tempVector.addLocal(0, 0, 1f);
        }
    }
    
    private void rotateY(float valueY){
        Quaternion rotateY = new Quaternion().fromAngleAxis(valueY, Vector3f.UNIT_Y);
        Quaternion worldRotation = rotateNode.getWorldRotation();
        rotateNode.setLocalRotation(worldRotation.mult(rotateY));
    }
    
    private void rotateX(float valueX){
        Quaternion rotateX = new Quaternion().fromAngleAxis(valueX, Vector3f.UNIT_X);
        Quaternion worldRotation = rotateNode.getWorldRotation();
        rotateNode.setLocalRotation(worldRotation.mult(rotateX));
    }

    public void cameraLookAt(Vector3f target){
        cam.lookAt(target, Vector3f.UNIT_Y);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        InputMapping input = InputMapping.valueOf(name);
        //System.out.println(input.name());
        //value = value * 100;
        if(value > 0.005f){
            switch(input){
                case MouseLeft:
                    if(rotateLook == true){
                        //camYAngle = camYAngle + value;
                        //rotateY(value * 100);
                        camYAngle = value;
                        //System.out.println("onAnalog " + value);
                    }
                    //System.out.println("onAnalog Post " + value);
                    break;
                case MouseRight:
                    if(rotateLook){
                        camYAngle = -value;
                        //rotateY(value);
                    }

                    break;
                case MouseUp:
                    if(rotateLook){
                        camXAngle = value;
                    }

                    break;
                case MouseDown:
                    if(rotateLook){
                        camXAngle = -value;
                    }

                    break;
            }
        }
        
    }
    
    

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        InputMapping input = InputMapping.valueOf(name);
        switch(input){
            case MoveUp:
                moveUp = isPressed;
                break;
            case MoveDown:
                moveDown = isPressed;
                break;
            case MoveLeft:
                moveLeft = isPressed;
                break;
            case MoveRight:
                moveRight = isPressed;
                break;
            case RotateRight:
                rotateRight = isPressed;
                break;
            case RotateLeft:
                rotateLeft = isPressed;
                break;
            case RotateUp:
                rotateUp = isPressed;
                break;
            case RotateDown:
                rotateDown = isPressed;
                break;
            case SelectJoint:
                // Do a ray-cast here to select a joint
                cursorPosition = inputManager.getCursorPosition();
                break;
            case CameraLook:
                freeLook = isPressed;
                break;
            case CameraRotate:
                if((isPressed == false)){
                    camYAngle = 0;
                    camXAngle = 0;
                }
                rotateLook = isPressed;
                break;
        }
    }
}
