package es.upm.fi.f980092.tfc.gameengine.ia.behaviours;

import java.util.ArrayList;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.ia.I_Behaviour;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionEngine;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.TileUtils;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.utils.MathUtils;

/**
 * El comportamiento por defecto se basa en moverse por el escenario en forma de cuadricula.
 * Se movera el objeto segun su velocidad/aceleracion/tiempo de acelaración y solo podra
 * cambiar su dirección cuando pase por un cruce de la cuadricula.
 * 
 * Se calcularán las colisiones duras con los distintos elementos del escenario retrocediendo
 * el elemento hasta el punto anterior a la colisión.
 */
public class PacmanBehaviour implements I_Behaviour {
	
	/***********************************
	 * Constantes
	 ***********************************/

	private static String TAG = "TfcGameEngine";
	private static String TAG2 = "TfcGameEngine::DefaultBehaviour - ";   	
			
	private int floor = 0;
	private int jump = 0;
	private int jumpAltitude  = 27000;
	private int jumpLongitude = 22000;
	// La ecuacion parabolica es  f(x) = ax^2 + b  
	// Los puntos de interes son  
	//     -> vertice f(0) = h
	//     -> interseccion con eje x    f( x0 ) = 0
	// 
	// Resolviendo el sistema de ecuacione tenemos que
	//    -> b = h 
	//    -> a = - h / x0^2
	//
	// Desplazando la ecuación sobre el eje x para que empiece en x = 0 tendriamos
	//
	//  f(x) =  - h/x0^2 * (x - x0)^2 - h donde 
	//    h  = altura maxima del salto
	//    x0 = longitud del salto entre dos
	//
	private float x0 = jumpLongitude / 2 ;
	private float b = jumpAltitude;
	private float a = - jumpAltitude / (x0 * x0);
	
	
	private int x = 0;
	
	
	public PacmanBehaviour(int floor) {
		this.floor = floor;
	}

	public void restart() {
		
	}
	
