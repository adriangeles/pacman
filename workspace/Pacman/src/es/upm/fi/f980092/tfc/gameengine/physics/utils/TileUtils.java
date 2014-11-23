/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.utils;

public class TileUtils {

//	/**
//	 * Este metodo determina la casilla de referencia teniendo 
//	 * en cuenta la casilla de referencia actual
//	 */
//	public static float[] getReferenceTile(float[] position, float[] tile) {
//
//		float[] result = tile;
//
//		if ((Math.abs(position[0] - tile[0]) >= TITLE_SIZE)
//				|| Math.abs(position[1] - tile[1]) >= TITLE_SIZE) {
//			result = getTile(position);
//		}
//
//		return result;
//	}


	/**
	 * Este metodo determina la casilla mas cercana a un 
	 * punto
	 */
	public static int[] getTile(int[] position, int tileSize) {
		float x = Math.round( (float) position[0] / tileSize);
		float y = Math.round( (float) position[1] / tileSize);
		return new int[] { (int) x , (int) y };
	}
	

}
