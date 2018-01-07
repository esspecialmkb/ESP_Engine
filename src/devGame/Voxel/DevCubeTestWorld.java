/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devGame.Voxel;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author esspe
 */
public class DevCubeTestWorld extends SimpleApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DevCubeTestWorld testWorld = new DevCubeTestWorld();
        testWorld.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        
        viewPort.setBackgroundColor(ColorRGBA.White);
        
        
        CubeWorldAppState cubeState = new CubeWorldAppState();
        stateManager.attach(cubeState);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
    
}
