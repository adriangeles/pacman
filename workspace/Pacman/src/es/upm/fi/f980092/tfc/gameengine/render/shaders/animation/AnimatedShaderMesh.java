package es.upm.fi.f980092.tfc.gameengine.render.shaders.animation;

import java.util.ArrayList;

import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

/**
 * Esta clase representa la estructura de informacion que contiene un listado de malla 
 * que seran procesadas por el AnimatedShader.
 * 
 * Esta compuesto por una secuencia de malla que se iran dibujando según transcurra el
 * timepo. 
 */
public class AnimatedShaderMesh implements I_Mesh {
	
	private Integer currentFrame;										// Malla que se esta mostrando actualmente
	private Integer frameTime;											// Tiempo que la malla ha de estar mostrandose
	
	private ArrayList<I_Mesh> meshes 	 = new ArrayList<I_Mesh>();		// Listado de mallas
	private ArrayList<Integer> durations = new ArrayList<Integer>();	// Listado con la duracion de cada malla	
		
	public AnimatedShaderMesh() {
		this.currentFrame = 0;
		this.frameTime = null;		
	}
	
	/**
	 * Este metod añade una nueva textura al final de la secuencia 
	 * indicando el tiempo que ha de ser mostrado
	 * 
	 * @param mesh Informacion de la textura
	 * @param duration Duracion que ha de estar mostrandose la textura
	 */
	public void addMesh(I_Mesh mesh , int duration) {
		meshes.add(mesh);
		durations.add(duration);
	}

	/**
	 * Este metodo es utilizando para indicar a la malla el tiempo que ha
	 * transcurrido y asi poder determinar que textura ha de mostrarse
	 * 
	 * @param t Tiempo transcurrido en milisegundos
	 */
	public void move(int t) {
		if (frameTime == null) {
			frameTime = 0;
		} else {
			frameTime -= t;			
			if (frameTime < 0) {
				nextFrame();				
			}
		}
	}

	public void updateDuration(int t) {
		for( int i = 0 ; i < durations.size() ; i++) {
			durations.set(i, t);
		}
	}
	
	/**
	 * Este metodo devuelve la textura actual. Antes de invocarse posiblemente
	 * ha de llamarse al metodo move para indicar el tiempo transcurrido
	 * desde la ultima interación
	 * 
	 * @return Malla actual
	 */
	public I_Mesh getCurrentMesh() {
		return meshes.get(currentFrame);
	}
		
	/***
	 * Este metodo determina cual es el siguiente frame teniendo en cuenta
	 * que la secuencia se repite a modo de bucle
	 */
	private void nextFrame() {
		currentFrame++;
		if (currentFrame >= meshes.size()) {
			currentFrame = 0;
		}
		
		frameTime += durations.get(currentFrame);
	}
}
