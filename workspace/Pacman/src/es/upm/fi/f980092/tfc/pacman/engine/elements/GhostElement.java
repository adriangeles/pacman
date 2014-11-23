package es.upm.fi.f980092.tfc.pacman.engine.elements;

import es.upm.fi.f980092.tfc.gameengine.ia.I_Behaviour;
import es.upm.fi.f980092.tfc.gameengine.ia.behaviours.RandomBehaviour;
import es.upm.fi.f980092.tfc.gameengine.ia.behaviours.SearchBehaviour;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

public class GhostElement extends A_PacmanElement {

//	private static final String TAG = "PACMAN# GhostElement";
	private int[] initialPosition;
	                                
	public GhostElement(int x, int y, String meshId, int v, int ia, int size, A_SceneElement target) {
		super();
		
		I_Behaviour behaviour = new RandomBehaviour();
		setMeshId(meshId);
		if ( ia == 1 ) {
			behaviour = new SearchBehaviour(target, ((Level)target.getSceneRef()).getPathFinder());			
		}
		
		setBehaviour(behaviour);
		setRenderizable(true);				
		
		int[] tile = new int[]{x,y,0};
		int[] velocity = new int[]{v,0,0};
		
		
		float increment = size / 2;		
		float[] max = new float[]{increment,increment,increment};
		float[] min = new float[]{- increment, - increment, -increment};
		AABBGeometry geometry = new AABBGeometry(max, min);
		setGeometry(geometry);
		setCollisionType(CollisionType.SOFT);
		setScale(0.5f);
		setPosition(new int[]{x,y,0});
		initialPosition = getPosition();
		setVelocity(velocity);
		setRotation(new int[]{0,0,0});
		setRefTile(tile);		
		setDynamic(true);
	}
	
	public void init() {
		super.init();
	}

	@Override
	public void restart() {
		setPosition(initialPosition);
		getBehaviour().restart();
	}
}
