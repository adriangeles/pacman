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
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import es.upm.fi.f980092.tfc.R;
import es.upm.fi.f980092.tfc.pacman.GameContext;
import es.upm.fi.f980092.tfc.pacman.persistence.ScoreDAO;
import es.upm.fi.f980092.tfc.pacman.persistence.ScoreTO;

public class Scores extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  
                
        setContentView(R.layout.score_table);
        
        TableLayout table = (TableLayout)findViewById(R.id.scoretable);
        
        ScoreDAO dao = new ScoreDAO(this.getApplicationContext());        
        ScoreTO[] scores = dao.getScores(GameContext.getModulePath(), "10");
        
        int padding = 10;
        
        for (int i = 0; i< scores.length; i++) {
        	TableRow row = new TableRow(this.getApplicationContext());
        	
        	TextView col_position = new TextView(this.getApplicationContext());
        	col_position.setGravity(Gravity.CENTER);
        	col_position.setText( String.valueOf(i));
        	col_position.setPadding(padding,0,padding,0);
			row.addView(col_position, 0);
        	
        	TextView col_name = new TextView(this.getApplicationContext());
        	col_name.setText(scores[i].getName());
        	col_name.setGravity(Gravity.LEFT);
        	col_name.setPadding(padding+30,0,padding+30,0);
        	row.addView(col_name,1); 
        	
        	TextView col_point = new TextView(this.getApplicationContext());
        	col_point.setText(scores[i].getScore());
        	col_point.setGravity(Gravity.CENTER);
        	col_point.setPadding(padding,0,padding,0);
        	row.addView(col_point,2);
        	
        	table.addView(row);        	
        }        
	}
}
