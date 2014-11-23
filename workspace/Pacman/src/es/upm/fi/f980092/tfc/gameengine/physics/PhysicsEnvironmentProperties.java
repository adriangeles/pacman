/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adri�n �ngeles Ram�n
 * Universidad: Polit�cnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics;


/**
 * Esta clase contiene las propiedades fisicas de un modelo f�sico como por ejemplo
 * la gravedad.
 */
public class PhysicsEnvironmentProperties  {

	/*******************
	 * Atributos
	 *******************/
	
	private int[] gravity;
	private int tileSize;
	
	/*******************
	 * Constructores
	 *******************/
	
	public PhysicsEnvironmentProperties(int[] gravity,int tileSize) {
		this.gravity = gravity;
		this.tileSize = tileSize;
	}
	
	/*************************************
	 * Interfaz I_PhysicsSceneProperties
	 *************************************/
	
	public int[] getGravity() {
		return gravity;
	}

	public int getTileSize() {		
		return tileSize;
	}
}