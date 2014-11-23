package es.upm.fi.f980092.tfc.pacman.engine.elements;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public abstract class A_PacmanElement extends A_SceneElement {

	/*******************
	 * Enumerados
	 *******************/
	
	
	public enum Type {
		PACMAN(0), GHOST(1), BIG_PILL(2), SMALL_PILL(3), WALL(4), GHOST_DOOR(5);

		private final int id;

		Type(int id) {
			this.id = id;
		}

		int value() {
			return id;
		}
	}

	/*******************
	 * Constantes
	 *******************/
	
	private static final String TAG =  "PACMANIA";
	private static final String TAG2 = "PACMANIA::A_PacmanElement - ";
	
	
	/*******************
	 * Constructores
	 *******************/
	
	public A_PacmanElement() {
		super();
	}

	/*******************
	 * Propiedades
	 *******************/
	
	protected Type type;		    // Determina el tipo de elemento dentro del pacman
	protected int SCALE = 10000;    // Escala  1 unidad render = 10000 unidades fisicas
	
	/********************
	 * Metodos publicos
	 ********************/
	
	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Este método modifica la dirección del elemento si es posible según la trayectoria actual
	 * si no es posible crea una accion diferida para que se cambie la dirección cuando sea 
	 * posible, es decir, cuadno se pase por una casilla de referencia.
	 * 
	 * Este metodo está sincronizado con el escenario debido a que no puede ser invocado 
	 * simultaneamente mientras que el motor del juego esta recalculando las nuevas posiciones
	 * de los distintos elementos.
	 */
	public void goTo(Direction direction) {

		PhysicsProperties request = this.getRequest();
		
		synchronized (request) {
			Log.i(TAG, TAG2 + "Bloqueo ");

			int[] velocity = this.getVelocity();
			int[] rotation = this.getRotation();

			int s = Math.abs(velocity[0] + velocity[1]);
			
			if ( direction == Direction.EAST) {
				velocity = new int[] {s, 0, 0}; 
				rotation = new int[] {0, 0, 90};
			} else if ( direction == Direction.WEST) {
				velocity = new int[] {-s, 0, 0};
				rotation = new int[] { 0, 0, 270};
			} else if ( direction == Direction.NORT ) {
				velocity = new int[] {0, s, 0};
				rotation = new int[] { 0, 0, 0};
			} else if ( direction == Direction.SOUTH) {
				velocity = new int[] {0, -s, 0};
				rotation = new int[] { 0, 0, 180};
			}			
			
			request.setVelocity(velocity);			
			this.setRotation(rotation);			
			Log.i(TAG, TAG2 + "DesBloqueo ");
		}
	}	
	
	public abstract void restart();
}
