package es.upm.fi.f980092.tfc.pacman.engine;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.GameEngine;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.CollisionEngine;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.TileUtils;
import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene.State;
import es.upm.fi.f980092.tfc.gameengine.sound.SoundEngine;
import es.upm.fi.f980092.tfc.pacman.GameContext;
import es.upm.fi.f980092.tfc.pacman.activities.Preferences;
import es.upm.fi.f980092.tfc.pacman.activities.StartLevel;
import es.upm.fi.f980092.tfc.pacman.engine.elements.A_PacmanElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.GhostElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.PillsNetElement;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

public class PacmanRenderEngine extends GameEngine {

	/**************
	 * Constantes
	 **************/
	
	private static final String TAG =  "TfcGameEngine";
	private static final String TAG2 = TAG + "::PacmanRenderEngine - ";
	
	private final static int SMALL_PILL_POINTS = 10;
//	private final static int BIG_PILL_POINTS = 50;
	/**************
	 * Propiedades
	 **************/
	
	private Level level;			// Contiene la descripción del nivel que ha de mostrarse
	private StartLevel activity;  	// Contiene la actividad asociada al nivel para poder finalizarlo cuando finalice el nivel
	
	/*****************
	 * Constructores
	 *****************/
	
	public PacmanRenderEngine(StartLevel activity, Level level) {
		super(
				level, 
				activity.getResources().getBoolean(R.bool.Autopause),
				PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext()).getBoolean(Preferences.RENDERING,true),
				PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext()).getBoolean(Preferences.PHYSICS,true),
				Integer.parseInt(activity.getResources().getString(R.string.frameTime)));
		
		
		this.level = level;
		this.activity = activity;		
	}

	/*********************************
	 * Métodos de GenericGameEngine
	 *********************************/
		
	@Override
	protected void postRender() {
			
	}

	
	@Override
	protected void preRender() {

		updateGameBar();
		switch (scene.getState()) {
		case START:
			// Antes de comezar el nuevo render verificamos si la animacion 
			// ha finalizado si es asi pasamos al siguiente estado y cambiamos
			// de camara a la que persigue al pacman
			if (scene.getCamera().isFinished()) {				
				level.getCamera().restart();	// Inicializamos la camara para la proxima vez que se invoque
				scene.setCamera(level.getRunningCamera()); 
				scene.setState( State.RUNNING);				
			}
			break;
		case RESTART:
			if (scene.getCamera().isFinished()) {
				level.getCamera().restart();	// Inicializamos la camara para la proxima vez que se invoque
				int lifes = GameContext.getLifes();				
				if (lifes > 0 ) {
					level.setCamera(level.getStartCamera());
					level.setState(State.START);	
					// Por cada una de los elementos dinamicos del juego hay que volverlos a colocar a la
					// posicion inicial
					for ( A_SceneElement e : level.getDynamicElements()) {
						if (e instanceof A_PacmanElement) {
							((A_PacmanElement) e).restart();
						}
					}
				} else {
					finalizarActividad();
				}
			}
			break;
		case RUNNING:
			// Es necesario si volvemos de la pausa
			SoundEngine.getInstance().resume();
			// Verificamos las colisiones existentes
			// Procedemos a calcular las colisiones para aplicar la lógica del juego
			//    Colision Pacman - pastilla --> suma puntos
			//    Colision Pacman - fantamas --> te quita una vida

			A_SceneElement pacman = level.getPacman();
			
			ArrayList<A_SceneElement> elements = level.getSoftCollisionElements();
			
			for( A_SceneElement e : elements) {

				// Una vez aplicada la fisica de los objetos calculamos las colisiones con los objetos 
				// blandos para que sean tratados por la lógica del juego
				if ( e != pacman && CollisionEngine.detectCollision(pacman, e) ) {
					if ( e instanceof GhostElement) {
						SoundEngine.getInstance().play(R.raw.pacman_death);
						scene.setState(State.RESTART);	
						GameContext.removeLife();
						scene.setCamera(level.getRestartCamera());
					} else if ( e instanceof PillsNetElement ) {
						
						PillsNetElement net = (PillsNetElement) e;
						int size = level.getProperties().getTileSize();
						
						int[] tile = TileUtils.getTile(
								pacman.getPosition(),
								size); 
						if ( net.removePill( tile[0] * size ,tile[1]*size)) {
							// Si hemos comido una pastilla aumentamos la puntuacion							
							GameContext.setScore(GameContext.getScore()+SMALL_PILL_POINTS);
							SoundEngine.getInstance().play(R.raw.pacman_chomp);
							if ( level.getNumberOfPill() == 0 ) {								
								finalizarActividad();
							}
						}
					}
				}	
			}
			break;
		case PAUSE:
			SoundEngine.getInstance().pause();
			break;
		case FINISH:
			finalizarActividad();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Este metodo actualiza la barra del juego que 
	 * contiene las vidas y la puntuacion enviando
	 * un mensaje al thread de dibujo de android.
	 * De hacerlo directamente obtendriamos una 
	 * excepcion de acceso ilegal
	 */
	private void updateGameBar() {
		 Thread update = new Thread(new Runnable() {

			   @Override
			   public void run() {
				   Message msg = new Message();
				   activity.handler.sendMessage(msg);
			    }
		 });
		update.start();
	}

	@Override
	protected boolean isActivePhysicsOnState() {		
		return level.getState() == State.RUNNING;
	}

	private void finalizarActividad() {
		level.setState(State.FINISH);
		String msg = (level.getNumberOfPill() == 0) ? "Nivel completado" : "Game over";
		Log.i(TAG,TAG2 + msg);
		Intent i = new Intent();			
		activity.grabarResult(i);
		activity.finish();
	}
}
