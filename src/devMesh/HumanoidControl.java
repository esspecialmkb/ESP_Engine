/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devMesh;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.scene.shape.Box;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 *
 * @author Michael B.
 */
public class HumanoidControl {
    protected Node model;
    protected VirtualMesh mesh;
    protected Mesh sceneMesh;
    
    protected FloatBuffer weights;
    protected ByteBuffer indices;
    protected Skeleton skeleton;
    protected SkeletonControl skeletonControl;
        
    protected AnimControl animControl;
    protected AnimChannel animChannel;
    protected BoneTrack boneTrack;
    protected Animation animation;
    protected Quaternion rotation = new Quaternion();
    
    protected float texture32Unit = 0.03125f;
    
    Bone root;
    Bone pelvis;
    Bone hip_L;
    Bone hip_R;
    Bone knee_L;
    Bone knee_R;
    Bone torso;
    Bone chest;
    Bone shld_L;       //Shoulder_L
    Bone shld_R;        //Shoulder_R
    Bone elb_L;         //Elbow_L
    Bone elb_R;         //Elbow_R
    Bone neck;
    
    
    public HumanoidControl(){
        
    }
    
    public void initMesh(AssetManager assetManager){
        mesh = new VirtualMesh();
        mesh.initMesh();
        
        buildLeg();
        buildTorso();
        buildArm();
        buildHead();
        System.out.println("buildBones()");
        buildBones(assetManager);
    }
    
    public Node getNode(){return model;}
    
    public void buildLeg(){
        //RIGHT LEG 0 - 25
        //       (24) ---(25)
        // 14 --- 15  --- 16 --- 17 - (23)
        // 10 --- 11  --- 12 --- 13 - (22)
        //     >  8   --- 9   <  
        // 4  --- 5   --- 6  --- 7  - (21)
        // 0  --- 1   --- 2  --- 3  - (20)
        //                      (18)- (19)
        
        // TCoords
        /*
        mesh.addTCoords(new Vector2f(0 * texture32Unit,0 * texture32Unit));
        mesh.addTCoords(new Vector2f(2 * texture32Unit,0 * texture32Unit));
        mesh.addTCoords(new Vector2f(4 * texture32Unit,0 * texture32Unit));
        mesh.addTCoords(new Vector2f(6 * texture32Unit,0 * texture32Unit));
        
        mesh.addTCoords(new Vector2f(0 * texture32Unit,5 * texture32Unit));
        mesh.addTCoords(new Vector2f(2 * texture32Unit,5 * texture32Unit));
        mesh.addTCoords(new Vector2f(4 * texture32Unit,5 * texture32Unit));
        mesh.addTCoords(new Vector2f(6 * texture32Unit,5 * texture32Unit));
        
        //mesh.addTCoords(new Vector2f(0 * texture32Unit,0 * texture32Unit));
        mesh.addTCoords(new Vector2f(2 * texture32Unit,6 * texture32Unit));
        mesh.addTCoords(new Vector2f(4 * texture32Unit,6 * texture32Unit));
        //mesh.addTCoords(new Vector2f(6 * texture32Unit,0 * texture32Unit));
        
        mesh.addTCoords(new Vector2f(0 * texture32Unit,7 * texture32Unit));
        mesh.addTCoords(new Vector2f(2 * texture32Unit,7 * texture32Unit));
        mesh.addTCoords(new Vector2f(4 * texture32Unit,7 * texture32Unit));
        mesh.addTCoords(new Vector2f(6 * texture32Unit,7 * texture32Unit));
        
        mesh.addTCoords(new Vector2f(0 * texture32Unit,10 * texture32Unit));
        mesh.addTCoords(new Vector2f(2 * texture32Unit,10 * texture32Unit));
        mesh.addTCoords(new Vector2f(4 * texture32Unit,10 * texture32Unit));
        mesh.addTCoords(new Vector2f(6 * texture32Unit,10 * texture32Unit)); */

        // Shift X by +1
        mesh.addVertex(new Vector3f(0, 0, -1)); //0
        mesh.addVertex(new Vector3f(0, 0, 1));  //1
        mesh.addVertex(new Vector3f(2, 0, 1));  //2
        mesh.addVertex(new Vector3f(2, 0, -1)); //3

        mesh.addVertex(new Vector3f(0, 5, -1)); //4
        mesh.addVertex(new Vector3f(0, 5, 1));  //5
        mesh.addVertex(new Vector3f(2, 5, 1));  //6
        mesh.addVertex(new Vector3f(2, 5, -1)); //7

        //mesh.addVertex(new Vector3f(1,6,-1));
        mesh.addVertex(new Vector3f(0, 6, 1));  //8
        mesh.addVertex(new Vector3f(2, 6, 1));  //9
        //mesh.addVertex(new Vector3f(-1,6,-1));

        mesh.addVertex(new Vector3f(0, 7, -1)); //10
        mesh.addVertex(new Vector3f(0, 7, 1));  //11
        mesh.addVertex(new Vector3f(2, 7, 1));  //12
        mesh.addVertex(new Vector3f(2, 7, -1)); //13

        mesh.addVertex(new Vector3f(0, 10, -1));//14
        mesh.addVertex(new Vector3f(0, 10, 1)); //15
        mesh.addVertex(new Vector3f(2, 10, 1)); //16
        mesh.addVertex(new Vector3f(2, 10, -1));//17
        
        mesh.addVertex(new Vector3f(0, 0, 1));  //18 -> 1
        mesh.addVertex(new Vector3f(2, 0, 1));  //19 -> 2
        mesh.addVertex(new Vector3f(0, 0, -1));  //20-> 0
        mesh.addVertex(new Vector3f(0, 5, -1));  //21-> 4
        mesh.addVertex(new Vector3f(0, 7, -1));  //22-> 7
        mesh.addVertex(new Vector3f(0, 10, -1)); //23 ->10
        mesh.addVertex(new Vector3f(0, 10, -1)); //24-> 14
        mesh.addVertex(new Vector3f(0, 10, -1)); //25-> 17

        // Normals
        mesh.addNormal(0, 0, -1); //0
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); //7

