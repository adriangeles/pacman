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

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import android.opengl.GLES20;
import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionEngine;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.QuadTreeNode;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.render.caches.TexturesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.DefaultShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderInfo;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene.State;
import es.upm.fi.f980092.tfc.gameengine.utils.Counter;
import es.upm.fi.f980092.tfc.pacman.GameContext;

public class TextureShader extends DefaultShader {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG+ "::TextureShader ";
	
	// Uniform
	private final static String U_MVPMATRIX = "u_mvpMatrix";
	private static int u_mvpMatrixRef;
	
	private final static String U_SCALE = "u_scale";
	private static int u_scaleRef;
	
	private static int u_modeRef;
	
	// Attributes
	private final static String A_VERTEX = "a_vertex";
	private static int a_vertexRef;
	
	private final static String A_TEXCOORD = "a_texCoord";
	private static int a_texCoordRef;
	
	// Vertex Shader
	private final static String vShader =
		"uniform   mat4 u_mvpMatrix;                 			\n" +
		"uniform   float u_scale;					 			\n"	+		
		"attribute vec4 a_vertex;     	    	    	    	\n"	+ 
		"attribute vec2 a_texCoord;     	    	    	    \n"	+
		"varying vec2 v_texCoord;                    			\n"	+ 
		"void main()                                 			\n"	+ 
		"{                 										\n" +
		"   vec4 scale_vertex = a_vertex;						\n" +
		"	scale_vertex.xyz *= u_scale;						\n" +
		"	gl_Position =   u_mvpMatrix * scale_vertex;			\n" +
		"   v_texCoord = a_texCoord;                 			\n"	+
		"}                                           			\n";
		
	// Fragmen Shaders 	
	private final static String fShader =
		"precision mediump float;                                                                 \n"   + 
		"varying vec2 v_texCoord;                                                                 \n"   + 
		"uniform sampler2D s_texture;                                                             \n"   +
		"uniform int u_mode;                                                                      \n"   +
		"                                                                                         \n"   +
		"void main()                                                                              \n"   + 
		"{                                                                                        \n"   +
		"   if (u_mode == 0) {                                                                    \n"   +
		"     gl_FragColor = texture2D( s_texture, v_texCoord );                                  \n"   +
		"   }                                                                                     \n"   +
		"                                                                                         \n"   +
		"   if (u_mode == 1) {                                                                	  \n"   +
		"     float grey = dot(texture2D(s_texture, v_texCoord).rgb, vec3(0.299, 0.587, 0.114));  \n"   +
		"     gl_FragColor = vec4(grey * vec3(1.2, 1.0, 0.8), 1.0);                               \n"   +
		"   }                                                                                     \n"   +
		"                                                                                         \n"   +
		"   if (u_mode == 2) {                                                                	  \n"   +
		"     float grey = dot(texture2D(s_texture, v_texCoord).rgb, vec3(0.299, 0.587, 0.114));  \n"   +
		"     gl_FragColor = vec4(grey, grey, grey, 1.0);                                         \n"   +
		"   }                                                                                     \n"   +
		"                                                                                         \n"   +
		"} " ;
		
	// Identificador del programa en GLSL
	private static int programId;			
    
	private float xx = 0f;
	private float yy = 0f;
	private float zz = 0.0f;

	private int N = 0;		// Peticiones a la tarjeta grafica
	private int M = 0; 		// Nodos explorados
	
	private Integer counter = Counter.initCounter();
	
	
	public void load() {
		try {
			programId = super.loadProgram(vShader, fShader);
			u_mvpMatrixRef = GLES20.glGetUniformLocation(programId, U_MVPMATRIX);
			u_scaleRef = GLES20.glGetUniformLocation(programId, U_SCALE );
			a_vertexRef = GLES20.glGetAttribLocation(programId, A_VERTEX);
			a_texCoordRef = GLES20.glGetAttribLocation( programId, A_TEXCOORD);

		} catch (Exception e) {
			Log.e(TAG, TAG2 + "Unable to load the texture shaders");
		}
	}
	
