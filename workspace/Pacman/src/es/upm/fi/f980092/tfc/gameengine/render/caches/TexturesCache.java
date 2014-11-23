package es.upm.fi.f980092.tfc.gameengine.render.caches;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TexturesCache {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::TexturesCache";
	
	private static TexturesCache instance = null;
	
	private HashMap<String,Integer> cache    = null;
	private HashMap<String,String>  location = null;
		
	public static TexturesCache getInstance() {
		if (instance == null) {
			instance = new TexturesCache();
		}		
		return instance;
	}
	
	private TexturesCache() {
		cache = new HashMap<String, Integer>();
		location = new HashMap<String, String>();
	}
	
	public void addTexture(String id, String pathName) {
		location.put(id,pathName);
	}
	
	public Integer getTexture(String id) {
		try {
			return cache.get(id);		
		} catch (Exception e) {
			return null;
		}
	}

	private void loadTexture(String id, String pathName) throws FileNotFoundException {
		
        int[] textureRef = new int[1];
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);        
            
        GLES20.glGenTextures ( 1, textureRef, 0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureRef[0] );
   
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
        
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        cache.put(id, textureRef[0]);
        Log.i(TAG,TAG2 + " .... Texture: " + id + ":" + pathName);
    }
	
	

	public void loadTextures() {		
		Log.i(TAG,TAG2 + " Cargando texturas");
		Date start = new Date();
		for ( String id : location.keySet() ) {
			String pathName = location.get(id);
			try {
				loadTexture(id, pathName);
			} catch (Exception e) {
				Log.e(TAG,TAG2 + pathName + e.getMessage());
			}
		}		
		Date end = new Date();
		float duration = end.getTime() - start.getTime();
		Log.i(TAG, TAG2 + " Tiempo de calculo: " + Math.round(duration/1000) + "seg");
	}	
}
