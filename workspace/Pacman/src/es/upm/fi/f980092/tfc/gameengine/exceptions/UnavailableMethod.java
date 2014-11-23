/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.exceptions;

/**
 * Esta clase representa una excepción la cual será
 * lanzada en tiempo de ejecución en los puntos del 
 * motor que se haya decido no implementar como 
 * parte del proyecto. Pudiendo ser desarrollado en
 * posteriores versiones.
 */
public class UnavailableMethod extends RuntimeException {

	/*********************
	 * Constantes
	 *********************/
	
	private static final long serialVersionUID = 1L;

	/*********************
	 * Constructores
	 *********************/
	
	public UnavailableMethod() {
		super("This method is nos supported in this versión");		
	}
}

