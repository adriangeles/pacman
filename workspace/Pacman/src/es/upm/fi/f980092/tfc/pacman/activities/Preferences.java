/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.sound.SoundEngine;
import es.upm.fi.f980092.tfc.pacman.GameContext;

/**
 * Esta actividad contiene las preferencias del usuario en la aplicación Se
 * podra configurar: 
 *    - Si el sonido esta activado 
 *    - El módulo de juego en el que se desea jugar el cual se carga dinámicamente
 *      con los directorios existentes (modulos) en la dirección definida por 
 *      SdCardDirectory 
 */
public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	public final static String MODULE = "module";
	public final static String SOUND  = "music";
	public final static String PHYSICS = "physics";
	public final static String RENDERING = "render";
	public final static String CAMERA_MODE = "cameraMode";
	public final static String RENDER_OPTIMIZACITON = "renderOptimization";
	public static final String PACMANCONFIG_PATH = "PacmanConfig Path";
	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + ":Preferences - ";
	
	private static final String MSG_MODULE_OK = "Por favor seleccione uno de los modulos existentes en dicho directorio";
	private static final String MSG_MODULE_ERROR = "Por favor seleccione otro directorio, el indicado no existe";
	
	// Listado de modulos
	private ListPreference moduleList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		moduleList= new ListPreference(this);
		loadModules();	
		this.getPreferenceManager();
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		settings.registerOnSharedPreferenceChangeListener(this);
		
	}
	
	private void loadModules() {

		String sdcardDir = GameContext.getGamePath() + "/modules/";

		File dir = new File(sdcardDir);

		if (dir.exists()) {
			moduleList.setDialogTitle(R.string.module_Path_title);
			moduleList.setKey(MODULE);
			moduleList.setTitle(R.string.module_Path);
			moduleList.setSummary(R.string.module_Path_summary);

			this.getPreferenceScreen().addPreference(moduleList);
			
			File[] files = new File(sdcardDir).listFiles();
			ArrayList<File> modules = new ArrayList<File>();

			for (File f : files) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					modules.add(f);
				}
			}

			String[] modulesName = new String[modules.size()];
			String[] modulesPath = new String[modules.size()];

			int i = 0;
			for (File f : modules) {
				modulesName[i] = f.getName();
				modulesPath[i++] = f.getName();
			}

			moduleList.setEntries(modulesName);
			moduleList.setEntryValues(modulesPath);
		} else {
			this.getPreferenceScreen().removePreference(moduleList);
		}
	}
	
    @Override
    protected void onStop() {
    	super.onStop();
    	try {
			GameContext.load(this.getApplicationContext(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		if ( MODULE.equals(key) ) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Para que los cambios hagan efecto ha de reiniciar la aplicación")
			       .setCancelable(true)
			       .setTitle("Cambio de modulo")
			       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		} else if (SOUND.equals(key) ) {
			SoundEngine soundEngine = SoundEngine.getInstance();
			
			if (soundEngine != null) {
				if (sharedPreferences.getBoolean(key, false)) {
					soundEngine.setActive(true);
					soundEngine.play(GameContext.getGamePath() + "/music/intro.mp3");
				} else {
					soundEngine.stop();
					soundEngine.setActive(false);
				}
			}
		} else if (PACMANCONFIG_PATH.equals(key)) {
			
			String sdcardDir = GameContext.getGamePath();
			File dir = new File(sdcardDir);
			String msg = MSG_MODULE_OK;
			if (dir.exists()) {
				loadModules();
			} else { 
				msg = MSG_MODULE_ERROR;				
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg)
			       .setCancelable(true)
			       .setTitle("Nuevo PacmanConfig")
			       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}		
	}
}