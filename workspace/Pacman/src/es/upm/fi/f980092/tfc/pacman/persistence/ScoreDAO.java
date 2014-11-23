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

public class ScoreDAO extends GenericDAO {

	// Table SCORES
    public static String TABLE_TITLE = "Score";
    public static String TABLE_ID = "id";
    public static String TABLE_NAME = "name";
    public static String TABLE_POINTS ="points";
    public static String TABLE_MODULE = "module";
    public static String TABLE_LEVEL = "level";
    public static String TABLE_DATE = "date";
    
	public ScoreDAO(Context context) {
		super(context);
	}
		
	public void addScore(String name, String module, String level, String points) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLE_NAME, name);
		values.put(TABLE_MODULE, module);
		values.put(TABLE_LEVEL, level);
		values.put(TABLE_POINTS, points);
		values.put(TABLE_DATE, "12/12/2010 12:00");

		db.insertOrThrow(TABLE_TITLE, null, values);
	}
	
	public ScoreTO[] getScores(String module, String size) {
		String[] from = { TABLE_NAME, TABLE_POINTS, TABLE_LEVEL, TABLE_DATE };
		String ORDER_BY = TABLE_POINTS + " DESC" ;
		
		SQLiteDatabase db = getReadableDatabase();		
		Cursor cursor = db.query(TABLE_TITLE, from, TABLE_MODULE + "='" + module + "'", null, null, null, ORDER_BY, size);
		int n = cursor.getCount();		
		ScoreTO[] score = new ScoreTO[n];
		int i = 0;
		while (cursor.moveToNext()) {
			String name   = cursor.getString(0);
			String points = cursor.getString(1);
			String level  = cursor.getString(2);
			String time   = cursor.getString(3);
			
			score[i++] = new ScoreTO(name, module, level, time, points);
		}		

		return score;
	}
}
