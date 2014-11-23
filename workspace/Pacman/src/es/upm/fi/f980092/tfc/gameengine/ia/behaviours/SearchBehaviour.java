package es.upm.fi.f980092.tfc.gameengine.ia.behaviours;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.ia.I_Behaviour;
import es.upm.fi.f980092.tfc.gameengine.ia.utils.pathfinder.PathFinder;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.TileUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.utils.MathUtils;



public class SearchBehaviour implements I_Behaviour {

	private static final String TAG = "TfcGameEngine";

	private static final String TAG2 = TAG + ":SearchBehaviour - ";

	private A_SceneElement target;			// Elemento que esta persiguiendo

	private int[] previousPoint;			// Ultimo punto por el que hemos pasado  
	private int[] nextPoint;				// Proximo punto por el que hemos de pasar			
	private int[] finalPoint;				// Punto final por el que hay que pasar
	private PathFinder pathfinder;			// Mapa de caminos mas cortos
	private RandomBehaviour randowBehaviur = new RandomBehaviour();
	
	public SearchBehaviour(A_SceneElement target,PathFinder pathfinder) {
		this.target = target;
		this.pathfinder = pathfinder;
	}

	public void restart() {
		previousPoint = null;
	}
	
	@Override
	public void move(A_SceneElement element, int step) {		
		try {
			int speed = Math.abs(element.getVelocity()[0] + element.getVelocity()[1]);
			int scale = (int)target.getSceneRef().getScale();
			// Inicialmente obtenemos la posicion del objetivo y nos dirijiremos 
			// hacia él		
			if ( previousPoint == null) {
				previousPoint = TileUtils.getTile(element.getPosition(),scale);				
				finalPoint = TileUtils.getTile(target.getPosition(),scale);	
				nextPoint = pathfinder.getShortPath(previousPoint, finalPoint);
				element.setVelocity(PhysicsUtils.getVelocityVector(previousPoint, nextPoint, speed));
			}

			// Calculamos la nueva posicion del elemento siguiendo la direcion/sentido/velocidad actual
			Direction d = PhysicsUtils.getDirection2D( element.getVelocity());
			int movementAxe = ( d == Direction.NORT || d == Direction.SOUTH) ? PhysicsUtils.Y : PhysicsUtils.X;					// Direccion del movimiento actual
			int position = element.getPosition()[movementAxe];
			int velocity = element.getVelocity()[movementAxe];

			int[] newPosition = PhysicsUtils.move1D(position, velocity, 0, 0, 0, step);

			// Una vez que tenemos la nueva posicion hemos de verificar si hemos pasado por el 
			// siguente punto de paso

			boolean changeOfTile = ((newPosition[0] - ( scale * nextPoint[movementAxe])) * 
					(element.getPosition()[movementAxe] - (scale * nextPoint[movementAxe]))) < 0;

			// Comprobamos si hemos cambiado de casilla de referencia 
			if (changeOfTile) {

				// Como hemos pasado por el siguiente punto hemos de calcular el nuevo punto
				int[] newFinalPoint = TileUtils.getTile(target.getPosition(),scale);
				int[] newNextPoint = pathfinder.getShortPath(nextPoint, newFinalPoint);

				int[] oldVelocityVector = PhysicsUtils.getVelocityVector(previousPoint, nextPoint, 1);
				int[] newVelocityVector = PhysicsUtils.getVelocityVector(nextPoint, newNextPoint, 1);

				boolean isGoingBack = ( oldVelocityVector[0] != 0 && oldVelocityVector[0] * newVelocityVector[0] < 0 ) || ( oldVelocityVector[1] != 0 && oldVelocityVector[1] * newVelocityVector[1] < 0 ); 

				if (isGoingBack) {
					previousPoint = nextPoint;
					nextPoint = pathfinder.getShortPath(nextPoint, finalPoint);					
				} else {
					// Reiniciamos el algoritmo de busqueda
					randowBehaviur.move(element, step);
					previousPoint = null;
				}

				int increment = Math.abs( newPosition[0] - previousPoint[movementAxe] * scale );

				newVelocityVector = PhysicsUtils.getVelocityVector(previousPoint, nextPoint, 1);
				d = PhysicsUtils.getDirection2D( newVelocityVector);
				movementAxe = ( d == Direction.NORT || d == Direction.SOUTH) ? PhysicsUtils.Y : PhysicsUtils.X;

				// Calculamos la nueva posicion añadiendo la distancia que nos hemos pasado del punto de referencia a la direccion correcta								

				newPosition = MathUtils.sum( MathUtils.mul(scale, previousPoint), MathUtils.mul(increment, newVelocityVector),2);				
				element.setPosition(new int[]{newPosition[0],newPosition[1], element.getPosition()[2]});
				element.setVelocity(MathUtils.mul(speed,newVelocityVector));

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
				// No hemos cambiado de celda por lo que la nueva posicion es la final y ademas
				// el vector velocidad sigue siendo el mismo.
				int[] currentPosition = new int[]{element.getPosition()[0],element.getPosition()[1],element.getPosition()[2]};
				currentPosition[movementAxe] = newPosition[0];
				element.setPosition(currentPosition);
			}			
		} catch (Exception e) {
			
		}
	}
}
