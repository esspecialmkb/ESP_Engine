/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devAnimation;

import devMesh.HumanoidControl;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.Skeleton;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Version 1.3 (KeyFrame development)
 * Milestone - Implement keyframe and animation data structure
 * Next Step - Implement loading and saving application state data (current keyframe/animation)
 * Planning Phase - Implemtation for animation playback/options/settings
 *
 * @author Michael B.
 */
public class AnimDev_1_3 extends SimpleApplication implements ScreenController {
    //New nifty member for gui dev

    private Nifty nifty;
        
    private AnimControl animControl;
    private AnimChannel animChannel;
    private BoneTrack boneTrack;
    private Animation animation;
    private Quaternion rotation = new Quaternion();
    
    ArrayList<Quaternion> rotRoot = new ArrayList();
    ArrayList<Quaternion> rotPelvis = new ArrayList();
    ArrayList<Quaternion> rotHip_L = new ArrayList();
    ArrayList<Quaternion> rotHip_R = new ArrayList();
    ArrayList<Quaternion> rotKnee_L = new ArrayList();
    ArrayList<Quaternion> rotKnee_R = new ArrayList();
    ArrayList<Quaternion> rotTorso = new ArrayList();
    ArrayList<Quaternion> rotChest = new ArrayList();
    ArrayList<Quaternion> rotShld_L = new ArrayList();
    ArrayList<Quaternion> rotShld_R = new ArrayList();
    ArrayList<Quaternion> rotElb_L = new ArrayList();
    ArrayList<Quaternion> rotElb_R = new ArrayList();
    ArrayList<Quaternion> rotNeck = new ArrayList();
    
    ArrayList<Vector3f> offsetRoot = new ArrayList();
    ArrayList<Vector3f> offsetPelvis = new ArrayList();
    ArrayList<Vector3f> offsetHip_L = new ArrayList();
    ArrayList<Vector3f> offsetHip_R = new ArrayList();
    ArrayList<Vector3f> offsetKnee_L = new ArrayList();
    ArrayList<Vector3f> offsetKnee_R = new ArrayList();
    ArrayList<Vector3f> offsetTorso = new ArrayList();
    ArrayList<Vector3f> offsetChest = new ArrayList();
    ArrayList<Vector3f> offsetShld_L = new ArrayList();
    ArrayList<Vector3f> offsetShld_R = new ArrayList();
    ArrayList<Vector3f> offsetElb_L = new ArrayList();
    ArrayList<Vector3f> offsetElb_R = new ArrayList();
    ArrayList<Vector3f> offsetNeck = new ArrayList();
    
    BoneTrack RootTarget;
    BoneTrack PelvisTarget;
    BoneTrack Hip_LTarget;
    BoneTrack Hip_RTarget;
    BoneTrack Knee_LTarget;
    BoneTrack Knee_RTarget;
    BoneTrack TorsoTarget;
    BoneTrack ChestTarget;
    BoneTrack Shld_LTarget;
    BoneTrack Shld_RTarget;
    BoneTrack Elb_LTarget;
    BoneTrack Elb_RTarget;
    BoneTrack NeckTarget;
    
    //THESE MEMBERS ARE FOR APP PERSISTENCE
    public float[] rootEuler = new float[3];
    public float[] pelvisEuler = new float[3];
    public float[] hipLEuler = new float[3];
    public float[] hipREuler = new float[3];
    public float[] kneeLEuler = new float[3];
    public float[] kneeREuler = new float[3];
    public float[] torsoEuler = new float[3];
    public float[] chestEuler = new float[3];
    public float[] shldLEuler = new float[3];
    public float[] shldREuler = new float[3];
    public float[] elbLEuler = new float[3];
    public float[] elbREuler = new float[3];
    public float[] neckEuler = new float[3];
    
    public float[] rootTranslate = new float[3];
    public float[] pelvisTranslate = new float[3];
    public float[] hipLTranslate = new float[3];
    public float[] hipRTranslate = new float[3];
    public float[] kneeLTranslate = new float[3];
    public float[] kneeRTranslate = new float[3];
    public float[] torsoTranslate = new float[3];
    public float[] chestTranslate = new float[3];
    public float[] shldLTranslate = new float[3];
    public float[] shldRTranslate = new float[3];
    public float[] elbLTranslate = new float[3];
    public float[] elbRTranslate = new float[3];
    public float[] neckTranslate = new float[3];
    
    public class KeyFrame{
        public int type = 0;
        public float[] root;
        public float[] pelvis;
        public float[] hipL;
        public float[] hipR;
        public float[] kneeL;
        public float[] kneeR;
        public float[] torso;
        public float[] chest;
        public float[] shldL;
        public float[] shldR;
        public float[] elbL;
        public float[] elbR;
        public float[] neck;
        
