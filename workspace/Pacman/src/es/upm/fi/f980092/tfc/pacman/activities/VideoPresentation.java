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

import es.upm.fi.f980092.tfc.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class VideoPresentation extends Activity implements OnCompletionListener{
	/********************
	 * CONSTANTES
	 ********************/
	
	public static final String IN_PARAMETER_PATH   = "PATH";

	public static final int ACTIVITY_CODE = 1;	
	
	/********************
	 * ATRIBUTOS
	 ********************/
	
	private VideoView videoView;       // Interfaz gráfica
	
	/*******************
	 * CONSTRUCTORES
	 *******************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.presentation_video);
		
		videoView = (VideoView) findViewById(R.id.VideoView);
		videoView.setOnCompletionListener(this);
		
		String path = getIntent().getStringExtra(IN_PARAMETER_PATH);
				
		videoView.setVideoPath(path);
		videoView.requestFocus();
		videoView.start();
	}

	/***********************
	 * OnCompletionListener
	 ***********************/
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		finish();		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	
	/*******************
	 * METODOS PUBLICOS
	 *******************/
}