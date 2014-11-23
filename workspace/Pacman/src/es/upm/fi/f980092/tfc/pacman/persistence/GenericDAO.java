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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDAO extends SQLiteOpenHelper {

    private static String DB_NAME = "PacmanDDBB";
    private static int DB_VERSION = 1;
    
	public GenericDAO(Context context) {
		super(context, DB_NAME, null, DB_VERSION);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Tabla de puntuaciones
		db.execSQL("CREATE TABLE " + ScoreDAO.TABLE_TITLE +  "(" + 
				ScoreDAO.TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				ScoreDAO.TABLE_NAME + " TEXT NOT NULL," +
				ScoreDAO.TABLE_MODULE + " TEXT NOT NULL," +
				ScoreDAO.TABLE_LEVEL + " TEXT NOT NULL," + 
				ScoreDAO.TABLE_POINTS + " INTEGER," + 
				ScoreDAO.TABLE_DATE + " TEXT NOT NULL);" );		
		
		// Tabla de avances de cada modulo
		db.execSQL("CREATE TABLE " +  BreadcrumbsDAO.TABLE_TITLE +  "(" + 			
				BreadcrumbsDAO.TABLE_MODULE + " TEXT NOT NULL," +
				BreadcrumbsDAO.TABLE_LEVEL + " TEXT NOT NULL," +
				"PRIMARY KEY (" +  BreadcrumbsDAO.TABLE_MODULE + "," + BreadcrumbsDAO.TABLE_LEVEL + "))");		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}	
}