        public KeyFrame(int type){
            if(type == 0 || type == 1){
                root = new float[3];
                pelvis = new float[3];
                hipL = new float[3];
                hipR = new float[3];
                kneeL = new float[3];
                kneeR = new float[3];
                torso = new float[3];
                chest = new float[3];
                shldL = new float[3];
                shldR = new float[3];
                elbL = new float[3];
                elbR = new float[3];
                neck = new float[3];
            }else if(type == 2){
                root = new float[6];
                pelvis = new float[6];
                hipL = new float[6];
                hipR = new float[6];
                kneeL = new float[6];
                kneeR = new float[6];
                torso = new float[6];
                chest = new float[6];
                shldL = new float[6];
                shldR = new float[6];
                elbL = new float[6];
                elbR = new float[6];
                neck = new float[6];
            }
        }
        public void saveData(){
            root[0] = rootEuler[0];
            root[1] = rootEuler[1];
            root[2] = rootEuler[2];
            root[3] = rootTranslate[0];
            root[4] = rootTranslate[1];
            root[5] = rootTranslate[2];
            
            pelvis[0] = pelvisEuler[0];
            pelvis[1] = pelvisEuler[1];
            pelvis[2] = pelvisEuler[2];
            pelvis[3] = pelvisTranslate[0];
            pelvis[4] = pelvisTranslate[1];
            pelvis[5] = pelvisTranslate[2];
            
            hipL[0] = hipLEuler[0];
            hipL[1] = hipLEuler[1];
            hipL[2] = hipLEuler[2];
            hipL[3] = hipLTranslate[0];
            hipL[4] = hipLTranslate[1];
            hipL[5] = hipLTranslate[2];
            
            hipR[0] = hipREuler[0];
            hipR[1] = hipREuler[1];
            hipR[2] = hipREuler[2];
            hipR[3] = hipRTranslate[0];
            hipR[4] = hipRTranslate[1];
            hipR[5] = hipRTranslate[2];
            
            torso[0] = torsoEuler[0];
            torso[1] = torsoEuler[1];
            torso[2] = torsoEuler[2];
            torso[3] = torsoTranslate[0];
            torso[4] = torsoTranslate[1];
            torso[5] = torsoTranslate[2];
            
            chest[0] = chestEuler[0];
            chest[1] = chestEuler[1];
            chest[2] = chestEuler[2];
            chest[3] = chestTranslate[0];
            chest[4] = chestTranslate[1];
            chest[5] = chestTranslate[2];
            
            shldL[0] = shldLEuler[0];
            shldL[1] = shldLEuler[1];
            shldL[2] = shldLEuler[2];
            shldL[3] = shldLTranslate[0];
            shldL[4] = shldLTranslate[1];
            shldL[5] = shldLTranslate[2];
            
            shldR[0] = shldREuler[0];
            shldR[1] = shldREuler[1];
            shldR[2] = shldREuler[2];
            shldR[3] = shldRTranslate[0];
            shldR[4] = shldRTranslate[1];
            shldR[5] = shldRTranslate[2];
            
            elbL[0] = elbLEuler[0];
            elbL[1] = elbLEuler[1];
            elbL[2] = elbLEuler[2];
            elbL[3] = elbLTranslate[0];
            elbL[4] = elbLTranslate[1];
            elbL[5] = elbLTranslate[2];
            
            elbR[0] = elbREuler[0];
            elbR[1] = elbREuler[1];
            elbR[2] = elbREuler[2];
            elbR[3] = elbRTranslate[0];
            elbR[4] = elbRTranslate[1];
            elbR[5] = elbRTranslate[2];
            
            neck[0] = neckEuler[0];
            neck[1] = neckEuler[1];
            neck[2] = neckEuler[2];
            neck[3] = neckTranslate[0];
            neck[4] = neckTranslate[1];
            neck[5] = neckTranslate[2];
        }
        
