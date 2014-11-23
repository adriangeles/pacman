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

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.gameengine.sound.SoundEngine;
import es.upm.fi.f980092.tfc.pacman.GameContext;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;
import es.upm.fi.f980092.tfc.pacman.persistence.BreadcrumbsDAO;
import es.upm.fi.f980092.tfc.pacman.persistence.ScoreDAO;
import es.upm.fi.f980092.tfc.pacman.persistence.ScoreTO;
import es.upm.fi.f980092.tfc.pacman.view.PacmanDialog;

/**
 * Actividad que se corresponde con la seleccion de un escneario
 * Al seleccionar un escenario se procede a iniar la actividad de 
 * Game y comenzar asi el nivel seleccionado.
 */
public class StartGame extends Activity implements OnClickListener {

	/*****************
	 * CONSTANTES
	 *****************/
	// Activities codes
	private final static int LEVEL_PRESENTATION_ACTIVITY = 0;
	private final static int LEVEL_ACTIVITY = 1;
	private final static int GAME_OVER_ACTIVITY = 2;
	private final static int END_GAME_ACTIVITY = 3;
	
	
	// Activity Parameters
	public final static String INPUT_PARAMETER_MODULE = "MODULE";   // Path of the module to be loaded
	
	// Logger
	private final static String TAG = "PACMAN# LEVEL_LOADER";
	
	
	/****************
	 * ATRIBUTTES
	 ***************/

