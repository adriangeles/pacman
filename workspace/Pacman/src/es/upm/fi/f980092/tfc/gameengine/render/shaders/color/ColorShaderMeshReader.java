package es.upm.fi.f980092.tfc.gameengine.render.shaders.color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;

public class ColorShaderMeshReader  {

    private static final String TAG = "TfcGameEngine";
    private static final String TAG2 = TAG + "::ColorShaderMeshReader ";
    
	private static String VERTEX = "v ";
    private static String TEXTURE = "vt ";
    private static String FACE = "f ";
    private static String SET_MATERIAL = "usemtl ";
    private static String SEPARATOR = " ";
    private static String LOAD_MATERIAL = "mtllib ";
    private static String FACE_SEPARATOR = "/";
   
    private static final int OPENGL_BUFFER_MAX_SIZE = 15; // MAX 42 ???
    
    public ColorShaderMesh read(String path) throws IOException {
    	Log.i(TAG, TAG2 + "Reading file" + path);
    	Date start = new Date();
    	ColorShaderMesh mesh = new ColorShaderMesh();
    	int submesh = -1;
    	int openglBuffer = 0;
    	try {
        FileReader fr = new FileReader(path + "/color.mesh");
        BufferedReader bf = new BufferedReader(fr);
        String materialId = null;
        String line;
        while ((line = bf.readLine()) != null) {
//        	Log.v(TAG, TAG2 + "Parsing Line: " + line);
        	
            if (line.startsWith(VERTEX)) {
            	String elements[] = line.split(SEPARATOR);
            	mesh.addVertex( Float.parseFloat(elements[1]), Float.parseFloat(elements[2]),  Float.parseFloat(elements[3]));
            } else if (line.startsWith(TEXTURE)) {
            	String elements[] = line.split(SEPARATOR);
            	mesh.addTextureVertex(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]));
            } else if (line.startsWith(FACE)) {
            	openglBuffer++;
            	if ( openglBuffer >= OPENGL_BUFFER_MAX_SIZE) {            		
            		submesh++;
            		mesh.addMaterialIndex(submesh, materialId); 
            		openglBuffer =0;
            	}
            	String elements[] = line.split(SEPARATOR);                        		
     	        for (int i = 1; i < elements.length; i++) {
     	            String subelements[] = elements[i].split(FACE_SEPARATOR);
     	            mesh.addIndex( submesh,  stringToInteger(subelements[0]),	stringToInteger(subelements[1]));
     	        }            
            } else if (line.startsWith(SET_MATERIAL)) {
            	submesh++;            	
            	materialId = line.split(SEPARATOR)[1];
            	mesh.addMaterialIndex(submesh, materialId);    
            } else if (line.startsWith(LOAD_MATERIAL)) {
                String mtlFilePath = line.split(SEPARATOR)[1];
                MtlFileReader mtlReader = new MtlFileReader();                
                try {
                	mtlReader.read( path , mtlFilePath, mesh);
                } catch (Exception e) {
                	Log.e(TAG,TAG2 + "Material not loaded: " + path , e);
                }
            } else {
            	// Log.v(TAG,TAG2 + "Ingored: " + line);
            }
        }
    	} catch (Exception e) {
    		Log.e(TAG,TAG2 + "Error al procesar el fichero: " + path);
    	}
        Date end = new Date();
        float duration = end.getTime() - start.getTime();
        Log.i(TAG,TAG2 + " Tiempo de calculo: " + Math.round(duration/1000) + "seg");
        return mesh;
    }
    
    private Integer stringToInteger(String s) {
        Integer result = null;
        try {
            result = new Integer(s);
        } catch (Exception e) {

        }
        return result;
    }
    
    public class MtlFileReader {


//        private static final String TAG = "TfcGAmeEngine";
    	
        private static final String NEWMTL = "newmtl ";
        private static final String SEPARATOR = " ";

        
        public void read(String path, String file, ColorShaderMesh mesh) throws IOException {

        	FileReader fr = new FileReader(path + "/" + file);              
        	BufferedReader bf = new BufferedReader(fr);
        	HashMap<String,Object> material = null;

        	String line;
        	while ((line = bf.readLine()) != null) {

        		if (line.startsWith(NEWMTL)) {
        			if (material != null)
        				mesh.addMaterial(material);
        			material = new HashMap<String, Object>();
        			material.put(ColorShaderMesh.ID, line.split(SEPARATOR)[1]);

        		} else if (line.startsWith(ColorShaderMesh.MAP_KD)) {
        			material.put(ColorShaderMesh.MAP_KD, path + "/" + line.split(SEPARATOR)[1]);
        		} else {
        			// Log.v(TAG,TAG2 + "Ingored: " + line);
        		}
        	}
        	if (material != null)
        		mesh.addMaterial(material);
        }
    }

}