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
package es.upm.fi.f980092.tfc.pacman.module.level.reader;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.aabb.geometries.AABBGeometry;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.MeshFileReader;
import es.upm.fi.f980092.tfc.pacman.activities.Boot;
import es.upm.fi.f980092.tfc.pacman.engine.elements.GhostElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.PillsNetElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.SceneElement;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Esta clase es usada por el lector de escenarios para parsear 
 * el fichero xml que contiene la estructura de un escenario.
 */
public class FullLevelHandler extends LevelHandler {

	private static final String TAG = "PACMAN";
	private static final String TAG2 = TAG + ":FullLevelHandler";
	
    // STATIC VALUES
    private static int DEFAULT_PACMAN_SPEED = 20;
    private static int SCALE = 10000;
    
    // PROPERTIES
        
    public FullLevelHandler(String path, Boot log) {    	
    	level = new Level(path);    	
    	this.log = log;
    }
     
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	
    	SceneElement scene = level.getScene();
    	PillsNetElement pillsNet = level.getPills();
    	
    	if (TAG_COMMENT.equalsIgnoreCase(qName)) {
    		//Se ignoran los comenetarios en el procesamiento del fichero
    	} else if (TAG_WORLD.equalsIgnoreCase(qName)) {    		
    		level.description = attributes.getValue(ATT_WORLD_DESCRIPTION);
    		level.name =  attributes.getValue(ATT_WORLD_NAME);    		
    	} else if (TAG_SCENE.equalsIgnoreCase(qName)) {
    		
    		// Procesamos la informacion de la malla del escenario
    		String meshObjFile = attributes.getValue(ATT_COMMON_MESH);
			try {
				I_Mesh mesh = MeshFileReader.readMesh(level.getPath(),log);
				scene.setMesh(mesh);
			} catch (IOException e) {
				Log.e(TAG, TAG2 + "No se ha podido procesar la malla del fichero: " + level.getPath() + "/" + meshObjFile);
			}    		
        } else if (TAG_WALL.equalsIgnoreCase(qName)) {        	
        	int x = SCALE * Integer.parseInt(attributes.getValue(ATT_COMMON_X));
        	int y = SCALE * Integer.parseInt(attributes.getValue(ATT_COMMON_Y));        	
        	int increment = SCALE / 2;		
    		float[] max = new float[]{ x + increment, y + increment,  30000};
    		float[] min = new float[]{ x - increment, y - increment, -30000};
    		AABBGeometry geometry = new AABBGeometry(max, min);
    		scene.addGeometry(geometry);   		
        } else if (TAG_SMALL_PILL.equalsIgnoreCase(qName)) {
        	int x = Integer.parseInt(attributes.getValue(ATT_COMMON_X));
        	int y = Integer.parseInt(attributes.getValue(ATT_COMMON_Y));       	
        	level.getPathFinder().addVertex( new int[]{x,y});
        	pillsNet.addPill(SCALE *x,SCALE *y);
        } else if (TAG_PACMAN_START.equalsIgnoreCase(qName)){
        	int x = Integer.parseInt(attributes.getValue(ATT_COMMON_X));
        	int y = Integer.parseInt(attributes.getValue(ATT_COMMON_Y));
        	level.getPacman().init(SCALE * x, SCALE * y, DEFAULT_PACMAN_SPEED);
        	level.getPathFinder().addVertex( new int[]{x,y});
        } else if (TAG_GHOST.equalsIgnoreCase(qName)) {
        	int x = SCALE * Integer.parseInt(attributes.getValue(ATT_COMMON_X));
        	int y = SCALE * Integer.parseInt(attributes.getValue(ATT_COMMON_Y));
        	String idMesh = attributes.getValue(ATT_COMMON_MESH);
        	int v = Integer.parseInt(attributes.getValue(ATT_COMMON_SPEED));
        	int ia = Integer.parseInt(attributes.getValue(ATT_COMMON_IA));
        	// Modififcamos la escala para que se vea bien como ha exisitdo colision
        	GhostElement ghost = new GhostElement(x, y, idMesh, v, ia, SCALE-100, level.getPacman());
        	level.addGhost(ghost);
        }
    }


	@Override
	public void endDocument() throws SAXException {
		super.endDocument();		
		log.updateLog("     Path:     ", false);		
		level.init();
		log.setOk();
	}    
    
    
}