        public void loadData(){
            rootEuler[0] = root[0];
            rootEuler[1] = root[1];
            rootEuler[2] = root[2];
            rootTranslate[0] = root[3];
            rootTranslate[1] = root[4];
            rootTranslate[2] = root[5];
            
            pelvisEuler[0] = pelvis[0];
            pelvisEuler[1] = pelvis[1];
            pelvisEuler[2] = pelvis[2];
            pelvisTranslate[0] = pelvis[3];
            pelvisTranslate[1] = pelvis[4];
            pelvisTranslate[2] = pelvis[5];
            
            hipLEuler[0] = hipL[0];
            hipLEuler[1] = hipL[1];
            hipLEuler[2] = hipL[2];
            hipLTranslate[0] = hipL[3];
            hipLTranslate[1] = hipL[4];
            hipLTranslate[2] = hipL[5];
            
            hipREuler[0] = hipR[0];
            hipREuler[1] = hipR[1];
            hipREuler[2] = hipR[2];
            hipRTranslate[0] = hipR[3];
            hipRTranslate[1] = hipR[4];
            hipRTranslate[2] = hipR[5];
            
            torsoEuler[0] = torso[0];
            torsoEuler[1] = torso[1];
            torsoEuler[2] = torso[2];
            torsoTranslate[0] = torso[3];
            torsoTranslate[1] = torso[4];
            torsoTranslate[2] = torso[5];
            
            chestEuler[0] = chest[0];
            chestEuler[1] = chest[1];
            chestEuler[2] = chest[2];
            chestTranslate[0] = chest[3];
            chestTranslate[1] = chest[4];
            chestTranslate[2] = chest[5];
            
            shldLEuler[0] = shldL[0];
            shldLEuler[1] = shldL[1];
            shldLEuler[2] = shldL[2];
            shldLTranslate[0] = shldL[3];
            shldLTranslate[1] = shldL[4];
            shldLTranslate[2] = shldL[5];
            
            shldREuler[0] = shldR[0];
            shldREuler[1] = shldR[1];
            shldREuler[2] = shldR[2];
            shldRTranslate[0] = shldR[3];
            shldRTranslate[1] = shldR[4];
            shldRTranslate[2] = shldR[5];
            
            elbLEuler[0] = elbL[0];
            elbLEuler[1] = elbL[1];
            elbLEuler[2] = elbL[2];
            elbLTranslate[0] = elbL[3];
            elbLTranslate[1] = elbL[4];
            elbLTranslate[2] = elbL[5];
            
            elbREuler[0] = elbR[0];
            elbREuler[1] = elbR[1];
            elbREuler[2] = elbR[2];
            elbRTranslate[0] = elbR[3];
            elbRTranslate[1] = elbR[4];
            elbRTranslate[2] = elbR[5];
            
            neckEuler[0] = neck[0];
            neckEuler[1] = neck[1];
            neckEuler[2] = neck[2];
            neckTranslate[0] = neck[3];
            neckTranslate[1] = neck[4];
            neckTranslate[2] = neck[5];
        }
    }
    
    /**
     *  KeyFrameStacks represent a collection of keyframes a.k.a an Animation
     *  Each Animation will need to keep track of the keyframes it uses
     *  Extra data for the Animation may need to be kept here
     */
    public class KeyFrameStack{
        protected int count;
        protected int nameCount;
        protected String name;
        protected float totalTime;
        
        //  Stack of keyFrames
        protected ArrayList<KeyFrame> keyFrames;
        /**
         *  Associate the name of the keyFrame with it's id within the list
         *  Allows us to separate the name from it's index after removing one
         */
        protected ArrayList<String> keyFrameIDList;
        protected ArrayList<Float> keyFrameTime;
        
        public KeyFrameStack(String name){
            this.name = name;
            keyFrames = new ArrayList<>();
            keyFrameIDList = new ArrayList<>();
            keyFrameTime = new ArrayList<>();
        }
        public void createKeyFrame(){
            nameCount++;
            keyFrameIDList.add("KeyFrame" + nameCount);
            keyFrames.add(new KeyFrame(2));
            keyFrameTime.add(0.5f);
            count++;
        }
        public void createKeyFrame(String name){
            nameCount++;
            keyFrameIDList.add(name);
            keyFrames.add(new KeyFrame(2));
            keyFrameTime.add(0.5f);
            count++;
        }
        public void deleteKeyFrame(String name){
            keyFrames.remove(keyFrameIDList.indexOf(name));
            keyFrameTime.remove(keyFrameIDList.indexOf(name));
            keyFrameIDList.remove(name);
            count--;
        }
        
        public void saveKeyFrame(int keyFrame){
            if(keyFrame < keyFrames.size()){
                keyFrames.get(keyFrame).saveData();
            }
        }
        public void loadKeyFrame(int keyFrame){
            if(keyFrame < keyFrames.size()){
                keyFrames.get(keyFrame).loadData();
            }
        }
    }
    
    /**
     *  AnimationStack holds a list of animation references
     *  
     */
    public class AnimationStack{
        protected int count;
        protected int nameCount;
        
        protected ArrayList<KeyFrameStack> animationStacks;
        protected ArrayList<String> animationIDList;
        
        public AnimationStack(){
            animationStacks = new ArrayList<>();
            animationIDList = new ArrayList<>();
            
            //createAnimation("BaseFrames");
        }
        public void createAnimation(){
            nameCount++;
            animationIDList.add("Animation" + nameCount);
            animationStacks.add(new KeyFrameStack("Animation" + nameCount));
            count++;
        }
        public void createAnimation(String name){
            nameCount++;
            animationIDList.add(name);
            animationStacks.add(new KeyFrameStack(name));
            count++;
        }
        public void deleteAnimation(String name){
            animationStacks.remove(animationIDList.indexOf(name));
            animationIDList.remove(name);
        }
        public int findAnimation(String name){
            return animationIDList.indexOf(name);
        }
        
        public int findKeyFrame(String stack, String frame){
            int index = animationIDList.indexOf(stack);
            return animationStacks.get(index).keyFrameIDList.indexOf(frame);
        }
        
