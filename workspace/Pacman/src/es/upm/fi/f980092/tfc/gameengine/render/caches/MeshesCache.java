package es.upm.fi.f980092.tfc.gameengine.render.caches;

import java.util.HashMap;

import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

public class MeshesCache {
	
	private String TAG = "TfcGameEngine";
	private String TAG2 = TAG + "::MeshesCache";
	
	private HashMap<String, I_Mesh> cache;	
	private static MeshesCache instance = null;
	

	public static MeshesCache getInstance() {
		if (instance == null) 
			instance = new MeshesCache();
		return instance;
	}
	
	private MeshesCache() {
		cache = new HashMap<String, I_Mesh>();
	}
	
	public I_Mesh getMesh(String id) {
		return cache.get(id);   
	}
	
	public void add(String id, I_Mesh mesh) {
		cache.put(id, mesh);
	}
	
	public void clear() {
		cache.clear();
	}
}