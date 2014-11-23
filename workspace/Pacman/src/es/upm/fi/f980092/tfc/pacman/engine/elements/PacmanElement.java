/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.engine.elements;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.ia.behaviours.PacmanBehaviour;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.render.caches.MeshesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

/**
 * Esta clase determina el pacman como elemento del juego, se define como
 * un elemento dinámico con una geometria esferica.
 */
public class PacmanElement extends A_PacmanElement {

	@Override
	public I_Mesh getMesh() {
		return MeshesCache.getInstance().getMesh("Pacman");		
	}

	private static final String TAG = "PACMAN# PacmanElement";
	
	private int[] initialPosition;
	private int[] initialVelocity;
	private int[] initialRotation = new int[]{0,0,90};
	
	public PacmanElement() {
		super();
		setType(Type.PACMAN);
		// Comportamiento de pacman
		setBehaviour( new PacmanBehaviour((int) (SCALE/2)));
		setDynamic(true);
		// Elemento renderizable
		setRenderizable(true);
		setScale(0.5f);		 			// Escala que aplicaremos a la malla
		setMeshId("Pacman");			// Nombre de la malla obtenida de la cache
		
		// Creamos la geometria para las colieiones
		float increment = SCALE / 2;		
		float[] max = new float[]{increment,increment,increment};
		float[] min = new float[]{- increment, - increment, -increment};
		AABBGeometry geometry = new AABBGeometry(max, min);
		setCollisionType(CollisionType.SOFT);
		setGeometry(geometry);
		
		// Prueba para geometrias compuestas
//		int increment = TITLE_SIZE / 2;		
//		float[] maxU = new float[]{increment,increment, increment};
//		float[] minU = new float[]{- increment, - increment, 0};
//		AABBGeometry geometryD = new AABBGeometry(maxU, minU);
//		
//		float[] maxD = new float[]{increment,increment,0};
//		float[] minD = new float[]{- increment, - increment, -increment};
//		AABBGeometry geometryI = new AABBGeometry(maxD, minD);
//		
//		
//		ArrayList<I_AABBGeometry> subElements = new ArrayList<I_AABBGeometry>();
//		subElements.add(geometryD);
//		subElements.add(geometryI);
//		I_AABBGeometry geometry = new ComplexGeometry(subElements );
	}
	
	
	public void init(int x , int y , int v) {		
		initialPosition = new int[]{x,y,0};		
		initialVelocity = new int[]{v,0,0};	
	}
	
	public void restart() {
		setPosition(initialPosition);
		setVelocity(initialVelocity);
		setRotation(initialRotation);
	}
	
	public void setInitialPosition(int[] position) {
		this.setPosition(position);
		this.initialPosition = position;
	}
	
	public void goTo(Direction direction) {
		if ( direction == Direction.EAST) {
			Log.i(TAG, "PACMANIA - GoTo Right");
		} else if ( direction == Direction.WEST) {
			Log.i(TAG, "PACMANIA - GoTo Left");
		} else if ( direction == Direction.NORT ) {
			Log.i(TAG, "PACMANIA - GoTo Up");
		} else if ( direction == Direction.SOUTH) {
			Log.i(TAG, "PACMANIA - GoTo Down");
		}
		super.goTo(direction);
	}
	
	public void jump() {
		PhysicsProperties request = getRequest();
		synchronized (request) {
			Log.i(TAG, "PACMANIA - Jump");
			request.setJump(true);
		}	
	}
}
