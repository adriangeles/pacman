/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adri�n �ngeles Ram�n
 * Universidad: Polit�cnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.collision;

import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

/**
 * Interfaz de un deterctor de colisiones entre dos figuras geom�tricas
 */
public interface I_CollisionDetector {
	
	/**
	 * Este metodo detecta si existe colisi�n entre dos elementos en funcion de sus posiciones y geometr�a teniendo
	 * en cuanta su posicion actual.
	 *  
	 * @param pA Posicion del elemento A
	 * @param pB Posicion del elemento B
	 * @param geoA Geometria del elemento A
	 * @param geoB Geometria del elemento B
	 * 
	 * @return Devuelve si existe o no colision entre ambos elementos
	 */
   boolean existStaticCollision(I_Geometry geoA, I_Geometry geoB );
   
   /**
    * Este metodo detecta posibles colisiones antes de que ocurran en funci�n de las trayectorias de los objetos.
    * En la versi�n del proyecto no esta implementada.
    * @return El punto de contacto que determina los objeto que colisionan y el inicio y fin de la colisi�n
    */
   public ContactPoint existDynamicCollision(A_SceneElement obj1, A_SceneElement obj2);

}
