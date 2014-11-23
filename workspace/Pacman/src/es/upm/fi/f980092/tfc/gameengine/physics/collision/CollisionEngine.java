package es.upm.fi.f980092.tfc.gameengine.physics.collision;

import java.util.ArrayList;

import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public class CollisionEngine {

	private static final String TAG  = "TfcGameEngine";
	private static final String TAG2 = "TfcGameEngine::CollisionEngine - ";
	
	/**
	 * Este método detecta la existencia de colisión entre dos geometrías.
	 * Es usado en el motor de juegos para detectar si parte de una malla ha de ser renderizada
	 * cuando existe colsiión entre la malla y el rango visual de la cámara.
	 * @param geo1 
	 * @param geo2
	 * @return Devuelve true si se produce colisión
	 */
	public static boolean existCollision(I_Geometry geo1 , I_Geometry geo2) {	  
	    I_CollisionDetector detector =  CollisionDetectorFactory.getDetector(geo1,geo2);
	    return detector.existStaticCollision(geo1, geo2);	    
	}

	/**
	 * Este método devuelve el listado de objetos de la lista geoList que colisionan con la geometria geo1
	 * @param geo1
	 * @param geoList
	 * @return Listado de geometrias con las que se ha producido una colisión.
	 */
	public static ArrayList<I_Geometry> calculateCollisions( I_Geometry geo1 , ArrayList<I_Geometry> geoList ) {

		ArrayList<I_Geometry> resultado = new ArrayList<I_Geometry>();

		for ( I_Geometry geo2 : geoList) {

			I_CollisionDetector detector =  CollisionDetectorFactory.getDetector(geo1,geo2);
			if (detector.existStaticCollision(geo1, geo2)) {
				resultado.add(geo2);
			}
		}		
		return resultado;
	}

	/**
	 * Este metodo es usado para detectar colisiones entre dos elementos de una scena.
	 * @param e1
	 * @param e2
	 * @return Devuelve si existe colisión
	 */
	public static boolean detectCollision( A_SceneElement e1, A_SceneElement e2) {
		I_Geometry geo1 = e1.getGeometry();
		I_Geometry geo2 = e2.getGeometry();

		I_CollisionDetector detector = CollisionDetectorFactory.getDetector(geo1,geo2);
		return detector.existStaticCollision(geo1, geo2);		
	}
}
