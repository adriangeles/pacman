/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adri�n �ngeles Ram�n
 * Universidad: Polit�cnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.physics.collision;

import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

/**
 * Esta clase contiene la informaci�n de una colisiones resultante entre dos objetos. 
 * Contiene las referencias a los dos objetos que colisionan, el tiempo en el 
 * que se produce la colision y el tiempo en el que finaliza.
 */
public class ContactPoint {

	/*******************
	 * Atributos
	 *******************/
	
    private A_SceneElement obj1;	// Primer objeto de la intersecci�n
    private A_SceneElement obj2;	// Segundo objeto de la intersecci�n
    private int tfirst;				// Tiempo trascurrido antes de la colisi�n
    private int tlast;				// Tiempo hasta que finaliza la colisi�n

    /*******************
	 * Constructores
	 *******************/
    
    public ContactPoint(A_SceneElement obj1, A_SceneElement obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
    
    public ContactPoint(A_SceneElement obj1, A_SceneElement obj2,
			int tfirst, int tlast) {
		super();
		this.obj1 = obj1;
		this.obj2 = obj2;
		this.tfirst = tfirst;
		this.tlast = tlast;
	}

    /*******************
	 * M�todos p�blicos
	 *******************/

    public void setFirstElement(A_SceneElement obj1) {
        this.obj1 = obj1;
    }
    
    public void setSecondElement(A_SceneElement obj2) {
        this.obj2 = obj2;
    }

    public void setInterval(int[] interval) {
    	tfirst = interval[0];
    	tlast  = interval[1];
    }
    
    /**************************
     * Interval I_ContactPoint
     **************************/
    

	public A_SceneElement getFirstElement() {
        return obj1;
    }

    public A_SceneElement getSecondElement() {
        return obj2;
    }
    
	public int[] getCollisionPeriod() {		
		return new int[]{ tfirst, tlast };
	}    
}
