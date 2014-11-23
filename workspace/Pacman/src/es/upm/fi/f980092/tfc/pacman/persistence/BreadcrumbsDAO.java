/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BreadcrumbsDAO extends GenericDAO {

	// Table SCORES
    public static String TABLE_TITLE = "Breadcrumbs";
    public static String TABLE_ID = "id";    
    public static String TABLE_MODULE = "module";
    public static String TABLE_LEVEL = "level";
    
	public BreadcrumbsDAO(Context context) {
		super(context);
	}
	
	public String[] getBreadcrumbs(String module) {
		String[] columns = { TABLE_LEVEL };	
				
		SQLiteDatabase db = getReadableDatabase();		
		Cursor cursor = db.query(TABLE_TITLE, columns, TABLE_MODULE + "= \"" + module + "\"", null, null, null, null);	
		
		int n = cursor.getCount();		
		String[] level = new String[n];
		
		int i = 0;
		while (cursor.moveToNext()) {
			level[i++] = cursor.getString(0);
		}		
		return level;
	}	
	
	/**
	 * Se añade un nuevo nivel indicando que ha sido superado para un determinado módulo
	 */
	public void addBreadcrumb(String modulePath, String levelName) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();		
		values.put(TABLE_MODULE, modulePath);
		values.put(TABLE_LEVEL, levelName);
		db.insertOrThrow(TABLE_TITLE, null, values);		
	}

}
