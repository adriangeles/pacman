package es.upm.fi.f980092.tfc.gameengine.physics.quadtree;

import java.util.ArrayList;
import java.util.HashMap;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionDetectorFactory;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_CollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;

public class ComplexGeometryQuadTreeBuilder {

	@SuppressWarnings("unchecked")
	public static QuadTreeNode genQuadTree( ComplexGeometry geo) {
		
		float[][] maxMin = genMaxMin(geo.getGeometries());		
		QuadTreeNode quadtree = new QuadTreeNode();
		quadtree.setGeometry(new AABBGeometry(maxMin[0],maxMin[1]));
		quadtree.setNodeElements( (ArrayList)geo.getGeometries());		
		splitNode(quadtree);		
		return quadtree;
	}
	
	
	@SuppressWarnings("unchecked")
	private static void splitNode( QuadTreeNode node) {
		// Establecemos la geometria del nodo
		float[][] maxMin = genMaxMin(node.getNodeElements());
		node.setGeometry(new AABBGeometry(maxMin[0],maxMin[1]));
		
		// Iteramos los elementos para saber a que subnodo pertenece 
		HashMap<I_Geometry, ArrayList<Object>> elementsDivision = new HashMap<I_Geometry, ArrayList<Object>>();
		elementsDivision.put(node.getGeometry(), new ArrayList());
		 
		I_Geometry[]  geometries = splitGeometry((I_AABBGeometry)node.getGeometry());
		
		for (I_Geometry spatialGeo : geometries) {
			elementsDivision.put(spatialGeo, new ArrayList());
		}
		// Comprobamos cada geometria a que subnodo pertenece
		for ( Object o : node.getNodeElements()) {
			 I_AABBGeometry elementGeo = (I_AABBGeometry) o;
			 
			 int numOfCollision = 0;
			 I_Geometry lastCollision = null;

			 for ( I_Geometry spatialGeo : geometries) {
				 I_CollisionDetector detector = CollisionDetectorFactory.getDetector(elementGeo, spatialGeo);
				 if (detector.existStaticCollision(elementGeo, spatialGeo)) {
					 numOfCollision++;
					 lastCollision = spatialGeo;
				 }
			 }
			 
			 // Si la geometria pertenece a un solo nodo, lo agregamos a el, si colisiona con mas nodo, lo
			 // agregamos al nodo principal
			 if ( numOfCollision == 1) {
				 elementsDivision.get(lastCollision).add(elementGeo);
			 } else {
				 elementsDivision.get(node.getGeometry()).add(elementGeo);
			 }
		 }
		 
		// Creamos los subnodos con la informacion obtenida
		 for (I_Geometry geo : elementsDivision.keySet()) {

			 ArrayList<Object> elementsInSpactialGeometry = elementsDivision.get(geo);
			 
			 if ( geo.equals(node.getGeometry())) {
				 node.setNodeElements(elementsInSpactialGeometry);
			 } else {				 
				 if (elementsInSpactialGeometry.size() > 0) {
					 QuadTreeNode subNode = new QuadTreeNode();
					 subNode.setGeometry(geo);
					 subNode.setNodeElements(elementsInSpactialGeometry);
					 node.getSubNodes().add(subNode);					 
					 if ( subNode.getNodeElements().size() > 9) {
						 splitNode(subNode);
					 }
				 }
			 }
		 }
	}
	
	private static float[][] genMaxMin( ArrayList<I_AABBGeometry> elements) {
		
		float[] max = null;
		float[] min = null; 
		
		for ( I_AABBGeometry geo : elements) {

			float[] eMax = geo.getMax();
			float[] eMin = geo.getMin();

			if ( max == null) {
				max = new float[3];
				min = new float[3];
				for (int i = 0; i< 3 ; i++ ) {
					max[i] = eMax[i];
					min[i] = eMin[i];
				}
			} else {

				for (int i = 0; i< 3 ; i++ ) {
					max[i] =  max[i] < eMax[i] ? eMax[i] : max[i];
					min[i] =  min[i] > eMin[i] ? eMin[i] : min[i];
				}
			}
		}
		return new float[][] { max, min };
	}


	/*  Representacion gráfica de vertices (Letras) y zonas (numeros)

	                   A - B - C 
	                   | 1 | 2 |
	                   D - E - F 
	                   | 3 | 4 |
	                   G - H - I 
	 */	
	private static I_AABBGeometry[] splitGeometry(I_AABBGeometry g) {
		float[] max = g.getMax();
		float[] min = g.getMin();
		float[] B = new float[]{ ( max[0] + min[0]) / 2,	max[1]					};							
		float[] C = new float[]{   max[0],					max[1]					};
		float[] D = new float[]{   min[0],				  ( max[1] + min[1]) / 2	};
		float[] E = new float[]{ ( max[0] + min[0]) / 2,  ( max[1] + min[1]) / 2	};
		float[] F = new float[]{   max[0],				  ( max[1] + min[1]) / 2	};
		float[] G = new float[]{   min[0],					min[1]					};
		float[] H = new float[]{ ( max[0] + min[0]) / 2,	min[1] 					};
					
		I_AABBGeometry geo1 = new AABBGeometry( new float[]{B[0],B[1], Float.MAX_VALUE}, new float[]{D[0],D[1],Float.MIN_VALUE});		
		I_AABBGeometry geo2 = new AABBGeometry( new float[]{C[0],C[1], Float.MAX_VALUE}, new float[]{E[0],E[1],Float.MIN_VALUE});		
		I_AABBGeometry geo3 = new AABBGeometry( new float[]{E[0],E[1], Float.MAX_VALUE}, new float[]{G[0],G[1],Float.MIN_VALUE});		
		I_AABBGeometry geo4 = new AABBGeometry( new float[]{F[0],F[1], Float.MAX_VALUE}, new float[]{H[0],H[1],Float.MIN_VALUE});
		
		return new I_AABBGeometry[] { geo1, geo2, geo3, geo4 };
	}
}

