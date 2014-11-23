package es.upm.fi.f980092.tfc.pacman.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.pacman.GameContext;

public class Boot  extends Activity {

	/******************
	 * CONSTANTES
	 ******************/

	private static final String TAG = "Pacman ";
	private static final String TAG2 = TAG + ":Boot - ";
	
	private static final String MSG_OK = "<font color=\"#00FF00\">    [OK]</font><BR>";
	private static final String MSG_ERROR = "<font color=\"#FF0000\">    [ERROR]</font><BR>";

	/*********************
	 * PROPIEDADES
	 ********************/
	
	private EditText logText = null;
	private Handler handler = null;
	private Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);              
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.boot);

		handler = new Handler();
		instance = this;
		logText = (EditText) this.findViewById(R.id.logText);

		startProgress();
	}

	public void startProgress() {
		writePresentation();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {		
				try {
					GameContext.load(getApplicationContext(), (Boot) instance);
					bootingEnd();
				} catch (Exception e) {
					setError();
					showPreferencesOnError();
				}				
			}
		};
		new Thread(runnable).start();
	}

	/**
	 * Actualización asincrona desde el thread de carga 
	 * inicial
	 * @param text Traza que se añadirá al log
	 */
	public void updateLog(final String text, final boolean isHtml) {
		
		handler.post(new Runnable() {
			@Override
			public void run() { 
				if (isHtml) { 
						logText.getText().append( Html.fromHtml(text));
				} else {
					 logText.getText().append( text);					 
				}
			}
		});
	}
	
	public void setOk() {
		updateLog(MSG_OK, true);
	}
	
	public void setError() {
		updateLog(MSG_ERROR, true);
	}
	
	private void updateLog(final String text) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				logText.getText().append(text);
			}
		});
	}
	
	private void writePresentation() {
		String presentacion = " ************************************** \n" + 
		                      " *      PROYECTO FIN DE CARRERA       * \n" +
		                      " ************************************** \n" + 
		                      " *                                    * \n" + 
		                      " *      Alumno: Adrián Ángeles Ramón  * \n" +
		                      " *       Tutor: Ángel Herranz Nieva   * \n" +
		                      " * Universidad: Politécnica de Madrid * \n" +
		                      " *    Facultad: Informática           * \n" +
		                      " *                                    * \n" + 
		                      " ************************************** \n\n\n";
		
		
		for ( char c : presentacion.toCharArray()) {		
			if ( c == ' ') {
				updateLog(" ");
			} else if ( c == '\n') {
				updateLog("\n");
			} else {
				updateLog( "<font color=\"#4682B4\">" + c + "</font>", true);
			}
		}
	}
	
	
	private void bootingEnd() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				finish();
				Intent i = new Intent(instance, Pacman.class);
				startActivity(i);
			}
		});
	}
	
	private void showPreferencesOnError() {
		Intent i = new Intent(instance, Preferences.class);
		startActivity(i);
	}
}





