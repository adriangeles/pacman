/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import android.util.Log;


/**
 * Esta  clase representa una figura geometrica de tipo AABB
 * (Axis-aligned Bounding Boxes) la cual esta alineada con los 
 * ejes.
 */
public class AABBGeometry implements I_AABBGeometry {

	/*******************
	 * Constantes
	 *******************/
	
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::AABBGeometry";
	
	/*******************
	 * Atributos
	 *******************/

	private float[] max;		// Se corresponde con el punto que representa el vertice superior 
	private float[] min;		// Se corresponde con el punto que representa el vertice inferior
//	private int[] refMax;	// Punto que representa el vertice superior en funcion de centor de geometria
//	private int[] refMin;   // Punto que representa el vertice inferior en funcion de centor de geometria
//	private int[] point; 	// Punto de referencia en el que se situa el centro de la geometria
	/*******************
	 * Constructores
	 *******************/
	
	public AABBGeometry(float[] max, float[] min) {
		this.max = max;
		this.min = min;
	}
	

	/**********************
	 * Interfaz I_Geometry
	 **********************/
	
	@Override
	public AABBGeometry translate(float[] point) {
		float[] max = new float[]{this.max[0] + point[0] , this.max[1] + point[1], this.max[2] + point[2]};
		float[] min = new float[]{this.min[0] + point[0] , this.min[1] + point[1], this.min[2] + point[2]};
		return new AABBGeometry(max,min);
	}
		 	
//	@Override
//	public AABBGeometry rotate(int grades, int exe) {
//		max = MathUtils.rotate(this.max, exe, grades);
//		min = MathUtils.rotate(this.min, exe, grades);		
//		return new AABBGeometry(max,min);
//		return this;
//	}
	
	
	/*******************
	 * Métodos públicos
	 *******************/
		
	public float[] getMax() {
		return max;
	}
	
	public void setMax(float[] max) {
		this.max = max;
	}
	
	public float[] getMin() {
		return min;
	}
	
	public void setMin(float[] min) {
		this.min = min;
	}
	
	public void printTrace() {
		Log.d(TAG, TAG2 + " Max = [" + max[0] + "," + max[1] + "," + max[2] + "]   Min =  [" + min[0] + "," + min[1] + "," + min[2] + "] ");
	}
}