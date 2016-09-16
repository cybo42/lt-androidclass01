package com.ltree.expenses.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DataTestHelper {

	private static final String TAG = "DataTestHelper";
	
    // Number of milliseconds in one day (milliseconds * seconds * minutes * hours)
    public static final long ONE_DAY_MILLIS = 1000 * 60 * 60 * 24;

    // Number of milliseconds in one week
    public static final long ONE_WEEK_MILLIS = ONE_DAY_MILLIS * 7;

    // Creates a calendar object equal to January 1, 2010 at 12 midnight
    public static final GregorianCalendar TEST_CALENDAR =
        new GregorianCalendar(2010, Calendar.JANUARY, 1, 0, 0, 0);

    // Stores a timestamp value, set to an arbitrary starting point
    public final static long START_DATE = TEST_CALENDAR.getTimeInMillis();	
	
    // A URI that the provider does not offer, for testing error handling.
    public static final Uri INVALID_URI =
        Uri.withAppendedPath(Expense.ExpenseItem.CONTENT_URI, "invalid");
    
    // Contains the test data, as an array of Expense instances.
    public static final Expense[] TEST_EXPENSES = {
        new Expense("Description0", 0.0f, START_DATE),
        new Expense("Description1", 1.0f, START_DATE + ONE_DAY_MILLIS),
        new Expense("Description2", 2.0f, START_DATE + ONE_DAY_MILLIS * 2),
        new Expense("Description3", 3.0f, START_DATE +  ONE_DAY_MILLIS * 3),
        new Expense("Description4", 4.0f, START_DATE + ONE_DAY_MILLIS * 4),
        new Expense("Description5", 5.0f, START_DATE +  ONE_DAY_MILLIS * 5),
        new Expense("Description6", 6.0f, START_DATE +  ONE_DAY_MILLIS * 6),
        new Expense("Description7", 7.0f, START_DATE +  ONE_DAY_MILLIS * 7),
        new Expense("Description8", 8.0f, START_DATE +  ONE_DAY_MILLIS * 8),
        new Expense("Description9", 9.0f, START_DATE +  ONE_DAY_MILLIS * 9) };	
	
    /*
     * Sets up test data.
     * The test data is in an SQL database. It is created in setUp() without any data,
     * and populated in insertData if necessary.
     */
    public static void insertData(SQLiteDatabase database) {


        // Sets up test data
        for (int index = 0; index < TEST_EXPENSES.length; index++) {

            // Adds a record to the database.
        	database.insertOrThrow(
                Expense.ExpenseItem.TABLE_NAME,             // the table name for the insert
                Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION,      // column set to null if empty values map
                TEST_EXPENSES[index].getContentValues()  // the values map to insert
            );
        }
        Log.i(TAG, "Added " + TEST_EXPENSES.length +"rows to the database");
    } 
    	
}