        //mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1); //10
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); //17
        
        mesh.addNormal(0, 0, -1); //18
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);//25

        // LEFT LEG   26 - 51
        //       (50) - (51)
        // 40 --- 41 --- 42 --- 43 - (49)
        // 36 --- 37 --- 38 --- 39 - (48)
        //     >  34 --- 35  <
        // 30 --- 31 --- 32 --- 33 - (47)
        // 26 --- 27 --- 28 --- 29 - (46)
        //                     (44)- (45)
        // Shift X by -1
               
        mesh.addVertex(new Vector3f(-2, 0, -1)); //26 
        mesh.addVertex(new Vector3f(-2, 0, 1));  //27
        mesh.addVertex(new Vector3f(0, 0, 1));   //28
        mesh.addVertex(new Vector3f(0, 0, -1));  //29

        mesh.addVertex(new Vector3f(-2, 5, -1)); //30
        mesh.addVertex(new Vector3f(-2, 5, 1));
        mesh.addVertex(new Vector3f(0, 5, 1));
        mesh.addVertex(new Vector3f(0, 5, -1));  //33

        //mesh.addVertex(new Vector3f(0,6,-1));
        mesh.addVertex(new Vector3f(-2, 6, 1));  //34
        mesh.addVertex(new Vector3f(0, 6, 1));   //35
        //mesh.addVertex(new Vector3f(-2,6,-1));

        mesh.addVertex(new Vector3f(-2, 7, -1)); //36
        mesh.addVertex(new Vector3f(-2, 7, 1));
        mesh.addVertex(new Vector3f(0, 7, 1));
        mesh.addVertex(new Vector3f(0, 7, -1));  //39

        mesh.addVertex(new Vector3f(-2, 10, -1));//40
        mesh.addVertex(new Vector3f(-2, 10, 1));
        mesh.addVertex(new Vector3f(0, 10, 1));
        mesh.addVertex(new Vector3f(0, 10, -1)); //43
        
        mesh.addVertex(new Vector3f(-2, 0, 1));  //44 -> 27
        mesh.addVertex(new Vector3f(0, 0, 1));  //45 -> 28
        mesh.addVertex(new Vector3f(-2, 0, -1));  //46-> 26
        mesh.addVertex(new Vector3f(-2, 5, -1));  //47-> 30
        mesh.addVertex(new Vector3f(-2, 7, -1));  //48-> 36
        mesh.addVertex(new Vector3f(-2, 10, -1)); //49 ->40
        mesh.addVertex(new Vector3f(-2, 10, -1)); //50-> 40
        mesh.addVertex(new Vector3f(0, 10, -1)); //51-> 43

        // Normals
        mesh.addNormal(0, 0, -1); //26
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); //33

        //mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1); //36
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); //43
        
        mesh.addNormal(0, 0, -1); //44
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); //51

        //RIGHT LEG
        //       (24) ---(25)
        // 14 --- 15  --- 16 --- 17 - (23)
        // 10 --- 11  --- 12 --- 13 - (22)
        //     >  8   --- 9   <  
        // 4  --- 5   --- 6  --- 7  - (21)
        // 0  --- 1   --- 2  --- 3  - (20)
        //                      (18)- (19)

        mesh.addTriangle((short) 4, (short) 0, (short) 1);
        mesh.addTriangle((short) 1, (short) 5, (short) 4);
        mesh.addTriangle((short) 5, (short) 1, (short) 2);
        mesh.addTriangle((short) 2, (short) 6, (short) 5);
        mesh.addTriangle((short) 6, (short) 2, (short) 3);
        mesh.addTriangle((short) 3, (short) 7, (short) 6);

        mesh.addTriangle((short) 10, (short) 4, (short) 8);
        mesh.addTriangle((short) 8, (short) 4, (short) 5);
        mesh.addTriangle((short) 8, (short) 11, (short) 10);

        mesh.addTriangle((short) 8, (short) 5, (short) 6);
        mesh.addTriangle((short) 6, (short) 9, (short) 8);
        mesh.addTriangle((short) 11, (short) 8, (short) 9);
        mesh.addTriangle((short) 9, (short) 12, (short) 11);

        mesh.addTriangle((short) 9, (short) 6, (short) 7);
        mesh.addTriangle((short) 7, (short) 13, (short) 9);
        mesh.addTriangle((short) 9, (short) 13, (short) 12);

        mesh.addTriangle((short) 14, (short) 10, (short) 11);
        mesh.addTriangle((short) 11, (short) 15, (short) 14);
        mesh.addTriangle((short) 15, (short) 11, (short) 12);
        mesh.addTriangle((short) 12, (short) 16, (short) 15);
        mesh.addTriangle((short) 16, (short) 12, (short) 13);
        mesh.addTriangle((short) 13, (short) 17, (short) 16);

        mesh.addTriangle((short) 3, (short) 18, (short) 19);
        mesh.addTriangle((short) 19, (short) 20, (short) 3);
        mesh.addTriangle((short) 7, (short) 3, (short) 20);
        mesh.addTriangle((short) 20, (short) 21, (short) 7);
        mesh.addTriangle((short) 13, (short) 7, (short) 21);
        mesh.addTriangle((short) 21, (short) 22, (short) 13);

        mesh.addTriangle((short) 17, (short) 13, (short) 22);
        mesh.addTriangle((short) 22, (short) 23, (short) 17);
        mesh.addTriangle((short) 24, (short) 15, (short) 16);
        mesh.addTriangle((short) 16, (short) 25, (short) 24);

        // LEFT LEG   26 - 51
        //       (50) - (51)
        // 40 --- 41 --- 42 --- 43 - (49)
        // 36 --- 37 --- 38 --- 39 - (48)
        //     >  34 --- 35  <
        // 30 --- 31 --- 32 --- 33 - (47)
        // 26 --- 27 --- 28 --- 29 - (46)
        //                     (44)- (45)

        mesh.addTriangle((short) 30, (short) 26, (short) 27);
        mesh.addTriangle((short) 27, (short) 31, (short) 30);
        mesh.addTriangle((short) 31, (short) 27, (short) 28);
        mesh.addTriangle((short) 28, (short) 32, (short) 31);
        mesh.addTriangle((short) 32, (short) 28, (short) 29);
        mesh.addTriangle((short) 29, (short) 33, (short) 32);

        mesh.addTriangle((short) 36, (short) 30, (short) 34);
        mesh.addTriangle((short) 34, (short) 30, (short) 31);
        mesh.addTriangle((short) 34, (short) 37, (short) 36);

        mesh.addTriangle((short) 34, (short) 31, (short) 32);
        mesh.addTriangle((short) 32, (short) 35, (short) 34);
        mesh.addTriangle((short) 37, (short) 34, (short) 35);
        mesh.addTriangle((short) 35, (short) 38, (short) 37);
        
        mesh.addTriangle((short) 35, (short) 32, (short) 33);
        mesh.addTriangle((short) 33, (short) 39, (short) 35);
        mesh.addTriangle((short) 35, (short) 39, (short) 38);

        mesh.addTriangle((short) 40, (short) 36, (short) 37);
        mesh.addTriangle((short) 37, (short) 41, (short) 40);
        mesh.addTriangle((short) 41, (short) 37, (short) 38);
        mesh.addTriangle((short) 38, (short) 42, (short) 41);
        mesh.addTriangle((short) 42, (short) 38, (short) 39);
        mesh.addTriangle((short) 39, (short) 43, (short) 42);
        
        mesh.addTriangle((short) 29, (short) 44, (short) 45);
        mesh.addTriangle((short) 45, (short) 46, (short) 29);
        mesh.addTriangle((short) 33, (short) 29, (short) 46);
        mesh.addTriangle((short) 46, (short) 47, (short) 33);
        mesh.addTriangle((short) 39, (short) 33, (short) 47);
        mesh.addTriangle((short) 47, (short) 48, (short) 39);
        mesh.addTriangle((short) 43, (short) 39, (short) 48);
        mesh.addTriangle((short) 48, (short) 49, (short) 43);
        mesh.addTriangle((short) 50, (short) 41, (short) 42);
        mesh.addTriangle((short) 42, (short) 51, (short) 50);

        //return mesh;
    }
    public void buildTorso(){
        //PELVIS   52 - 65
        //
        //       (64) - (65)
        // 56 --- 57 --- 58 --- 59 - (63)
        // 52 --- 53 --- 54 --- 55 - (62)
        //                     (60)- (61)

        mesh.addVertex(new Vector3f(-2, 10, -1)); //52
        mesh.addVertex(new Vector3f(-2, 10, 1));  //53
        mesh.addVertex(new Vector3f(2, 10, 1));   //54
        mesh.addVertex(new Vector3f(2, 10, -1));  //55

        mesh.addVertex(new Vector3f(-2, 12, -1)); //56
        mesh.addVertex(new Vector3f(-2, 12, 1));  //57
        mesh.addVertex(new Vector3f(2, 12, 1));   //58
        mesh.addVertex(new Vector3f(2, 12, -1));  //59
        
        mesh.addVertex(new Vector3f(-2, 10, 1));   //60 -> 53
        mesh.addVertex(new Vector3f(2, 10, 1));  //61 -> 54
        mesh.addVertex(new Vector3f(-2, 10, -1)); //62
        mesh.addVertex(new Vector3f(-2, 12, -1));  //63
        mesh.addVertex(new Vector3f(-2, 12, -1));   //64
        mesh.addVertex(new Vector3f(2, 12, -1));  //65

        //normals
        mesh.addNormal(0, 0, -1); // 52
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        //Extra normals
        mesh.addNormal(0, 0, 1); // 60
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); // 65
        //LOCATED POSSIBLE INDEX BUG
        //mesh.addTriangle((short) 40, (short) 36, (short) 47);
        mesh.addTriangle((short) 56, (short) 52, (short) 53);
        mesh.addTriangle((short) 53, (short) 57, (short) 56);
        mesh.addTriangle((short) 57, (short) 53, (short) 54);
        mesh.addTriangle((short) 54, (short) 58, (short) 57);
        mesh.addTriangle((short) 58, (short) 54, (short) 55);
        mesh.addTriangle((short) 55, (short) 59, (short) 58);

        mesh.addTriangle((short) 55, (short) 60, (short) 61);
        mesh.addTriangle((short) 61, (short) 62, (short) 55);
        mesh.addTriangle((short) 59, (short) 55, (short) 62);
        mesh.addTriangle((short) 62, (short) 63, (short) 59);
        mesh.addTriangle((short) 64, (short) 57, (short) 58);
        mesh.addTriangle((short) 58, (short) 65, (short) 64);

        //TODO: Create pelvis verts, normals, and tris

        //TORSO   66 - 79
        //
        //       (78) - (79)
        // 70 --- 71 --- 72 --- 73 - (77)
        // 66 --- 67 --- 68 --- 69 - (76)
        //                     (74)- (75)

        mesh.addVertex(new Vector3f(-2, 12, -1));
        mesh.addVertex(new Vector3f(-2, 12, 1));
        mesh.addVertex(new Vector3f(2, 12, 1));
        mesh.addVertex(new Vector3f(2, 12, -1));

        mesh.addVertex(new Vector3f(-2, 15, -1));
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, -1));
        
        mesh.addVertex(new Vector3f(-2, 12, 1)); //74 -> 67
        mesh.addVertex(new Vector3f(2, 12, 1));  //75 -> 68
        mesh.addVertex(new Vector3f(-2, 12, -1)); //76 -> 66
        mesh.addVertex(new Vector3f(-2, 15, -1)); //77 -> 70
        mesh.addVertex(new Vector3f(-2, 15, -1)); //78 -> 70
        mesh.addVertex(new Vector3f(2, 15, -1));  //79 -> 73

        //normals
        mesh.addNormal(0, 0, -1); // 66
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); // 73 -> old
        
        //extra normals
        mesh.addNormal(0, 0, -1); // 74
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);  // 79

        mesh.addTriangle((short) 70, (short) 66, (short) 76);
        mesh.addTriangle((short) 67, (short) 71, (short) 70);
        mesh.addTriangle((short) 71, (short) 67, (short) 68);
        mesh.addTriangle((short) 68, (short) 72, (short) 71);
        mesh.addTriangle((short) 72, (short) 68, (short) 69);
        mesh.addTriangle((short) 69, (short) 73, (short) 72);
        
        mesh.addTriangle((short) 69, (short) 74, (short) 75);
        mesh.addTriangle((short) 75, (short) 76, (short) 69);
        mesh.addTriangle((short) 73, (short) 69, (short) 76);
        mesh.addTriangle((short) 76, (short) 77, (short) 73);
        mesh.addTriangle((short) 78, (short) 71, (short) 72);
        mesh.addTriangle((short) 72, (short) 79, (short) 78);

        //CHEST  80 - 93
        //
        //       (92) - (93)
        // 84 --- 85 --- 86 --- 87 - (91)
        // 80 --- 81 --- 82 --- 83 - (90)
        //                     (88)- (89)
        mesh.addVertex(new Vector3f(-2, 15, -1));
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, -1));

        mesh.addVertex(new Vector3f(-2, 18, -1));
        mesh.addVertex(new Vector3f(-2, 18, 1));
        mesh.addVertex(new Vector3f(2, 18, 1));
        mesh.addVertex(new Vector3f(2, 18, -1));
        
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(-2, 15, -1));
        mesh.addVertex(new Vector3f(-2, 18, -1));
        mesh.addVertex(new Vector3f(-2, 18, -1));
        mesh.addVertex(new Vector3f(2, 18, -1));

        //normals
        mesh.addNormal(0, 0, -1); // 74 -> 80
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); // 81 (old) ->87
        
        //extra normals
        mesh.addNormal(0, 0, -1); // 88
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1); // 93

        mesh.addTriangle((short) 84, (short) 80, (short) 81);
        mesh.addTriangle((short) 81, (short) 85, (short) 84);
        mesh.addTriangle((short) 85, (short) 81, (short) 82);
        mesh.addTriangle((short) 82, (short) 86, (short) 85);
        mesh.addTriangle((short) 86, (short) 82, (short) 83);
        mesh.addTriangle((short) 83, (short) 87, (short) 86);
        
        mesh.addTriangle((short) 83, (short) 88, (short) 89);
        mesh.addTriangle((short) 89, (short) 90, (short) 83);
        mesh.addTriangle((short) 87, (short) 83, (short) 90);
        mesh.addTriangle((short) 90, (short) 91, (short) 87);
        mesh.addTriangle((short) 92, (short) 85, (short) 86);
        mesh.addTriangle((short) 86, (short) 93, (short) 92);
    }
    public void buildArm(){
        //RIGHT ARM   94  - 120
        //
        //        (119) - (120)
        // 108 --- 109 --- 110 --- 111 - (118)
        // 104 --- 105 --- 106 --- 107 - (117)
        // 102  <   |      |    >  103 - (116)
        // 98  --- 99  --- 100 --- 101 - (115)
        // 94  --- 95  --- 96  --- 97  - (114)
        //                        (112)- (113)

        // Shift X by +1*2
        mesh.addVertex(new Vector3f(2, 10, -1)); // 94
        mesh.addVertex(new Vector3f(2, 10, 1));
        mesh.addVertex(new Vector3f(4, 10, 1));
        mesh.addVertex(new Vector3f(4, 10, -1));

        mesh.addVertex(new Vector3f(2, 13, -1)); // 98
        mesh.addVertex(new Vector3f(2, 13, 1));
        mesh.addVertex(new Vector3f(4, 13, 1));
        mesh.addVertex(new Vector3f(4, 13, -1));

        mesh.addVertex(new Vector3f(2, 14, -1)); // 102
        //mesh.addVertex(new Vector3f(2,14,1));
        //mesh.addVertex(new Vector3f(0,14,1));
        mesh.addVertex(new Vector3f(4, 14, -1)); // 103

        mesh.addVertex(new Vector3f(2, 15, -1)); // 104
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(4, 15, 1));
        mesh.addVertex(new Vector3f(4, 15, -1));

        mesh.addVertex(new Vector3f(2, 18, -1)); //108
        mesh.addVertex(new Vector3f(2, 18, 1));
        mesh.addVertex(new Vector3f(4, 18, 1));
        mesh.addVertex(new Vector3f(4, 18, -1));
        
        mesh.addVertex(new Vector3f(2, 10, 1)); // 112
        mesh.addVertex(new Vector3f(4, 10, 1)); // 113
        mesh.addVertex(new Vector3f(2, 10, -1)); // 114 -> 94
        mesh.addVertex(new Vector3f(2, 13, -1)); // 115 -> 98
        mesh.addVertex(new Vector3f(2, 14, -1)); // 116 -> 102
        mesh.addVertex(new Vector3f(2, 15, -1)); // 117 -> 104
        mesh.addVertex(new Vector3f(2, 18, -1)); // 118 -> 108
        mesh.addVertex(new Vector3f(2, 18, -1)); // 119 -> 108
        mesh.addVertex(new Vector3f(4, 18, -1)); // 120 -> 111

        // Normals
        mesh.addNormal(0, 0, 1); // 82 (old) -> 94
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1); //99 (old) -> 111
        
        //extra normals
        mesh.addNormal(0, 0, 1); // 112
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1); // 120

        // LEFT ARM -> FIXED INDICES!!!
        //  121  -  147
        //
        //       (146)---(147)
        // 135 --- 136 --- 137 --- 138 - (145)
        // 131 --- 132 --- 133 --- 134 - (144)
        // 129  <   |       |   >  130 - (143)
        // 125 --- 126 --- 127 --- 128 - (142)
        // 121 --- 122 --- 123 --- 124 - (141)
        //       (139) - (140)
        // Shift X by -1*2
        mesh.addVertex(new Vector3f(-4, 10, -1));
        mesh.addVertex(new Vector3f(-4, 10, 1));
        mesh.addVertex(new Vector3f(-2, 10, 1));
        mesh.addVertex(new Vector3f(-2, 10, -1));

        mesh.addVertex(new Vector3f(-4, 13, -1));
        mesh.addVertex(new Vector3f(-4, 13, 1));
        mesh.addVertex(new Vector3f(-2, 13, 1));
        mesh.addVertex(new Vector3f(-2, 13, -1));

        mesh.addVertex(new Vector3f(-4, 14, -1));
        //mesh.addVertex(new Vector3f(0,14,1));
        //mesh.addVertex(new Vector3f(-2,14,1));
        mesh.addVertex(new Vector3f(-2, 14, -1));

        mesh.addVertex(new Vector3f(-4, 15, -1));
        mesh.addVertex(new Vector3f(-4, 15, 1));
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(-2, 15, -1));

        mesh.addVertex(new Vector3f(-4, 18, -1));
        mesh.addVertex(new Vector3f(-4, 18, 1));
        mesh.addVertex(new Vector3f(-2, 18, 1));
        mesh.addVertex(new Vector3f(-2, 18, -1));

        // Normals
        mesh.addNormal(0, 0, 1); // 100 (old) -> 121
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, 1);
        //mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1); // 117 (old) -> 138
        
        //extra normals
        mesh.addNormal(0, 0, 1); // 139
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);

        mesh.addNormal(0, 0, 1); // 147

        //RIGHT ARM   94  - 120
        //
        //        (119) - (120)
        // 108 --- 109 --- 110 --- 111 - (118)
        // 104 --- 105 --- 106 --- 107 - (117)
        // 102  <   |      |    >  103 - (116)
        // 98  --- 99  --- 100 --- 101 - (115)
        // 94  --- 95  --- 96  --- 97  - (114)
        //                        (112)- (113)

        mesh.addTriangle((short) 98, (short) 94, (short) 95);
        mesh.addTriangle((short) 95, (short) 99, (short) 98);
        mesh.addTriangle((short) 99, (short) 95, (short) 96);
        mesh.addTriangle((short) 96, (short) 100, (short) 99);
        mesh.addTriangle((short) 100, (short) 96, (short) 97);
        mesh.addTriangle((short) 97, (short) 101, (short) 100);

        mesh.addTriangle((short) 102, (short) 98, (short) 99);
        mesh.addTriangle((short) 102, (short) 99, (short) 105);
        mesh.addTriangle((short) 104, (short) 102, (short) 105);

        mesh.addTriangle((short) 105, (short) 99, (short) 100);
        mesh.addTriangle((short) 100, (short) 106, (short) 105);
        mesh.addTriangle((short) 106, (short) 100, (short) 103);
        mesh.addTriangle((short) 100, (short) 101, (short) 103);
        mesh.addTriangle((short) 103, (short) 107, (short) 106);

        mesh.addTriangle((short) 108, (short) 104, (short) 105);
        mesh.addTriangle((short) 105, (short) 109, (short) 108);
        mesh.addTriangle((short) 109, (short) 105, (short) 106);
        mesh.addTriangle((short) 106, (short) 110, (short) 109);
        mesh.addTriangle((short) 110, (short) 106, (short) 107);
        mesh.addTriangle((short) 107, (short) 111, (short) 110);

        //left tris
        mesh.addTriangle((short) 97, (short) 112, (short) 113);
        mesh.addTriangle((short) 113, (short) 114, (short) 97);
        mesh.addTriangle((short) 101, (short) 97, (short) 114);
        mesh.addTriangle((short) 114, (short) 115, (short) 101);
        mesh.addTriangle((short) 103, (short) 101, (short) 115);
        mesh.addTriangle((short) 115, (short) 116, (short) 103);
        mesh.addTriangle((short) 107, (short) 103, (short) 116);
        mesh.addTriangle((short) 116, (short) 117, (short) 107);

        //TOP AND BOTTOM
        mesh.addTriangle((short) 111, (short) 107, (short) 117);
        mesh.addTriangle((short) 117, (short) 118, (short) 111);
        mesh.addTriangle((short) 119, (short) 109, (short) 110);
        mesh.addTriangle((short) 110, (short) 120, (short) 119);

        // LEFT ARM -> FIXED INDICES!!!
        //  121  -  147
        //
        //        (146)---(147)
        // 135 --- 136 --- 137 --- 138 - (145)
        // 131 --- 132 --- 133 --- 134 - (144)
        // 129  <   |       |   >  130 - (143)
        // 125 --- 126 --- 127 --- 128 - (142)
        // 121 --- 122 --- 123 --- 124 - (141)
        //                        (139)- (140)

        //FINISH THESE!!!
        mesh.addTriangle((short) 125, (short) 121, (short) 122);
        mesh.addTriangle((short) 122, (short) 126, (short) 125);
        mesh.addTriangle((short) 126, (short) 122, (short) 123);
        mesh.addTriangle((short) 123, (short) 127, (short) 126);
        mesh.addTriangle((short) 127, (short) 123, (short) 124);
        mesh.addTriangle((short) 124, (short) 128, (short) 127);

        mesh.addTriangle((short) 129, (short) 125, (short) 126);
        mesh.addTriangle((short) 126, (short) 132, (short) 129);
        mesh.addTriangle((short) 131, (short) 129, (short) 132);
        
        mesh.addTriangle((short) 132, (short) 126, (short) 127);
        mesh.addTriangle((short) 127, (short) 133, (short) 132);
        
        mesh.addTriangle((short) 133, (short) 127, (short) 130);
        mesh.addTriangle((short) 130, (short) 127, (short) 128);
        mesh.addTriangle((short) 130, (short) 134, (short) 133);

        mesh.addTriangle((short) 135, (short) 131, (short) 132);
        mesh.addTriangle((short) 132, (short) 136, (short) 135);
        mesh.addTriangle((short) 136, (short) 132, (short) 133);
        mesh.addTriangle((short) 133, (short) 137, (short) 136);
        mesh.addTriangle((short) 137, (short) 133, (short) 134);
        mesh.addTriangle((short) 134, (short) 138, (short) 137);
        
        mesh.addTriangle((short) 124, (short) 139, (short) 140);
        mesh.addTriangle((short) 140, (short) 141, (short) 124);
        mesh.addTriangle((short) 128, (short) 124, (short) 141);
        mesh.addTriangle((short) 141, (short) 142, (short) 128);
        mesh.addTriangle((short) 130, (short) 128, (short) 142);
        mesh.addTriangle((short) 142, (short) 143, (short) 130);
        mesh.addTriangle((short) 134, (short) 130, (short) 143);
        mesh.addTriangle((short) 143, (short) 144, (short) 134);

        //BOTTOM/TOP VERTS
        mesh.addTriangle((short) 138, (short) 134, (short) 144);
        mesh.addTriangle((short) 144, (short) 145, (short) 138);
        mesh.addTriangle((short) 146, (short) 136, (short) 137);
        mesh.addTriangle((short) 137, (short) 147, (short) 146);

        
    }
    public void buildHead(){ //103 to 161 (+ 58 verts)
        //Dimension variables
        float headWidth = 1.5f;
        float headHeight = 3f;
        float neckHeight = 18f;
        
        //HEAD  148 -  161
        //
        //        (160) - (161)
        // 152 --- 153 --- 154 --- 155 - (159)
        // 148 --- 149 --- 150 --- 151 - (158)
        //                        (156)- (157)

        mesh.addVertex(new Vector3f(-headWidth, neckHeight, -headWidth)); // 148
        mesh.addVertex(new Vector3f(-headWidth, neckHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight, -headWidth));  // 151

        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, -headWidth)); // 152
        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight + headHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight + headHeight, -headWidth));  // 155
        
        mesh.addVertex(new Vector3f(-headWidth, neckHeight, headWidth)); //156 -> 149
        mesh.addVertex(new Vector3f(headWidth, neckHeight, headWidth)); // 157 -> 150
        mesh.addVertex(new Vector3f(-headWidth, neckHeight, -headWidth)); //158 -> 148
        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, -headWidth)); // 159 -> 152
        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, -headWidth)); // 160 -> 152
        mesh.addVertex(new Vector3f(headWidth, neckHeight + headHeight, -headWidth));  // 161 -> 155

        //normals
        mesh.addNormal(0, 0, -1); // 118 (old) 148
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1); // 125 (old) // 155
        
        mesh.addNormal(0, 0, -1); // 156
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1); // 161
        
        mesh.addTriangle((short) 152, (short) 148, (short) 149);
        mesh.addTriangle((short) 149, (short) 153, (short) 152);
        mesh.addTriangle((short) 153, (short) 149, (short) 150);
        mesh.addTriangle((short) 150, (short) 154, (short) 153);
        mesh.addTriangle((short) 154, (short) 150, (short) 151);
        mesh.addTriangle((short) 151, (short) 155, (short) 154);
        
        //FIX THESE INDICES!!!
        mesh.addTriangle((short) 151, (short) 156, (short) 157);
        mesh.addTriangle((short) 157, (short) 158, (short) 151);
        mesh.addTriangle((short) 155, (short) 151, (short) 158);
        mesh.addTriangle((short) 158, (short) 159, (short) 155);
        mesh.addTriangle((short) 160, (short) 153, (short) 154);
        mesh.addTriangle((short) 154, (short) 161, (short) 160);
    }
    public void buildBones(AssetManager assetManager){
        sceneMesh = mesh.buildBlockMesh();
        // Setup bone weight buffer
        
        
        VertexBuffer weightsHW = new VertexBuffer(Type.HWBoneWeight);
        VertexBuffer indicesHW = new VertexBuffer(Type.HWBoneIndex);
        indicesHW.setUsage(Usage.CpuOnly);
        weightsHW.setUsage(Usage.CpuOnly);
        sceneMesh.setBuffer(weightsHW);
        sceneMesh.setBuffer(indicesHW);

        // Setup bone weight buffer
        weights = FloatBuffer.allocate( sceneMesh.getVertexCount() * 4 );
        VertexBuffer weightsBuf = new VertexBuffer(Type.BoneWeight);
        weightsBuf.setupData(Usage.CpuOnly, 4, VertexBuffer.Format.Float, weights);
        sceneMesh.setBuffer(weightsBuf);

        // Setup bone index buffer
        //indices = ByteBuffer.allocate( sceneMesh.getVertexCount() * 4 );
        System.out.println("Vertex vCount: " + mesh.positions.size());
        System.out.println("Vertex mCount: " + sceneMesh.getVertexCount());
        indices = ByteBuffer.allocate( 162 * 4 );
        VertexBuffer indicesBuf = new VertexBuffer(Type.BoneIndex);
        indicesBuf.setupData(Usage.CpuOnly, 4, VertexBuffer.Format.UnsignedByte, indices);
        sceneMesh.setBuffer(indicesBuf);
        
        // Create bind pose buffers
        sceneMesh.generateBindPose(true);

        // Create skeleton
        root = new Bone("Root");
        pelvis = new Bone("Pelvis");
        
        
        hip_L = new Bone("Hip_L");
        hip_R = new Bone("Hip_R");
        knee_L = new Bone("Knee_L");
        knee_R = new Bone("Knee_R");
        //waist = new Bone("Waist");
        torso = new Bone("Torso");
        chest = new Bone("Chest");
        shld_L = new Bone("Shld_L");       //Shoulder_L
        shld_R = new Bone("Shld_R");        //Shoulder_R
        elb_L = new Bone("Elb_L");         //Elbow_L
        elb_R = new Bone("Elb_R");         //Elbow_R
        neck = new Bone("Neck");
        //(Vector3f translation, Quaternion rotation, Vector3f scale)
        root.setBindTransforms(Vector3f.ZERO, Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        pelvis.setBindTransforms(new Vector3f(0, 10, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        hip_L.setBindTransforms(new Vector3f(-1, 0, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        hip_R.setBindTransforms(new Vector3f(1, 0, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        knee_L.setBindTransforms(new Vector3f(0, -4, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        knee_R.setBindTransforms(new Vector3f(0, -4, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);

        torso.setBindTransforms(new Vector3f(0, 2, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        chest.setBindTransforms(new Vector3f(0, 3, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        shld_L.setBindTransforms(new Vector3f(-3, 3, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        shld_R.setBindTransforms(new Vector3f(3, 3, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        elb_L.setBindTransforms(new Vector3f(0, -4, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        elb_R.setBindTransforms(new Vector3f(0, -4, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        neck.setBindTransforms(new Vector3f(0, 3, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);

        // Create the parent-child relationships here
        root.addChild(pelvis);
        pelvis.addChild(hip_L);
        pelvis.addChild(hip_R);
        hip_L.addChild(knee_L);
        hip_R.addChild(knee_R);

        pelvis.addChild(torso);
        torso.addChild(chest);
        chest.addChild(shld_L);
        chest.addChild(shld_R);
        shld_L.addChild(elb_L);
        shld_R.addChild(elb_R);
        chest.addChild(neck);

        skeleton = new Skeleton(new Bone[]{root, pelvis, hip_L, hip_R, knee_L, knee_R, torso, chest,
            shld_L, shld_R, elb_L, elb_R, neck});

        // Enable user controls
        root.setUserControl(true);
        pelvis.setUserControl(true);
        hip_L.setUserControl(true);
        hip_R.setUserControl(true);
        knee_L.setUserControl(true);
        knee_R.setUserControl(true);
        torso.setUserControl(true);
        chest.setUserControl(true);
        shld_L.setUserControl(true);
        shld_R.setUserControl(true);
        elb_L.setUserControl(true);
        elb_R.setUserControl(true);
        neck.setUserControl(true);

        // Assign all verticies to bone 0 with weight 1
        /*
        RootTarget = new BoneTrack(0);
        PelvisTarget = new BoneTrack(1);
        Hip_LTarget = new BoneTrack(2);
        Hip_RTarget = new BoneTrack(3);
        Knee_LTarget = new BoneTrack(4);
        Knee_RTarget = new BoneTrack(5);
        TorsoTarget = new BoneTrack(6);
        ChestTarget = new BoneTrack(7);
        Shld_LTarget = new BoneTrack(8);
        Shld_RTarget = new BoneTrack(9);
        Elb_LTarget = new BoneTrack(10);
        Elb_RTarget = new BoneTrack(11);
        NeckTarget = new BoneTrack(12);

        // Create bone weight indices
        //*/
        //RIGHT LEG 0 - 25
        //       (24) ---(25)
        // 14 --- 15  --- 16 --- 17 - (23)
        // 10 --- 11  --- 12 --- 13 - (22)
        //     >  8   --- 9   <  
        // 4  --- 5   --- 6  --- 7  - (21)
        // 0  --- 1   --- 2  --- 3  - (20)
        //                      (18)- (19)
        boneIndex(0, 5);
        boneIndex(1, 5);
        boneIndex(2, 5);
        boneIndex(3, 5);
        boneIndex(4, 5);
        boneIndex(5, 5);
        boneIndex(6, 5);
        boneIndex(7, 5);
        boneIndex(18, 5);
        boneIndex(19, 5);
        boneIndex(20, 5);
        boneIndex(21, 5);

        boneIndexMult(8, 3, 5);
        boneIndexMult(9, 3, 5);

        boneIndex(10, 3);
        boneIndex(11, 3);
        boneIndex(12, 3);
        boneIndex(13, 3);
        boneIndex(14, 3);
        boneIndex(15, 3);
        boneIndex(16, 3);
        boneIndex(17, 3);
        boneIndex(22, 3);
        boneIndex(23, 3);
        boneIndex(24, 3);
        boneIndex(25, 3);
        /* */

        // LEFT LEG   26 - 51
        //       (50) - (51)
        // 40 --- 41 --- 42 --- 43 - (49)
        // 36 --- 37 --- 38 --- 39 - (48)
        //     >  34 --- 35  <
        // 30 --- 31 --- 32 --- 33 - (47)
        // 26 --- 27 --- 28 --- 29 - (46)
        //                     (44)- (45)
        boneIndex(26, 4);
        boneIndex(27, 4);
        boneIndex(28, 4);
        boneIndex(29, 4);
        boneIndex(30, 4);
        boneIndex(31, 4);
        boneIndex(32, 4);
        boneIndex(33, 4);
        boneIndex(44, 4);
        boneIndex(45, 4);
        boneIndex(46, 4);
        boneIndex(47, 4);

        boneIndexMult(34, 2, 4);
        boneIndexMult(35, 2, 4);

        boneIndex(36, 2);
        boneIndex(37, 2);
        boneIndex(38, 2);
        boneIndex(39, 2);
        boneIndex(40, 2);
        boneIndex(41, 2);
        boneIndex(42, 2);
        boneIndex(43, 2);
        boneIndex(48, 2);
        boneIndex(49, 2);
        boneIndex(50, 2);
        boneIndex(51, 2);

        //PELVIS   52 - 65
        //
        //       (64) - (65)
        // 56 --- 57 --- 58 --- 59 - (63)
        // 52 --- 53 --- 54 --- 55 - (62)
        //                     (60)- (61)
        boneIndex(52, 1);
        boneIndex(53, 1);
        boneIndex(54, 1);
        boneIndex(55, 1);
        boneIndex(56, 1);
        boneIndex(57, 1);
        boneIndex(58, 1);
        boneIndex(59, 1);
        boneIndex(60, 1);
        boneIndex(61, 1);
        boneIndex(62, 1);
        boneIndex(63, 1);
        boneIndex(64, 1);
        boneIndex(65, 1);

        //TORSO   66 - 79
        //
        //       (78) - (79)
        // 70 --- 71 --- 72 --- 73 - (77)
        // 66 --- 67 --- 68 --- 69 - (76)
        //                     (74)- (75)
        boneIndex(66, 6);
        boneIndex(67, 6);
        boneIndex(68, 6);
        boneIndex(69, 6);
        boneIndex(70, 6);
        boneIndex(71, 6);
        boneIndex(72, 6);
        boneIndex(73, 6);
        boneIndex(74, 6);
        boneIndex(75, 6);
        boneIndex(76, 6);
        boneIndex(77, 6);
        boneIndex(78, 6);
        boneIndex(79, 6);


        //CHEST  80 - 93
        //
        //       (92) - (93)
        // 84 --- 85 --- 86 --- 87 - (91)
        // 80 --- 81 --- 82 --- 83 - (90)
        //                     (88)- (89)
        boneIndex(80, 7);
        boneIndex(81, 7);
        boneIndex(82, 7);
        boneIndex(83, 7);
        boneIndex(84, 7);
        boneIndex(85, 7);
        boneIndex(86, 7);
        boneIndex(87, 7);
        boneIndex(88, 7);
        boneIndex(89, 7);
        boneIndex(90, 7);
        boneIndex(91, 7);
        boneIndex(92, 7);
        boneIndex(93, 7);
        
        //RIGHT ARM   94  - 120
        //
        //        (119) - (120)
        // 108 --- 109 --- 110 --- 111 - (118)
        // 104 --- 105 --- 106 --- 107 - (117)
        // 102  <   |      |    >  103 - (116)
        // 98  --- 99  --- 100 --- 101 - (115)
        // 94  --- 95  --- 96  --- 97  - (114)
        //                        (112)- (113)
        boneIndex(94, 11);
        boneIndex(95, 11);
        boneIndex(96, 11);
        boneIndex(97, 11);
        boneIndex(98, 11);
        boneIndex(99, 11);
        boneIndex(100, 11);
        boneIndex(101, 11);
        boneIndex(112, 11);
        boneIndex(113, 11);
        boneIndex(114, 11);
        boneIndex(115, 11);

        boneIndexMult(102, 11, 9);
        boneIndexMult(103, 11, 9);
        boneIndexMult(116, 11, 9);

        boneIndex(104, 9);
        boneIndex(105, 9);
        boneIndex(106, 9);
        boneIndex(107, 9);
        boneIndex(108, 9);
        boneIndex(109, 9);
        boneIndex(110, 9);
        boneIndex(111, 9);
        boneIndex(117, 9);
        boneIndex(118, 9);
        boneIndex(119, 9);
        boneIndex(120, 9);

        // LEFT ARM -> FIXED INDICES!!!
        //  121  -  147
        //
        //        (146)---(147)
        // 135 --- 136 --- 137 --- 138 - (145)
        // 131 --- 132 --- 133 --- 134 - (144)
        // 129  <   |       |   >  130 - (143)
        // 125 --- 126 --- 127 --- 128 - (142)
        // 121 --- 122 --- 123 --- 124 - (141)
        //                         139 - (140)                         
        boneIndex(121, 10);
        boneIndex(122, 10);
        boneIndex(123, 10);
        boneIndex(124, 10);
        boneIndex(125, 10);
        boneIndex(126, 10);
        boneIndex(127, 10);
        boneIndex(128, 10);
        boneIndex(139, 10);
        boneIndex(140, 10);
        boneIndex(141, 10);
        boneIndex(142, 10);

        boneIndexMult(129, 10, 8);
        boneIndexMult(130, 10, 8);
        boneIndexMult(143, 10, 8);

        boneIndex(131, 8);
        boneIndex(132, 8);
        boneIndex(133, 8);
        boneIndex(134, 8);
        boneIndex(135, 8);
        boneIndex(136, 8);
        boneIndex(137, 8);
        boneIndex(138, 8);
        boneIndex(144, 8);
        boneIndex(145, 8);
        boneIndex(146, 8);
        boneIndex(147, 8);
        
        //HEAD  148 -  161
        //
        //        (160) - (161)
        // 152 --- 153 --- 154 --- 155 - (159)
        // 148 --- 149 --- 150 --- 151 - (158)
        //                        (156)- (157)
        boneIndex(148, 12);
        boneIndex(149, 12);
        boneIndex(150, 12);
        boneIndex(151, 12);
        boneIndex(152, 12);
        boneIndex(153, 12);
        boneIndex(154, 12);
        boneIndex(155, 12);
        boneIndex(156, 12);
        boneIndex(157, 12);
        boneIndex(158, 12);
        boneIndex(159, 12);
        boneIndex(160, 12);
        boneIndex(161, 12);

        // Maximum number of weights per bone is now 2
        sceneMesh.setMaxNumWeights(2);

        // Create model
        Box box2 = new Box(0.25f, 0.25f, 0.25f);
        Geometry debugRoot = new Geometry("debugRoot", box2);
        Geometry debugPelvis = new Geometry("debugPelvis", box2);
        Geometry debugHipL = new Geometry("debugHipL", box2);
        Geometry debugHipR = new Geometry("debugHipR", box2);
        Geometry debugKneeL = new Geometry("debugKneeL", box2);
        Geometry debugKneeR = new Geometry("debugKneeR", box2);
        Geometry debugTorso = new Geometry("debugTorso", box2);
        Geometry debugChest = new Geometry("debugChest", box2);
        Geometry debugShldL = new Geometry("debugShldL", box2);
        Geometry debugShldR = new Geometry("debugShldR", box2);
        Geometry debugElbL = new Geometry("debugElbL", box2);
        Geometry debugElbR = new Geometry("debugElbR", box2);
        Geometry debugNeck = new Geometry("debugNeck", box2);

        Geometry geom = new Geometry("sceneMesh", sceneMesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matR = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matG = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        Material matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        matR.setColor("Color", ColorRGBA.Red);
        matG.setColor("Color", ColorRGBA.Green);

        matWire.setColor("Color", ColorRGBA.Blue);
        matWire.getAdditionalRenderState().setWireframe(true);

        debugRoot.setMaterial(matR);
        debugPelvis.setMaterial(mat);
        debugHipL.setMaterial(mat);
        debugHipR.setMaterial(mat);
        debugKneeL.setMaterial(matG);
        debugKneeR.setMaterial(matG);
        debugTorso.setMaterial(matG);
        debugChest.setMaterial(matR);
        debugShldL.setMaterial(mat);
        debugShldR.setMaterial(mat);
        debugElbL.setMaterial(matG);
        debugElbR.setMaterial(matG);
        debugNeck.setMaterial(mat);

        geom.setMaterial(matWire);
        model = new Node("model");
        model.attachChild(geom);

        // Create skeleton control
        skeletonControl = new SkeletonControl(skeleton);
        model.addControl(skeletonControl);

        //rootNode.attachChild(model);
        skeletonControl.getAttachmentsNode("Root").attachChild(debugRoot);
        skeletonControl.getAttachmentsNode("Pelvis").attachChild(debugPelvis);
        skeletonControl.getAttachmentsNode("Hip_L").attachChild(debugHipL);
        skeletonControl.getAttachmentsNode("Hip_R").attachChild(debugHipR);
        skeletonControl.getAttachmentsNode("Knee_L").attachChild(debugKneeL);
        skeletonControl.getAttachmentsNode("Knee_R").attachChild(debugKneeR);
        skeletonControl.getAttachmentsNode("Torso").attachChild(debugTorso);
        skeletonControl.getAttachmentsNode("Chest").attachChild(debugChest);
        skeletonControl.getAttachmentsNode("Shld_L").attachChild(debugShldL);
        skeletonControl.getAttachmentsNode("Shld_R").attachChild(debugShldR);
        skeletonControl.getAttachmentsNode("Elb_L").attachChild(debugElbL);
        skeletonControl.getAttachmentsNode("Elb_R").attachChild(debugElbR);
        skeletonControl.getAttachmentsNode("Neck").attachChild(debugNeck);
    }
    public void boneIndex(int vert, int bone) {
        int i = vert * 4;

        // Assign vertex to bone index
        indices.array()[i + 0] = (byte) bone;
        indices.array()[i + 1] = (byte) bone;
        indices.array()[i + 2] = (byte) bone;
        indices.array()[i + 3] = (byte) bone;

        // Assign weights for influence
        weights.array()[i + 0] = 1;
        weights.array()[i + 1] = 0;
        weights.array()[i + 2] = 0;
        weights.array()[i + 3] = 0;
    }
    public void boneIndexMult(int vert, int boneA, int boneB) {
        int i = vert * 4;

        // Assign vertex to bone index
        indices.array()[i + 0] = (byte) boneA;
        indices.array()[i + 1] = (byte) boneA;
        indices.array()[i + 2] = (byte) boneB;
        indices.array()[i + 3] = (byte) boneB;

        // Assign weights for influence
        weights.array()[i + 0] = 0.5f;
        weights.array()[i + 1] = 0.5f;
        weights.array()[i + 2] = 0;
        weights.array()[i + 3] = 0;
    }
    public void updateHumanoid(float tpf){
        skeleton.updateWorldVectors();
    }
    public void setUserTransforms(String name, float[] eulerRots, float[] translation){
        Quaternion rotate = new Quaternion();
        Vector3f offset = new Vector3f();
        rotate.fromAngles(eulerRots);
        offset.set(translation[0],translation[1],translation[2]);
        switch(name){
            case "root":
                root.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "pelvis":
                pelvis.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "hip_L":
                hip_L.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "hip_R":
                hip_R.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "knee_L":
                knee_L.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "knee_R":
                knee_R.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "torso":
                torso.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "chest":
                chest.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "shld_L":
                shld_L.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "shld_R":
                shld_R.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "elb_L":
                elb_L.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "elb_R":
                elb_R.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
            case "neck":
                neck.setUserTransforms(offset, rotate, Vector3f.UNIT_XYZ);
                break;
        }
    }
}