	public void move(A_SceneElement element, int step) {
		
		//######## Región de acceso compartido entre thread  ###########
		int[] rv  = null;
		int[] ra  = null;
		int[] rat = null;
		
		PhysicsProperties request = element.getRequest();
		
		synchronized (request) {
			rv = ( request.getVelocity() != null) ? request.getVelocity().clone() : null;
			 
			if (request.isJump()) {
				request.setJump(false);
				if ( jump == 0) {
					jump = 1;
				}
			}
			
			// Solo si el objeto esta en el suelo
			if ( element.getPosition()[2] == floor) {
				ra = (request.getAceleration() != null ) ? request.getAceleration().clone() :   null;
				rat = (request.getAcelerationTime() != null) ? request.getAcelerationTime().clone() :  null;
				// Anulamos la peticion de salto ya que al estar el objeto en el suelo sera atendida
				// Esto es necesario porque en otro caso estariamos aplicando una aceleracion 
				// en cada iteracion. 
				// No ocurre lo mismo con la velocidad por lo que no hace falta modificarla en el request
				request.setAceleration(null);
				request.setAcelerationTime(null);
			}
		}
		 
		//##############################################################
		
		int tileSize = element.getSceneRef().getProperties().getTileSize();
		int[] g = element.getSceneRef().getProperties().getGravity();
		ArrayList<A_SceneElement> collisionsElements = element.getSceneRef().getHardCollisionElements();
		
		ArrayList<I_Geometry> collisionGeometries = new ArrayList<I_Geometry>();
		for( A_SceneElement e : collisionsElements) {
			collisionGeometries.add(e.getGeometry());
		}
		
		int[] p = element.getPosition();
		I_Geometry geo = element.getInitialGeometry();
		// Podemos sumar las velocidades/aceleraciones porque una de ellas es siempre cero
		int vGrid = rv == null ? element.getVelocity()[0] + element.getVelocity()[1] : rv[0] + rv[1];		
		int aGrid  = ra == null ? element.getAceleration()[0] + element.getAceleration()[1] : ra[0] + ra[1];
		int atGrid = rat == null ? element.getAcelerationTime()[0] + element.getAcelerationTime()[1] : rat[0] + rat[1];
		
		int vAxeZ = element.getVelocity()[2];
		int aAxeZ = ra == null ? element.getAceleration()[2] : ra[2];
		int atAxeZ = rat == null ? element.getAcelerationTime()[2]: rat[2];
		int gAxeZ = g[2];
		
		Direction dGrid = PhysicsUtils.getDirection2D( element.getVelocity());
		Direction rdGrid = rv == null ? null : PhysicsUtils.getDirection2D(rv);
		
		PhysicsProperties finalState = null;
		try { 
			// Intentamos movernos en la direccion solicitada			
			finalState = tryMovement(p, dGrid, rdGrid, vGrid, aGrid, atGrid, vAxeZ, aAxeZ, atAxeZ, gAxeZ, step, tileSize, geo, collisionGeometries, false);
		} catch (Exception e1 ) {
			// Como no hemos podido movernos en la direccion solicitada seguimos el movimiento normal			
			try { 
				finalState = tryMovement(p, dGrid, dGrid, vGrid, aGrid, atGrid, vAxeZ, aAxeZ, atAxeZ, gAxeZ, step, tileSize, geo, collisionGeometries, false);	
			} catch (Exception e2) {
				try {
				// Si tampoco nos podemos mover en la posicio inicial nos quedamos en la posicion en la casilla de referencia
					finalState = tryMovement(p, dGrid, dGrid, vGrid, aGrid, atGrid, vAxeZ, aAxeZ, atAxeZ, gAxeZ, step, tileSize, geo, collisionGeometries, true);
				} catch (Exception e) {
					// Esto no deberia ocurrir nunca
					Log.e(TAG,TAG2 + "Error no controlado en el pacman behabiour");
				}
			}			
		}		
		if ( finalState != null) {
			element.setPosition( finalState.getPosition());
			element.setVelocity( finalState.getVelocity());		
			element.setAceleration(finalState.getAceleration());
			element.setAcelerationTime(finalState.getAcelerationTime());
			int[] rotation = new int[]{0,0,0};
			Direction direction  = PhysicsUtils.getDirection2D(finalState.getVelocity());
			if ( direction == Direction.EAST) {			 
				rotation = new int[] {0, 0, 90};
			} else if ( direction == Direction.WEST) {			
				rotation = new int[] { 0, 0, 270};
			} else if ( direction == Direction.NORT ) {			
				rotation = new int[] { 0, 0, 0};
			} else if ( direction == Direction.SOUTH) {			
				rotation = new int[] { 0, 0, 180};
			}			

			element.setRotation(rotation);			
		}
		
		// Tratamiento del salto
		int[] finalPosition = element.getPosition();
		if (jump != 0) {
			
			// Salto lineal 
//			int increment = step * Math.abs(vGrid) * ( 2 * jumpAltitude / jumpLongitude);
//			
//			if ( jump == 1) {
//				finalPosition[2] = p[2] +  increment;
//				if ( finalPosition[2] >= jumpAltitude) {
//					finalPosition[2] = jumpAltitude;
//					jump = 2;
//				}
//			} else {
//				finalPosition[2] = p[2] - increment;
//				if (finalPosition[2] < 0 ) {
//					finalPosition[2] = 0;
//					jump = 0;
//				}
//			}
			
			// Salto parabolico 
			
			x += step * Math.abs(vGrid);
			
			float y = a * (x-x0)* (x-x0) + b;
			if ( y < 0 ) { 
				y = 0;
				jump = 0;
				x = 0;
			}
			finalPosition[2] = Math.round(y);
			Log.i(TAG,TAG2 + " f(" + x + ") = " + y);
		} 
		
		element.setPosition(finalPosition);
	}
	
	
	private PhysicsProperties tryMovement(int p[], Direction dGrid, Direction rdGrid, int vGrid, int aGrid, int atGrid, int vAxeZ, int aAxeZ, int atAxeZ, int gAxeZ, int t, int tileSize, I_Geometry geo, ArrayList<I_Geometry> collisionGeometries,boolean resolveCollision) throws Exception {

		PhysicsProperties newState = PhysicsUtils.getNewPositionInGrid2DwithFreeZ(p, dGrid, rdGrid, vGrid, aGrid, atGrid, vAxeZ, aAxeZ, atAxeZ, gAxeZ, t, tileSize);		
		// Corregimos la posicion del objetos si cae por el umbral de suelo definido para el comportamiento
		if (newState.getPosition()[2] < floor) {
			newState.getPosition()[2] = 0;
			newState.getVelocity()[2] = 0;  
			newState.getAceleration()[2] = 0;
			newState.getAcelerationTime()[2] = 0;
		}
				
		I_Geometry procesedGeometry = geo.translate( new float[] { newState.getPosition()[0],newState.getPosition()[1],newState.getPosition()[2] } );
		ArrayList<I_Geometry> collisionsDetected = CollisionEngine.calculateCollisions(procesedGeometry, collisionGeometries);
		
		if (collisionsDetected.size() != 0) {
			//Movimiento no permitido debido a colisiones duras
			if ( resolveCollision) {
				// Si no hemos hecho un cambio de direccion implica que nos hemos de quedar estaticos en la 
				// casilla de referencia
				int[] tile = MathUtils.mul(tileSize, TileUtils.getTile(p, tileSize));
				newState.getPosition()[0] = tile[0];
				newState.getPosition()[1] = tile[1];
			} else {
				throw new Exception("Error al calcular el movimiento");
			}
		}		
		return newState;
	}
	
}
