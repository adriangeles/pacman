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

import org.xml.sax.helpers.DefaultHandler;

import es.upm.fi.f980092.tfc.pacman.activities.Boot;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Esta clase contiene los elementos comunes para procesar un fichero xml 
 * con la informacion de un nivel del pacman. 
 * Es extendido por ShortLevelHandler y FullLevelHandler
 */
public class LevelHandler extends DefaultHandler {
	
    protected static final String TAG_WORLD = "WORLD";
    protected static final String TAG_WALL = "WALL";
    protected static final String TAG_SMALL_PILL = "SMALL_PILL";
    protected static final String TAG_PACMAN_START = "PACMAN_START";
    protected static final String TAG_SCENE =  "SCENE";
    protected static final String TAG_COMMENT = "#_";
    protected static final String TAG_GHOST = "GHOST";
    
    protected static final String ATT_COMMON_X = "x";
    protected static final String ATT_COMMON_Y = "y";
    protected static final String ATT_COMMON_MESH = "mesh";
    protected static final String ATT_COMMON_IA = "ia";
    protected static final String ATT_COMMON_SPEED = "speed";
    
    protected static final String ATT_WORLD_DESCRIPTION = "description";
    protected static final String ATT_WORLD_NAME = "name";
 
    // PROPERTIES
    protected Level level;     
    protected Boot log;
        
    
    public Level getWorld() {
    	return level;
    }
}