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

package es.upm.fi.f980092.tfc.pacman.module.reader;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import es.upm.fi.f980092.tfc.pacman.activities.Boot;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;
import es.upm.fi.f980092.tfc.pacman.module.level.reader.LevelReader;

public class ModuleHandler extends DefaultHandler {

	private static final String TAG ="PACMAN# ModuleHandler";
	
	// XML TAG
	private static final String TAG_LEVEL     = "Level";
	private static final String TAG_MODULE	  = "Module";
		
	private static final String ATT_LEVEL_NAME = "name";
	private static final String ATT_MODULES_LIFES = "lifes";

	// PROPERTIES

	private String path;
	private ArrayList<Level> levels;
	private int lifes;
	private Boot log;

	
	public Boot getLog() {
		return log;
	}

	public void setLog(Boot log) {
		this.log = log;
	}

	public ArrayList<Level> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<Level> levels) {
		this.levels = levels;
	}

	public int getLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}

	public ModuleHandler(String path, Boot log) {
		super();
		this.path = path;
		this.log = log;
		levels = new ArrayList<Level>();		
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (TAG_MODULE.equals(qName)){
			lifes = Integer.parseInt(attributes.getValue(ATT_MODULES_LIFES)); 
		} else if (TAG_LEVEL.equals(qName)) {
			String levelName = attributes.getValue(ATT_LEVEL_NAME);
			try {						
				Level level = LevelReader.read( path+ "/" + levelName + "/", log);
				levels.add(level);
			} catch (Exception e) {
				log.setError();
				Log.e(TAG, "Unable to read the Level " + levelName);				
				throw new SAXException(e);
			}			
		} 
	}
}
