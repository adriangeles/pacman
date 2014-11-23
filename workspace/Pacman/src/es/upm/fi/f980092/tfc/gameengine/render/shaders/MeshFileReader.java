package es.upm.fi.f980092.tfc.gameengine.render.shaders;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.caches.TexturesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.animation.AnimatedShaderMeshReader;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShaderMesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.texture.TextureShaderMeshReader;
import es.upm.fi.f980092.tfc.gameengine.utils.Counter;
import es.upm.fi.f980092.tfc.pacman.activities.Boot;

public class MeshFileReader {
	
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + ":MeshFileReader - ";
	

	private static void escanearDirectorio(File mf) {

		while (mf.exists()) {
			
			for (File f : mf.listFiles()) {
				if (f.isFile()) {
					Log.i("AAR", f.getAbsolutePath());
				} else if ( f.isDirectory()) {
					escanearDirectorio(f);
				} else {
					Log.i("AAR"," NO SE LO QUE ES");
				}
			}
		}
	}


	public static I_Mesh readMesh(String path, Boot log) throws IOException {
		Log.i(TAG,TAG2 + " Reading mesh at dir " +  path );

		// Escanear directorios
		File mf = new File(path);
//		escanearDirectorio(mf);

		
		Integer counterId = Counter.initCounter();
		I_Mesh mesh = null;
		if ( new File(path + "/animated.mesh").exists()) {
			
			mesh = new AnimatedShaderMeshReader().read(path,log);
			
		} else if ( new File(path + "/texture.mesh").exists()) {

			TextureShaderMesh textureMesh = new TextureShaderMeshReader().read(path,log); 		
			textureMesh.load();
			HashMap<String, HashMap<String, Object>> materials = textureMesh.getAllMaterial();

			for (String key : materials.keySet() ) { 
				HashMap<String, Object> material = materials.get(key);
				String textureId = (String) material.get(TextureShaderMesh.ID);
				String texturePath = (String) material.get(TextureShaderMesh.MAP_KD);
				TexturesCache.getInstance().addTexture(textureId + ":" + TextureShaderMesh.MAP_KD, texturePath);					
			}
			mesh = textureMesh;			
		} else {
			throw new IOException("There is not file with name 'animated.mesh' or 'texture.mesh' in the folder: " + path);
		}

		long time = Counter.stopCounter(counterId);  
		Log.i(TAG,TAG2 + "Readed mesh at " +  path + " in " + time + "seg");
		Counter.remove(counterId);
		
		return mesh;
	}

}