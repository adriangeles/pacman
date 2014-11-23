package es.upm.fi.f980092.tfc.gameengine.physics.utils;

import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsProperties;
import es.upm.fi.f980092.tfc.gameengine.utils.MathUtils;


public class PhysicsUtils {
	
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	
	public enum Direction {
		STATIC  (new int[]{ 0,  0,  0}),
		EAST  	(new int[]{ 1,  0,  0}), 
		WEST 	(new int[]{-1,  0,  0}),
		NORT	(new int[]{ 0,  1,  0}),
		SOUTH	(new int[]{ 0, -1,  0});
//		UP		(new int[]{ 0,  0,  1}),
//		DOWN	(new int[]{ 0,  0, -1});
		
		private final int[] v;
		
		private Direction(int[] v) {
			this.v = v;
		}
		
		public int[] vector() { return v; }
	}
	
	/**
	 * Calcula la posicion final de un elemento en N dimension
	 * @param p  Position 			
	 * @param v[]  Velocity 			
	 * @param a  Aceleration 		
	 * @param at Aceleration Time 	
	 * @param g  Gravity
	 * @param t	 Time 
	 */
	public static int[][] move( int[] p, int[] v, int[] a, int[] at, int[] g, int t) {

		int[][] resultado = new int[p.length][];

		for ( int i = 0 ; i < p.length ; i++) {
			resultado[i] = move1D(p[i], v[i], a[i], at[i], g[i], t);			
		}

		return resultado;
	}
	
	/**
	 * Calcula la posicion final de un elemento en 1 dimension
	 * @param p  Position 			
	 * @param v  Velocity 			
	 * @param a  Aceleration 		
	 * @param at Aceleration Time 	
	 * @param g  Gravity
	 * @param t	 Time 
	 */
	public static int[] move1D( int p, int v, int a, int at, int g, int t) {
		int pFinal; 
		int vFinal; 
		int aFinal; 
		int atFinal;
		
		if ( at != 0 && at < t ) {
			// Tiempo aceleracion < Tiempo del intervalo  ( 2 subintervalos)
	
			//    Intervalo 1:  a = aceleracion + gravedad   [ 0 ... tiempo aceleracion del objeto ]
			int t1 = at;				
			pFinal = Math.round((float) p + (float) v * (float) t1 + 0.5f * (a+g)/1000 * t1 * t1);
			vFinal = (int) (v + (g+a)/1000 * t1);
			aFinal = 0;
			atFinal = 0;
			//   Intervalo 2:  a = gravedad    [ tiempo aceleracion del objeto .... fin del frame ]
			int t2 = t - at;
			pFinal += vFinal * t2 + Math.round(0.5f * g/1000* t2 * t2);
			vFinal += t2 * g;				
		} else {				
			// Tiempo aceleracion > Tiempo del intervalo  ( 1 subintervalo )
			pFinal = Math.round((float) p + (float) v * (float) t + 0.5f * (a+g)/1000 * t * t);
			vFinal = Math.round( (v + (a+g)/1000*t ));
			atFinal = at-t;
			aFinal = a;
		}	
		  			
		if (atFinal <= 0) {
			aFinal = 0;
			atFinal = 0;
		}
		
		return  new int[]{pFinal, vFinal, aFinal, atFinal};
	}
	
