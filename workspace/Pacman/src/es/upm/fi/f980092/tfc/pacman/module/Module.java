/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.module;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Esta clase es responsable de almacenar la información de un módulo
 * que esta compuesta por:
 *    - Numero de vidas inciales
 *    - Niveles del juevo insertados en orden
 */
public class Module {
	
	/***************************
	 * ATRIBUTOS 
	 ***************************/
												// Número de vidas
	private String path;													// Directorio donde se ubica el modulo
	private ArrayList<Level> levels = new ArrayList<Level>();				// Orden de los distintos niveles
	private ArrayList<Boolean> unlockLevels = new ArrayList<Boolean>();		// Lista de niveles desbloqueados
	private int currentLevel = 0;
	
	/**************************
	 * 	CONTRUCTORES
	 **************************/
	
	public Module(String path) {		
		this.path = path; 
	}	
	
	/**************************
	 * GETTER AND SETTER
	 **************************/
	
	public String getPath() {
		return path;
	}
	
	/*******************************
	 * PUBLIC METHODS
	 ******************************/
	
	public ArrayList<Level> getLevels() {
		return levels;
	}
	
	/**
	 * Añade la información de un nuevo nivel al final de la lista de niveles
	 */
	public void addLevel(Level level) throws IOException, ParserConfigurationException, SAXException{		
		levels.add(level);
		unlockLevels.add(levels.size() == 1);
	}

	/**
	 * Determina si el nivel ha sido desbloqueado
	 * El primer nivel siempre esta desbloqueado
	 */
	public boolean isVisible(Level level) {
		int position = levels.indexOf(level);
		return  unlockLevels.get(position);
	}

	/**
	 * Obtiene el nivel actual si se devuelve null
	 * es que todos los niveles han sido finalizados
	 */
	public Level getCurrentLevel() {
		try {
			return levels.get(currentLevel);
		} catch (Exception e) {			
			return null;
		}
	}

	/**
	 * Establece el nivel de partida
	 */
	public void setCurrentLevel(int position) {
		currentLevel = position;		
	}
	
	/**
	 * Mediante esta función se realiza un cambio de nivel
	 */
	public Level goToNextLevel() {
		currentLevel++;
		if (currentLevel > levels.size())
			return null;
		else  {
			return getCurrentLevel();
		}
	}

	/**
	 * Desbloquea los niveles para poder ser seleccionados
	 * en la pantalla de selección de nivel
	 */
	public void unBlock(String[] visibleLevels) {

		for (int i = 1; i < levels.size(); i++) {
			Level level = levels.get(i);

			for (int j = 0; j < visibleLevels.length && unlockLevels.get(i) == false; j++) {
				if (visibleLevels[j].equals(level.getName())) {
					unlockLevels.set(i, true);
				}
			}
		}
	}
}