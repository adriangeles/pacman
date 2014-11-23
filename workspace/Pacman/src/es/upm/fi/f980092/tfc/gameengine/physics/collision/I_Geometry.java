package es.upm.fi.f980092.tfc.gameengine.physics.collision;


public interface I_Geometry {

	/** 
	 * Rota la geometria especificando el eje y el angulo
	 * @param grades Numero de grados a rotar
	 * @param exe Eje sobre el que rotar Z=0, Y=1 y X=2
	 * @return Devuelve una nueva geometrica aplicando la rotacion
	 */
	// No implementada
	// I_AABBGeometry rotate(int grades, int exe);
	
	/**
	 * Traslada la geometria hasta el punto determinado
	 * @param point Nuevo centro de la geometria
	 * @return Devuelve la nueva geometria aplicando la traslacion
	 */
	I_Geometry translate(float[] point);
}
