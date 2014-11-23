/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;

/**
 * Interfaz que define las operaciones que han de poder aplicarse 
 * sobre una geometría de AABB
 */
public interface I_AABBGeometry extends I_Geometry {
	
	/**
	 * Obtiene un vector x,y,z con las posiciones maximas de la geometria
	 */
	float[] getMax();
	
	/**
	 * Obtiene un vector x,y,z con las posiciones minimas de la geometria
	 */
	float[] getMin();
	
}
