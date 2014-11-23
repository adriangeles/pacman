/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.scene.cameras;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.scene.I_CameraMovement;



public class CameraZoomFunction implements I_CameraMovement {

	public float[] p0;						// Posicion en el instante inicial
	private float[] p1;						// Posición en el instante final
	private float[] r0;						// Rotacion en el instante inicial
	private float[] r1;						// Rotación en el instante final
	private float[] v = new float[3];		// Velocidad lineal
	private float[] w = new float[3];		// Velocidad angular
	private Integer t = null;				// Tiempo transcurrido
	private Long duration;					// Duración total de la transformación
	
	private float fovy = 15.0f;
	private float aspect = 0.6f;
	private float near = 30;
	private float far = 500;


	// Matrices de transformacion de la camara vista/perspectiva
	private I_AABBGeometry cameraGeometry;
	private ESTransform viewMatrix = new ESTransform();
	private ESTransform perspectiveMatrix = new ESTransform();

	public CameraZoomFunction(float[] p0, float[] p1, float[] r0, float[] r1, long duration, float aspect) {
		
		this.p0 = p0;
		this.p1 = p1;
		this.r0 = r0;
		this.r1= r1;
	
		this.aspect = aspect;
		this.duration = duration; 
		this.t = null;
		// Calculo de la velocidad lineal y angular
		for (int i=0; i<3; i++) {
			v[i] = (p1[i] - p0[i]) / duration;
			w[i] = (r1[i] - r0[i]) / duration;
		}
		cameraGeometry = new AABBGeometry( 
				new float[]{ Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE},
				new float[]{ Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE});
	}

	//	
	
	

	@Override
	public I_AABBGeometry getCameraGeometry() {
		return cameraGeometry;
	}

	@Override
	public ESTransform getPerspectiveMatrix() {		
		return perspectiveMatrix;
	}

	@Override
	public ESTransform getViewMatrix() {		
		return viewMatrix;
	}

	@Override
	public void moveToDown() {


	}

	@Override
	public void moveToLeft() {

	}

	@Override
	public void moveToRight() {

	}

	@Override
	public void moveToUp() {
	
	}

	@Override
	public void setAspect(float aspect) {
		this.aspect = aspect;
	}

	

	@Override
	public float getFovy() {
		return 0;
	}

	@Override
	public float getzFar() {
		return 0;
	}

	@Override
	public float getzNear() {
		return 0;
	}

	@Override
	public boolean isFinished() {
		return (t!= null) && t > duration;
	}

	@Override
	public boolean isPhysicsActive() throws Exception {
		return false;
	}

	@Override
	public void move(int time) {
		t =(t == null) ? t = 0 : t + time;		 
		buildPerspectiveMatrix();
		buildViewMatrix();
	}

	/**************************************
	 * Metodos privados
	 *************************************/
	
	private void buildPerspectiveMatrix() {
		perspectiveMatrix.matrixLoadIdentity();
		perspectiveMatrix.perspective(fovy, aspect, near, far);		
	}
	
	private void buildViewMatrix() {
		viewMatrix.matrixLoadIdentity();
		float[] p = getCameraCenter();
		float[] r = getCameraEye();
		viewMatrix.translate(0,0,p[2]);
		viewMatrix.rotate(r[0], 1.0f, 0.0f, 0.0f);
		viewMatrix.rotate(r[1], 0.0f, 1.0f, 0.0f);
		viewMatrix.rotate(r[2], 0.0f, 0.0f, 1.0f);
		viewMatrix.translate(p[0],p[1],0);
	}
	
	@Override
	public float[] getCameraCenter() {		
		if (t == null) {			
			t = 0;		
			return p0;
		} else if (t > duration) {			
			return p1;			
		} else {
			float[] p = new float[3];			
			for (int i= 0 ; i<3; i++) {
				p[i] = p0[i] + v[i]*t; 
			}				
			return p; 
		}
	}
	
	@Override
	public float[] getCameraEye() {
		if (t == null) {
			t = 0; 
			return r0;
		} else if ( t > duration) {
			return r1;
		} else {
			float[] r = new float[3];		
			for (int i= 0 ; i<3; i++) {
				r[i] = r0[i] + w[i]*t; 
			}	
			return r;
		}
	}

	@Override
	public void restart() {
		t = null;
		
	}
}
