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
        mesh.addTCoords(new Vector2f(6 * texture32Unit,10 * texture32Unit));

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
        //PELVIS   51 - 64
        //       (63) - (64)
        // 55 --- 56 --- 57 --- 58 - (62)
        // 51 --- 52 --- 53 --- 54 - (61)
        //       (59) - (60)

        mesh.addVertex(new Vector3f(-2, 10, -1)); //51
        mesh.addVertex(new Vector3f(-2, 10, 1));  //52
        mesh.addVertex(new Vector3f(2, 10, 1));   //53
        mesh.addVertex(new Vector3f(2, 10, -1));  //54

        mesh.addVertex(new Vector3f(-2, 12, -1)); //55
        mesh.addVertex(new Vector3f(-2, 12, 1));  //56
        mesh.addVertex(new Vector3f(2, 12, 1));   //57
        mesh.addVertex(new Vector3f(2, 12, -1));  //58
        
        mesh.addVertex(new Vector3f(-2, 10, 1));   //59
        mesh.addVertex(new Vector3f(2, 10, 1));  //60

        mesh.addVertex(new Vector3f(-2, 10, -1)); //61
        mesh.addVertex(new Vector3f(-2, 12, -1));  //62
        mesh.addVertex(new Vector3f(-2, 12, -1));   //63
        mesh.addVertex(new Vector3f(2, 12, -1));  //64

        //normals
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        //Extra normals
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        //LOCATED POSSIBLE INDEX BUG
        //mesh.addTriangle((short) 40, (short) 36, (short) 47);
        mesh.addTriangle((short) 40, (short) 36, (short) 37);
        mesh.addTriangle((short) 37, (short) 41, (short) 40);
        mesh.addTriangle((short) 41, (short) 37, (short) 38);
        mesh.addTriangle((short) 38, (short) 42, (short) 41);
        mesh.addTriangle((short) 42, (short) 38, (short) 39);
        mesh.addTriangle((short) 39, (short) 43, (short) 42);

        mesh.addTriangle((short) 40, (short) 41, (short) 42);
        mesh.addTriangle((short) 42, (short) 43, (short) 40);
        mesh.addTriangle((short) 43, (short) 39, (short) 36);
        mesh.addTriangle((short) 36, (short) 40, (short) 43);
        mesh.addTriangle((short) 37, (short) 36, (short) 39);
        mesh.addTriangle((short) 39, (short) 38, (short) 37);

        //TODO: Create pelvis verts, normals, and tris

        //TORSO
        //       (48) - (51)
        // 48 --- 49 --- 50 --- 51 - (48)
        // 44 --- 45 --- 46 --- 47 - (44)
        //       (44) - (47)

        mesh.addVertex(new Vector3f(-2, 12, -1));
        mesh.addVertex(new Vector3f(-2, 12, 1));
        mesh.addVertex(new Vector3f(2, 12, 1));
        mesh.addVertex(new Vector3f(2, 12, -1));

        mesh.addVertex(new Vector3f(-2, 15, -1));
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, -1));

        //normals
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addTriangle((short) 48, (short) 44, (short) 45);
        mesh.addTriangle((short) 45, (short) 49, (short) 48);
        mesh.addTriangle((short) 49, (short) 45, (short) 46);
        mesh.addTriangle((short) 46, (short) 50, (short) 49);
        mesh.addTriangle((short) 50, (short) 46, (short) 47);
        mesh.addTriangle((short) 47, (short) 51, (short) 50);
        mesh.addTriangle((short) 45, (short) 44, (short) 47);
        mesh.addTriangle((short) 47, (short) 46, (short) 45);
        mesh.addTriangle((short) 48, (short) 49, (short) 50);
        mesh.addTriangle((short) 50, (short) 51, (short) 48);

        //CHEST
        //       (56) - (59)
        // 56 --- 57 --- 58 --- 59 - (56)
        // 52 --- 53 --- 54 --- 55 - (52)
        //       (52) - (55)
        mesh.addVertex(new Vector3f(-2, 15, -1));
        mesh.addVertex(new Vector3f(-2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(2, 15, -1));

        mesh.addVertex(new Vector3f(-2, 18, -1));
        mesh.addVertex(new Vector3f(-2, 18, 1));
        mesh.addVertex(new Vector3f(2, 18, 1));
        mesh.addVertex(new Vector3f(2, 18, -1));

        //normals
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);

        mesh.addTriangle((short) 56, (short) 52, (short) 53);
        mesh.addTriangle((short) 53, (short) 57, (short) 56);
        mesh.addTriangle((short) 57, (short) 53, (short) 54);
        mesh.addTriangle((short) 54, (short) 58, (short) 57);
        mesh.addTriangle((short) 58, (short) 54, (short) 55);
        mesh.addTriangle((short) 55, (short) 59, (short) 58);
        mesh.addTriangle((short) 59, (short) 55, (short) 52);
        mesh.addTriangle((short) 52, (short) 56, (short) 59);
        mesh.addTriangle((short) 53, (short) 52, (short) 55);
        mesh.addTriangle((short) 55, (short) 54, (short) 53);
        mesh.addTriangle((short) 56, (short) 57, (short) 58);
        mesh.addTriangle((short) 58, (short) 59, (short) 56);
    }
    public void buildArm(){
        //RIGHT ARM
        //       (74) - (77)
        // 74 --- 75 --- 76 --- 77 - (74)
        // 70 --- 71 --- 72 --- 73 - (70)
        // 68  <   |     |   >  69 - (68)
        // 64 --- 65 --- 66 --- 67 - (64)
        // 60 --- 61 --- 62 --- 63 - (60)
        //       (60) - (63)

        // Shift X by +1*2
        mesh.addVertex(new Vector3f(2, 10, -1));
        mesh.addVertex(new Vector3f(2, 10, 1));
        mesh.addVertex(new Vector3f(4, 10, 1));
        mesh.addVertex(new Vector3f(4, 10, -1));

        mesh.addVertex(new Vector3f(2, 13, -1));
        mesh.addVertex(new Vector3f(2, 13, 1));
        mesh.addVertex(new Vector3f(4, 13, 1));
        mesh.addVertex(new Vector3f(4, 13, -1));

        mesh.addVertex(new Vector3f(2, 14, -1));
        //mesh.addVertex(new Vector3f(2,14,1));
        //mesh.addVertex(new Vector3f(0,14,1));
        mesh.addVertex(new Vector3f(4, 14, -1));

        mesh.addVertex(new Vector3f(2, 15, -1));
        mesh.addVertex(new Vector3f(2, 15, 1));
        mesh.addVertex(new Vector3f(4, 15, 1));
        mesh.addVertex(new Vector3f(4, 15, -1));

        mesh.addVertex(new Vector3f(2, 18, -1));
        mesh.addVertex(new Vector3f(2, 18, 1));
        mesh.addVertex(new Vector3f(4, 18, 1));
        mesh.addVertex(new Vector3f(4, 18, -1));

        // Normals
        mesh.addNormal(0, 0, 1);
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
        mesh.addNormal(0, 0, 1);

        // LEFT ARM -> FIXED INDICES!!!
        //       (92)---(95)
        // 92 --- 93 --- 94 --- 95 - (92)
        // 88 --- 89 --- 90 --- 91 - (88)
        // 86  <   |     |   >  87 - (86)
        // 82 --- 83 --- 84 --- 85 - (82)
        // 78 --- 79 --- 80 --- 81 - (78)
        //       (78) - (81)
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
        mesh.addNormal(0, 0, 1);
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
        mesh.addNormal(0, 0, 1);

        //RIGHT ARM -> FIXED INDICES!!!
        //       (74) - (77)
        // 74 --- 75 --- 76 --- 77 - (74)
        // 70 --- 71 --- 72 --- 73 - (70)
        // 68  <   |     |   >  69 - (68)
        // 64 --- 65 --- 66 --- 67 - (64)
        // 60 --- 61 --- 62 --- 63 - (60)
        //       (60) - (63)

        mesh.addTriangle((short) 64, (short) 60, (short) 61);
        mesh.addTriangle((short) 61, (short) 65, (short) 64);
        mesh.addTriangle((short) 65, (short) 61, (short) 62);
        mesh.addTriangle((short) 62, (short) 66, (short) 65);
        mesh.addTriangle((short) 66, (short) 62, (short) 63);
        mesh.addTriangle((short) 63, (short) 67, (short) 66);

        mesh.addTriangle((short) 68, (short) 64, (short) 65);
        mesh.addTriangle((short) 65, (short) 71, (short) 68);
        mesh.addTriangle((short) 70, (short) 68, (short) 71);

        mesh.addTriangle((short) 71, (short) 65, (short) 66);
        mesh.addTriangle((short) 66, (short) 72, (short) 71);
        mesh.addTriangle((short) 72, (short) 66, (short) 69);
        mesh.addTriangle((short) 66, (short) 67, (short) 69);
        mesh.addTriangle((short) 69, (short) 73, (short) 72);

        mesh.addTriangle((short) 74, (short) 70, (short) 71);
        mesh.addTriangle((short) 71, (short) 75, (short) 74);
        mesh.addTriangle((short) 75, (short) 71, (short) 72);
        mesh.addTriangle((short) 72, (short) 76, (short) 75);
        mesh.addTriangle((short) 76, (short) 72, (short) 73);
        mesh.addTriangle((short) 73, (short) 77, (short) 76);

        //left tris
        mesh.addTriangle((short) 67, (short) 63, (short) 60);
        mesh.addTriangle((short) 60, (short) 64, (short) 67);
        mesh.addTriangle((short) 69, (short) 67, (short) 64);
        mesh.addTriangle((short) 64, (short) 68, (short) 69);
        mesh.addTriangle((short) 73, (short) 69, (short) 68);
        mesh.addTriangle((short) 68, (short) 70, (short) 73);
        mesh.addTriangle((short) 77, (short) 73, (short) 70);
        mesh.addTriangle((short) 70, (short) 74, (short) 77);

        //TOP AND BOTTOM
        mesh.addTriangle((short) 61, (short) 60, (short) 63);
        mesh.addTriangle((short) 63, (short) 62, (short) 61);
        mesh.addTriangle((short) 74, (short) 75, (short) 76);
        mesh.addTriangle((short) 76, (short) 77, (short) 74);

        // LEFT ARM -> FIXED INDICES!!!
        //       (92)---(95)
        // 92 --- 93 --- 94 --- 95 - (92)
        // 88 --- 89 --- 90 --- 91 - (88)
        // 86  <   |     |   >  87 - (86)
        // 82 --- 83 --- 84 --- 85 - (82)
        // 78 --- 79 --- 80 --- 81 - (78)
        //       (78) - (81)

        //FINISH THESE!!!
        mesh.addTriangle((short) 82, (short) 78, (short) 79);
        mesh.addTriangle((short) 79, (short) 83, (short) 82);
        mesh.addTriangle((short) 83, (short) 79, (short) 80);
        mesh.addTriangle((short) 80, (short) 84, (short) 83);
        mesh.addTriangle((short) 84, (short) 80, (short) 81);
        mesh.addTriangle((short) 81, (short) 85, (short) 84);

        mesh.addTriangle((short) 86, (short) 82, (short) 83);
        mesh.addTriangle((short) 83, (short) 89, (short) 86);
        mesh.addTriangle((short) 86, (short) 89, (short) 88);
        mesh.addTriangle((short) 89, (short) 83, (short) 84);
        mesh.addTriangle((short) 84, (short) 90, (short) 89);
        mesh.addTriangle((short) 90, (short) 84, (short) 87);
        mesh.addTriangle((short) 84, (short) 85, (short) 87);
        mesh.addTriangle((short) 87, (short) 91, (short) 90);

        mesh.addTriangle((short) 92, (short) 88, (short) 89);
        mesh.addTriangle((short) 89, (short) 93, (short) 92);
        mesh.addTriangle((short) 93, (short) 89, (short) 90);
        mesh.addTriangle((short) 90, (short) 94, (short) 93);
        mesh.addTriangle((short) 94, (short) 90, (short) 91);
        mesh.addTriangle((short) 91, (short) 95, (short) 94);

        //SIDE VERTS
        mesh.addTriangle((short) 85, (short) 81, (short) 78);
        mesh.addTriangle((short) 78, (short) 82, (short) 85);
        mesh.addTriangle((short) 87, (short) 85, (short) 82);
        mesh.addTriangle((short) 82, (short) 86, (short) 87);
        mesh.addTriangle((short) 91, (short) 87, (short) 86);
        mesh.addTriangle((short) 86, (short) 88, (short) 91);
        mesh.addTriangle((short) 95, (short) 91, (short) 88);
        mesh.addTriangle((short) 88, (short) 92, (short) 95);

        //BOTTOM/TOP VERTS
        mesh.addTriangle((short) 79, (short) 78, (short) 81);
        mesh.addTriangle((short) 81, (short) 80, (short) 79);
        mesh.addTriangle((short) 92, (short) 93, (short) 94);
        mesh.addTriangle((short) 94, (short) 95, (short) 92);
    }
    public void buildHead(){
        //HEAD
        //        (100) - (103)
        // 100 --- 101 --- 102 --- 103 - (100)
        // 96  --- 97  --- 98  --- 99  - (96)
        //        (96)  - (99)
        
        float headWidth = 1.5f;
        float headHeight = 3f;
        float neckHeight = 18f;

        mesh.addVertex(new Vector3f(-headWidth, neckHeight, -headWidth));
        mesh.addVertex(new Vector3f(-headWidth, neckHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight, -headWidth));

        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, -headWidth));
        mesh.addVertex(new Vector3f(-headWidth, neckHeight + headHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight + headHeight, headWidth));
        mesh.addVertex(new Vector3f(headWidth, neckHeight + headHeight, -headWidth));

        //normals
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, -1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, 1);
        mesh.addNormal(0, 0, -1);
        
        mesh.addTriangle((short) 100, (short) 96, (short) 97);
        mesh.addTriangle((short) 97, (short) 101, (short) 100);
        mesh.addTriangle((short) 101, (short) 97, (short) 98);
        mesh.addTriangle((short) 98, (short) 102, (short) 101);
        mesh.addTriangle((short) 102, (short) 98, (short) 99);
        mesh.addTriangle((short) 99, (short) 103, (short) 102);
        
        //FIX THESE INDICES!!!
        mesh.addTriangle((short) 100, (short) 101, (short) 102);
        mesh.addTriangle((short) 102, (short) 103, (short) 100);
        mesh.addTriangle((short) 103, (short) 99, (short) 96);
        mesh.addTriangle((short) 96, (short) 100, (short) 103);
        mesh.addTriangle((short) 97, (short) 96, (short) 99);
        mesh.addTriangle((short) 99, (short) 98, (short) 97);
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
        indices = ByteBuffer.allocate( sceneMesh.getVertexCount() * 4 );
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
        boneIndex(0, 5);
        boneIndex(1, 5);
        boneIndex(2, 5);
        boneIndex(3, 5);
        boneIndex(4, 5);
        boneIndex(5, 5);
        boneIndex(6, 5);
        boneIndex(7, 5);

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
        /* */

        // LEFT LEG
        // 32 --- 33 --- 34 --- 35
        // 28 --- 29 --- 30 --- 31
        //     >  26 --- 27  <
        // 22 --- 23 --- 24 --- 25
        // 18 --- 19 --- 20 --- 21
        boneIndex(18, 4);
        boneIndex(19, 4);
        boneIndex(20, 4);
        boneIndex(21, 4);
        boneIndex(22, 4);
        boneIndex(23, 4);
        boneIndex(24, 4);
        boneIndex(25, 4);

        boneIndexMult(26, 2, 4);
        boneIndexMult(27, 2, 4);

        boneIndex(28, 2);
        boneIndex(29, 2);
        boneIndex(30, 2);
        boneIndex(31, 2);
        boneIndex(32, 2);
        boneIndex(33, 2);
        boneIndex(34, 2);
        boneIndex(35, 2);

        //PELVIS
        //       (40) - (43)
        // 40 --- 41 --- 42 --- 43 - (40)
        // 36 --- 37 --- 38 --- 39 - (36)
        //       (36) - (39)
        boneIndex(36, 1);
        boneIndex(37, 1);
        boneIndex(38, 1);
        boneIndex(39, 1);
        boneIndex(40, 1);
        boneIndex(41, 1);
        boneIndex(42, 1);
        boneIndex(43, 1);

        //TORSO - BONE NOT DEFINED
        //       (48) - (51)
        // 48 --- 49 --- 50 --- 51 - (48)
        // 44 --- 45 --- 46 --- 47 - (44)
        //       (44) - (47)
        boneIndex(44, 6);
        boneIndex(45, 6);
        boneIndex(46, 6);
        boneIndex(47, 6);
        boneIndex(48, 6);
        boneIndex(49, 6);
        boneIndex(50, 6);
        boneIndex(51, 6);


        //CHEST
        //       (56) - (59)
        // 56 --- 57 --- 58 --- 59 - (56)
        // 52 --- 53 --- 54 --- 55 - (52)
        //       (52) - (55)
        boneIndex(52, 7);
        boneIndex(53, 7);
        boneIndex(54, 7);
        boneIndex(55, 7);
        boneIndex(56, 7);
        boneIndex(57, 7);
        boneIndex(58, 7);
        boneIndex(59, 7);

        //RIGHT ARM
        //       (74) - (77)
        // 74 --- 75 --- 76 --- 77 - (74)
        // 70 --- 71 --- 72 --- 73 - (70)
        // 68  <   |     |   >  69 - (68)
        // 64 --- 65 --- 66 --- 67 - (64)
        // 60 --- 61 --- 62 --- 63 - (60)
        //       (60) - (63)
        boneIndex(60, 11);
        boneIndex(61, 11);
        boneIndex(62, 11);
        boneIndex(63, 11);
        boneIndex(64, 11);
        boneIndex(65, 11);
        boneIndex(66, 11);
        boneIndex(67, 11);

        boneIndexMult(68, 11, 9);
        boneIndexMult(69, 11, 9);

        boneIndex(70, 9);
        boneIndex(71, 9);
        boneIndex(72, 9);
        boneIndex(73, 9);
        boneIndex(74, 9);
        boneIndex(75, 9);
        boneIndex(76, 9);
        boneIndex(77, 9);

        // LEFT ARM -> FIXED INDICES!!!
        //       (92)---(95)
        // 92 --- 93 --- 94 --- 95 - (92)
        // 88 --- 89 --- 90 --- 91 - (88)
        // 86  <   |     |   >  87 - (86)
        // 82 --- 83 --- 84 --- 85 - (82)
        // 78 --- 79 --- 80 --- 81 - (78)
        //       (78) - (81)
        boneIndex(78, 10);
        boneIndex(79, 10);
        boneIndex(80, 10);
        boneIndex(81, 10);
        boneIndex(82, 10);
        boneIndex(83, 10);
        boneIndex(84, 10);
        boneIndex(85, 10);

        boneIndexMult(86, 10, 8);
        boneIndexMult(87, 10, 8);

        boneIndex(88, 8);
        boneIndex(89, 8);
        boneIndex(90, 8);
        boneIndex(91, 8);
        boneIndex(92, 8);
        boneIndex(93, 8);
        boneIndex(94, 8);
        boneIndex(95, 8);
        
        //HEAD
        //        (100) - (103)
        // 100 --- 101 --- 102 --- 103 - (100)
        // 96  --- 97  --- 98  --- 99  - (96)
        //        (96)  - (99)
        boneIndex(96, 12);
        boneIndex(97, 12);
        boneIndex(98, 12);
        boneIndex(99, 12);
        boneIndex(100, 12);
        boneIndex(101, 12);
        boneIndex(102, 12);
        boneIndex(103, 12);

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

