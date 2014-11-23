/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.collision;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.AABBCollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.AABBComplexCollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.ComplexComplexCollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;

/**
 * Esta clase sigue el patron Factory, su objetivo es obtener el detector de
 * colisiones adecuado para dos elementos geometricos. 
 * Actualmente sólo soporta geometrias compuestas y AABB
 */
public class CollisionDetectorFactory {

	/**
	 * Este método es utilizado para obtener el detector adecuado que calcula si existe o no 
	 * colision entre dos elementos en funcion de su geometría.
	 */
    public static I_CollisionDetector getDetector(I_Geometry obj1, I_Geometry obj2) {

        I_CollisionDetector result = null;
       
        if (obj1 instanceof AABBGeometry) {
        	if (obj2 instanceof AABBGeometry) {
        		result = new AABBCollisionDetector();
        	} else if (obj2 instanceof ComplexGeometry) {
        		result = new AABBComplexCollisionDetector();
        	} 
        } else if (obj1 instanceof ComplexGeometry){
        	if (obj2 instanceof AABBGeometry) {
        		result = new AABBComplexCollisionDetector();
        	} else if (obj2 instanceof ComplexGeometry) {
        		result = new ComplexComplexCollisionDetector();
        	}
        }
        	
        if (result == null)
            throw new RuntimeException("Geometric object collision not implemented");

        return result;
    }
}
