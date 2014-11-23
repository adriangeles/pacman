package es.upm.fi.f980092.tfc.pacman;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.preference.PreferenceManager;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.render.caches.MeshesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.I_Mesh;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.MeshFileReader;
import es.upm.fi.f980092.tfc.gameengine.sound.SoundEngine;
import es.upm.fi.f980092.tfc.pacman.activities.Boot;
import es.upm.fi.f980092.tfc.pacman.activities.Preferences;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;
import es.upm.fi.f980092.tfc.pacman.module.reader.ModuleHandler;

public class GameContext {

	public static final String PACMAN_MESH      = "Pacman";
	public static final String BLUE_GHOST_MESH  = "BlueGhost";
	public static final String GREEN_GHOST_MESH = "GreenGhost";
	public static final String RED_GHOST_MESH   = "RedGhost";
	public static final String PINK_GHOST_MESH  = "PinkGhost";
	
	private static final String PACMAN_MESH_FILEPATH      = "meshes/pacman2";
	private static final String BLUE_GHOST_MESH_FILEPATH  = "meshes/blueghost/";
	private static final String GREEN_GHOST_MESH_FILEPATH = "meshes/greenghost/";
	private static final String RED_GHOST_MESH_FILEPATH   = "meshes/redghost/";
	private static final String PINK_GHOST_MESH_FILEPATH  = "meshes/pinkghost/";
	
	public static Context context = null; 
	
	private static boolean physicsOn = true;
	private static boolean renderOn = true;
	private static boolean cameraModeOn = false;
	private static boolean renderOptimizationOn = false;
			
	private static int initialLifes = 3;		
	private static ArrayList<Level> levels = new ArrayList<Level>();				// Orden de los distintos niveles
	private static ArrayList<Boolean> unlockLevels = new ArrayList<Boolean>();		// Lista de niveles desbloqueados
	
	// Datos actuales de la partida
	private static int currentLevelPosition = 0;		// Posicion dentro del array de levels del nivel en que se esta jugando la partida actual
	private static int lifes = 3;						// Numero de vidas que tiene el jugador en la partida actual
	private static int score = 0;						// Puntuacion de la partida actual
	
	
			
	public static void load(Context context, Boot log) throws Exception {
		GameContext.context = context;
		updateLog(log,"PacmanConfig encontrado:  ", false);
		
		String sdcardDir = GameContext.getGamePath();
		File dir = new File(sdcardDir);
		if (dir.exists()) {
			log.setOk();
		} else { 
			log.setError();
			throw new Exception("No se encuentra el directorio: " + sdcardDir);
		}

		// Cargando musica		
		updateLog(log,"Cargando sonidos:\n", false);
		SoundEngine soundEngine = SoundEngine.init(context, 3 );  
		updateLog(log,"   Chomp ", false);
        soundEngine.addSoundFx(R.raw.pacman_chomp);
        log.setOk();
        updateLog(log,"   Death ", false);
        soundEngine.addSoundFx(R.raw.pacman_death);
        log.setOk();
                
		// Cargando texturas por defecto
		log.updateLog("Cargando texturas...\n",false);
		
		log.updateLog("\tTextura pacman:   \n",false);
		I_Mesh pacmanMesh = MeshFileReader.readMesh(getGamePath() + PACMAN_MESH_FILEPATH, log);

		if (pacmanMesh != null) {
			MeshesCache.getInstance().add(PACMAN_MESH, pacmanMesh);
		}
		
		log.updateLog("\tTextura fantasmas: \n",false);
		I_Mesh ghoshMesh1 = MeshFileReader.readMesh(getGamePath() + BLUE_GHOST_MESH_FILEPATH, log);
		I_Mesh ghoshMesh2 = MeshFileReader.readMesh(getGamePath() + GREEN_GHOST_MESH_FILEPATH, log);
		I_Mesh ghoshMesh3 = MeshFileReader.readMesh(getGamePath() + RED_GHOST_MESH_FILEPATH, log);
		I_Mesh ghoshMesh4 = MeshFileReader.readMesh(getGamePath() + PINK_GHOST_MESH_FILEPATH, log);
		
		if (ghoshMesh1 != null) {
			MeshesCache.getInstance().add(BLUE_GHOST_MESH,ghoshMesh1);
		} 				
		
		if (ghoshMesh2 != null) {
			MeshesCache.getInstance().add(GREEN_GHOST_MESH,ghoshMesh2);
		}
		
		if (ghoshMesh3 != null) {
			MeshesCache.getInstance().add(RED_GHOST_MESH,ghoshMesh3);
		}
		
		if (ghoshMesh4!= null) {
			MeshesCache.getInstance().add(PINK_GHOST_MESH,ghoshMesh4);
		}

		// Cargando modulo
		String filemodule = getModulePath() + "/module.xml";
		updateLog(log,"Modulo: " + getModulePath().substring(getModulePath().lastIndexOf("/") + 1) + "\n", false);
		SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
		SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
		XMLReader myXMLReader = mySAXParser.getXMLReader();
		ModuleHandler handler = new ModuleHandler(getModulePath(), log);
		myXMLReader.setContentHandler(handler);
		
		FileReader fr = new FileReader(filemodule);
		myXMLReader.parse( new InputSource(fr));    
		
		initialLifes = handler.getLifes();
		levels = handler.getLevels();
		// Inicialmente todos los niveles estan bloqueados menos el primero
		for( int i = 0 ; i < levels.size() ; i++) {
			unlockLevels.add( i == 0);
		}	

	}
		
