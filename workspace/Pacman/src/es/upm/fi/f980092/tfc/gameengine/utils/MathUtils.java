/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.utils;

/**
 * Clase con utilizades sobre vectores
 */
public class MathUtils {
	

	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	
	/**
	 * Este metodo determinal a distancia entre dos puntos
	 * 
	 * <pre>Ambos vectores han de tener el mismo tamaño</pre>
	 */
	public static int distance(int[] p1, int[] p2) {
		if (p1.length == p2.length) {
			int sum = 0;

			for (int i = 0; i < p1.length; i++) {
				sum += Math.pow(p1[i] - p2[i], 2);
			}
			return Math.round((float) Math.sqrt(sum));
		} else {
			throw new RuntimeException("p1.length == " + p1.length	+ " != p2.length == " + p2.length);
		}
	}
	
	public static int distance(int[] p1, int[] p2, int size) {
		if (p1.length >= size  && p2.length >= size) {
			float sum = 0;

			for (int i = 0; i < size; i++) {
				sum += (float) Math.pow(p1[i] - p2[i], 2);
			}
			return Math.round( (float) Math.sqrt(sum));
		} else {
			throw new RuntimeException("p1.length == " + p1.length	+ " != p2.length == " + p2.length);
		}
	}

	
	public static int[] sum(int[] p1, int[] p2) {
		return sum(p1,p2,p1.length);
	}
	
	/**
	 * Este metodo suma dos vectores
	 * 
	 * <pre>Ambos vectores han de tener el mismo tamaño</pre>
	 */
	public static int[] sum(int[] p1, int[] p2, int length) {
		int[] result = new int[p1.length];

		if (p1.length >= length && p2.length >= length) {
			for (int i = 0; i < length; i++) {
				result[i] = p1[i] + p2[i];
			}
		} else {
			throw new RuntimeException("p1.length == " + p1.length	+ " != p2.length == " + p2.length);
		}
		return result;
	}
	
	/**
	 * Este metodo resta dos vectores p1 - p2
	 * 
	 * <pre>Ambos vectores han de tener el mismo tamaño</pre>
	 */
	public static int[] sustract(int[] p1, int[] p2, int length) {
		int[] result = new int[p1.length];

		if (p1.length >= length && p2.length >= length) {
			for (int i = 0; i < length; i++) {
				result[i] = p1[i] - p2[i];
			}
		} else {
			throw new RuntimeException("p1.length == " + p1.length	+ " != p2.length == " + p2.length);
		}
		return result;
	}
	
	public static int[] sustract(int[] p1, int[] p2) {
		return sustract(p1,p2, p1.length);
	}

	/**
	 * Este metodo realiza una multiplicacion de un escalar por 
	 * un vector
	 */
	public static int[] mul(int scalar, int[] vector) {
		int[] result = new int[vector.length];
		for (int i = 0; i < vector.length; i++) {
			result[i] = vector[i] * scalar;
		}
		return result;
	}

	/**
	 * Este metodo determina si dos vectores son exactamente 
	 * el mismo. 
	 */
	public static boolean equals(int[] v1, int[] v2) {
		boolean result = true;

		if (v1.length == v2.length) {
			result = equals(v1,v2,v1.length);
		} else {
			throw new RuntimeException("v1.length == " + v1.length	+ " != v2.length == " + v2.length);
		}
		return result;
	}
	
	/**
	 * Este metodo determina si dos vectores son exactamente 
	 * el mismo. 
	 */
	public static boolean equals(int[] v1, int[] v2, int length) {
		boolean result = true;

		if (v1.length >= length && v2.length >= length) {
			int i = 0;
			while (result && i < length) {
				result &= v1[i] == v2[i];
				i++;
			}
		} else {
			throw new RuntimeException("v1.length or v2.length is less than " + length);
		}
		return result;
	}

	public static double module(int[] vector) {
		return module(vector, vector.length);
	}
	
	public static int module(int[] vector, int length) {
		float result = 0;
		for(int i = 0; i < length; i++) {
			result += vector[i] * vector[i];
		}
		return Math.round((float) Math.sqrt(result));
	}
	
	/**
	 * Gira un punto sobre un eje un determinado numero de grados
	 * @param point Punto que queremos girar
	 * @param exe Eje sobre el que queremos girar Z=0 , Y=1 y X = 2 
	 * @param grades Numero de grados que queremos girarlo
	 * @return posicion del punto girado
	 */
	public static int[] rotate( int[] point, int exe, int grades) {
		int[] result = new int[3];
		double radians = Math.toRadians(grades);
		
		switch (exe) {
		case 0:	// Eje Z
			result[0] = Math.round((float) (point[0] * Math.cos(radians) + point[1] * Math.sin(radians))); 
			result[1] = Math.round((float) (point[1] * Math.cos(radians) - point[0] * Math.sin(radians) ));
			result[2] = point[2];
			break;
		case 1:	// Eje Y
			result[0] = Math.round((float) (point[2] * Math.sin(radians) + point[0] * Math.cos(grades))  );
			result[1] = point[1];
			result[2] = Math.round((float) (point[2] * Math.cos(radians) - point[0] * Math.sin(grades))  );			
			break;
		case 2:	// Eje X
			result[0] = point[0];
			result[1] = Math.round((float) (point[1] * Math.cos(radians) - point[2] * Math.sin(grades))  );
			result[2] = Math.round((float) (point[1] * Math.sin(radians) + point[2] * Math.cos(grades))  );			
			break;
		}
		return result;		
	}
	

}
