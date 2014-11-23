package es.upm.fi.f980092.tfc.gameengine.render.shaders.staticParticles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;

public class StaticParticleMesh implements I_Mesh {
	
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::StaticParticleMesh - ";
	
	protected List<Float> vertex = new ArrayList<Float>();
	protected HashMap<Float, HashMap<Float, Boolean>> visible = new HashMap<Float, HashMap<Float,Boolean>>();
	protected FloatBuffer  particleBuffer;
	
	public void addParticle( float x, float y) {
		vertex.add(x);
		vertex.add(y);
		vertex.add(0.2f);
		HashMap<Float, Boolean> visibleX = visible.get(x);
		if ( visibleX == null) {
			visibleX = new HashMap<Float, Boolean>();
			visible.put(x, visibleX);
		}
		Boolean v = visibleX.get(y);
		if (v == null) {
			visibleX.put(y, true);
		}	
	}
	
	public boolean hideParticle( float x, float y ) {
		
		boolean state = false;
		try {
			state = visible.get(x).get(y);
			if (state) {  
				visible.get(x).put(y, false);
				genBuffer();
			}
			
		} catch (Exception e) {
			Log.e(TAG,TAG2 + " There is not particle at [" + x + "," + y + "]");
		}
		return state;
	}
	
	public FloatBuffer getParticleBuffer() {
		load();
		return particleBuffer;
	}
	
	public void load() {
		if (particleBuffer == null) {
			genBuffer();
		}		
	}
	
	private void genBuffer() {
		
		try {
			int size = vertex.size() / 3;
			float[] particleArray = new float[ size  * 4];
			
			for (int i = 0 ; i < size; i++) {
				float x = vertex.get(3*i);    // x
				float y = vertex.get(3*i+1);  // y
				float z = vertex.get(3*i+2);  // z
				
				particleArray[4*i+0] = visible.get(x).get(y) ? 1.0f : 0.0f;
				particleArray[4*i+1] = x; 
				particleArray[4*i+2] = y; 
				particleArray[4*i+3] = z; 
			}
			particleBuffer = ByteBuffer.allocateDirect(particleArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			particleBuffer.position(0); 
			particleBuffer.put(particleArray);
		} catch (Exception e) {
			Log.e(TAG, TAG2 + "Imposible to create opengl buffer");			
		}
	}

	public int getNumParticles() {		
		return vertex.size() / 3;
	}

	public void restart() {
		for( HashMap<Float, Boolean> value : visible.values()) {
			for( Float key : value.keySet()) {
				value.put(key, true);
			}
		}
		genBuffer();
		
	}

}