	/**
	 * 
	 * @param p position
	 * @param d direction
	 * @param nd new directdion
	 * @param v velocity
	 * @param a aceleration
	 * @param at aceleration time
	 * @param g gravity
	 * @param t time
	 * @param tileSize Size of the tile in the grid
	 * @param tileOffset Offset, by default must be zero
	 * @return
	 */
	public static PhysicsProperties getNewPositionInGrid2D(int p[], Direction d, Direction rd, int v, int a, int at, int g, int t, int tileSize) {
		
		PhysicsProperties resultado = new PhysicsProperties();
		 
		int movementAxe = ( d == Direction.NORT || d == Direction.SOUTH) ? Y : X;					// Direccion del movimiento actual
		int requestMovementAxe = (rd == Direction.NORT || rd == Direction.SOUTH) ? Y : X ;			// Direccion del movimiento acutal
		
		int[] originTile = MathUtils.mul(tileSize, TileUtils.getTile(p, tileSize));
		
		// Calculamos el movimiento como si no hubiera peticion de cambio ya que despues lo usaremos para 
		// detectar si hemos cambiado de casilla
		int[] move1d = move1D(0,Math.abs(v),a,at,g,t);
		int distance = move1d[0];
		
		int[] finalPosition = MathUtils.sum(p, MathUtils.mul(distance, d.vector()));
		int[] finalVelocity = MathUtils.mul( move1d[1], d.vector());
		int[] finalAcceleration = MathUtils.mul( move1d[2], d.vector());
		int[] finalAccelerationTime = MathUtils.mul( move1d[3], d.vector());
				
		int increment = MathUtils.distance(originTile, finalPosition, 2);
		
		if ( rd != null && increment > 0) {			
			if ( movementAxe != requestMovementAxe || d == Direction.STATIC)  {	// Cambio de direccion

				// Calculamos si hemos cambiado de casilla y esto lo sabemos porque la distancia entre el origen y el destino cambia de sentido 
				// En el caso en que sea cero implica que estamos en un punto de giro
				boolean changeOfTile = (MathUtils.sustract(originTile, p,2)[movementAxe] * MathUtils.sustract(originTile, finalPosition,2)[movementAxe]) <= 0;
				boolean staticOnTile = originTile[0] == p[0] && originTile[1] == p[1];
				if (changeOfTile) {
					// Hay un cambio de casilla de referencia
					
					// Calculamos la nueva posicion añadiendo la distancia que nos hemos pasado del punto de referencia a la direccion correcta
					finalPosition = MathUtils.sum(originTile, MathUtils.mul(increment, rd.vector()),2);				
					finalVelocity = MathUtils.mul( move1d[1], rd.vector());
					finalAcceleration = MathUtils.mul( move1d[2], rd.vector());
					finalAccelerationTime = MathUtils.mul( move1d[3], rd.vector());
				} else if ( staticOnTile && d == Direction.STATIC) {
					// Estamos parados en una casilla de referencia
					finalPosition = MathUtils.sum(originTile, MathUtils.mul(distance, rd.vector()),2);				
					finalVelocity = MathUtils.mul( move1d[1], rd.vector());
					finalAcceleration = MathUtils.mul( move1d[2], rd.vector());
					finalAccelerationTime = MathUtils.mul( move1d[3], rd.vector());
					
				}

			} else { // Cambio de sentido
				finalPosition = MathUtils.sum(p, MathUtils.mul(distance, rd.vector()));
				finalVelocity = MathUtils.mul( move1d[1], rd.vector());
				finalAcceleration = MathUtils.mul( move1d[2], rd.vector());
				finalAccelerationTime = MathUtils.mul( move1d[3], rd.vector());
			}
		}
		
		resultado.setPosition( finalPosition );
		resultado.setVelocity(finalVelocity);
		resultado.setAceleration(finalAcceleration);
		resultado.setAcelerationTime(finalAccelerationTime);		
		
		return resultado;
			
	}
	
	public static PhysicsProperties getNewPositionInGrid2DwithFreeZ(int p[], Direction dGrid, Direction rdGrid, int vGrid, int aGrid, int atGrid, int vAxeZ, int aAxeZ, int atAxeZ, int gAxeZ, int t, int tileSize) {

		PhysicsProperties result = getNewPositionInGrid2D(p, dGrid, rdGrid, vGrid, aGrid, atGrid, 0, t, tileSize);
		int[] moveAxeZ = move1D(p[2], vAxeZ, aAxeZ, atAxeZ, gAxeZ, t);
		
		result.setPosition( new int[]{ result.getPosition()[0], result.getPosition()[1], moveAxeZ[0]});
		result.setVelocity( new int[]{ result.getVelocity()[0], result.getVelocity()[1], moveAxeZ[1]});
		result.setAceleration(new int[]{ result.getAceleration()[0], result.getAceleration()[1], moveAxeZ[2]});
		result.setAcelerationTime(new int[]{ result.getAcelerationTime()[0], result.getAcelerationTime()[1], moveAxeZ[3]});
		return result;
	}
	
	public static Direction getDirection2D( int[] velocity) {
		if ( velocity[0] == 0 && velocity[1] == 0 ) {
			return Direction.STATIC;
		} else if ( velocity[0] == 0 ) {
			return  ( velocity[1] > 0) ? Direction.NORT : Direction.SOUTH;
		} else if ( velocity[1] == 0 ) {
			return  ( velocity[0] > 0) ? Direction.EAST : Direction.WEST;
		} else {
			throw new RuntimeException("The element is not moving in a grid");
		}
	}
	
	/**
	 * Esta funcion devuelve el vector velocidad entre dos puntos que se encuentran
	 * a una unidad de distancia manhanttan.
	 * 
	 * @param origin Posicion inicial
	 * @param destiny Posicion final
	 * @param speed Velocidad del elemento
	 * @return
	 */
	public static int[] getVelocityVector( int[] origin, int[] destiny, int speed) {
		
		if ( origin[0] == destiny[0] ) {
			// Si la coordenada X es igual significa que nos movemos en el eje Y
			if ( origin[1] < destiny[1]) {
				// Nos movemos hacia arriba
				return new int[]{0, speed, 0};
			} else {
				// Nos movemos hacia abajo
				return new int[]{0, -speed, 0};
			}
		} else {
			// Al ser las coordenadas distintas nos movemos en el eje X 
			if ( origin[0] < destiny[0]) {
				// Nos movemos hacia arriba
				return new int[]{speed,0,0};
			} else {
				// Nos movemos hacia abajo
				return new int[]{-speed,0,0};
			}
		}			
	}
}
