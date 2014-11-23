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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.sound.SoundEngine;
import es.upm.fi.f980092.tfc.pacman.GameContext;

/**
 * Actividad principal del videojuego que contiene
 * el menu inicial.
 */
public class Pacman extends Activity implements OnClickListener {

	private static final String TAG = "PACMAN# Pacman";
	private static final String TAG2 = TAG + ":Pacman - ";
	
	public static final int PACMAN_MAIN_SOUND = 0;
	

	private Boolean onPauseMusic = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);              
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        setContentView(R.layout.main);
        
        for( int ref : new int[]{R.id.StartButton,R.id.ConfigButton,R.id.AboutButton,R.id.ScoreButton,R.id.ExitButton}) {
        	View v = findViewById(ref);
    		v.setOnClickListener(this);    		
        }          
    }
	    

	@Override
	protected void onStart() {
		if (onPauseMusic == null || onPauseMusic == true)
			SoundEngine.getInstance().play(GameContext.getGamePath() + "/music/intro.mp3");
		onPauseMusic = null;
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (onPauseMusic == null)
			SoundEngine.getInstance().pause();
	}

	@Override
	public void onClick(View v) {
		
		Intent i; 
		switch (v.getId()) {
		case R.id.StartButton:	
			onPauseMusic = true;
			i = new Intent(this, StartGame.class);			
			startActivity(i);
			break;
		case R.id.ConfigButton:
			onPauseMusic = false;
			 i = new Intent(this, Preferences.class);			
			startActivity(i);
			break;
		case R.id.ScoreButton:
			onPauseMusic = false;
			i = new Intent(this, Scores.class);			
			startActivity(i);
			break;
		case R.id.ExitButton:
			SoundEngine.getInstance().stop();
			finish();
			break;
		case R.id.AboutButton:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Proyecto fin de carrera\n" +
						"Universidad: Politecnica de Madrid\n" +	
						"Facultad de informatica\n" +
						"Author: Adrián Ángeles Ramón\n" + 
						"Tutor: Ángel Herranz Nieva")
			       .setCancelable(true)
			       .setTitle("About Pacman")
			       .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			break;
		}		
	}


	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

}