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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.render.caches.MeshesCache;
import es.upm.fi.f980092.tfc.gameengine.render.shaders.animation.AnimatedShaderMesh;
import es.upm.fi.f980092.tfc.pacman.GameContext;
import es.upm.fi.f980092.tfc.pacman.engine.GameView;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Actividad que contiene la logica de una pantalla del videojuego
 * Esta compuesta por una vista de opengl
 */
public class StartLevel extends Activity {
	
	/*****************
	 * CONSTANTES
	 *****************/
		
	public static final String IN_PARAMETER_PATH   = "PATH";	
	public static final String IN_PARAMATER_LIFES  = "LIFES";
	public static final String IN_PARAMATER_POINTS = "POINTS";
	public static final String IN_PARAMATER_LEVEL  = "LEVEL";
	
	public static final String OUT_PARAMATER_LIFES = "FINAL_LIFES";
	public static final String OUT_PARAMATER_POINTS = "FINAL_POINTS";

	// Logger
	private static final String TAG = "PACMAN# StartLevel";
		
	/****************
	 * ATRIBUTTES
	 ***************/
	
	private Level level;				// Estructura de datos del nivel
	private String path;				// Directorio donde se encuentran los datos del nivel
		
	private GameView view;				// Interfaz gráfica
	private TextView scoreView; 		// Label con la puntuacion
    private ImageView life1; 			// Imagen que representa la primera vida
    private ImageView life2; 			// Imagen que representa la segunda vida
    private ImageView life3; 			// Imagen que representa la tercera vida
    
//	private SensorManager sensorManager;

    public Handler handler =  new Handler() {

    	public void handleMessage(Message msg) {
    		updateLifesAndScore();
    	}
    };
		
	/***********************************
	 * METODOS DE LA CLASE ACTIVITY
	 ***********************************/
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  

        level = GameContext.getCurrentLevel();        
        level.restart();
        
        // Modificamos la velocidad de cambio de mallas del pacman en funcion de su velocidad
        int velocidad = Math.abs(level.getPacman().getVelocity()[0] + level.getPacman().getVelocity()[1]);
        float distancia = level.getScale() / 2;
        int duracion = Math.round(distancia / velocidad) ;
        String meshId = level.getPacman().getMeshId();
		AnimatedShaderMesh mesh = (AnimatedShaderMesh) (( level.getPacman().getMesh() == null ) ? MeshesCache.getInstance().getMesh(meshId): level.getPacman().getMesh());
		mesh.updateDuration(duracion);
        //
        
		view = new GameView(this);
		setContentView(R.layout.levelframe);		
        LinearLayout v = (LinearLayout) this.findViewById(R.id.openglLayout);
        v.addView(view);
        scoreView = (TextView) this.findViewById(R.id.score);
        life1 = (ImageView) this.findViewById(R.id.life1);              
        life2 = (ImageView) this.findViewById(R.id.life2);
        life3 = (ImageView) this.findViewById(R.id.life3);
        updateLifesAndScore();
        
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }
   
    @Override
    protected void onPause(){
    	super.onPause();
    	view.onPause();
//    	sensorManager.unregisterListener(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	view.onResume();
//    	sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
//    	sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_FASTEST);
//    	sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),   SensorManager.SENSOR_DELAY_FASTEST);

    }
    
    @Override
	protected void onDestroy() {    	
		super.onDestroy();
	}

	/***********************************
	 * METODOS PUBLICOS
	 ***********************************/
    
	public Level getLevel() {
		return level;
	}

	public String getPath() {
		return path;
	}
	
	public void grabarResult( Intent i) {
		setResult(StartLevel.RESULT_OK, i);
	}
		
	public void updateLifesAndScore() {
		scoreView.setText("Puntuación: " + GameContext.getScore());
		int lifes = GameContext.getLifes();
		
		life1.setVisibility(   lifes > 0 ? View.VISIBLE : View.GONE);
		life2.setVisibility(   lifes > 1 ? View.VISIBLE : View.GONE);
		life3.setVisibility(   lifes > 2 ? View.VISIBLE : View.GONE);
	}
	

    /***********************************
	 * METODOS PRIVADOS
	 ***********************************/
	
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		
//		
//	}
//
//
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//			getAccelerometer(event);
//		}
//	}
//
//	private void getAccelerometer(SensorEvent event) {
//		float[] values = event.values;
//		// Movement
//		float x = values[0];
//		float y = values[1];
//		float z = values[2];
//
//		Direction nextDirection = null;
//		
//		if ( x > 1 ) {
//			nextDirection = Direction.WEST;
//		} else if ( x < -1 )
//			nextDirection = Direction.EAST;			
//		else if ( y < -1 ) 
//			nextDirection = Direction.NORT;
//		else if ( y > 1)
//			nextDirection = Direction.SOUTH;
//		else if ( z > 12) {
//			nextDirection = Direction.STATIC;
//		}
//		
//		if (currentDir == null || nextDirection != null && currentDir != nextDirection) {
//			level.getPacman().goTo(nextDirection);
//			currentDir = nextDirection;
//		}
//		
//	}
//	
//	private Direction currentDir = null;
	
}
