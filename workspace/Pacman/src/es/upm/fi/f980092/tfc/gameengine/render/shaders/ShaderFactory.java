package es.upm.fi.f980092.tfc.gameengine.render.shaders;

import es.upm.fi.f980092.tfc.gameengine.render.shaders.animation.AnimatedShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.animation.AnimatedShaderMesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.color.ColorShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.color.ColorShaderMesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles.StaticParticleMesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles.StaticParticleShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShaderMesh;

public class ShaderFactory {

	private static TextureShader textureSh = new TextureShader();
	private static ColorShader colorSh = new ColorShader();
	private static AnimatedShader animatedSh = new AnimatedShader(); 
	private static StaticParticleShader staticParticleShader = new StaticParticleShader();
	
	
	public static I_Shader getShader(I_Mesh mesh) {
				
		if ( mesh instanceof TextureShaderMesh) {
			return textureSh;
		} else if ( mesh instanceof AnimatedShaderMesh) {
			return animatedSh;
		} else if ( mesh instanceof ColorShaderMesh) {
			return colorSh;
		} else if ( mesh instanceof StaticParticleMesh) {
			return staticParticleShader;
		} else {
			throw new IllegalArgumentException("Supporte mesh:  TextureShaderMesh and AnimationShaderMesh");
		}
	}
}
