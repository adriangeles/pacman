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
import es.upm.fi.f980092.tfc.gameengine.physics.collision.ContactPoint;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_CollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.utils.MathUtils;

/**
 * Esta clase es responsable de calcular si existen colisiones entre dos geometrias
 * del tipo AABB ( Axis aligned bounding box )
 */
public class AABBCollisionDetector implements I_CollisionDetector {


	/*******************************
	 * Métodos I_CollisionDetector
	 *******************************/
		
	@Override
	public boolean existStaticCollision(I_Geometry geoA, I_Geometry geoB) {
		
		if ( geoA instanceof AABBGeometry && geoB instanceof AABBGeometry) {
			return existCollision( (AABBGeometry) geoA, (AABBGeometry) geoB);
		} else {
			throw new IllegalArgumentException("Both elements must be instances of AABBGeometry");
		}
	}
	
	@Override
	public ContactPoint existDynamicCollision(A_SceneElement obj1, A_SceneElement obj2) {
		throw new UnavailableMethod();
	}
	
	/*******************
	 * Métodos privados
	 *******************/
	
	/**
	 * Este metodo devuelve si existe un colisión cuando dos objetos no se estan moviendo
	 */
	private boolean existCollision(AABBGeometry a, AABBGeometry b) {
		float[] aMax = a.getMax();
		float[] aMin = a.getMin();
		float[] bMax = b.getMax();
		float[] bMin = b.getMin();
		if (aMax[0] <= bMin[0] || bMax[0] <= aMin[0]) return false;
		if (aMax[1] <= bMin[1] || bMax[1] <= aMin[1]) return false;
		if (aMax[2] <= bMin[2] || bMax[2] <= aMin[2]) return false;		
		return true;
	}
	
	/**
	 * Con este método se calcula los tiempos de colisiones entre dos objetos con geometrias AABB que se 
	 * mueven a velocidad constante.
	 * @param pa Posicion del objeto A
	 * @param pb Posicion del objeto B
	 * @param va Velocidad del objeto A
	 * @param vb Velocidad del objeto B
	 * @param a Geometria del objeto A
	 * @param b Geometria del objeto B
	 * @return Devuelve la información del punto de contacto si existe.
	 */
	@SuppressWarnings("unused")
	private float[] existCollision(int[] va, int[] vb, AABBGeometry a, AABBGeometry b) {		
		float tfirst = 0.0f;
		float tlast = 1.0f;

		if (existCollision(a, b)) {
			// Si existe inicialmente una colision y no ha sido tratada
			// se asume que ha de ser ignorada
			return new float[] {0,0};
		}

		int[] v = MathUtils.sustract(vb, va);

		float[] aMax = a.getMax();
		float[] aMin = a.getMin();
		float[] bMax = b.getMax();
		float[] bMin = b.getMin();

		for (int i = 0; i < 3; i++) {
			if (v[i] < 0.0f) {
				if (bMax[i] < aMin[i]) return null; 
				if (aMax[i] < bMin[i]) tfirst = Math.max((aMax[i] - bMin[i]) / v[i], tfirst);
				if (bMax[i] > aMin[i]) tlast = Math.min((aMin[i] - bMax[i]) / v[i], tlast);
			}
			if (v[i] > 0.0f) {
				if (bMin[i] > aMax[i]) return null;
				if (bMax[i] < aMin[i]) tfirst = Math.max((aMin[i] - bMax[i]) / v[i], tfirst);
				if (aMax[i] > bMin[i]) tlast = Math.min((aMax[i] - bMin[i]) / v[i], tlast);
			}
			
			if (tfirst > tlast) return null;
		}
		return new float[]{Math.round(tfirst),Math.round(tlast)};
	}
	
	/**
	 * Este metodo determina los tiempos de colisión de dos objetos en movimiento y sobre los que se estan aplicando aceleraciones
	 * @param pa Posicion del objeto A
	 * @param pb Posicion del objeto B
	 * @param va Velocidad del objeto A
	 * @param vb Velocidad del objeto B
	 * @param a Geometria del objeto A
	 * @param b Geometria del objeto B
	 * @param aa Aceleracion del objeto A
	 * @param ab Aceleracion del objeto B
	 * @param ta Tiempo de aceleracion del objeto A
	 * @param tb Tiempo de aceleracion del objeto B
	 */
	@SuppressWarnings("unused")
	private ContactPoint existCollision(int[] va, int[] vb, int[] aa, int[] ab, int[] ta, int[] tb, AABBGeometry a, AABBGeometry b) {
		throw new UnavailableMethod();
	}
}

	