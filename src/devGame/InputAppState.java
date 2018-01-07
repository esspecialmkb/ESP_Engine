/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devGame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;

/**
 *
 * @author esspe
 */
public class InputAppState extends AbstractAppState implements ActionListener, AnalogListener{
    public enum InputMapping{
        RotateLeft,
        RotateRight,
        LookUp,
        LookDown,
        StrafeLeft,
        StrafeRight,
        MoveForward,
        MoveBackward;
    }
    
    private InputManager inputManager;
    private GameCharacterControl character;
    private float sensitivity;
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.RotateLeft.name(),new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(InputMapping.RotateRight.name(),new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(InputMapping.LookUp.name(),new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(InputMapping.LookDown.name(),new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(InputMapping.StrafeLeft.name(), new KeyTrigger(KeyInput.KEY_A) , new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(InputMapping.StrafeLeft.name(), new KeyTrigger(KeyInput.KEY_D) , new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(InputMapping.MoveForward.name(),new KeyTrigger(KeyInput.KEY_W) , new  KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(InputMapping.MoveBackward.name(),new KeyTrigger(KeyInput.KEY_S) , new  KeyTrigger(KeyInput.KEY_DOWN));
        
        for(InputMapping i : InputMapping.values()){
            inputManager.addListener(this, i.name());
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.inputManager = app.getInputManager();
        addInputMappings();
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        for(InputMapping i : InputMapping.values()){
            if(inputManager.hasMapping(i.name())){
                inputManager.deleteMapping(i.name());
            }
        }
        inputManager.removeListener(this);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if( character != null){
            character.onAction(name, isPressed, tpf);
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if( character != null ){
            character.onAnalog(name, value * sensitivity, tpf);
        }
    }
    
}
