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

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.scene.I_CameraMovement;

public class PersuivanCamera implements I_CameraMovement {

	private A_SceneElement element;
	private float scale;
	private float[] distance;
	private float[] eye;
	private float near;
	private float far;
	private float fovy;
	private float aspect = 1.0f;
	private AABBGeometry cameraGeometry = new AABBGeometry( new float[]{0,0,0}, new float[]{0,0,0});  
	
	private ESTransform viewMatrix = new ESTransform();
	private ESTransform perspectiveMatrix = new ESTransform();
	
	// Variables temporales
	private float[] lastCenter = new float[]{0,0,0};
	
	
	public PersuivanCamera(A_SceneElement element, float scale, float[] distance, float[] eye, float fovy, float near, float far, float aspect) {
		this.element = element;
		this.scale = scale;
		this.distance = distance;
		this.eye = eye;
		this.aspect = aspect;
		this.fovy = fovy;
		this.near = near;
		this.far = far;
		
		verifyPersuivanPosition();  			// Internamente actualiza la viewMatrix
		buildPerspectiveMatrix();
	}

	@Override
	public float[] getCameraCenter() {	
		verifyPersuivanPosition();		
		return lastCenter;
	}
	
	@Override
	public void setAspect(float aspect) {
		this.aspect = aspect;
		buildPerspectiveMatrix();
	}

	@Override
	public float[] getCameraEye() {
		return eye;
	}

	@Override
	public boolean isFinished(){
		return false;
	}

	@Override
	public boolean isPhysicsActive() throws Exception {
		return true;
	}

	@Override
	public float getFovy() {		
		return fovy;
	}

	@Override
	public float getzFar() {		 
		return far;
	}

	@Override
	public float getzNear() {		
		return near;
	}
	
	@Override
	public void move(int time) {
		buildPerspectiveMatrix();
	}
	
	@Override
	public final ESTransform getViewMatrix() {
		verifyPersuivanPosition();
		return viewMatrix;
	}
	
	@Override
	public final ESTransform getPerspectiveMatrix() {
		return perspectiveMatrix;
	}
	
	public I_AABBGeometry getCameraGeometry() {
		verifyPersuivanPosition();
		return cameraGeometry;
	}

	@Override
	public void moveToDown() {
		distance[1] += 1;
		verifyPersuivanPosition();
	}

	public void increaseFovy() {
		fovy += 1 ;
		buildPerspectiveMatrix();
	}
	
	public void decreaseFovy() {
		fovy -= 1;
		buildPerspectiveMatrix();
	}
	
	@Override
	public void moveToLeft() {
		distance[0] -= 1;
		verifyPersuivanPosition();
		
	}

	@Override
	public void moveToRight() {
		distance[0] += 1;
		verifyPersuivanPosition();
	}

	@Override
	public void moveToUp() {
		distance[1] -= 1;	
		verifyPersuivanPosition();
	}
	
	@Override
	public void restart() {
		// Esta camara no se puede reinciar
	}
	
	// Private method
	
	private void buildPerspectiveMatrix() {
		perspectiveMatrix.matrixLoadIdentity();
		perspectiveMatrix.perspective(fovy, aspect, near, far);		
		buildViewGeometry();
	}
	
	private void buildViewMatrix() {
		float[] center = getCameraCenter();
		
		viewMatrix.matrixLoadIdentity();	
		viewMatrix.translate(0.0f, 0.0f, center[2]);		  

		viewMatrix.rotate(eye[0], 1.0f, 0.0f, 0.0f);
		viewMatrix.rotate(eye[1], 0.0f, 1.0f, 0.0f);
		viewMatrix.rotate(eye[2], 0.0f, 0.0f, 1.0f);
		viewMatrix.translate(center[0], center[1], 0.0f);		
		buildViewGeometry();
	}
	
	private boolean verifyPersuivanPosition() {
		float x = -element.getPosition()[0] / scale + distance[0];
		float y = -element.getPosition()[1] / scale + distance[1];
		float z = -element.getPosition()[2] / scale + distance[2];
		
		if ( x == lastCenter[0] && y == lastCenter[1] && z == lastCenter[2]) {
			return false;
		} else {
			lastCenter[0] = x;
			lastCenter[1] = y;
			lastCenter[2] = z;			
			buildViewMatrix();
			return false;
		}
	}
		
	private void buildViewGeometry() {
		
		double fovyRadians = Math.toRadians(fovy);
		double xRadians = Math.toRadians( this.eye[0]);
		double yRadians = Math.toRadians( this.eye[1]);
		
		// Calculamos el punto mas alejado 
		double max = Math.tan(fovyRadians) * far / 2;		
		// Aplicamos el giro sobre x 		
		float maxY = Math.round(max / Math.cos(xRadians));		
		float maxX = Math.round(max / Math.cos(yRadians) * aspect) ;
						
		float displacamenteX = element.getPosition()[0] / scale;
		float displacementeY = element.getPosition()[1] / scale;
		cameraGeometry.getMax()[0] =  maxX + displacamenteX;
		cameraGeometry.getMax()[1] =  maxY + displacementeY; 
		cameraGeometry.getMax()[2] = Float.MAX_VALUE;
		
		cameraGeometry.getMin()[0] = displacamenteX-maxX; 
		cameraGeometry.getMin()[0] = displacementeY-maxY;
		cameraGeometry.getMin()[0] = -Float.MAX_VALUE;
	}
}

