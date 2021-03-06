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
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
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
public class DevCameraAppState extends AbstractAppState implements AnalogListener, ActionListener {

    private boolean follow;
    
    public enum InputMapping{
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
    private Node rotationNode;
    private InputManager inputManager;
    
    private Vector3f camLocation = new Vector3f(0, 20, 0);
    private Vector3f lookAtDirection = new Vector3f(0, -0.8f, -0.2f);
    private float camDistance = 20.0f;
    private float moveSpeed = 50.0f;
    float rotateSpeed;

    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.cam = app.getCamera();
        Vector3f lookAtDirection = new Vector3f(0, -0.8f, -0.2f);
        cam.lookAtDirection(lookAtDirection, Vector3f.UNIT_Y);
        camLocation.set(cam.getDirection().mult(-camDistance));
        cam.setLocation(camLocation);
        this.inputManager = app.getInputManager();
        addInputMappings();
    }
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.MoveLeft.name(), new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputMapping.MoveRight.name(), new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(InputMapping.MoveUp.name(), new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputMapping.MoveDown.name(), new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(InputMapping.RotateLeft.name(), new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping(InputMapping.RotateRight.name(), new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(this, new String[]{InputMapping.MoveLeft.name(), InputMapping.MoveRight.name(), InputMapping.MoveUp.name(), InputMapping.MoveDown.name(),InputMapping.RotateLeft.name(),InputMapping.RotateRight.name()});
        
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
        
        if(rotateLeft){
            rotate(rotateSpeed);
        } else if (rotateRight){
            rotate(-rotateSpeed);
        }
        
        //if(zoomIn){
            
        //} else if(zoomOut){
            
        //}
        
        cam.getRotation().multLocal(tempVector);
        tempVector.multLocal(1, 0, 1).normalizeLocal();
        tempVector.multLocal(tpf).multLocal(moveSpeed);
        camLocation.addLocal(tempVector);

        /**
         * Follow / Goto
         *
        if(tempVector.length() > 0){
            follow = false;
        }
        
        if(follow){
            Vector3f targetLocation2D = targetLocation.mult(UNIT_XZ);
            Vector3f camDirOffset = cam.getDirection().mult(camDistance);
            Vector3f camLocation2D = camLocation.add(camDirOffset).multLocal(UNIT_XZ); 
            if(targetLocation2D.distance(camLocation2D) > 0.01f){
                camLocation2D.interpolateLocal(targetLocation2D, 0.3f);
                camLocation.set(camLocation2D);
                camLocation.subtractLocal(camDirOffset);
            } else {
                camLocation2D.set(targetLocation2D);
            }
        }
        /**
         * 
         */
        
        /**
         * Follow terrain
         *
        camLocation.setY(checkHeight() + camDistance);
        /**
         * 
         */
        
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
    
    private void rotate(float value){
        Quaternion rotate = new Quaternion().fromAngleAxis(FastMath.PI * value, Vector3f.UNIT_Y);
        rotate.multLocal(cam.getRotation());
        cam.setRotation(rotate);
    }

    public void cameraLookAt(Vector3f target){
        cam.lookAt(target, Vector3f.UNIT_Y);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected boolean moveUp;
    protected boolean moveDown;
    protected boolean moveLeft;
    protected boolean moveRight;
    protected boolean rotateRight;
    protected boolean rotateLeft;
    protected boolean rotateUp;
    protected boolean rotateDown;

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
        }
    }
    /**
     * Follow / Go to
     *
    
    public Vector3f getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Vector3f targetLocation) {
        this.targetLocation = targetLocation;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
    
    /**
     * 
     */
    
    
    /**
     * Follow terrain
     *
    
    public void setTerrain(Spatial terrain){
        this.terrain = terrain;
    }
    
    public float checkHeight(){
        float height = 0;
        if(terrain != null){
            Ray ray = new Ray(cam.getLocation(), cam.getDirection());
            CollisionResults results = new CollisionResults();
            terrain.collideWith(ray, results);
            if(results.size() > 0){
                height = results.getClosestCollision().getContactPoint().y;
            }
        }
        return height;
    }
    /**
     * 
     */
}
