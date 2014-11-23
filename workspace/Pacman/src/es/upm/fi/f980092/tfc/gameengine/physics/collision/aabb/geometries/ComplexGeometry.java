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

import java.util.ArrayList;
import java.util.Iterator;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;



/**
 * Esta clase determina una geometria compuesta por varios 
 * elemento I_Collisionable. 
 * 
 * Debido a que la geometria puede tener varios elementos, 
 * existe un atributo partial que determina una región que
 * contiene todos los elementos y sirve para determinar 
 * rapidamente si no hay colisión o para incluirla dentro 
 * de un arbol AABB o similar.
 * 
 * Un ejemplo de su aplicación es determinar la geometria de 
 * un caja usando varias esferas.
 */
public class ComplexGeometry implements I_AABBGeometry {

	/******************************
	 * Atributos
	 ******************************/
	
	private ArrayList<I_AABBGeometry> subElements;		// Elementos que determinan la geometria 
	private float[] max = null;
	private float[] min = null;

	/******************************
	 * Métodos públicos
	 ******************************/
	
	public ComplexGeometry(ArrayList<I_AABBGeometry> subElements) {
		this.subElements = subElements;
	}
	
	public ComplexGeometry() {
		this.subElements = new ArrayList<I_AABBGeometry>();
	}
	
	public ArrayList<I_AABBGeometry> getGeometries() {
		return subElements;
	}

	/******************************
	 * Interfaz I_Geometry
	 ******************************/
	
//	@Override
//	public I_AABBGeometry rotate(int grades, int exe) {
//		ArrayList<I_Geometry> newPositions = new ArrayList<I_Geometry>();
//		for (I_Geometry subGeometry : subElements) {
//			newPositions.add(subGeometry.rotate(grades, exe));
//		}
//		return new ComplexGeometry(newPositions);
//		return this;
//	}

	@Override
	public I_AABBGeometry translate(float[] point) {
		ArrayList<I_AABBGeometry> newPositions = new ArrayList<I_AABBGeometry>();
		for (I_AABBGeometry subGeometry : subElements) {
			newPositions.add((I_AABBGeometry)subGeometry.translate(point));
		}
		return new ComplexGeometry(newPositions);
	}
	
	
	public float[] getMax() {
		if (max == null) {
			genMaxMin();
		}
		return max;
	}
		
	public float[] getMin() {
		if (min == null) {
			genMaxMin();
		}
		return min;
	}

	private void genMaxMin() {
		max = new float[3];
		min = new float[3];
		
		Iterator<I_AABBGeometry> iterator = subElements.iterator();
		I_AABBGeometry firstElement = iterator.next();
		float[] eMax = firstElement.getMax();
		float[] eMin = firstElement.getMin();
		
		for (int i = 0; i< 3 ; i++ ) {
			max[i] = eMax[i];
			min[i] = eMin[i];
		}
		
		while( iterator.hasNext() ) {
			I_AABBGeometry e = iterator.next();
			eMax = e.getMax();
			eMin = e.getMin();
		
			for (int i = 0; i< 3 ; i++ ) {
				max[i] =  max[i] < eMax[i] ? eMax[i] : max[i];
			}
			
			for (int i = 0; i< 3 ; i++ ) {
				min[i] =  min[i] > eMin[i] ? eMin[i] : min[i];
			}	
		}
	}
}

