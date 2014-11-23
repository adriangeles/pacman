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

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.DefaultShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderInfo;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene;

public class ColorShader extends DefaultShader {

	private static final String TAG = "TFCGameEngine";
	private static final String TAG2 = TAG+ "::TextureShader ";
	
	// Uniform
	private final static String U_MVPMATRIX = "u_mvpMatrix";
	private static int u_mvpMatrixRef;
	
	private final static String U_SCALE = "u_scale";
	private static int u_scaleRef;
	
	// Attributes
	private final static String A_VERTEX = "a_vertex";
	private static int a_vertexRef;
	
	// Vertex Shader
	private final static String vShader =
		"uniform   mat4 u_mvpMatrix;                 			\n" +
		"uniform   float u_scale;					 			\n"	+		
		"attribute vec4 a_vertex;     	    	    	    	\n"	+ 
		"void main()                                 			\n"	+ 
		"{                 										\n" +
		"   vec4 scale_vertex = a_vertex;						\n" +
		"	scale_vertex.xyz *= u_scale;						\n" +
		"	gl_Position =   u_mvpMatrix * scale_vertex;			\n" +
		"}                                           			\n";
	
	// Fragment Shaders
	private final static String fShader =
		"precision mediump float;                            \n"   + 
		"void main()                                         \n"   + 
		"{                                                   \n"   + 
		"  gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);			 \n"   + 
		"} " ;

	// Identificador del programa en GLSL
	private static int programId;			
    
	public void load() {
		try {
			programId = super.loadProgram(vShader, fShader);
			if (programId == 0) 
				throw new Exception();
			u_mvpMatrixRef = GLES20.glGetUniformLocation(programId, U_MVPMATRIX);
			u_scaleRef = GLES20.glGetUniformLocation(programId, U_SCALE );
			a_vertexRef = GLES20.glGetAttribLocation(programId, A_VERTEX);

		} catch (Exception e) {
			Log.e(TAG, TAG2 + "Unable to load the texture shaders");
		}
	}
	

	@Override	
	public void draw(ShaderInfo info) {
	
		GLES20.glUseProgram ( programId );
		 
		A_SceneElement e = info.element;
		ESTransform perspective = info.perspective;
		Scene scene = e.getSceneRef();
	
		ESTransform modelview = new ESTransform();
		modelview.matrixLoadIdentity();
        
		float[] center = scene.getCamera().getCameraCenter();
		float[] eye = scene.getCamera().getCameraEye();

		// Transformaciones de la vista
		modelview.translate(center[0], center[1], center[2]);  										// Posicionamiento segun la camara
		modelview.rotate(eye[0], 1.0f, 0.0f, 0.0f);
		modelview.rotate(eye[1], 0.0f, 1.0f, 0.0f);
		modelview.rotate(eye[2], 0.0f, 0.0f, 1.0f);
				
		// Transformaciones del modelo
		float x = e.getPosition()[0] / scene.getScale();
		float y = e.getPosition()[1] / scene.getScale();
		float z = e.getPosition()[2] / scene.getScale();        		
		modelview.translate(x, y, z);  																// Posicionamiento segun la camara
		
		int[] r = e.getRotation();		
		modelview.rotate(r[0], 1.0f, 0.0f, 0.0f);
		modelview.rotate(r[1], 0.0f, 1.0f, 0.0f);
		modelview.rotate(r[2], 0.0f, 0.0f, 1.0f);

		// Generando la matriz de transformación modelo/vista/perspectiva
		ESTransform mMVPMatrix = new ESTransform();
		mMVPMatrix.matrixMultiply(modelview.get(), perspective.get());
		
		ColorShaderMesh mesh = (ColorShaderMesh) info.mesh;
		
		for (int i = 0 ; i < mesh.getSize(); i++) {
		
			GLES20.glUniformMatrix4fv( u_mvpMatrixRef, 1, false, mMVPMatrix.getAsFloatBuffer());
			GLES20.glUniform1f(u_scaleRef, e.getScale() );

			// Añadiendo los vertices
			GLES20.glVertexAttribPointer(a_vertexRef, 3, GLES20.GL_FLOAT, false, 0, (FloatBuffer) mesh.getBuffer(i)[1]);		 
			GLES20.glEnableVertexAttribArray(a_vertexRef);

			// Dibujamos parte de la malla
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndex(i).size(), GLES20.GL_UNSIGNED_SHORT, (ShortBuffer) mesh.getBuffer(i)[0]);
			
        }
	}
}