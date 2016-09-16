package com.ltree.expenses.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;


/**
 * Data Access Object supporting the Expenses Application
 * @author Course 2771 Development Team
 *
 */
public class DAO {

	@SuppressWarnings("unused")
	private static final String TAG="DAO";
	private DatabaseHelper mDbHelper;

	
    /**
     * Normal constructor for the DAO
     * @param context
     */
	public DAO(Context context) {
		// TODO 1 Create an instance of the Database helper
		// Hint: note that the DatabaseHelper constructor takes a context
		 mDbHelper = new DatabaseHelper(context);
	}
	
	
	/**
	 * Special constructor to support unit testing
	 * @param context
	 * @param testMode if true then an in-memory DB will be created which avoids breaking the real db
	 * during testing
	 */
	protected DAO(Context context, boolean testMode) {
		
		if(testMode){
			mDbHelper = new DatabaseHelper(context, testMode);
		} else {
			mDbHelper = new DatabaseHelper(context);
		}
	}	
	
	
	
	/**
	 * General query method for expenses. Allows where clause and sort order to be 
	 * specified @see {@link android.database.sqlite.SQLiteQueryBuilder#query(SQLiteDatabase, String[], String, String[], String, String, String)}
	 * @param projection The columns to return from the query
	 * @param selection A filter declaring columns for the where clause
	 * @param selectionArgs The values for the where clause - you may include ?s 
	 * in selection, which will be replaced by the values from selectionArgs
	 * @return
	 */
	public Cursor queryExpenses(String[] projection, String selection, String[] selectionArgs)
	{
		// TODO 2 Create an SQLiteQueryBuilder
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		// TODO 3 Specify which tables to query (Expense.ExpenseItem.TABLE_NAME)
		qb.setTables(Expense.ExpenseItem.TABLE_NAME);
		
		// TODO 4 get a readable instance of the database from the database helper
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		
		// TODO  5 Complete and un-comment the query command below.
		// The parameters you need have been passed in to this method - see the Javadoc comments for this
		// method for clues about the where clause parameters.
	       Cursor c = qb.query(
		           db,            // The database to query
		           projection,     // The columns to return from the query (the projection)
		           selection,     // The columns for the where clause
		           selectionArgs, 	  // The values for the where clause
		           null,          // don't group the rows
		           null,          // don't filter by row groups
		           Expense.ExpenseItem.DEFAULT_SORT_ORDER        // The sort order
		       );		
		// TODO 7 - Replace this with the returned cursor
		return c;
	}
	
	/**
     * Used by test classes to directly access the database helper
     * @return a handle to the database helper object for the provider's data.
     */
	public int deleteExpensesById(int id){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int deleted = db.delete(Expense.ExpenseItem.TABLE_NAME, Expense.ExpenseItem.COLUMN_NAME_ID + " = ?", new String[]{Integer.toString(id)});


		return deleted;
	}

    public DatabaseHelper getDbHelperForTest() {
        return mDbHelper;
    }    
    
}
