package es.upm.fi.f980092.tfc.gameengine.physics.quadtree;

import java.util.ArrayList;
import java.util.HashMap;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionDetectorFactory;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_CollisionDetector;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShaderMesh;

public class RenderQuadTreeBuilder {

	
	public static QuadTreeNode genQuadTree(TextureShaderMesh mesh) {
		QuadTreeNode quadtree = new QuadTreeNode();
		ArrayList<Object> subMeshes = new ArrayList<Object>();
		
		for ( int i = 0 ; i < mesh.getSize() ; i++) {
			subMeshes.add(i);
		}
		
		float[][] maxMin = genMaxMin(mesh, subMeshes);		
		
		quadtree.setGeometry(new AABBGeometry(maxMin[0],maxMin[1]));

		quadtree.setNodeElements(subMeshes);		
		splitNode(mesh,quadtree);		
		return quadtree;
	}


	@SuppressWarnings("unchecked")
	private static void splitNode(TextureShaderMesh mesh, QuadTreeNode node) {
		float[][] maxMin = genMaxMin(mesh, node.getNodeElements());
		node.setGeometry(new AABBGeometry(maxMin[0],maxMin[1]));
		I_AABBGeometry[]  geometries = splitGeometry((I_AABBGeometry) node.getGeometry());
		HashMap<I_Geometry, ArrayList<Object>> elementsDivision = new HashMap<I_Geometry, ArrayList<Object>>();

		for (I_AABBGeometry spatialGeo : geometries) {
			elementsDivision.put(spatialGeo, new ArrayList<Object>());
		}

		elementsDivision.put(node.getGeometry(), new ArrayList<Object>());

		for ( Object o : node.getNodeElements()) {
			Integer index = (Integer) o;

			I_AABBGeometry buffertGeo = mesh.getGeometry(index);

			int numOfCollision = 0;
			I_AABBGeometry lastCollision = null;

			for ( I_AABBGeometry spatialGeo : geometries) {
				I_CollisionDetector detector = CollisionDetectorFactory.getDetector(buffertGeo, spatialGeo);
				if (detector.existStaticCollision(buffertGeo, spatialGeo)) {
					numOfCollision++;
					lastCollision = spatialGeo;
				}
			}

			if ( numOfCollision == 1) {
				elementsDivision.get(lastCollision).add(o);
			} else {
				elementsDivision.get(node.getGeometry()).add(o);
			}
		}

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
					if ( subNode.getNodeElements().size() > 4) {
						splitNode(mesh,subNode);
					}
				}
			}
		}
	}

private static float[][] genMaxMin( TextureShaderMesh mesh, ArrayList<Object> subMeshes) {
	
	float[] max = new float[] { Float.MIN_VALUE , Float.MIN_VALUE, Float.MIN_VALUE };	
	float[] min = new float[] { Float.MAX_VALUE , Float.MAX_VALUE, Float.MAX_VALUE };
		
	for( Object index : subMeshes) {
		
		AABBGeometry geo = mesh.getGeometry((Integer)index);

		float[] eMax = geo.getMax();
		float[] eMin = geo.getMin();

		for (int i = 0; i< 3 ; i++ ) {
			max[i] =  max[i] < eMax[i] ? eMax[i] : max[i];
			min[i] =  min[i] > eMin[i] ? eMin[i] : min[i];
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