        public void saveKeyFrame(int frameStack){
            //Create a new keyFrame at the end of frameStack and save data
            if(frameStack < animationStacks.size()){
                animationStacks.get(frameStack).createKeyFrame();
                animationStacks.get(frameStack).saveKeyFrame( animationStacks.get(frameStack).keyFrames.size()-1 );
            }
            
        }
        public void saveKeyFrame(int frameStack, int keyFrame){
            //Update selected keyframe with rotation/translation data
            if(frameStack < animationStacks.size()){
                if(keyFrame < animationStacks.get(frameStack).keyFrames.size()){
                    animationStacks.get(frameStack).saveKeyFrame(keyFrame);
                }
            }
        }
        public void loadKeyFrame(int frameStack, int keyFrame){
            if(frameStack < animationStacks.size()){
                if(keyFrame < animationStacks.get(frameStack).keyFrames.size()){
                    animationStacks.get(frameStack).loadKeyFrame(keyFrame);
                }
            }
        }
        
        public void loadKeyFrame(String stack, String frame){
            int frameStack = animationIDList.indexOf(stack);
            int keyFrame = animationStacks.get(frameStack).keyFrameIDList.indexOf(frame);
            
            if(frameStack < animationStacks.size()){
                if(keyFrame < animationStacks.get(frameStack).keyFrames.size()){
                    animationStacks.get(frameStack).loadKeyFrame(keyFrame);
                }
            }
        }
    }
    
    protected AnimationStack animationStack;
    protected int currentKeyFrame;
    protected int currentAnimation;

