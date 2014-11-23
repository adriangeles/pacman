/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.render.shaders.texture;


import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.QuadTreeNode;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.RenderQuadTreeBuilder;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

public class TextureShaderMesh implements I_Mesh {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::DefaultMesh";
	
	public final static String ID = "id";    
	public final static String MAP_KD = "map_Kd";
	
	/*******************
	 * Propiedades
	 *******************/
	private ArrayList<Float> vertex = new ArrayList<Float>();					// Listado de vertices
	private ArrayList<Float> vertexTexture = new ArrayList<Float>();			// Listado de coordenadas de las texturas
	
	private ArrayList<ArrayList<Integer>>   index = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>>   textureIndex = new ArrayList<ArrayList<Integer>>();
	
	private ArrayList<String>  materialIndex = new ArrayList<String>();
	
	private float scale = 1.0f;												// Escala de la malla que se esta dibujando
	private ArrayList<Buffer[]> buffers = null;
	protected HashMap<String,HashMap<String, Object>> materials = new HashMap<String, HashMap<String,Object>>();
	private ArrayList<AABBGeometry> subMeshGeometry;
	private QuadTreeNode quadtree;
	
	/*******************
	 * Constructor
	 *******************/
	
	public TextureShaderMesh() {
		super();
	}
	
	/*******************
	 * Interfaz I_Mesh
	 *******************/
	
	public int getSize() {
		return index.size();
	}

	public ArrayList<Integer> getIndex(int index) {
		return this.index.get(index);
	}
		
	public HashMap<String, Object> getMaterial(int index) {
		String materialId = materialIndex.get(index);
		return materials.get(materialId);
	}
	
	public HashMap<String,HashMap<String, Object>> getAllMaterial() {		
		return materials;
	}
	
	public void load() {
		if (buffers == null) {
			buffers = genBuffer();
			// Limpiamos los array para liberar memoria
			vertex = null;
			vertexTexture = null;		
			
			System.gc();
		}		
	}
	public Buffer[] getBuffer(int i) {
		
		if (buffers == null) {
			Log.w(TAG,TAG2 + " The texture shader mesh should be load before of rendering");
			load();
		}
		return buffers.get(i);
	}
	
	public AABBGeometry getGeometry(int id) {
		return subMeshGeometry.get(id);
	}
	
	public void addIndex(int submesh, int vertexIndex, int textureVertexIndex) {
		ArrayList<Integer> submeshIndex = null;
		ArrayList<Integer> submeshTextureIndex = null;
		try {
			submeshIndex = index.get(submesh);
			submeshTextureIndex = textureIndex.get(submesh);
		} catch (Exception e) {
			submeshIndex = new ArrayList<Integer>();
			submeshTextureIndex = new ArrayList<Integer>();
			index.add(submeshIndex); 
			textureIndex.add(submeshTextureIndex);
		}
		submeshIndex.add(vertexIndex);
		submeshTextureIndex.add(textureVertexIndex);
	}

	public void addMaterial(HashMap<String, Object> material) {
		this.materials.put( (String) material.get(ID), material);
	}

	public void addMaterialIndex(int submesh, String materialId) {
		materialIndex.add(submesh, materialId);
	}

	public void addTextureVertex(float u, float v) {
		vertexTexture.add(u);
		vertexTexture.add(v);	
	}

	public void addVertex(float x, float y, float z) {
		vertex.add(x);
		vertex.add(y);
		vertex.add(z);
		
	}	

	public void setScale(float scale) {
		this.scale = scale; 
	}
	
	public float getScale() {
		return scale;
	}

	/*******************
	 * Métodos privados
	 *******************/
	
	private ArrayList<Buffer[]> genBuffer(){
		Date init = new Date();
		ArrayList<Buffer[]> result = new ArrayList<Buffer[]>();
		subMeshGeometry = new ArrayList<AABBGeometry>();
		
		for( int i = 0 ; i < this.getSize(); i++) {
			
			ArrayList<Integer> indexList = this.getIndex(i);
			ArrayList<Integer> textureIndexList = this.textureIndex.get(i);

			float[] max = new float[]{  Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY };
			float[] min = new float[]{  Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY };
			
			try {
				
				short[] indexArray = new short[indexList.size()];
				float[] vertexArray = new float[3 * indexList.size()];
				float[] textureArray = new float[2 * indexList.size()];

				for( int j = 0 ; j < indexList.size() ; j++) {

					int vertexIndex  = indexList.get(j) - 1;
					float x = vertex.get(3*vertexIndex);
					float y = vertex.get(3*vertexIndex + 1);
					float z = vertex.get(3*vertexIndex + 2);
					vertexArray[3 * j]     = x;
					vertexArray[3 * j + 1] = y;
					vertexArray[3 * j + 2] = z;
					
					if ( x > max[0]) 
						max[0] = x;
					if ( x < min[0])
						min[0] = x;
					
					if ( y > max[1]) 
						max[1] = y;
					if ( y < min[1])
						min[1] = y;
					
					if ( z > max[2]) 
						max[2] = z;
					if ( z < min[2])
						min[2] = z;
					
					int textureIndex = textureIndexList.get(j) - 1;
					textureArray[2 * j]     = vertexTexture.get(2* textureIndex);
					textureArray[2 * j + 1] = 1-vertexTexture.get(2* textureIndex + 1);

					indexArray[j] = (byte) j;					
				}
				
				this.subMeshGeometry.add(new AABBGeometry(max, min));
				Log.d(TAG,"GEOMESH_" + i + "[ " + max[0] + "," + max[1] + "] - [" + min[0] + "," + min[1] + "]");
				FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
				vertexBuffer.put(vertexArray).position(0);

				FloatBuffer textureBuffer = ByteBuffer.allocateDirect(textureArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
				textureBuffer.put(textureArray).position(0);

				ShortBuffer indexBuffer = ByteBuffer.allocateDirect(indexArray.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
				indexBuffer.put(indexArray).position(0);

				result.add(new Buffer[]{ indexBuffer, vertexBuffer, textureBuffer});
				
			} catch (Exception e) {
				Log.e(TAG, TAG2 + "Imposible to create opengl buffer");
				return null;
			}
		}
		quadtree = RenderQuadTreeBuilder.genQuadTree(this);
		quadtree.printDebug();
		float time = new Date().getTime() - init.getTime();
				
		Log.d(TAG, TAG + " GenBufferTime for " + ID + ": " +  Math.round(time));
		return result;		
	}
	
	
	public void printMeshData() {
		Log.v(TAG, TAG2 + "------------------------ MESH INFO ---------------------");
		for (int i = 0 ; i < vertex.size() ; i++) {
			Log.v(TAG,TAG2 + " v  = " + vertex.get(i++) + " " + vertex.get(i++) + " " + vertex.get(i));
		}
		
		for (int i = 0 ; i < vertexTexture.size() ; i++) {
			Log.v(TAG,TAG2 + " vt  = " + vertexTexture.get(i++) + " " + vertexTexture.get(i++) + " " + vertexTexture.get(i));
		}
		
		for (  ArrayList<Integer> submesh: this.index ) {
			
			for ( int j = 0 ; j < submesh.size() ; ) {
				Log.v(TAG,TAG2 + " f " + submesh.get(j++) + " " + submesh.get(j++) + " " + submesh.get(j++)); 
			}
		}		
		quadtree.printDebug();		
		Log.v(TAG, TAG2 + "..................... MESH INFO .........................");
	}
	

	public QuadTreeNode getQuadtree() {
		return quadtree;
	}
}