	@Override	
	public void draw(ShaderInfo info) {
		Counter.restart(counter);
		GLES20.glUseProgram ( programId );
		 
		A_SceneElement e = info.element;
		ESTransform perspective = info.perspective;
		Scene scene = e.getSceneRef();
	
		// Transformacion de la malla
		ESTransform meshMatrix = new ESTransform();
		
		meshMatrix.matrixLoadIdentity();
		meshMatrix.translate(xx,yy,zz);
		
		// Transformaciones del modelo
		ESTransform modelview = new ESTransform();
		modelview.matrixMultiply(meshMatrix.get(), info.modelview.get() );
	  
		
		float x = e.getPosition()[0] / scene.getScale();
		float y = e.getPosition()[1] / scene.getScale();
		float z = e.getPosition()[2] / scene.getScale();        		
		modelview.translate(x, y, z);  		
		

		int[] r = e.getRotation();
		modelview.rotate(r[0], 1.0f, 0.0f, 0.0f);
		modelview.rotate(r[1], 0.0f, 1.0f, 0.0f);
		modelview.rotate(r[2], 0.0f, 0.0f, 1.0f);
		
		// Generando la matriz de transformación modelo/vista/perspectiva
		ESTransform mMVPMatrix = new ESTransform();		
		mMVPMatrix.matrixMultiply(modelview.get(), perspective.get());
				
		TextureShaderMesh mesh = (TextureShaderMesh) info.mesh;
		int mode = ( info.element.getSceneRef().getState() == State.PAUSE) ? 1 : 0;
		
		N = 0;	
		M = 0;
		draw(mesh,e.getScale(), mMVPMatrix, mesh.getQuadtree(), info.cameraGeometry, new float[]{x,y,z}, mode);
		Log.d(TAG,TAG2 + "Render " + N + " of " + mesh.getSize() + " [" + M + "Nodes] in " + Counter.stopCounter(counter) + "seg");
	}
	
	private void draw( TextureShaderMesh mesh, float scale, ESTransform mMVPMatrix, QuadTreeNode node, I_Geometry cameraGeometry, float[] displacement, int mode) {
		
		boolean render = true;
		M++;
		
		if ( GameContext.isRenderOptimizationOn() ) { 
			I_Geometry geo1 = cameraGeometry; 
			I_Geometry geo2 = node.getGeometry().translate(displacement);
			render = CollisionEngine.existCollision(geo1, geo2);
			
		}
		
		if (render) {
			// Renderizamos las distintas submallas del nodo
			for ( Object index : node.getNodeElements() ) {
				draw( mesh, (Integer) index, scale, mMVPMatrix, mode);
			}
			
			for ( QuadTreeNode subnode : node.getSubNodes()) {
				draw ( mesh, scale, mMVPMatrix, subnode, cameraGeometry, displacement, mode);
			}
		}		
	}
	
	private void draw(TextureShaderMesh mesh, int submeshIndex, float scale, ESTransform mMVPMatrix, int mode) {
		N++;
		GLES20.glUniformMatrix4fv( u_mvpMatrixRef, 1, false, mMVPMatrix.getAsFloatBuffer());
		GLES20.glUniform1f(u_scaleRef, scale );

		// Añadiendo los vertices
		GLES20.glVertexAttribPointer(a_vertexRef, 3, GLES20.GL_FLOAT, false, 0, (FloatBuffer) mesh.getBuffer(submeshIndex)[1]);		 
		GLES20.glEnableVertexAttribArray(a_vertexRef);

		// Añadiendo las coordenadas de las texturas
		GLES20.glVertexAttribPointer(a_texCoordRef, 2, GLES20.GL_FLOAT, false, 0,(FloatBuffer) mesh.getBuffer(submeshIndex)[2]);
		GLES20.glEnableVertexAttribArray (a_texCoordRef);

		// Añadiendo el modo  0 = Standard  , 1 = Sepia , 2 = Escala grises
		GLES20.glUniform1i(u_modeRef, mode);
		
		// Añadiendo la textura
		HashMap<String, Object> material = mesh.getMaterial(submeshIndex);
		String textureId = (String) material.get(TextureShaderMesh.ID) + ":" + TextureShaderMesh.MAP_KD;			 
		int texture = TexturesCache.getInstance().getTexture(textureId);

		GLES20.glActiveTexture (GLES20.GL_TEXTURE0);
		GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, texture);
		GLES20.glEnable (GLES20.GL_TEXTURE_2D);	        
		GLES20.glUniform1i ( texture,  0 );

		// Dibujamos parte de la malla
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndex(submeshIndex).size(), GLES20.GL_UNSIGNED_SHORT, (ShortBuffer) mesh.getBuffer(submeshIndex)[0]);        	
	}
}