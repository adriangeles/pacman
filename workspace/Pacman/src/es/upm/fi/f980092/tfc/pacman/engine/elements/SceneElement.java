/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 * Date: 02-may-2010
 */
package es.upm.fi.f980092.tfc.pacman.engine.elements;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.I_AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.ComplexGeometry;
import es.upm.fi.f980092.tfc.gameengine.physics.quadtree.ComplexGeometryQuadTreeBuilder;

/**
 * Esta clase determina una elemento del escenario, se determina como
 * un elemento estatico y con una geometrica cuadrada
 */
public class SceneElement extends A_PacmanElement {

	public static final String MeshId = "SceneMesh";
	
	public SceneElement() {
		super();
		setMeshId(MeshId);
		setScale(1.0f);		
		setAceleration(null);
		setVelocity(null);
		setAcelerationTime(null);
		setGeometry(  new ComplexGeometry());
		setPosition(null);
		setRefTile(null);
		setRotation(null);	
		setCollisionType(CollisionType.HARD);
		setRenderizable(true);
		
		// Creamos la geometria inicial que estara unicamente compuesta por el suelo		
		float[] max = new float[]{  Float.MAX_VALUE,  Float.MAX_VALUE, -5000};
		float[] min = new float[]{ -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE};
		AABBGeometry floor = new AABBGeometry(max, min);
		getComplexGeometry().getGeometries().add(floor);
		setCollisionType(CollisionType.HARD);
		
	}	
	
	public ComplexGeometry getComplexGeometry() {
		return (ComplexGeometry) super.getGeometry();
	}
	
	/**
	 * Este metodo es el encargado de inicializar el elemento realizando las siguientes acciones:
	 *    - Crear un QuadTree con la geometria total
	 */
	public void init() {
		this.setGeometricQuadTree(ComplexGeometryQuadTreeBuilder.genQuadTree((ComplexGeometry) getGeometry()));		
	}
	
	public void addGeometry( I_AABBGeometry geometry) {
		((ComplexGeometry) getGeometry()).getGeometries().add(geometry);
	}

	@Override
	public void restart() {
		
	}
	
	
}
