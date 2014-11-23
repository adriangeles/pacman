package es.upm.fi.f980092.tfc.gameengine.ia.utils.pathfinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class PathFinder {

	/**
	 * Estructura de información donde se guarda un listado de los nodos origen 
	 * por cada uno de ellos un listado de los nodos destino y en cada uno
	 * la posición mas corta a la que tiene que dirigirse. 
	 */
	private HashMap<Vertex, HashMap<Vertex, List<Vertex>>> shortPath;
	private HashMap<Vertex, HashMap<Vertex, Vertex>> nextStep;
	private List<Vertex> vertexList;
	private boolean initialized = false;
	private final static String TAG = "TfcGameEngine";
	private final static String TAG2 = TAG + ":PathFinder - ";
	
	// Variables creadas para impedir invocacion del gc
	private Vertex v0 = new Vertex(0, 0, null);
	private Vertex v1 = new Vertex(0, 0, null);
	
	public PathFinder() {		 
		 vertexList = new ArrayList<Vertex>();
		 nextStep = new HashMap<Vertex, HashMap<Vertex,Vertex>>();
	}
	
	public void addVertex( int[] v) {		
		vertexList.add( new Vertex(v[0],v[1],null));
	}
		
	public void initialize() {
		Log.i(TAG, TAG2 + " Calculando información de caminos minimos");
		Date start = new Date();
		try {
			// Calculamos las conexiones 
			for( Vertex v1 : vertexList) {
				for( Vertex v2 : vertexList) {
					if (  	( v1.x == v2.x && ( Math.abs(v1.y - v2.y) == 1)) ||
							( v1.y == v2.y && ( Math.abs(v1.x - v2.x) == 1))) {
						v1.adjacent.add(v2);
					}
				}
			}

			// Ejecutamos algoritmo distktra por cada vertice origen
			shortPath = new HashMap<Vertex, HashMap<Vertex,List<Vertex>>>();
			
			for( Vertex v : vertexList) {
								
				HashMap<Vertex, List<Vertex>> data = executeAlgorithm(v,vertexList);
				shortPath.put(v, data);
				
				HashMap<Vertex, Vertex> step = new HashMap<Vertex, Vertex>();
				nextStep.put(v, step);
				
				for ( Vertex destVextex :  data.keySet()) {		
					if (data.get(destVextex).size() > 1  ) { 
						Vertex next = data.get(destVextex).get(1);
						step.put(destVextex, next);
					} else {
						step.put(destVextex, destVextex);
					}
				}
			}
			initialized = true;
			Date end = new Date();
			
			long duration = end.getTime() - start.getTime();
			Log.i(TAG, TAG2 + " Tiempo de calculo: " + Math.round(duration / 1000) + " seg");
		} catch (Exception e) {
			Log.e(TAG,TAG2 + "Initialize error" + e.getMessage());
		} 
			
	}
	
	public int[] getShortPath( int[] init, int[] end) {
		if ( initialized == false) {
			Log.e(TAG, TAG2 + " PathFinder no se inicializo previamente antes de su uso");
			initialize();
		}		
		v0.x = init[0];
		v0.y = init[1];
		v1.x = end[0];
		v1.y = end[1];
		
		HashMap<Vertex, Vertex> vp = nextStep.get(v0);
		if (vp != null) {
			Vertex v  = vp.get(v1); 
			if ( v != null) {
			return new int[]{v.x, v.y};
			} else {
				Log.e(TAG,TAG2 + "The end vertex [" + end[0] + "," + end[1] + "] does not exist" );
			}
		} else {
			Log.e(TAG,TAG2 + "The origin vertex [" + init[0] + "," + init[1] + "] does not exist" );
		}
		return init;
	}
	
	private HashMap<Vertex, List<Vertex>> executeAlgorithm(Vertex orig, final List<Vertex> vertices) throws Exception {
		
		HashMap<Vertex, List<Vertex>> bestpath = new HashMap<Vertex, List<Vertex>>();
		List<Vertex> pendientes = new ArrayList<Vertex>();
		pendientes.addAll(vertices);
		HashMap<Vertex, Integer> distance = new HashMap<Vertex, Integer>();
		
		try {
		
			// Inicialización de los pesos

			for( Vertex v : pendientes) {
				distance.put(v, ( v.equals(orig)) ?  0 : Integer.MAX_VALUE);
				bestpath.put(v, new ArrayList<Vertex>());
			}
			
			// Iteraciones

			Vertex currentVertex = orig;
			int    currentDistance = 0;
			
			while (currentVertex != null) {
				
				pendientes.remove(currentVertex);
				List<Vertex> currentPath = new ArrayList<Vertex>();
				currentPath.addAll(bestpath.get(currentVertex));
				currentPath.add(currentVertex);

				for ( Vertex adjVertex : currentVertex.adjacent) {

					if ( currentDistance + 1 < distance.get(adjVertex)) {
						distance.put(adjVertex, currentDistance + 1);
						bestpath.put(adjVertex, currentPath);
					}
				}

				// Obtener siguiente nodo a evaluar

				currentVertex = null;
				currentDistance = 0;

				for( Vertex v : pendientes) {
					if ( currentVertex == null  || ( distance.get(v) < currentDistance)) {
						currentVertex = v;
						currentDistance = distance.get(v);
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG,TAG + " Unable to generate disktra for v " + orig.x + orig.y);
			throw e;
		}
		return bestpath;
	}
	
	public void printDebug() {
		Log.d(TAG,TAG2 + "----------------------------------------------------------");
		for (Vertex origen : this.shortPath.keySet()) {
			
			String outFrom = "From: [" + origen.x + "," + origen.y + "] ";
			HashMap<Vertex, List<Vertex>> destinos = shortPath.get(origen);
			
			for( Vertex destino : destinos.keySet()) {
				String outTo = "to [" +  destino.x + "," + destino.y + "] ==> ";
				
				for( Vertex nodo: destinos.get(destino)) {		
					outTo += " [" + nodo.x + "," + nodo.y + "] -> ";
				}
				Log.v(TAG, TAG2 + outFrom + outTo + " [" + destino.x + "," + destino.y + "]");
			}
		}
		Log.d(TAG,TAG2 + "----------------------------------------------------------");
	}
}
