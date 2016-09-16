package lt.samples.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "weather.db";	
	private static final int DATABASE_VERSION = 1;
    private static final String WEATHER_DB_CREATE =
        "CREATE TABLE WeatherStations (_id INTEGER PRIMARY KEY, " +
        		"stationName TEXT, lastUpdatedDate REAL );"; 
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		// Run the query defined above to initialize the database.
		db.execSQL(WEATHER_DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing - we don't have a 2nd version yet
	}

}
