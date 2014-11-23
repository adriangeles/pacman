@Override
public void onCreate(SQLiteDatabase db) {

   db.execSQL(
        "CREATE TABLE " + 
        ScoreDAO.TABLE_TITLE +  "(" + ScoreDAO.TABLE_ID + 
        " INTEGER PRIMARY KEY AUTOINCREMENT," +
        ScoreDAO.TABLE_NAME   + " TEXT NOT NULL," +
        ScoreDAO.TABLE_MODULE + " TEXT NOT NULL," +
        ScoreDAO.TABLE_LEVEL  + " TEXT NOT NULL," + 
        ScoreDAO.TABLE_POINTS + " INTEGER," + 
        ScoreDAO.TABLE_DATE   + " TEXT NOT NULL);" 
   );		
}