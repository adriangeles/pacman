package es.upm.fi.f980092.tfc.pacman.activities;

import es.upm.fi.f980092.tfc.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Esta clase corresponde con una actividad que sirve unicamente 
 * para indicar que aun no se ha desarrollado esta parte de la 
 * aplicación.
 * 
 * @author aangeles
 *
 */
public class UnderConstructionActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        setContentView(R.layout.underconstruction);       
	}
}
