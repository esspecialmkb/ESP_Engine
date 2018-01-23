/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devGUI;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Michael B.
 * Introducing GUIAppState into _2_1
 */
public class GUIAppState extends AbstractAppState {
    private Node guiNode;
    private InputManager inputManager;
    
    private BitmapFont fnt;
    
    private BitmapText txt;
    private BitmapText txt2;
    private BitmapText txt3;
    private String txtB = "abc";
    
    private Sphere jointHandle;
    
    public GUIAppState(Node gui){
        guiNode = gui;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        fnt = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        txt = new BitmapText(fnt, false);
        txt.setBox(new Rectangle(0, 0, app.getCamera().getWidth(), app.getCamera().getHeight()));
        txt.setSize(fnt.getPreferredSize() * 2f);
        txt.setText(txtB);
        txt.setLocalTranslation(0, txt.getHeight(), 0);
        guiNode.attachChild(txt);
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
    }
    
}
