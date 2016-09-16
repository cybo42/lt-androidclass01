package lt.samples.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

public class DAO {

	public static final String COL_NAME = "stationName";
	public static final String COL_UPDATED = "lastUpdatedDate";
	public static final String COL_ID = "_id";
	public static final String TABLE_NAME = "WeatherStations";
	public static final String DEFAULT_SORT_ORDER = "stationName ASC";
	private static final String TAG = "DAO";
	private DatabaseHelper mDbHelper;
	

    /**
     * Normal constructor for the DAO
     * @param context
     */
	public DAO(Context context) {
	
		mDbHelper = new DatabaseHelper(context);
	}
	
	
	public long insertStation(ContentValues values) {


	     // If the incoming values map is not null, uses it for the new values.
	     if (values != null) {
	         values = new ContentValues(values);

	     } else {
	         // Otherwise, create a new value map
	         values = new ContentValues();
	     }

	     // Gets the current system time in milliseconds
	     Long now = Long.valueOf(System.currentTimeMillis());

	     // Populate the values object with dummy data if needed
	     if (values.containsKey(COL_NAME) == false) {
	         values.put(COL_NAME, "");
	     }        
	     if (values.containsKey(COL_UPDATED) == false) {
	         values.put(COL_UPDATED, now);
	     }        
	     
	     // Opens the database object in "write" mode.
	     SQLiteDatabase db = mDbHelper.getWritableDatabase();

	     // Performs the insert and returns the ID of the new expense item.
	     long rowId = db.insert(
	    		 TABLE_NAME,        // The table to insert into.
	    		 COL_NAME,  // A hack, SQLite sets this column value to null
	                                          // if values is empty.
	         values                           // A map of column names, and the values to insert
	                                          // into the columns.
	     );
	     //Log.d(TAG, "Insert ROWID= " + rowId);
	     if(rowId < 0){
	    	 throw new SQLException("Failed to insert station.");
	     }
	     return rowId;	
	}

	public Cursor queryStations(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	       // Constructs a new query builder and sets its table name
	       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	       qb.setTables(TABLE_NAME);
	       
	       return performQuery(qb, projection, selection, selectionArgs, sortOrder);
	}

	public Cursor queryStationById(int expenseId, String[] projection) {
	       // Constructs a new query builder and sets its table name
	       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	       qb.setTables(TABLE_NAME);
	       qb.appendWhere(COL_ID +    // the name of the ID column
	    	        "=" +
	    	        expenseId);
	       return performQuery(qb, projection, null, null, null);	
	}
	
	private Cursor performQuery(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
	       // Opens the database object in "read" mode, since no writes need to be done.
	       SQLiteDatabase db = mDbHelper.getReadableDatabase();       
	       String orderBy;
	       // If no sort order is specified, uses the default
	       if (TextUtils.isEmpty(sortOrder)) {
	           orderBy = DEFAULT_SORT_ORDER;
	       } else {
	           // otherwise, uses the incoming sort order
	           orderBy = sortOrder;
	       }
	       
	       Log.i(TAG, "performQuery. Projection: " + projection);
	       /*
	        * Performs the query. If no problems occur trying to read the database, then a Cursor
	        * object is returned; otherwise, the cursor variable contains null. If no records were
	        * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
	        */
	       Cursor c = qb.query(
	           db,            // The database to query
	           projection,    // The columns to return from the query
	           selection,     // The columns for the where clause
	           selectionArgs, // The values for the where clause
	           null,          // don't group the rows
	           null,          // don't filter by row groups
	           orderBy        // The sort order
	       );
	       return c;
	}	

	public int deleteStations(String where, String[] whereArgs) {
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int count = db.delete(
                    TABLE_NAME,  // The database table name
                    where,                     // The incoming where clause column names
                    whereArgs                  // The incoming where clause values
                );
        // Returns the number of rows deleted.
        return count;
	}

	public int deleteStationById(int id) {
        String where =
            COL_ID +                              // The ID column name
            " = " +                                          // test for equality
            id;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
	    // Performs the delete.
	    int count = db.delete(
	        TABLE_NAME,  // The database table name.
	        where,                // The final WHERE clause
	        null                  // The incoming where clause values.
	    );
		return count;
	}

	public int updateStations(ContentValues values, String where,
			String[] whereArgs) {
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;

        // Does the update and returns the number of rows updated.
        count = db.update(
            TABLE_NAME, // The database table name.
            values,                   // A map of column names and new values to use.
            where,                    // The where clause column names.
            whereArgs                 // The where clause column values to select on.
        );
        return count;
	}

	public int updateStationById(int id, ContentValues values) {
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        String where =
                        COL_ID +                              // The ID column name
                        " = " +  id;                                        // test for equality

        // Does the update and returns the number of rows updated.
        count = db.update(
            TABLE_NAME, // The database table name.
            values,                   // A map of column names and new values to use.
            where,               // The final WHERE clause to use
                                 // placeholders for whereArgs
            null                 // The where clause column values to select on, or
                                 // null if the values are in the where argument.
        );
 
        // Returns the number of rows updated.
        return count;
	}

}
