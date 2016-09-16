package lt.samples;

import java.util.GregorianCalendar;

import lt.samples.db.DatabaseHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DatabaseActivity extends Activity {
	private static final String TAG="DatabaseActivity";
	
	private DatabaseHelper mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_me);
        mDbHelper = new DatabaseHelper(this);
        findViewById(R.id.main_bt_clickme).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long id = insertStation();
				updateStationById(id);
				queryRows(id);
				
			}
		});
    }
    
	
    /**
     * Inserts an Expense item which is expressed in expenseValues 
     * 
     * @param expenseValues the values to populate the Expense item with. Any that are missing will
     * be defaulted
     * @return the id of the expense added. This is in fact the SQLite ROWID value
     */
	public long insertStation() {

    
     // Opens the database object in "write" mode.
     SQLiteDatabase db = mDbHelper.getWritableDatabase();
     ContentValues values = new ContentValues();
     // Adds map entries for the user-controlled fields in the map
     values.put("stationName", "A Station");
     values.put("lastUpdatedDate", new GregorianCalendar().getTimeInMillis());
     
     // Performs the insert and returns the ID of the new expense item.
     long rowId = db.insert(
    	 "WeatherStations",        // The table to insert into.
         "stationName",  			// A hack, SQLite sets this column value to null
                                     // if values is empty.
         values                           // A map of column names, and the values to insert
                                          // into the columns.
     );
     //Log.d(TAG, "Insert ROWID= " + rowId);
     if(rowId < 0){
    	 throw new SQLException("Failed to insert expense.");
     }
     return rowId;
	}	
	
	  public int updateStationById( long id) {

	        // Opens the database object in "write" mode.
	        SQLiteDatabase db = mDbHelper.getWritableDatabase();
	        ContentValues values = new ContentValues();
	        values.put("stationName", "A different Station");
	        values.put("lastUpdatedDate", new GregorianCalendar().getTimeInMillis());
	        
	        int count;     
	        
	        String where = "stationId = ?";
	        String[] selectionArgs = {Long.toString(id)};

	        // Does the update and returns the number of rows updated.
	        count = db.update(
	        	"WeatherStations", // The database table name.
	            values,            // A map of column names and new values to use.
	            where,             // The WHERE clause to use
	            selectionArgs      // The where clause column values 
	                               
	        );
	        
	 
	        // Returns the number of rows updated.
	        Log.i(TAG, count + " Rows updated");
	        return count;
	    }
	  
	  private void queryRows(long id)
	  {
		  SQLiteDatabase db =  mDbHelper.getReadableDatabase();
		  SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		  qb.setTables("WeatherStations");
	      String where = "stationId = ?";
	      String[] selectionArgs = {id + ""};
	      Cursor c = qb.query(
		           db,            // The database to query
		           new String[] {"stationName, lastUpdatedDate"},    // The columns to return from the query
		           where,     // The columns for the where clause
		           selectionArgs, // The values for the where clause
		           null,          // don't group the rows
		           null,          // don't filter by row groups
		           "stationName ASC"   // The sort order
		       );
	      while(c.moveToNext()){
	    	Log.i(TAG, "stationName=" + c.getString(0));  
	    	Log.i(TAG, "lastUpdatedDate=" + c.getLong(1));
	      }
	  }
	    	
}
