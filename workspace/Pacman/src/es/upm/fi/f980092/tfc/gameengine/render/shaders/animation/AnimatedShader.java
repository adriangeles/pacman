package es.upm.fi.f980092.tfc.gameengine.render.shaders.animation;

import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Shader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderFactory;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.ShaderInfo;

/**
 * Este render es capaz de dibujar una secuencia de mallas representados en 
 * un único objeto, el AnimatedShaderMesh.
 */
public class AnimatedShader implements I_Shader {

	@Override
	public void draw(ShaderInfo info) {
		
		AnimatedShaderMesh mesh = (AnimatedShaderMesh) info.mesh;		
		mesh.move(info.step);
		I_Mesh currentMesh = mesh.getCurrentMesh();
		info.mesh = currentMesh;
		I_Shader shader = ShaderFactory.getShader(currentMesh);
		shader.draw(info);		
	}

	@Override
	public void load() {
		
	}

}
