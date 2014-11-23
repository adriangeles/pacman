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

import es.upm.fi.f980092.tfc.gameengine.exceptions.UnavailableMethod;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionDetectorFactory;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_CollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

/**
 * Este clase es utilizada para detectar las colisiones entre dos elementos cuya geometria
 * esta definida por un subconjunto de geometrias.
 */
public class ComplexComplexCollisionDetector implements I_CollisionDetector {  

	@Override
	public boolean existStaticCollision(I_Geometry geoA, I_Geometry geoB) {
		if ( geoA instanceof ComplexGeometry && geoB instanceof ComplexGeometry) {

			for( es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry subGeometry : ((ComplexGeometry) geoA).getGeometries()) {
				I_CollisionDetector detector = CollisionDetectorFactory.getDetector(geoB, subGeometry);
				if (detector.existStaticCollision(geoB, subGeometry))
					return true;
			}			
			return false;
		}
		throw new IllegalArgumentException("Both element must be instances of ComplexGeometry");
	}
	
	@Override
	public es.upm.fi.f980092.tfc.gameengine.physics.collision.ContactPoint existDynamicCollision(A_SceneElement obj1, A_SceneElement obj2) {
		throw new UnavailableMethod();
	}
}

