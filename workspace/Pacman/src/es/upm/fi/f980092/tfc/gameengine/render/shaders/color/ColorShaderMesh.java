/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.render.shaders.color;


import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

import android.util.Log;

public class ColorShaderMesh implements I_Mesh {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::DefaultMesh";
	
	public final static String ID = "id";    
	public final static String MAP_KD = "map_Kd";
	
	/*******************
	 * Propiedades
	 *******************/
	protected List<Float> vertex = new ArrayList<Float>();					// Listado de vertices
	protected List<Float> vertexTexture = new ArrayList<Float>();			// Listado de coordenadas de las texturas
	
	protected List<List<Integer>>   index = new ArrayList<List<Integer>>();
	protected List<List<Integer>>   textureIndex = new ArrayList<List<Integer>>();
	
	protected List<String>  materialIndex = new ArrayList<String>();
	
	private float scale = 1.0f;												// Escala de la malla que se esta dibujando
	
	private List<Buffer[]> buffers = null;
	protected HashMap<String,HashMap<String, Object>> materials = new HashMap<String, HashMap<String,Object>>();
	
	/*******************
	 * Constructor
	 *******************/
	
	public ColorShaderMesh() {
		super();
	}
	
	/*******************
	 * Interfaz I_Mesh
	 *******************/

	
	/*********************************/
	
	public int getSize() {
		return index.size();
	}

	public List<Integer> getIndex(int index) {
		return this.index.get(index);
	}
	
	public List<Integer> getTextureIndex(int index) {
		return this.textureIndex.get(index);
	}
	
	public List<Float> getVertex() {
		return vertex;
	}
	
	public List<Float> getTextureVertex() {		
		return vertexTexture;
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
		}		
	}
	public Buffer[] getBuffer(int i) {
		
		if (buffers == null) {
			Log.w(TAG,TAG2 + " The texture shader mesh should be load before of rendering");
			load();
		}
		return buffers.get(i);
	}
	
	public void addIndex(int submesh, int vertexIndex, int textureVertexIndex) {
		List<Integer> submeshIndex = null;
		List<Integer> submeshTextureIndex = null;
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
				
		ArrayList<Buffer[]> result = new ArrayList<Buffer[]>();
				
		for( int i = 0 ; i < this.getSize(); i++) {
			
			List<Integer> indexList = this.getIndex(i);
			List<Integer> textureIndexList = this.getTextureIndex(i);
			
			try {
				
				short[] indexArray = new short[indexList.size()];
				float[] vertexArray = new float[3 * indexList.size()];
				float[] textureArray = new float[2 * indexList.size()];
				
				float[] totalArray = new float[5 * indexList.size()];

				for( int j = 0 ; j < indexList.size() ; j++) {

					int vertexIndex  = indexList.get(j) - 1;
					vertexArray[3 * j]     = vertex.get(3*vertexIndex);
					vertexArray[3 * j + 1] = vertex.get(3*vertexIndex + 1);
					vertexArray[3 * j + 2] = vertex.get(3*vertexIndex + 2);
					
					totalArray[5 * j]     = vertex.get(3*vertexIndex);
					totalArray[5 * j + 1] = vertex.get(3*vertexIndex + 1);
					totalArray[5 * j + 2] = vertex.get(3*vertexIndex + 2);
					
					int textureIndex = textureIndexList.get(j) - 1;
					textureArray[2 * j]     = vertexTexture.get(2* textureIndex);
					textureArray[2 * j + 1] = 1-vertexTexture.get(2* textureIndex + 1);
					
					totalArray[5 * j + 3]     = vertexTexture.get(2* textureIndex);
					totalArray[5 * j + 4] 	= 1-vertexTexture.get(2* textureIndex + 1);

					indexArray[j] = (byte) j;
					
				}
			
				FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
				vertexBuffer.put(vertexArray).position(0);

				FloatBuffer textureBuffer = ByteBuffer.allocateDirect(textureArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
				textureBuffer.put(textureArray).position(0);
				
				FloatBuffer totalBuffer = ByteBuffer.allocateDirect(totalArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
				totalBuffer.put(totalArray).position(0);

				ShortBuffer indexBuffer = ByteBuffer.allocateDirect(indexArray.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
				indexBuffer.put(indexArray).position(3);

				result.add(new Buffer[]{ indexBuffer, vertexBuffer, textureBuffer, totalBuffer});
			} catch (Exception e) {
				Log.e(TAG, TAG2 + "Imposible to create opengl buffer");
				return null;
			}
		}
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
		
		for (  List<Integer> submesh: this.index ) {
			
			for ( int j = 0 ; j < submesh.size() ; ) {
				Log.v(TAG,TAG2 + " f " + submesh.get(j++) + " " + submesh.get(j++) + " " + submesh.get(j++)); 
			}
		}
		Log.v(TAG, TAG2 + "..................... MESH INFO .........................");
	}
}