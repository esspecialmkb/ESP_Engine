/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devMesh;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Michael B.
 */
public class VirtualMesh{
    ArrayList<Vector3f> positions;
    ArrayList<Vector2f> tCoords;
    ArrayList<Short> indexList;
    ArrayList<Float> normals;
    short offset;
    public boolean hasTCoords;

    //INIT ARRAYLISTS
    public void initMesh(){
        positions = new ArrayList();
        indexList = new ArrayList();
        normals = new ArrayList();
        tCoords = new ArrayList();
        offset = 0;
        hasTCoords = false;
    }
    
    public void initMesh(boolean useTCoords){
        positions = new ArrayList();
        indexList = new ArrayList();
        normals = new ArrayList();
        tCoords = new ArrayList();
        offset = 0;
        hasTCoords = useTCoords;
    }
    //CLEAR ARRAYLISTS
    public void resetMesh(){
        positions.clear();
        tCoords.clear();
        indexList.clear();
        normals.clear();
        offset = 0;
    }

    public void updateIndex(){
        offset = (short)positions.size();
    }

    public void addVertex(Vector3f pos){
        positions.add(pos);
    }
    public void addTriangle(short a, short b, short c){
        indexList.add((short)(a));
        indexList.add((short)(b));
        indexList.add((short)(c));
        //offset = (short) positions.size();
    }
    public void addNormal(float nx, float ny, float nz){
        normals.add(nx);
        normals.add(ny);
        normals.add(nz);
    }
    
    public void addTCoords(Vector2f tCoord){
        tCoords.add(tCoord);
    }

    public Mesh buildBlockMesh() {
        Mesh mesh = new Mesh();

        Vector3f[] pVertices = new Vector3f[positions.size()];
        Iterator<Vector3f> positionsIterator = positions.iterator();
        for(int i=0;positionsIterator.hasNext();i++){
            pVertices[i] = positionsIterator.next();
        }

        short[] indices = new short[indexList.size()];
        Iterator<Short> indicesIterator = indexList.iterator();
        for(int i=0;indicesIterator.hasNext();i++){
            indices[i] = indicesIterator.next();
        }

        //float[] fNormals = new float[normals.size()]; 
        Vector3f[] pNormals = new Vector3f[normals.size()/3];
        Iterator<Float> normalsIterator = normals.iterator();
        for(int i=0;normalsIterator.hasNext();i++){
            pNormals[i] = new Vector3f(normalsIterator.next(),normalsIterator.next(),normalsIterator.next());
        }
        
        //mesh.setDynamic();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(pVertices));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(pNormals));
        
        if(hasTCoords){
            Vector2f[] pTCoors = new Vector2f[tCoords.size()];
            Iterator<Vector2f> tCoordIterator = tCoords.iterator();
            for(int i=0;tCoordIterator.hasNext();i++){
                pTCoors[i] = tCoordIterator.next();
            }
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(pTCoors));
        }
        
        mesh.updateBound();
        mesh.updateCounts();

        //System.out.println(mesh.getTriangleCount() + " Tris, " + mesh.getVertexCount() + " Verts");
        return mesh;
    }
}

