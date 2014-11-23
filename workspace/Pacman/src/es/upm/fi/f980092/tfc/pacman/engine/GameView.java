/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.engine;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import es.upm.fi.f980092.tfc.gameengine.physics.utils.PhysicsUtils.Direction;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene.State;
import es.upm.fi.f980092.tfc.pacman.GameContext;
import es.upm.fi.f980092.tfc.pacman.activities.StartLevel;
import es.upm.fi.f980092.tfc.pacman.engine.elements.PacmanElement;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Vista OpenGL de un nivel del pacman asociado a la 
 * actividad Game
 */
public class GameView extends GLSurfaceView {

	private static String TAG ="PACMAN# GameView";


	public GameView(StartLevel context) {
		super(context);
		this.setEGLContextClientVersion(2);
		PacmanRenderEngine render = new PacmanRenderEngine(context,context.getLevel());
		this.setRenderer(render);
		this.requestFocus();
		this.setFocusableInTouchMode(true);	
	}

	/**
	 * Este metodo captura las pulsaciones de teclado del movil
	 * y cambiando el estado del videojuego de las siguientes 
	 * maneras:
	 *    - Cambiar la dirección en la que el pacman debe moverse
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Level level = ((StartLevel)this.getContext()).getLevel();
		PacmanElement pacman = level.getPacman();

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.v(TAG, "DOWN pressed");
			pacman.goTo(Direction.SOUTH);			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {			
			Log.v(TAG, "UP pressed");
			pacman.goTo(Direction.NORT);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.v(TAG, "RIGHT pressed");
			pacman.goTo(Direction.EAST);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			Log.v(TAG, "LEFT Pressed");
			pacman.goTo(Direction.WEST);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.v(TAG, "CENTER Pressed");
			pacman.jump();			
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {	
			if (level.getState() == State.RUNNING) {
				level.setState(State.PAUSE);
			} else if ( level.getState() == State.PAUSE){
				level.setState(State.RUNNING);
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			GameContext.removeLife();
			GameContext.removeLife();
			GameContext.removeLife();
			level.setState(State.FINISH);
		} else {
			return false;
		}		
		return true;
	}

	/**
	 * PROPIEDADES
	 */
	private float mLastTouchX;
	private float mLastTouchY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {

			final float x = e.getX();
			final float y = e.getY();
			Log.i(TAG,"Press screen at [" + x + "," + y + "]");
			mLastTouchX = x;
			mLastTouchY = y;
			break;
		}
		case MotionEvent.ACTION_UP: {
			final float x = e.getX();
			final float y = e.getY();

			Log.i(TAG,"Unpress at [" + x + "," + y + "]");
			final float dx = e.getX() - mLastTouchX;
			final float dy = e.getY() - mLastTouchY;

			mLastTouchX = x;
			mLastTouchY = y;

			Level level = ((StartLevel)this.getContext()).getLevel();
			PacmanElement pacman = level.getPacman();

			if (Math.abs(dx) > Math.abs(dy)) {
				if ( -10 < dx && dx < 10) {
					Log.i(TAG, "Touch event Jump");
					pacman.jump();
				} else if (dx > 0 ) { 
					Log.i(TAG, "Touch event to Right " + dx);
					if (GameContext.isCameraModeOn()) {
						level.getCamera().moveToRight(); 
					} else {
						pacman.goTo(Direction.EAST);
					}
				} else {
					Log.i(TAG, "Touch event to Left" + dx);
					if (GameContext.isCameraModeOn()) {
						level.getCamera().moveToLeft();
					} else {
						pacman.goTo(Direction.WEST);
					}
				}
			} else {
				if ( -10 < dy && dy < 10) {
					Log.i(TAG, "Touch event Jump");
					pacman.jump();
				} else if (dy > 0 ) { 
					Log.i(TAG, "Touch event to Down " + dy);
					if (GameContext.isCameraModeOn()) {
						level.getCamera().moveToUp();						
					} else {
						pacman.goTo(Direction.SOUTH);					
					}
				} else {
					Log.i(TAG, "Touch event to Up " + dy);
					if (GameContext.isCameraModeOn()) {
						level.getCamera().moveToDown();
					} else {
						pacman.goTo(Direction.NORT);
					}
				}
			}
			break;			
		}
		}
		return true;
	}
}