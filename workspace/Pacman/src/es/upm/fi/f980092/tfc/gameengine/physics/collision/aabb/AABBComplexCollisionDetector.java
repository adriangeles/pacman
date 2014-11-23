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
import es.upm.fi.f980092.tfc.gameengine.physics.collision.ContactPoint;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_CollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public class AABBComplexCollisionDetector implements I_CollisionDetector {

	/*********************************
	 * Interfaz I_Collision Detector
	 *********************************/

	@Override
	public boolean existStaticCollision(I_Geometry geoA, I_Geometry geoB) {
		AABBGeometry aabbGeo;
		ComplexGeometry complexGeo;
		
		if ( geoA instanceof ComplexGeometry && geoB instanceof AABBGeometry) {
			aabbGeo = (AABBGeometry) geoB;
			complexGeo = (ComplexGeometry) geoA;			
		} else if (geoA instanceof AABBGeometry && geoB instanceof ComplexGeometry) {
			aabbGeo = (AABBGeometry) geoA;
			complexGeo = (ComplexGeometry) geoB;
		} else {
			throw new IllegalArgumentException("One of the objet must be an AABBGeometry and the other ComplexGeometry");
		}
				
		for( I_AABBGeometry subGeo : complexGeo.getGeometries()) {
			I_CollisionDetector detector = CollisionDetectorFactory.getDetector(aabbGeo, subGeo);
			if ( detector.existStaticCollision(aabbGeo, subGeo)) {
				return true;
			}
		}
		return false;		
	}	
	
	@Override
	public ContactPoint existDynamicCollision(A_SceneElement obj1, A_SceneElement obj2) {
		throw new UnavailableMethod();
	}
}
