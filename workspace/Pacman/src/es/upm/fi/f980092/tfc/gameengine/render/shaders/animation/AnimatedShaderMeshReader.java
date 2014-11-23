package es.upm.fi.f980092.tfc.gameengine.render.shaders.animation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.MeshFileReader;
import es.upm.fi.f980092.tfc.pacman.activities.Boot;

public class AnimatedShaderMeshReader {

	  private static final String TAG = "TfcGameEngine";
	  private static final String TAG2 = TAG + "::AnimatedShaderMeshReader ";
	    
	  public AnimatedShaderMesh read(String path, Boot log) throws IOException {
		  Log.d(TAG, TAG2 + "Reading file" + path);

		  FileReader fr = new FileReader(path + "/animated.mesh");
		  BufferedReader bf = new BufferedReader(fr);
		  AnimatedShaderMesh mesh = new AnimatedShaderMesh();
		  
		  String line;
		  
		  while ((line = bf.readLine()) != null) {
			  Log.v(TAG, TAG2 + "Parsing Line: " + line);

			  String[] data = line.split("/");
			  if ( data.length != 2) {
				  Log.e(TAG, TAG2 + "Syntax error reading line: " + line);
			  } else {
				  I_Mesh frameMesh = MeshFileReader.readMesh( path + "/" + data[0], log);
				  Integer duration = Integer.parseInt(data[1]);
				  mesh.addMesh(frameMesh, duration);
			  }
		  }
		  return mesh;
	  }
}