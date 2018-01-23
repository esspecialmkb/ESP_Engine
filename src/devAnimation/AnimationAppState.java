/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devAnimation;

import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import devMesh.HumanoidControl;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author esspe
 */
public class AnimationAppState extends AbstractAppState implements ActionListener{
    private SkeletonControl skeletonControl;
    Material mat;
    Material matR;
    Material matG;
    private Skeleton skeleton;
    private InputManager inputManager;
    private Node guiNode;
    
    private BitmapFont fnt;
    
    private BitmapText txt;
    
    private Box jointHandle;
    
    private Node selectables;
    private ArrayList<Node> jointHandleList;
    private Camera camera;
    
    private boolean selectBone = false;
    private BoneSelectionHandle activeSelection_Bone = BoneSelectionHandle.Null;
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        InputMapping input = InputMapping.valueOf(name);
        if(isPressed == false){
            selectBone = true;
        }
        
    }
    
    public enum InputMapping{
        BoneSelect
    }
    
    public enum BoneSelectionHandle{
        Root,
        Pelvis,
        Hip_R,
        Hip_L,
        Knee_R,
        Knee_L,
        Torso,
        Chest,
        Neck,
        Shld_R,
        Shld_L,
        Elb_R,
        Elb_L,
        Null
    }
    
    public AnimationAppState(){
        activeSelection_Bone = BoneSelectionHandle.Null;
    }
    
    public AnimationAppState(HumanoidControl humanoid){
        skeletonControl = humanoid.getNode().getControl(SkeletonControl.class);
    }
    
    public AnimationAppState(Node gui_Node, HumanoidControl humanoid){
        guiNode = gui_Node;
        skeletonControl = humanoid.getNode().getControl(SkeletonControl.class);
        jointHandle = new Box(1,1,1);
        jointHandleList = new ArrayList<>();
    }
    
    private void addInputMappings(){
        inputManager.addMapping(InputMapping.BoneSelect.name(),new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(this, InputMapping.BoneSelect.name());
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = app.getInputManager();
        addInputMappings();
        camera = app.getCamera();
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        if(skeletonControl == null){
            System.out.println("No skeleton present");
        }else{
            System.out.println(skeletonControl.getSkeleton().getBoneCount() + " bones present.");
            
            skeleton = skeletonControl.getSkeleton();
            
            
            mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            matR = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            matG = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            
            selectables = new Node("selectables");
            for(int b = 0; b < skeleton.getBoneCount();b++){
                Geometry boneHandle = new Geometry(skeleton.getBone(b).getName(),jointHandle);
                boneHandle.setMaterial(matG);
                Node selectNode = new Node(skeleton.getBone(b).getName());
                selectNode.setLocalScale(10);
                selectNode.attachChild(boneHandle);
                jointHandleList.add(selectNode);
                selectables.attachChild(jointHandleList.get(b));
            }

            guiNode.attachChild(selectables);
            mat.setColor("Color", ColorRGBA.Blue);
            matR.setColor("Color", ColorRGBA.Red);
            matG.setColor("Color", ColorRGBA.Green);
        }
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        Vector3f location = Vector3f.ZERO;
        for(int b = 0; b < skeleton.getBoneCount();b++){
            location = camera.getScreenCoordinates(skeleton.getBone(b).getModelSpacePosition());
            jointHandleList.get(b).setLocalTranslation(location.x, location.y, 0);
            //System.out.println(location);
        }
        
        if(selectBone){
            //select bone joint
            // 1. Reset results list.
            //CollisionResults results = new CollisionResults();

            // 2. Aim the ray from cam loc to cam direction.
            Vector2f cursorPosition = inputManager.getCursorPosition();
            Vector3f localTranslation = Vector3f.ZERO;
            
            for(int s = 0; s < jointHandleList.size();s++){
                localTranslation = jointHandleList.get(s).getLocalTranslation();
                
                Vector3f subtract = localTranslation.subtract(cursorPosition.x,cursorPosition.y,0);
                //System.out.println(localTranslation + " - " + cursorPosition + " = " + subtract + ", length = " + subtract.length());
                if(subtract.length() < 10){
                    System.out.println("Click-Trigger: " + jointHandleList.get(s).getName());
                    for(int i = 0;i < jointHandleList.size();i++){
                        if(activeSelection_Bone.name().equals(jointHandleList.get(i).getName())){
                            jointHandleList.get(i).getChild(0).setMaterial(matG);
                        }
                    }
                    activeSelection_Bone = BoneSelectionHandle.valueOf(jointHandleList.get(s).getName());
                    jointHandleList.get(s).getChild(0).setMaterial(matR);
                }
            }
            
            selectBone = false;
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    public BoneSelectionHandle getSelectedBone(){
        return activeSelection_Bone;
    }
    
}
