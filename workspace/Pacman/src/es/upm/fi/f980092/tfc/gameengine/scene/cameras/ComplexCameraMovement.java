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

import java.util.ArrayList;

import android.util.Log;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.scene.I_CameraMovement;

public class ComplexCameraMovement implements I_CameraMovement {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + ":ComplexCameraMovement - ";
	
	private ArrayList<I_CameraMovement> movementList = new ArrayList<I_CameraMovement>();
	private Integer index = null;
	
	
	
	public ComplexCameraMovement(ArrayList<I_CameraMovement> movementList) {
		this.movementList = movementList;
		index = new Integer(0);
	}


	public void addMovement( I_CameraMovement movement) {
		movementList.add(movement);
	}
	
	
	@Override
	public float[] getCameraCenter() {
		
		I_CameraMovement movement = movementList.get(index);
		
		try {		
			if (movement.isFinished()){
				index =  (( index + 1 ) == movementList.size() ) ? index : ++index ;
				movement = movementList.get(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movement.getCameraCenter();
	}

	@Override
	public float[] getCameraEye() {
		
		I_CameraMovement movement = movementList.get(index);
		try {
		
			if (movement.isFinished()){
				index =  (( index + 1 ) == movementList.size() ) ? index : index++;
				movement = movementList.get(index);
			}
		} catch (Exception e) {
		}
		return movement.getCameraEye();
	}

	@Override
	public boolean isPhysicsActive() throws Exception {
		return movementList.get(index).isPhysicsActive();
	}


	@Override
	public boolean isFinished()  {
		// El movimiento finaliza si el ultimo de todos los movimiento ha terminado
		return index >= movementList.size();
	}

	
	
	@Override
	public float getFovy() {
		return movementList.get( index == null ? 0 : index).getFovy();
	}


	@Override
	public float getzFar() {
		return movementList.get(index == null ? 0 : index).getzFar();
	}


	@Override
	public float getzNear() {
		return movementList.get(index == null ? 0 : index).getzNear();
	}
	
	@Override
	public void move(int time) {
		movementList.get(index == null ? 0 : index).move(time);		
	}


	@Override
	public I_AABBGeometry getCameraGeometry() {
		return movementList.get( index == null ? 0 : index).getCameraGeometry();
	}


	@Override
	public ESTransform getPerspectiveMatrix() {
		return movementList.get( index == null ? 0 : index).getPerspectiveMatrix();
	}


	@Override
	public void setAspect(float aspect) {
		movementList.get( index == null ? 0 : index).setAspect(aspect);
		
	}


	@Override
	public void moveToDown() {
		movementList.get( index == null ? 0 : index).moveToDown();
		
	}


	@Override
	public void moveToLeft() {
		movementList.get( index == null ? 0 : index).moveToLeft();		
	}


	@Override
	public void moveToRight() {
		movementList.get( index == null ? 0 : index).moveToRight();
		
	}


	@Override
	public void moveToUp() {
		movementList.get( index == null ? 0 : index).moveToUp();
		
	}


	@Override
	public ESTransform getViewMatrix() {
		return movementList.get( index == null ? 0 : index).getViewMatrix();
	}
	
	@Override
	public void restart() {
		index = new Integer(0);
		for ( I_CameraMovement c : movementList) {
			c.restart();
		}
		
	}
}

