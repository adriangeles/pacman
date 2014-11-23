/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.scene;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;

public interface I_CameraMovement {
	void move(int time);
	float[] getCameraCenter();					 	
	float[] getCameraEye();						
	boolean isPhysicsActive() throws Exception;			 	
	ESTransform getPerspectiveMatrix();
	ESTransform getViewMatrix();
	float getFovy();	
	float getzNear();	
	float getzFar();	
	
	boolean isFinished();				// Determina si el movimiento ha finalizado 
	void restart();						// Vuelve la camara a la posicion en el punto 0

	I_AABBGeometry getCameraGeometry();	// Determina la región visible por la cámara para no mostrar elementos fuera de su alcance
	void setAspect(float aspect);
	void moveToDown();
	void moveToUp();
	void moveToLeft();
	void moveToRight();
}