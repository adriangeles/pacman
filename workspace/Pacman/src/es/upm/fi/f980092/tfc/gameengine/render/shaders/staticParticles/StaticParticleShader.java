package es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles;

import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.DefaultShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderInfo;
import es.upm.fi.f980092.tfc.gameengine.utils.Counter;

public class StaticParticleShader extends DefaultShader {

	private Integer counter = Counter.initCounter();
	
	private static int programId;		
	
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG+ "::StaticParticleShader ";
	
	// Uniform
	private final static String U_MVPMATRIX = "u_mvpMatrix";
	private static int u_mvpMatrixRef;
	
		
	// Attributes
	private final static String A_VERTEX = "a_position";
	private static int a_positionRef;
	
	private final static String A_VISIBLE = "a_visible";
	private static int a_visibleRef;
	
	
	private final static String vShader =
           "attribute vec3 a_position;                           \n" +
           "attribute float a_visible;                           \n" +
           "uniform mat4 u_mvpMatrix;                 	     \n" +
           "varying float v_visible;                    			\n"	+ 

           "void main()                                          \n" +
           "{                                                    \n" +
           "  gl_Position.xyz = a_position;                      \n" + 
           "  gl_Position.w = 1.0;                               \n" +
           "  gl_Position = u_mvpMatrix * gl_Position;           \n" +
           "  gl_PointSize = 10.0; 							     \n" +
           "  v_visible = a_visible;                             \n" +
           "}";


	private final static String fShader =
        "precision mediump float;                             \n" +
        "varying float v_visible;                    		  \n" + 
        "void main()                                          \n" +
        "{                                                    \n" +
        "  if ( v_visible == 1.0 ) {                          \n" + 
        "     gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);        \n" +
        "     gl_FragColor.a = 1.0;		                      \n" +
        "  } else {                                           \n" +
        "       discard;                                      \n" +
        "  }                                      \n" + 
        "}                                                    \n";

	@Override
	public void load() {
		try {
			programId = super.loadProgram(vShader, fShader);
			u_mvpMatrixRef = GLES20.glGetUniformLocation(programId, U_MVPMATRIX);
			a_positionRef = GLES20.glGetAttribLocation(programId, A_VERTEX);
			a_visibleRef = GLES20.glGetAttribLocation(programId, A_VISIBLE);
		} catch (Exception e) {
			Log.e(TAG, TAG2 + "Unable to load the static particle shaders");
		}
	}
	
	@Override	
	public void draw(ShaderInfo info) {
		Counter.restart(counter);
		GLES20.glUseProgram ( programId );
		ESTransform perspective = info.perspective;
		ESTransform view = info.modelview;
				
		// Generando la matriz de transformación modelo/vista/perspectiva
		ESTransform mMVPMatrix = new ESTransform();
		mMVPMatrix.matrixMultiply(view.get(), perspective.get());
		StaticParticleMesh mesh = (StaticParticleMesh) info.mesh;
		GLES20.glUniformMatrix4fv( u_mvpMatrixRef, 1, false, mMVPMatrix.getAsFloatBuffer());
		
		// Añadiendo que particulas son visibles
		FloatBuffer buffer = (FloatBuffer) mesh.getParticleBuffer();
		
		buffer.position(0);
		GLES20.glVertexAttribPointer(a_visibleRef, 1, GLES20.GL_FLOAT, false, 16, buffer);		 
		GLES20.glEnableVertexAttribArray(a_visibleRef);
		
		// Añadiendo las particulas
		buffer.position(1);
		GLES20.glVertexAttribPointer(a_positionRef, 3, GLES20.GL_FLOAT, false, 16, buffer);		 
		GLES20.glEnableVertexAttribArray(a_positionRef);

		
		// Dibujamos parte de la malla
		GLES20.glDrawArrays( GLES20.GL_POINTS, 0, mesh.getNumParticles());     
		Log.d(TAG,TAG2 + "Rendering in " + Counter.stopCounter(counter) + "seg");
	}

}
