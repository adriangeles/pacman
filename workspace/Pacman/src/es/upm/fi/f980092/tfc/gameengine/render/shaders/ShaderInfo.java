package es.upm.fi.f980092.tfc.gameengine.render.shaders;

import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;
import es.upm.fi.f980092.tfc.gameengine.render.ESTransform;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public class ShaderInfo {

	public A_SceneElement element;
	public I_Geometry cameraGeometry;
	public ESTransform perspective; 
	public ESTransform modelview;
	public int step; 
	public I_Mesh mesh;
}
