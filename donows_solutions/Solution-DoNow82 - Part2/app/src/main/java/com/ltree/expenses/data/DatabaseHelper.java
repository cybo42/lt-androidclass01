package com.ltree.expenses.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
	
	private boolean mTestMode = false;

	private static final String DATABASE_NAME = "expenses.db";	// Should this be qualified?

	private static final int DATABASE_VERSION = 1;

    private static final String EXPENSES_TABLE_CREATE =
                "CREATE TABLE " + Expense.ExpenseItem.TABLE_NAME + " (" +
                Expense.ExpenseItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + 
                Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Expense.ExpenseItem.COLUMN_NAME_AMOUNT + " REAL, " + 
                Expense.ExpenseItem.COLUMN_NAME_EXPENSE_DATE + " TEXT " +
                ");";
    
    private static final String EXPENSES_DATA_POPULATE_1 =
    		"INSERT INTO " + Expense.ExpenseItem.TABLE_NAME + " VALUES (" +
    				"1, 'Samples Restaurant', 28.50, '130000000'" + ");";
    private static final String EXPENSES_DATA_POPULATE_2 = 
    		"INSERT INTO " + Expense.ExpenseItem.TABLE_NAME + " VALUES (" +
    				"2, 'Night in 6 star hotel', 22222.99, '130000000'" + ");";
    //
	private static final String EXPENSES_DATA_POPULATE_3 =
    		"INSERT INTO " + Expense.ExpenseItem.TABLE_NAME + " VALUES (" +
    				"3, 'Lunch at the Restaurant at the End of the Universe', 9991.99,'130430000'" + ");";

	private static final String EXPENSES_DATA_POPULATE_4 =
    		"INSERT INTO " + Expense.ExpenseItem.TABLE_NAME + " VALUES (" +
    				"4, 'Travel to Restaurant at the End of the Universe', 132221.99,'130210000'" + ");";
    	
    public DatabaseHelper(Context context) {
    	// If the 2nd parameter is null then the database is held in memory -- this form creates a file backed database
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Alternative constructor for test mode
     * @param context
     * @param testMode state of flag is irrelevant. The presence of the 2nd argument causes the in-memory db to be created
     */
    public DatabaseHelper(Context context, boolean testMode) {
    	// If the 2nd parameter is null then the database is held in memory -- this form creates an in memory database
    	super(context, null, null, DATABASE_VERSION);
    	mTestMode = testMode;
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// NOTE: the db passed in here seems to be linked to the file system
		// The exec does effect the database.
		db.execSQL(EXPENSES_TABLE_CREATE);
		// For the purposes of the course, add some dummy data items into the database
		if(!mTestMode){
			// Don't populate the DB in test mode
			db.execSQL(EXPENSES_DATA_POPULATE_1);
			db.execSQL(EXPENSES_DATA_POPULATE_2);
			db.execSQL(EXPENSES_DATA_POPULATE_3);
			db.execSQL(EXPENSES_DATA_POPULATE_4);
		}
	}

	/**
	 * Not sure what to do with this. Could ignore for the course...
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