	public static boolean isMusicOn() {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Preferences.PHYSICS,true);
	}
	
	public static boolean isRenderOn() {
		return renderOn;
	}
	
	public static boolean isPhysicsOn() {
		return physicsOn;
	}
	
	public static boolean isCameraModeOn() {
		return cameraModeOn;
	}
	
	public static boolean isRenderOptimizationOn() {
		return renderOptimizationOn;
	}
	
	public static String getModulePath() {
		return getGamePath() + "modules/" + PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.MODULE, "Final");
	}
	
	public static String getModuleName() {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.MODULE, "Final");
	}

	public static int getLifes() {
		return lifes;
	}

	public static void removeLife() {
		GameContext.lifes--;
	}
	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		GameContext.score = score;
	}
	
	public static void restart() {
		currentLevelPosition = 0;
		score = 0;
		lifes = initialLifes;
		
		for ( Level level : levels) {
			level.restart();
		}		
	}
	
	public static ArrayList<Level> getLevels() {
		return levels;
	}
	
	/**
	 * Determina si el nivel ha sido desbloqueado
	 * El primer nivel siempre esta desbloqueado
	 */
	public static boolean isVisible(Level level) {
		int position = levels.indexOf(level);
		return  unlockLevels.get(position);
	}
	/**
	 * Obtiene el nivel actual si se devuelve null
	 * es que todos los niveles han sido finalizados
	 */
	public static Level getCurrentLevel() {
		try {
			return levels.get(currentLevelPosition);
		} catch (Exception e) {			
			return null;
		}
	}
	
	public static int currentLevelPosition() {
		return currentLevelPosition;
	}
	
	/**
	 * Mediante esta función se realiza un cambio de nivel
	 */
	public static Level goToNextLevel() {
		currentLevelPosition++;
		if (currentLevelPosition > levels.size())
			return null;
		else  {
			return getCurrentLevel();
		}
	}

	public static String getGamePath() {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.PACMANCONFIG_PATH, "/sdcard/PacmanConfig");
	}

	/**
	 * Establece el nivel de partida
	 */
	public static void setCurrentLevel(int position) {
		currentLevelPosition = position;		
	}
	
	/**
	 * Desbloquea los niveles para poder ser seleccionados
	 * en la pantalla de selección de nivel
	 */
	public static void unBlock(String[] visibleLevels) {

		for (int i = 0; i < levels.size(); i++) {
			Level level = levels.get(i);

			for (int j = 0; j < visibleLevels.length && unlockLevels.get(i) == false; j++) {
				if (visibleLevels[j].equals(level.getName())) {
					unlockLevels.set(i, true);
				}
			}
		}
	}
			
	/****************************
	 * Metodos privados 
	 ****************************/
	
	private static void updateLog(Boot boot, String msg, boolean isHtml) {
		if ( boot != null)
			boot.updateLog(msg, isHtml);
	}
	
	
	
}
