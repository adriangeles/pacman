/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 * Date: 02-may-2010
 */
package es.upm.fi.f980092.tfc.gameengine.scene;

import java.util.ArrayList;

import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsEnvironmentProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.QuadTreeNode;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement.CollisionType;

/**
 * Esta clase contiene la definicion de un escenario del videojuego, es decir:
 *    - Pacman
 *    - Fantasmas
 *    - Elementos del escenario
 */
public class Scene {
	
	public enum State {
	    START, RUNNING, PAUSE, RESTART, FINISH
	}
	
	/*******************************************
	 * PROPIEDADES
	 *******************************************/
	
	// Propiedades que definen el escenario
	
	public String name;
	public String description;
	public String path;
	private State state = State.START; 
		 
	// Propiedades que definen la cámara
	
	private I_CameraMovement camera;			// Camara que habitual del juego
	
	private float scale = 1.0f;
		
	// Propiedades físicas del escenario
	
	private PhysicsEnvironmentProperties physicsProperties;
	
	/**
	 *  Elementos que componen el escenario
	 */
    protected ArrayList<A_SceneElement> renderableElements = new ArrayList<A_SceneElement>();    
    protected ArrayList<A_SceneElement> softCollisionableElements = new ArrayList<A_SceneElement>();
    protected ArrayList<A_SceneElement> hardCollisionableElements = new ArrayList<A_SceneElement>();
    protected ArrayList<A_SceneElement> dynamicElements = new ArrayList<A_SceneElement>();

    
            
    protected QuadTreeNode quadtree;
	/*******************************************
     * Contructores
     *******************************************/
    
	public Scene(String path, PhysicsEnvironmentProperties physicsProperties) {
		this.path = path;
		this.physicsProperties = physicsProperties;
	}
	
	public void init (float aspect) {
		camera.setAspect(aspect);		
	}
	
	/*******************************************
	 * Métodos de I_RenderableScene
	 *******************************************/
	
	public void setCamera(I_CameraMovement camera) {
		this.camera = camera;
	}
	
	public I_CameraMovement getCamera() {
		return this.camera; 
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}


	/*******************************************
	 * Métodos interfaz I_Physics_Scene
	 *******************************************/
	
	public PhysicsEnvironmentProperties getProperties() {
		return physicsProperties;
	}
	
	/*******************************************
	 * Metodos publicos
	 *******************************************/
	
	// Propiedades de la escena
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
				
	public String getPath() {
		return path;		
	}

	// Elementos de la escena 
	
	/**
	 * Añadimos un elemento a la escena y lo incluimos a las 
	 * distintas estructuras dependiendo de:
	 *  	- Si es dinamico 
	 *  	- Tipo de colision
	 *  	- Si se tiene que renderizar
	 */
	public void addElement(A_SceneElement element) {
		element.setSceneRef(this);
		if ( element.getCollisionType() == CollisionType.SOFT) {
			softCollisionableElements.add(element);
		} else if (element.getCollisionType() == CollisionType.HARD) {
			hardCollisionableElements.add(element);
		}
		
		if (element.isDynamic()) {
			dynamicElements.add(element);
		}
		
		if (element.isRenderizable()) {
			renderableElements.add(element);
		}
		
		element.init();
	}
	
	public void removeElement(A_SceneElement element) {
		softCollisionableElements.remove(element);
		hardCollisionableElements.remove(element);
		dynamicElements.remove(element);
		renderableElements.remove(element);
	}
		
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public QuadTreeNode getQuadtree() {
		return quadtree;
	}

	public final ArrayList<A_SceneElement> getRenderableElements() {		
		return renderableElements;
	}
	
	public final ArrayList<A_SceneElement> getSoftCollisionElements() {
		return softCollisionableElements;
	}
	
	public final ArrayList<A_SceneElement> getHardCollisionElements() {
		return hardCollisionableElements;
	}
	
	public final ArrayList<A_SceneElement> getDynamicElements() {
		return dynamicElements;
	}
	
	public void restart() {
		state = State.START;
		for( A_SceneElement e : softCollisionableElements) {			
			e.restart();
		}
	}
}