	private Gallery gallery;			// Elemento gráfico
	private ImageView imgView;			// Elemento grafico	
	private TextView nameTextView;		// Elemento gráfico
	private TextView descTextView;		// Elemento gráfico
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		try {
			GameContext.restart();
						
			BreadcrumbsDAO dao = new BreadcrumbsDAO(getApplicationContext());
			String[] visibleLevels = dao.getBreadcrumbs(GameContext.getModulePath());
			GameContext.unBlock(visibleLevels);
			
			setContentView(R.layout.levelconfiguration);

			nameTextView = (TextView) findViewById(R.id.NameText);
			nameTextView.setText("");
			
			descTextView = (TextView) findViewById(R.id.DescriptionText);
			descTextView.setText("");
			
			imgView = (ImageView) findViewById(R.id.LevelImage);						
			imgView.setOnClickListener(this);
			imgView.setImageResource(R.drawable.select_level);
						
			
			gallery = (Gallery) findViewById(R.id.gallery);
			gallery.setAdapter(new ImageAdapter(this));			
			gallery.setOnItemClickListener(new OnItemClickListener() {
				@SuppressWarnings("unchecked")
				public void onItemClick(AdapterView parent, View v,	int position, long id) {
					Level selectedLevel = GameContext.getLevels().get(position);
					nameTextView.setText(selectedLevel.getName());
					descTextView.setText(selectedLevel.getDescription());
					imgView.setTag(position);
					if (GameContext.isVisible(selectedLevel)) {
						imgView.setImageURI(selectedLevel.getBigScreenshot());		
						SoundEngine.getInstance().play(selectedLevel.getPath()  + "/music.mp3");			

					} else {
						imgView.setImageResource(R.drawable.block_level);
					}
				}
			});

			registerForContextMenu(gallery);		

		} catch (Exception e) {
			AlertDialog alert = PacmanDialog.buildFatalErrorDialog(getApplicationContext(), this, R.string.dialog_parsing_module_title, R.string.dialog_parsing_module_text);
			alert.show();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(R.string.config);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Toast.makeText(this, "Longpress: " + info.position, Toast.LENGTH_SHORT).show();
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		private int mGalleryItemBackground;
		private Context mContext;
		
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return GameContext.getLevels().size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			Level w = GameContext.getLevels().get(position);
			i.setImageURI(w.getSmallImageURI());					
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new Gallery.LayoutParams(136, 88));
			i.setBackgroundResource(mGalleryItemBackground);
			return i;
		}
	}

	@Override
	/**
	 * Al realizar un click sobre la imagen de un nivel, si este no esta bloqueado
	 * inicializamos una nueva actividad que se corresponde con inicializar el
	 * nivel de juego.
	 */
	public void onClick(View v) {
		Integer position = (Integer) v.getTag();
		if (position != null) { 
			// Position == null cuando se presiona sobre la imagen de seleccion de nivel 
			Level level = GameContext.getLevels().get(position);
			if (GameContext.isVisible(level) || true) {   //TODO En desarrollo todos los escenarios son visibles
				GameContext.setCurrentLevel(position);				
				showVideo(level.getPath() + "/video.mp4",LEVEL_PRESENTATION_ACTIVITY);
			}
		}
	}
	
	/**
	 * Este método inicializa el nivel actual determinado por el modulo
	 */
	private void startLevel() {
		// Al iniciar el nivel lo desbloqueamos y lo hacemos seleccionable 
		// en las proximas partidas
		Level level = GameContext.getCurrentLevel();
		if (!GameContext.isVisible(level)) {
			BreadcrumbsDAO dao = new BreadcrumbsDAO(getApplicationContext());
			dao.addBreadcrumb(GameContext.getModulePath(), level.getName());
			GameContext.unBlock(new String[] { level.getName() });
		}
		
		Intent i = new Intent(this, StartLevel.class);		
		startActivityForResult(i, LEVEL_ACTIVITY);		
	}
	
	@Override
	/**
	 * Este metodo contiene el ciclo de vida de una partida 
	 *   - Inicia el siguiente nivel si seguimos teniendo vidas
	 *   - Finaliza el juego si hemos perdido todas las vidas
	 *   - Finaliza el juego si hemos terminado todos los niveles
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {

		case LEVEL_PRESENTATION_ACTIVITY:
			// Acción tras presentar un video mostramos el nivel
			startLevel();
			break;			
		case LEVEL_ACTIVITY:			
				
				if ( GameContext.getLifes() < 1 ) {
					SoundEngine.getInstance().stop();
					showVideo(GameContext.getGamePath() + "/videos/GameOver.mp4", END_GAME_ACTIVITY);
				} else {					
					Level nextLevel = GameContext.goToNextLevel();
					
					if (nextLevel == null) {
						SoundEngine.getInstance().stop();
						showVideo(GameContext.getGamePath() + "/videos/GameComplete.mp4", END_GAME_ACTIVITY);												
					} else {
						SoundEngine.getInstance().play(nextLevel.getPath()  + "/music.mp3");			 	
						showVideo(nextLevel.path + "/video.mp4", LEVEL_PRESENTATION_ACTIVITY);
					}
				}
		
			break;
		case END_GAME_ACTIVITY:
		case GAME_OVER_ACTIVITY:
			saveScore();
		default:
			break;
		}
	}
	
	private void showVideo(String path, int code) {
		Log.i(TAG,"Mostrando video " + path);
		Intent i = new Intent(this, VideoPresentation.class);
		i.putExtra(StartLevel.IN_PARAMETER_PATH, path);
		startActivityForResult(i, code);
	}
	
	private void saveScore() {
 
		ScoreDAO dao = new ScoreDAO(GameContext.context);
		ScoreTO[] scores = dao.getScores(GameContext.getModulePath(), "10");
		if ( scores.length < 10 || Integer.valueOf(scores[scores.length -1].getScore()) < GameContext.getScore()) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Nuevo top record");
			builder.setMessage("¿Cuál es su nombre?");
			final EditText input = new EditText(this);
			builder.setView(input);
			builder.setCancelable(false);        
			builder.setPositiveButton("Aceptar",  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					String name = input.getText().toString();
					String module = GameContext.getModulePath();

					String level = "";
					// Buscamos el nivel en el que se a re
					try {
						level = GameContext.getCurrentLevel().name;
					} catch (Exception e) {
						level = " - ";
					}
					int points = GameContext.getScore();

					ScoreDAO dao = new ScoreDAO(GameContext.context);
					dao.addScore(name, module, level, String.valueOf(points));
					finish();
				}
			});				  

			builder.create();
			builder.show();
		} else {
			finish();
		}
    }
}
