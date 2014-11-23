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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LevelSimulation extends Activity {

	public static final int ACTIVITY_CODE = 0;

	public static final String IN_PARAMETER_PATH = "PATH";
	public static final String IN_PARAMATER_LIFES = "LIFES";
	public static final String IN_PARAMATER_POINTS = "POINTS";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.level_simulation);

		final Button buttonCANCEL = (Button) findViewById(R.id.Button01);
		buttonCANCEL.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
			}
		});

		final Button buttonNEXT = (Button) findViewById(R.id.Button02);
		buttonNEXT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = getIntent();
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
