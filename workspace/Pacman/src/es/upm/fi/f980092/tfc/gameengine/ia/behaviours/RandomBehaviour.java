package es.upm.fi.f980092.tfc.gameengine.ia.behaviours;

import java.util.ArrayList;
import java.util.Random;

import es.upm.fi.f980092.tfc.gameengine.ia.I_Behaviour;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionEngine;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.TileUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.utils.MathUtils;



public class RandomBehaviour implements I_Behaviour {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + ":RandomBehaviour - ";
	
	private static Random random = new Random();
	
	public RandomBehaviour() {
		
	}

	public void restart() {
		
	}
	
	@Override
	public void move(A_SceneElement element, int step) {		
		
		int[] p = element.getPosition();		
		int tileSize = (int) element.getSceneRef().getScale();
		
		// Comprobamos si hemos llegado a un punto de interseccion

		Direction d = PhysicsUtils.getDirection2D( element.getVelocity());
		int movementAxe = ( d == Direction.NORT || d == Direction.SOUTH) ? PhysicsUtils.Y : PhysicsUtils.X;					// Direccion del movimiento actual
		int velocity = element.getVelocity()[movementAxe];

		int[] newPosition = PhysicsUtils.move1D(0, velocity, 0, 0, 0, step);
		int distance = Math.abs(newPosition[0]);		
		int[] finalPosition = MathUtils.sum(p, MathUtils.mul(distance, d.vector()));
		
		// Una vez que tenemos la nueva posicion hemos de verificar si hemos pasado por el 
		// siguente punto de paso

		int[] originTile = MathUtils.mul(tileSize, TileUtils.getTile(p, tileSize));
		
		boolean changeOfTile = (MathUtils.sustract(originTile, p,2)[movementAxe] * MathUtils.sustract(originTile, finalPosition,2)[movementAxe]) <= 0;
				
		if (changeOfTile) {
			PhysicsProperties selected = selecteNewMovement(element, distance);
			
			element.setPosition( selected.getPosition());
			element.setVelocity(selected.getVelocity());

			d = PhysicsUtils.getDirection2D( element.getVelocity());
			
			int[] rotation = new int[] {0,0,90};
			if ( d == Direction.EAST) {			 
				rotation = new int[] {0, 0, 90};
			} else if ( d == Direction.WEST) {			
				rotation = new int[] { 0, 0, 270};
			} else if ( d == Direction.NORT ) {			
				rotation = new int[] { 0, 0, 0};
			} else if ( d == Direction.SOUTH) {			
				rotation = new int[] { 0, 0, 180};
			}		
			element.setRotation(rotation);			
		} else {
			element.setPosition( finalPosition);
		}

	
	}
	
	private PhysicsProperties selecteNewMovement(A_SceneElement element, int distance) {
		ArrayList<A_SceneElement> collisionsElements = element.getSceneRef().getHardCollisionElements();
		
		int tileSize = (int) element.getSceneRef().getScale();
		I_Geometry geo = element.getInitialGeometry();
		int v = Math.abs(element.getVelocity()[0] + element.getVelocity()[1]);

		
		ArrayList<I_Geometry> collisionGeometries = new ArrayList<I_Geometry>();
		for( A_SceneElement e : collisionsElements) {
			collisionGeometries.add(e.getGeometry());
		}
		
		Direction d = PhysicsUtils.getDirection2D( element.getVelocity());
		int[] tile = MathUtils.mul(tileSize, TileUtils.getTile(element.getPosition(), tileSize));
		
		PhysicsProperties nort  = tryMovement(tile, Direction.NORT,  v, distance, geo, collisionGeometries);
		PhysicsProperties south = tryMovement(tile, Direction.SOUTH, v, distance, geo, collisionGeometries);
		PhysicsProperties west  = tryMovement(tile, Direction.WEST,  v, distance, geo, collisionGeometries);
		PhysicsProperties east  = tryMovement(tile, Direction.EAST,  v, distance, geo, collisionGeometries);
		
		ArrayList<PhysicsProperties> options = new ArrayList<PhysicsProperties>();
		
		if (nort != null && d != Direction.SOUTH) options.add(nort);
		if (south!= null && d != Direction.NORT ) options.add(south);
		if (east != null && d != Direction.WEST ) options.add(east);
		if (west != null && d != Direction.EAST ) options.add(west);
		
		if (options.size() == 1) {
			return options.get(0);
		} else {
			return options.get(random.nextInt( options.size()));
		}
	}
	
	private PhysicsProperties tryMovement(int[] tile, Direction direction, int v, int distance, I_Geometry geo, ArrayList<I_Geometry> collisionGeometries) {
		PhysicsProperties result = new PhysicsProperties();
		
		int[] empty = new int[]{0,0,0};
		result.setAceleration(empty);
		result.setAcelerationTime(empty);
		result.setJump(false);
		int[] newPosition = MathUtils.sum( tile, MathUtils.mul(distance, direction.vector()));
		result.setPosition( new int[]{newPosition[0],newPosition[1],0});		
		result.setVelocity( MathUtils.mul(v, direction.vector()));
		
		I_Geometry procesedGeometry = geo.translate( new float[] { result.getPosition()[0],result.getPosition()[1],result.getPosition()[2] } );
		ArrayList<I_Geometry> collisionsDetected = CollisionEngine.calculateCollisions(procesedGeometry, collisionGeometries);
		
		if (collisionsDetected.size() != 0) {
			//Movimiento no permitido debido a colisiones duras
			return null;
		}		
		return result;
	}
}
