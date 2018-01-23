/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devAnimation;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Michael B. (recipe)
 * usage: skinnedMesh.addControl(new CharacterAnimationManager());
 */
public class CharacterAnimationManager extends AbstractControl implements AnimEventListener{
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    
    //  Use an Enum so we dont have to use string comparisions
    public enum Animation{
        Idle(LoopMode.Loop, 0.0f),
        Walk(LoopMode.Loop, 0.2f),
        Run(LoopMode.Loop, 0.2f),
        SideKick(LoopMode.DontLoop, 0.1f),
        JumpStart(LoopMode.DontLoop, 0.1f),
        Jumping(LoopMode.DontLoop, 0.1f);
        
        Animation(LoopMode loopMode, float blendTime){
        this.loopMode = loopMode;
        this.blendTime = blendTime;
        }
        LoopMode loopMode;
        float blendTime;
    }
    
    public AnimControl animControl;
    public AnimChannel mainChannel;
    
    @Override
    public void setSpatial(Spatial spatial){
        //  We set the AnimControl and AnimChannel in the setSpatial method, as shown in the following code. Don't
        //forget to add the class to the AnimControl field as a listener, or we won't receive
        //any calls when animations are finished:
        super.setSpatial(spatial);
        animControl = spatial.getControl(AnimControl.class);
        mainChannel = animControl.createChannel();
        animControl.addListener(this);
    }
    
    public void setAnimation(Animation animation) {
        //We define a new method called setAnimation in the following code. Inside this,
        //we set the supplied animation to be mainChannel as the current one if it's not the
        //same as the one playing now. We also set loopMode according to how it's defined in
        //the enum:
        if(mainChannel.getAnimationName() == null || !mainChannel.getAnimationName().equals(animation.name())){
            mainChannel.setAnim(animation.name(), animation.blendTime);
            mainChannel.setLoopMode(animation.loopMode);
        }
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        //In the onAnimCycleDone method, we create a control so that all animations that
        //don't loop return to the idle animation, with the exception of JumpStart, which
        //should switch to Jumping (as in midair) as shown in the following code:
        if(channel.getLoopMode() == LoopMode.DontLoop){
            Animation newAnim = Animation.Idle;
            Animation anim = Animation.valueOf(animName);
            switch(anim){
                case JumpStart:
                    newAnim = Animation.Jumping;
                break;
            }
            setAnimation(newAnim);
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        CharacterAnimationManager control = new CharacterAnimationManager();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
