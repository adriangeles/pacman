/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.scene;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.ia.I_Behaviour;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.QuadTreeNode;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

/**
 * Clase abstracta de un elemento del pacman que contiene las propiedades
 * y metodos comunes
 */
public abstract class A_SceneElement {
	
	/***************
	 * Enumerados
	 ***************/
	
	public enum CollisionType{
		SOFT, HARD, NONE;
	}
	
	/***************
	 * Constantes
	 ***************/
	
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = "TfcGameEngine::PhysicsElement - ";
	private final int[] empty = new int[] {0,0,0};
	
	/***************
	 * Atributos
	 ***************/

	// Atributos del modelo general 
	private int[] position = empty;														// Ubicacion del objeto 
	private int[] refTile  = empty;														// Casilla de referencia que ha cruzado el elemento
	private int velocity[] = empty;														// Vector velocidad
	private int aceleration[] = empty;													// Vector aceleracion
	private int acelerationTime[] = empty;												// Tiempo que se aplica el vector aceleracion
	private boolean renderizable = false;												// Determina si el objeto ha de renderizarse
	private boolean dynamic = false;													// Determina si el objeto es dinamico y ha de estudiarse sus colisiones
	private CollisionType  collisionType = CollisionType.NONE;							// Determina el tipo de colisiones, blandas o duras, si son duras las procesa el motor
	private I_Geometry geometry = null;													// Forma implicita del objeto	
	private I_Geometry procesedGeometry = null;											// Forma implicita del objeto procesada respecto a la posicion y rotacion
	private QuadTreeNode geometricQuadTree = null;
	private I_Behaviour behaviour = null;
	
	private PhysicsProperties request = new PhysicsProperties();
		
	// Atributos de la malla
	@Deprecated
	private String meshId = null;							// Identificador de la malla asociada con el elemento
	private I_Mesh mesh = null;								// Malla
	private float  meshScale = 1.0f;						// Escala de la malla que se esta dibujando
	private int[]  meshRotation = empty;					// Rotaciones aplicadas sobre el objeto	
	
	private Scene scene = null;			// Escena que esta relacionada con el objeto y con la que se sincronizara
	
	
	/*****************
	 * Constructores
	 *****************/
	
	public A_SceneElement() {
		
	}
	
	
	public void init(){
		
	}
	
	/******************************
	 * Interfaz I_PhysicsElement
	 ******************************/
	
	
	
	public int[] getPosition() {
		return position.clone();
	}
	
	public I_Behaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(I_Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	public void setPosition(int[] position) {		
		if ( position == null || position.length != 3) {
			Log.w(TAG,TAG2 + " Incorrect 3d position, initialized by default at [0,0,0]");
			this.position = new int[]{0,0,0};
		} else {
			this.position = position;
		}
		// Al cambiar la posicion recalculamos la geometria
		this.procesedGeometry = this.geometry.translate(  new float[] { this.position[0],this.position[1],this.position[2]});
		
	}
	
		public I_Geometry getGeometry() {		
		return procesedGeometry;
	}
	
	public QuadTreeNode getGeometricQuadTree() {
			return geometricQuadTree;
		}

		public void setGeometricQuadTree(QuadTreeNode geometricQuadTree) {
			this.geometricQuadTree = geometricQuadTree;
		}

	public I_Geometry getInitialGeometry() {
		return geometry;
	}
	
	public void setGeometry(I_Geometry geometry) {
		this.geometry = geometry;	
		this.procesedGeometry = this.geometry.translate(  new float[] { this.position[0],this.position[1],this.position[2]});
	}
	
	public int[] getRefTile() {		
		return refTile.clone();
	}

	public void setRefTile(int[] refTile) {
		this.refTile = refTile;
	}
	
	public void setVelocity(int[] velocity) {
		this.velocity = velocity;
	}
	
	public int[] getVelocity() {
		return velocity.clone();
	}
	
	public int[] getRotation() {
		return meshRotation.clone();
	}
	
	public void setRotation(int[] rotation) {
				
		if ( rotation == null || rotation.length != 3) {
			Log.w(TAG,TAG2 + " Incorrect 3d rotation, initialized by default at [0,0,0]");
			this.meshRotation = new int[]{0,0,0};
		} else {
			this.meshRotation = rotation;
		}
	}
	
	public void setDynamic(boolean value) {
		dynamic = value;
	}
	
	public boolean isDynamic() {		
		return dynamic;
	}
	
	
	public PhysicsProperties getRequest() {
		return request;		
	}
	

	public int[] getAceleration() {		
		return aceleration.clone();
	}

	
	public void setAceleration(int[] aceleration) {
		this.aceleration = aceleration;
	}

	public int[] getAcelerationTime() {		
		return acelerationTime.clone();		
	}

	public void setAcelerationTime(int[] acelerationTime) {
		this.acelerationTime = acelerationTime;
	}

	public I_Mesh getMesh() {
		return mesh;
	}

	public void setMesh(I_Mesh mesh) {
		this.mesh = mesh;
		renderizable =  mesh != null; 
	}
	
	/**************************
	 * I_RenderElement
	 **************************/
		
	public String getMeshId() {
		return meshId;
	}

	public void setMeshId(String meshId) {
		this.meshId = meshId;		
	}
	
	public void setScale(float scale) {
		this.meshScale = scale; 
	}
	
	public float getScale() {
		return meshScale;
	}
	
	/*****************************
	 * Interfaz I_SceneElement
	 *****************************/
	
	public void setSceneRef(Scene scene) {
		this.scene = scene;		
	}	
	
	public Scene getSceneRef() {
		return scene;
	}

	public void setCollisionType(CollisionType collisionType) {
		this.collisionType = collisionType;
	}

	public CollisionType getCollisionType() {
		return collisionType;
	}
	
	public boolean isRenderizable() {
		return renderizable;
	}

	public void setRenderizable(boolean renderizable) {
		this.renderizable = renderizable;
	}

	public void move(int step) {
		if (behaviour != null) 
			behaviour.move(this, step);		
	}
	
	public void restart() {
		Log.i(TAG,TAG2 + " The restart method should be overwrited");
	}
}