    public static void main(String[] args) {
        AnimDev_1_3 app = new AnimDev_1_3();
        AppSettings settings = new AppSettings(false);
        settings.setTitle("AnimDev 1.2.3");
        app.setSettings(settings);
        //app.showSettings = false;
        app.setDisplayStatView(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    //New member
    protected HumanoidControl humanoid;
    
    public void saveKeyFrame(){
        
    }

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
        
        animationStack = new AnimationStack();

        //animControl = new AnimControl(skeleton);
        //animControl.setEnabled(false);
        //animation = new Animation("default",10.0f);
        //boneTrack;
        rootNode.attachChild(humanoid.getNode());
        cam.setLocation(new Vector3f(-5.0f, 9.0f, 37.0f));
        flyCam.setMoveSpeed(50);

        //Nifty stuff...
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/AnimGUIDev.xml", "GScreen1", this);
        Screen screen = nifty.getScreen("GScreen1");
        
        //screen.findNiftyControl(currentBone, NiftySlider);
        
        //nifty.subscribeAnnotations(this);
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        
        //Setup inputManager       
        inputManager.addMapping("My Action",
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("Save KeyFrame",new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("PopUp", new KeyTrigger(KeyInput.KEY_TAB));

        // Test multiple listeners per mapping
        inputManager.addListener(actionListener, "My Action","Save KeyFrame");
        
    }

    public boolean boneUpdate = false;
    public String currentBone = "BoneButton0";
    public String screenID = "GScreen1";
    public String popupID = "Null";
    public String transformMode = "Rotation";
    
    //New members to keep track of current animation and keyframe
    public String animationID = "Null";
    public String keyFrameID = "Null";
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if(boneUpdate){
            prepareBone();
        }
        
        

        // After changing skeleton transforms, must update world data
        humanoid.updateHumanoid(tpf);
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private Slider xSlider;
    private Slider ySlider;
    private Slider zSlider;
    
    private ListBox animListBox;
    private ListBox keyFrameListBox;
    
    private TextField newAnimName;
    private TextField newKeyFrameName;
    
    //Nifty methods
    @NiftyEventSubscriber(pattern = "GHSlider.*") // use can use whatever you want really. It goes with the control's ID
    public void onSliderChangedEvent(final String id, final SliderChangedEvent event) {
        System.out.println(id + ", " + event.getSlider().getValue()* FastMath.DEG_TO_RAD);
        float value = event.getSlider().getValue();
        if(!currentBone.equals("BoneButton")){
            boneUpdate = true;
        }
        //System.out.println(event.getSlider().getId() + ", " +event.getSlider().getValue());
        if(editMode.equals("Rotation")){
            //Rotation mode
            switch (currentBone) {
                case "BoneButton0":
                    rotateBone( id, rootEuler, value);
                    break;
                case "BoneButton1":
                    rotateBone( id, pelvisEuler, value);
                    break;
                case "BoneButton2":
                    rotateBone( id, hipLEuler, value);
                    break;  
                case "BoneButton3":
                    rotateBone( id, hipREuler, value);
                    break;
                case "BoneButton4":
                    rotateBone( id, kneeLEuler, value);
                    break;
                case "BoneButton5":
                    rotateBone( id, kneeREuler, value);
                    break;
                case "BoneButton6":
                    rotateBone( id, torsoEuler, value);
                    break;
                case "BoneButton7":
                    rotateBone( id, chestEuler, value);
                    break;
                case "BoneButton8":
                    rotateBone( id, shldLEuler, value);
                    break;  
                case "BoneButton9":
                    rotateBone( id, shldREuler, value);
                    break;
                case "BoneButton10":
                    rotateBone( id, elbLEuler, value);
                    break;
                case "BoneButton11":
                    rotateBone( id, elbREuler, value);
                    break;
                case "BoneButton12":
                    rotateBone( id, neckEuler, value);
                    break;
            }
        }else if(editMode.equals("Translation")){
            //Translation mode
            switch (currentBone) {
                case "BoneButton0":
                    translateBone( id, rootTranslate, value / 20);
                    break;
                case "BoneButton1":
                    translateBone( id, pelvisTranslate, value / 20);
                    break;
                case "BoneButton2":
                    translateBone( id, hipLTranslate, value / 20);
                    break;  
                case "BoneButton3":
                    translateBone( id, hipRTranslate, value / 20);
                    break;
                case "BoneButton4":
                    translateBone( id, kneeLTranslate, value / 20);
                    break;
                case "BoneButton5":
                    translateBone( id, kneeRTranslate, value / 20);
                    break;
                case "BoneButton6":
                    translateBone( id, torsoTranslate, value / 20);
                    break;
                case "BoneButton7":
                    translateBone( id, chestTranslate, value / 20);
                    break;
                case "BoneButton8":
                    translateBone( id, shldLTranslate, value / 20);
                    break;  
                case "BoneButton9":
                    translateBone( id, shldRTranslate, value / 20);
                    break;
                case "BoneButton10":
                    translateBone( id, elbLTranslate, value / 20);
                    break;
                case "BoneButton11":
                    translateBone( id, elbRTranslate, value / 20);
                    break;
                case "BoneButton12":
                    translateBone( id, neckTranslate, value / 20);
                    break;
            }
        }
        
    }
    
    private String editMode = "Rotation";
    
    @NiftyEventSubscriber(pattern = "BoneButton.*")
    public void onButtonClickedEvent(final String id, final ButtonClickedEvent event) {
        //System.out.println("Sider :" + id);
        System.out.println(id);
        currentBone = id;
        if(editMode.equals("Rotation")){
            //Will be slightly different if were editing translation instead of rotation
            switch (currentBone) {
                case "BoneButton0":
                    xSlider.setValue(rootEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(rootEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(rootEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton1":
                    xSlider.setValue(pelvisEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(pelvisEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(pelvisEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton2":
                    xSlider.setValue(hipLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(hipLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(hipLEuler[2] * FastMath.RAD_TO_DEG);
                    break;  
                case "BoneButton3":
                    xSlider.setValue(hipREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(hipREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(hipREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton4":
                    xSlider.setValue(kneeLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(kneeLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(kneeLEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton5":
                    xSlider.setValue(kneeREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(kneeREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(kneeREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton6":
                    xSlider.setValue(torsoEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(torsoEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(torsoEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton7":
                    xSlider.setValue(chestEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(chestEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(chestEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton8":
                    xSlider.setValue(shldLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(shldLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(shldLEuler[2] * FastMath.RAD_TO_DEG);
                    break;  
                case "BoneButton9":
                    xSlider.setValue(shldREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(shldREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(shldREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton10":
                    xSlider.setValue(elbLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(elbLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(elbLEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton11":
                    xSlider.setValue(elbREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(elbREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(elbREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton12":
                    xSlider.setValue(neckEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(neckEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(neckEuler[2] * FastMath.RAD_TO_DEG);
                    break;
            }
        }else if(editMode.equals("Translation")){
            //Mode for traslation
            switch(currentBone){
                case "BoneButton0":
                    xSlider.setValue(rootTranslate[0]);
                    ySlider.setValue(rootTranslate[1]);
                    zSlider.setValue(rootTranslate[2]);
                    break;
                case "BoneButton1":
                    xSlider.setValue(pelvisTranslate[0]);
                    ySlider.setValue(pelvisTranslate[1]);
                    zSlider.setValue(pelvisTranslate[2]);
                    break;
                case "BoneButton2":
                    xSlider.setValue(hipLTranslate[0]);
                    ySlider.setValue(hipLTranslate[1]);
                    zSlider.setValue(hipLTranslate[2]);
                    break;  
                case "BoneButton3":
                    xSlider.setValue(hipRTranslate[0]);
                    ySlider.setValue(hipRTranslate[1]);
                    zSlider.setValue(hipRTranslate[2]);
                    break;
                case "BoneButton4":
                    xSlider.setValue(kneeLTranslate[0]);
                    ySlider.setValue(kneeLTranslate[1]);
                    zSlider.setValue(kneeLTranslate[2]);
                    break;
                case "BoneButton5":
                    xSlider.setValue(kneeRTranslate[0]);
                    ySlider.setValue(kneeRTranslate[1]);
                    zSlider.setValue(kneeRTranslate[2]);
                    break;
                case "BoneButton6":
                    xSlider.setValue(torsoTranslate[0]);
                    ySlider.setValue(torsoTranslate[1]);
                    zSlider.setValue(torsoTranslate[2]);
                    break;
                case "BoneButton7":
                    xSlider.setValue(chestTranslate[0]);
                    ySlider.setValue(chestTranslate[1]);
                    zSlider.setValue(chestTranslate[2]);
                    break;
                case "BoneButton8":
                    xSlider.setValue(shldLTranslate[0]);
                    ySlider.setValue(shldLTranslate[1]);
                    zSlider.setValue(shldLTranslate[2]);
                    break;  
                case "BoneButton9":
                    xSlider.setValue(shldRTranslate[0]);
                    ySlider.setValue(shldRTranslate[1]);
                    zSlider.setValue(shldRTranslate[2]);
                    break;
                case "BoneButton10":
                    xSlider.setValue(elbLTranslate[0]);
                    ySlider.setValue(elbLTranslate[1]);
                    zSlider.setValue(elbLTranslate[2]);
                    break;
                case "BoneButton11":
                    xSlider.setValue(elbRTranslate[0]);
                    ySlider.setValue(elbRTranslate[1]);
                    zSlider.setValue(elbRTranslate[2]);
                    break;
                case "BoneButton12":
                    xSlider.setValue(neckTranslate[0]);
                    ySlider.setValue(neckTranslate[1]);
                    zSlider.setValue(neckTranslate[2]);
                    break;
            }
        }
        
    }
    
    @NiftyEventSubscriber(pattern = "GButton.*")
    public void onListBoxButtonClickedEvent(final String id, final ButtonClickedEvent event){
        switch(id){
            case "GButton0":
                //ADD ANIMATION
                nifty.gotoScreen("GScreen2");
                break;
            case "GButton1":
                //DELETE SELECTED ANIMATION
                break;
            case "GButton2":
                //ADD KEYFRAME TO SELECTED ANIMATION
                nifty.gotoScreen("GScreen3");
                break;
            case "GButton3":
                //DELETE KEYFRAME FROM SELECTED ANIMATION
                break;
        }
    }
    
    @NiftyEventSubscriber(pattern = "PButton.*")
    public void onPopUpButtonClickedEvent(final String id, final ButtonClickedEvent event){
        String name;
        switch(id){
            case "PButton0":
                //CANCEL ADD ANIMATION
                break;
            case "PButton1":
                //CONFIRM ADD ANIMATION
                
                //Grab the text in the textfield 
                name = newAnimName.getRealText();
                //Then create a new animation with the new name
                animationStack.createAnimation(name);
                animationStack.animationStacks.get(animationStack.findAnimation(name)).createKeyFrame(name + " start");
                //Update the animation listbox
                animListBox.addItem(name);
                animationID = name;
                break;
            case "PButton2":
                //CANCEL ADD KEYFRAME TO SELECTED ANIMATION                
                break;
            case "PButton3":
                //CONFIRM ADD KEYFRAME TO SELECTED ANIMATION
                
                //Grab the text in the textfield 
                name = newKeyFrameName.getRealText();
                //Then create a new keyframe with the new name inside the currently selected animation
                animationStack.animationStacks.get(animationStack.findAnimation(animationID)).createKeyFrame(name);
                //Update the keyframe listbox for the current animation
                keyFrameListBox.addItem(name);
                keyFrameID = name;
                break;
        }
        
        //When any button is clicked (cancel or ok) return to the previous screen
        popupID = "Null";
        nifty.gotoScreen(screenID);
    }
    
    @NiftyEventSubscriber(pattern = "GListBox.*")
    public void onListBoxSelect(final String id, final ListBoxSelectionChangedEvent event){
        List<String> eventList;
        switch(id){
            case "GListBox2":
                //Select animation
                eventList = event.getSelection();
                //Load keyFrameStack and set transforms to first frame
                animationID = eventList.get(0);System.out.println("Anim " + animationID);
                int animId = animationStack.findAnimation(animationID);
                keyFrameID = animationStack.animationStacks.get(animId).keyFrameIDList.get(0);
                animationStack.loadKeyFrame(animationStack.findAnimation(animationID), 0);
                System.out.println(eventList);
                //Update listBox
                keyFrameListBox.clear();
                for(int i = 0; i<animationStack.animationStacks.get(animId).keyFrameIDList.size();i++){
                    keyFrameListBox.addItem(animationStack.animationStacks.get(animId).keyFrameIDList.get(i));
                }
                break;
            case "GListBox3":
                //Select keyFrame
                eventList = event.getSelection();
                //Load the selected keyFrame from the current keyFrameStack
                keyFrameID = eventList.get(0);
                animationStack.loadKeyFrame(animationID, keyFrameID);
                System.out.println(eventList);
                break;
        }
    }
    
    public void onTextFiledChanged(final String id, final TextFieldChangedEvent event){
        
    }
    
    @NiftyEventSubscriber(pattern = "GRadioGroup.*")
    public void onRadioGroupStateChange(final String id, final RadioButtonGroupStateChangedEvent event){
        System.out.println(event.getSelectedId() + " selected from radio group");
        String eventId = event.getSelectedId();
        if(eventId.equals("GRadioButton3")){
            //Translation mode
            editMode = "Translation";
            switch(currentBone){
                case "BoneButton0":
                    xSlider.setValue(rootTranslate[0]);
                    ySlider.setValue(rootTranslate[1]);
                    zSlider.setValue(rootTranslate[2]);
                    break;
                case "BoneButton1":
                    xSlider.setValue(pelvisTranslate[0]);
                    ySlider.setValue(pelvisTranslate[1]);
                    zSlider.setValue(pelvisTranslate[2]);
                    break;
                case "BoneButton2":
                    xSlider.setValue(hipLTranslate[0]);
                    ySlider.setValue(hipLTranslate[1]);
                    zSlider.setValue(hipLTranslate[2]);
                    break;  
                case "BoneButton3":
                    xSlider.setValue(hipRTranslate[0]);
                    ySlider.setValue(hipRTranslate[1]);
                    zSlider.setValue(hipRTranslate[2]);
                    break;
                case "BoneButton4":
                    xSlider.setValue(kneeLTranslate[0]);
                    ySlider.setValue(kneeLTranslate[1]);
                    zSlider.setValue(kneeLTranslate[2]);
                    break;
                case "BoneButton5":
                    xSlider.setValue(kneeRTranslate[0]);
                    ySlider.setValue(kneeRTranslate[1]);
                    zSlider.setValue(kneeRTranslate[2]);
                    break;
                case "BoneButton6":
                    xSlider.setValue(torsoTranslate[0]);
                    ySlider.setValue(torsoTranslate[1]);
                    zSlider.setValue(torsoTranslate[2]);
                    break;
                case "BoneButton7":
                    xSlider.setValue(chestTranslate[0]);
                    ySlider.setValue(chestTranslate[1]);
                    zSlider.setValue(chestTranslate[2]);
                    break;
                case "BoneButton8":
                    xSlider.setValue(shldLTranslate[0]);
                    ySlider.setValue(shldLTranslate[1]);
                    zSlider.setValue(shldLTranslate[2]);
                    break;  
                case "BoneButton9":
                    xSlider.setValue(shldRTranslate[0]);
                    ySlider.setValue(shldRTranslate[1]);
                    zSlider.setValue(shldRTranslate[2]);
                    break;
                case "BoneButton10":
                    xSlider.setValue(elbLTranslate[0]);
                    ySlider.setValue(elbLTranslate[1]);
                    zSlider.setValue(elbLTranslate[2]);
                    break;
                case "BoneButton11":
                    xSlider.setValue(elbRTranslate[0]);
                    ySlider.setValue(elbRTranslate[1]);
                    zSlider.setValue(elbRTranslate[2]);
                    break;
                case "BoneButton12":
                    xSlider.setValue(neckTranslate[0]);
                    ySlider.setValue(neckTranslate[1]);
                    zSlider.setValue(neckTranslate[2]);
                    break;
            }
        }else if(eventId.equals("GRadioButton2")){
            //Rotation mode
            editMode = "Rotation";
            switch (currentBone) {
                case "BoneButton0":
                    xSlider.setValue(rootEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(rootEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(rootEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton1":
                    xSlider.setValue(pelvisEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(pelvisEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(pelvisEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton2":
                    xSlider.setValue(hipLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(hipLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(hipLEuler[2] * FastMath.RAD_TO_DEG);
                    break;  
                case "BoneButton3":
                    xSlider.setValue(hipREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(hipREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(hipREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton4":
                    xSlider.setValue(kneeLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(kneeLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(kneeLEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton5":
                    xSlider.setValue(kneeREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(kneeREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(kneeREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton6":
                    xSlider.setValue(torsoEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(torsoEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(torsoEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton7":
                    xSlider.setValue(chestEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(chestEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(chestEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton8":
                    xSlider.setValue(shldLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(shldLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(shldLEuler[2] * FastMath.RAD_TO_DEG);
                    break;  
                case "BoneButton9":
                    xSlider.setValue(shldREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(shldREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(shldREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton10":
                    xSlider.setValue(elbLEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(elbLEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(elbLEuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton11":
                    xSlider.setValue(elbREuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(elbREuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(elbREuler[2] * FastMath.RAD_TO_DEG);
                    break;
                case "BoneButton12":
                    xSlider.setValue(neckEuler[0] * FastMath.RAD_TO_DEG);
                    ySlider.setValue(neckEuler[1] * FastMath.RAD_TO_DEG);
                    zSlider.setValue(neckEuler[2] * FastMath.RAD_TO_DEG);
                    break;
            }
        }
        
    }
    
    //@NiftyEventSubscriber(pattern = "GRadioButton.*")
    public void onRadioButtonStateChane(final String id, final RadioButtonStateChangedEvent event){
        //System.out.println(event.toString() + " selected radio button");
    }
    
    

    public void rotateBone( String axis, float[] euler, float value) {
        switch(axis){
            case "GHSlider0":
                euler[0] = (value ) * FastMath.DEG_TO_RAD;
                break;
            case "GHSlider1":
                euler[1] = (value ) * FastMath.DEG_TO_RAD;
                break;
            case "GHSlider2":
                euler[2] = (value ) * FastMath.DEG_TO_RAD;
                break;
        }
        
    }
    
    public void translateBone( String axis, float[] translation, float value) {
        switch(axis){
            case "GHSlider0":
                translation[0] = value;
                break;
            case "GHSlider1":
                translation[1] = value;
                break;
            case "GHSlider2":
                translation[2] = value;
                break;
        }
        
    }
    
    /**
     *  Making changes in 1.3 to transform bone
     */
    public void prepareBone(HumanoidControl human){
        //Quaternion q = new Quaternion();
        //q.set(0, 0, 0, 0);
        boneUpdate = false;
        switch (currentBone) {
            case "BoneButton0":
                humanoid.setUserTransforms("root",rootEuler, rootTranslate);
                break;
            case "BoneButton1":
                humanoid.setUserTransforms("pelvis",pelvisEuler, pelvisTranslate);
                break;
            case "BoneButton2":
                humanoid.setUserTransforms("hip_L",hipLEuler, hipLTranslate);
                break;  
            case "BoneButton3":
                humanoid.setUserTransforms("hip_R",hipREuler, hipRTranslate);
                break;
            case "BoneButton4":
                humanoid.setUserTransforms("knee_L",kneeLEuler, kneeLTranslate);
                break;
            case "BoneButton5":
                humanoid.setUserTransforms("knee_R",kneeREuler, kneeRTranslate);
                break;
            case "BoneButton6":
                humanoid.setUserTransforms("torso",torsoEuler, torsoTranslate);
                break;
            case "BoneButton7":
                humanoid.setUserTransforms("chest",chestEuler, chestTranslate);
                break;
            case "BoneButton8":
                humanoid.setUserTransforms("shld_L",shldLEuler, shldLTranslate);
                break;  
            case "BoneButton9":
                humanoid.setUserTransforms("shld_R",shldREuler, shldRTranslate);
                break;
            case "BoneButton10":
                humanoid.setUserTransforms("elb_L",elbLEuler, elbLTranslate);
                break;
            case "BoneButton11":
                humanoid.setUserTransforms("elb_R",elbREuler, elbRTranslate);
                break;
            case "BoneButton12":
                humanoid.setUserTransforms("neck",neckEuler, neckTranslate);
                break;
        }
        
        
        //rotate.fromAngleAxis(tpf / 2, Vector3f.UNIT_X);

        // Combine rotation with previous
        //rotation.multLocal(rotate);

        // Set new rotation into bone
        //hip_R.setUserTransforms(Vector3f.ZERO, rotation, Vector3f.UNIT_XYZ);
        //knee_R.setUserTransforms(Vector3f.ZERO, rotation, Vector3f.UNIT_XYZ);
    }
    
    public void prepareBone(){
        prepareBone(humanoid);
    }

    public void BoneSelect(String selection) {
        System.out.println("selection :" + selection);
    }
    
    //Create ActionListener to respond to keyboard events
    private ActionListener actionListener = new ActionListener(){
        @Override
        public void onAction(String name, boolean pressed, float tpf){
            System.out.println(name + " = " + pressed);
            //Switch screens when MyAction is called
            if(name.equals("My Action") == true && !pressed){
                if(screenID.equals("GScreen1")){
                    nifty.gotoScreen("GScreen0");
                    screenID = "GScreen0";
                }else if(screenID.equals("GScreen0")){
                    nifty.gotoScreen("GScreen1");
                    screenID = "GScreen1";
                }
                //Save keyFrames when 'k' is pressed
            }else if(name.equals("Save KeyFrame") == true && !pressed){
                System.out.println("Pop up menu create");
                int stackIndex = animationStack.findAnimation(animationID);
                int frameIndex = animationStack.findKeyFrame(animationID,keyFrameID);
                animationStack.saveKeyFrame(stackIndex, frameIndex);
            }
        }
    };
    
    /*public void RotateBone(String axis){
     System.out.println("axis" + axis + " , " + nifty.getCurrentScreen().getScreenId());
     }*/
    
    public void createPopUp(){
        
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        //System.out.println("bind( " + screen.getScreenId() + ")");
        switch(screen.getScreenId()){
            case "GScreen0":
                screenID = screen.getScreenId();
                popupID = "Null";
                animListBox = screen.findNiftyControl("GListBox2",ListBox.class);
                animListBox.addItem("Base Frames");
                animationStack.createAnimation("Base Frames");
                keyFrameListBox = screen.findNiftyControl("GListBox3",ListBox.class);
                keyFrameListBox.addItem("Bind Pose");
                animationStack.animationStacks.get(0).createKeyFrame("Bind Pose");
                break;
            case "GScreen1":
                screenID = screen.getScreenId();
                popupID = "Null";
                xSlider = screen.findNiftyControl("GHSlider0",Slider.class);
                ySlider = screen.findNiftyControl("GHSlider1",Slider.class);
                zSlider = screen.findNiftyControl("GHSlider2",Slider.class);
                break;
            case "GScreen2":
                popupID = screen.getScreenId();
                newAnimName = screen.findNiftyControl("GTextfield1", TextField.class);
                break;
            case "GScreen3":
                popupID = screen.getScreenId();
                newKeyFrameName = screen.findNiftyControl("GTextfield2", TextField.class);
                break;
        }
        
    }
        
    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
        
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit() {
        nifty.gotoScreen("end");
    }
}

