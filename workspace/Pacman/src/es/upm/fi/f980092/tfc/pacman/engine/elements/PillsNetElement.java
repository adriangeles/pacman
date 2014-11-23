package es.upm.fi.f980092.tfc.pacman.engine.elements;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles.StaticParticleMesh;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public class PillsNetElement extends A_SceneElement {

	private static final String TAG = null;
	private static final String TAG2 = null;

	private static final float INCREMENT = 0.2f;

	// Propiedades
	private int visitbleNumberOfPills = 0;	// Numero visible de pastillas
	private int totalNumberOfPills = 0;	    // Numero total de pastillas
	
	// Constructores 
	
	public PillsNetElement() {
		super();
		setMesh(new StaticParticleMesh());
		setGeometry(new ComplexGeometry());
		setCollisionType(CollisionType.SOFT);
		setRenderizable(true);
	}

	public void init() {
		// No se requiere hacer nada
	}
	
	
	public void restart() {
		// Inicializamos la malla mostrando de nuevo todas las pastillas
		((StaticParticleMesh) getMesh()).restart();
		visitbleNumberOfPills = totalNumberOfPills;
	}
	
	/************************
	 * Getter && Setter
	 ***********************/
	
	public int getNumberOfPills() {
		return visitbleNumberOfPills;
	}
	
	/**********************
	 *  Metodos publicos
	 **********************/
	
	public void addPill(int x, int y) {
		if (getSceneRef() != null) {	
			float scale = getSceneRef().getScale();
			float[] max = new float[]{x + INCREMENT, y + INCREMENT,  INCREMENT  };
			float[] min = new float[]{x - INCREMENT, y - INCREMENT, -INCREMENT  };
			AABBGeometry geometry = new AABBGeometry(max, min);
			((StaticParticleMesh) getMesh()).addParticle(x/scale ,y/scale);
			((ComplexGeometry) getGeometry()).getGeometries().add(geometry);
			totalNumberOfPills++;
			visitbleNumberOfPills++;
		} else {
			Log.w(TAG, TAG2 + "PillsNet must be inside a level to know de render scale. Default scale is 1:1");
		}
	}	
	
	public boolean removePill(int x,int y) {
		float scale = getSceneRef().getScale();
		if (((StaticParticleMesh) getMesh()).hideParticle(x/scale, y/scale)) {
			visitbleNumberOfPills--;
			return true;
		}
		return false;
	}
}
