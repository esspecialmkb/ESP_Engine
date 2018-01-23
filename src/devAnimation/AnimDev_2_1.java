/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devAnimation;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.system.AppSettings;
import devCamera.AnimDev_CamState;
import devGUI.GUIAppState;
import devMesh.HumanoidControl;

/**
 *
 * @author Michael B.
 */
public class AnimDev_2_1 extends SimpleApplication{

    private static AnimDev_2_1 app;
    private AnimDev_CamState camState;
    private AnimationAppState animState;
    private GUIAppState guiState;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        app = new AnimDev_2_1();
        AppSettings settings = new AppSettings(false);
        settings.setTitle("AnimDev 2.1");
        app.setSettings(settings);
        //app.showSettings = false;
        app.setDisplayStatView(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }
    private HumanoidControl humanoid;

    @Override
    public void simpleInitApp() {
        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(Vector3f.UNIT_XYZ.negate());
        rootNode.addLight(dl);

        humanoid = new HumanoidControl();
        // This is the object being used for vertices
        humanoid.initMesh(assetManager);
        rootNode.attachChild(humanoid.getNode());
        //cam.setLocation(new Vector3f(-5.0f, 9.0f, 37.0f));
        //flyCam.setMoveSpeed(50);
        
        setupEnvironment();
        animState = new AnimationAppState(guiNode,humanoid);
        camState = new AnimDev_CamState();
        guiState = new GUIAppState(guiNode);
        
        //cam.setLocation(new Vector3f(2,1.5f,2));
        //cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_X, ColorRGBA.Red);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Y, ColorRGBA.Green);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Z, ColorRGBA.Blue);

        //putBox(new Vector3f(2, 0, 0), 0.5f, ColorRGBA.Yellow);
        //putGrid(new Vector3f(0, 0, 0), ColorRGBA.White);
        //putSphere(new Vector3f(0, 0, 10f), ColorRGBA.Magenta);
        
        app.getStateManager().getState(FlyCamAppState.class).setEnabled(false);
        app.getStateManager().attach(animState);
        app.getStateManager().attach(camState);
        app.getStateManager().attach(guiState);
    }
    
    @Override
    public void simpleUpdate(float tpf){
        
    }
    
    public void setupEnvironment(){
        // Here we set up the floor-grid
        Grid floorShape = new Grid(11, 11, 0.2f);
        Geometry gridGeo = new Geometry("Floor", floorShape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(1);
        mat.setColor("Color", ColorRGBA.White);
        gridGeo.setMaterial(mat);
        
        gridGeo.setLocalScale(10);
        rootNode.attachChild(gridGeo);
        gridGeo.center();
        // Set up the Animated Model
    }
    
    public Geometry putShape(Mesh shape, ColorRGBA color, float lineWidth){
        Geometry g = new Geometry("shape", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(lineWidth);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
    
    public void putArrow(Vector3f pos, Vector3f dir, ColorRGBA color){
        Arrow arrow = new Arrow(dir);
        putShape(arrow, color, 4).setLocalTranslation(pos);
    }

    public void putBox(Vector3f pos, float size, ColorRGBA color){
        putShape(new WireBox(size, size, size), color, 1).setLocalTranslation(pos);
    }

    public void putGrid(Vector3f pos, ColorRGBA color){
        putShape(new Grid(36, 36, 0.2f), color, 1).center().move(pos);
    }

    public void putSphere(Vector3f pos, ColorRGBA color){
        putShape(new WireSphere(1), color, 1).setLocalTranslation(pos);
    }
}
