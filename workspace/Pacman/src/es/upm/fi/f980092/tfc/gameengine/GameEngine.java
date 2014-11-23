/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine;

import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.caches.MeshesCache;
import es.upm.fi.f980092.tfc.gameengine.render.caches.TexturesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Shader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderFactory;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderInfo;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.animation.AnimatedShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.color.ColorShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles.StaticParticleShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShader;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.scene.I_CameraMovement;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene;
import es.upm.fi.f980092.tfc.gameengine.utils.Counter;

public abstract class GameEngine implements Renderer {
	
	/***********************************
	 * Constantes
	 ***********************************/

	private static String TAG = "TfcGameEngine";
	private static String TAG2 = "TfcGameEngine::GameEngine - ";   	
		
	/***********************************
	 * Propiedades
	 ***********************************/
		
	protected Scene scene;
	
	/***********************************
	 * Variables
	 ***********************************/

	private long lastFrameTime;
	private long numFramesPerSecondStartTime;
	private long  numFramesPerSecond;
	private boolean autopause;
	private int frameTime;
	private Integer frameTimeAverage = null;
	
	private boolean activeRender;
	private boolean activePhysics;
	
	private int mWidth;
	private int mHeight;
		
	private static Integer physicsCounter = Counter.initCounter();
	private static Integer counterId = Counter.initCounter();
	
	/***********************************
	 * Constructores
	 ***********************************/
	
	public GameEngine(Scene scene, boolean autopause, boolean activeRender, boolean activePhysics, int frameTime) {
		this.scene = scene;
		this.autopause = autopause;
		this.activeRender = activeRender;
		this.activePhysics = activePhysics;
		this.lastFrameTime = new Date().getTime();
		this.numFramesPerSecond = 1;
		this.numFramesPerSecondStartTime = lastFrameTime;
		this.frameTime = frameTime;
	}
	
	public GameEngine(Scene scene) {
		this.scene = scene;
		this.autopause = false;
		this.frameTime = 0;
		this.activeRender = true;
		this.activePhysics = true;
		this.lastFrameTime = new Date().getTime();
		this.numFramesPerSecond = 1;
		this.numFramesPerSecondStartTime = lastFrameTime;
	}
	
	/***********************************
	 * Metodos de la interfaz Renderer
	 ***********************************/
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {		
		mWidth = width;
		mHeight = height;		
		scene.init((float) mWidth / (float) mHeight);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Date init = new Date();
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		// Cargamos los shaders definidos
		
		TexturesCache.getInstance().loadTextures();
		new TextureShader().load();
		new ColorShader().load();
		new AnimatedShader().load();
		new StaticParticleShader().load();
		float time = new Date().getTime() - init.getTime();
		Log.i(TAG,TAG2 + "Tiempo total de inicialización: " + Math.round(time/1000) + "seg");	
	}
	
	
	
	
	@Override
	/**
	 * En cada frame se realizan las siguientes acciones:
	 * 	1- Se genera un modelo de simulación
	 *  2- Se modifica el modelo mediante IA
	 *  3- Se realiza un simulación física
	 *  4- Se calculan las colisiones
	 *  5- Se valida el resultado ( No hay colisiones incorrectas ni hay peticiones ejecutables
	 *        5.1- [Incorrecto]  Corrección del modelo de simulación y vuelta al punto 2
	 *        5.2- [Finalizar ]  Finaliza el escenario
	 *  6- Se actualiza el escena con la información simulada
	 *  7- Se renderiza la escena
	 *  8- Post-procesamiento de la escena
	 */
	public void onDrawFrame(GL10 gl) {

		Counter.restart(counterId);
		int step =  (autopause) ? frameTime : getNumberOfStep();
		
		boolean activePhysicsOnState = isActivePhysicsOnState();
		if (activePhysics && activePhysicsOnState) {
			try  {
				Counter.restart(physicsCounter);
				for( A_SceneElement e : scene.getDynamicElements()) {
					e.move(step);
				}
				Log.d(TAG,TAG2 + " Physics engine time: " + Counter.stopCounter(physicsCounter) + "seg");
			} catch (Exception excep) {
				Log.e(TAG, TAG2 + excep.getMessage());
			}
		}
				
		if (activeRender) {
			preRender();
			scene.getCamera().move(step);  
			renderScene(activePhysicsOnState ? step : 0 , scene.getCamera());
			postRender();
		}		
		
		Log.d(TAG, TAG2 + " FrameTime = " + Counter.stopMsegCounter(counterId));
	}

	

	

	/**********************************************
	 * Métodos del flujo de vida del videojuego
	 **********************************************/
	
	protected abstract void preRender();
	
	protected abstract boolean isActivePhysicsOnState();
	
	protected abstract void postRender();
		
	private void renderScene(int step, I_CameraMovement camera) {
		
//		Integer counter = Counter.initCounter();
		GLES20.glViewport(0, 0, mWidth, mHeight);        
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        for ( A_SceneElement e : scene.getRenderableElements()) {
        	try {        		
        		String meshId = e.getMeshId();
        		I_Mesh mesh = ( e.getMesh() == null ) ? MeshesCache.getInstance().getMesh(meshId): e.getMesh();
				if (mesh != null) {
					ShaderInfo shaderInfo = new ShaderInfo();
					shaderInfo.element = e;
					shaderInfo.step = step;
					shaderInfo.mesh = mesh;
					shaderInfo.perspective = camera.getPerspectiveMatrix();
					shaderInfo.modelview = camera.getViewMatrix();
					shaderInfo.cameraGeometry = camera.getCameraGeometry();
					I_Shader shader = ShaderFactory.getShader(mesh);					
					shader.draw(shaderInfo);
				}
        	} catch (Exception exception) { 
        		Log.e(TAG, TAG2 + "Error rendering mesh: " + e.getMesh().getClass() + " ==> " + exception.getCause());
        	}         		     
        }    
        
//        Log.d(TAG, TAG2 + "------------ Render duration: " +  Counter.stopCounter(counter));
	}
		
	/***********************************
	 * Metodos privados
	 ***********************************/
		
	private int getNumberOfStep() {
		long now = new Date().getTime();		
		long time = now - lastFrameTime;
		
		if ( now - numFramesPerSecondStartTime > 1000 ) {
			Log.i(TAG, TAG2 + " FPS: " + numFramesPerSecond);			
			numFramesPerSecondStartTime = now;		
			if (frameTimeAverage != null)
				frameTimeAverage = (frameTimeAverage +  Math.round( 1000 / numFramesPerSecond)) / 2 ;
			else 
				frameTimeAverage = Math.round( 1000 / numFramesPerSecond);
			numFramesPerSecond = 1;
		} else {
			numFramesPerSecond++;
		}
		
		lastFrameTime = now;
		// Si el tiempo transcurrido es muy elevado establecemos que le tiempo transcurrido es Tmax = 1000mseg
		return Math.min( 200, (int) time);
	}
}
		
