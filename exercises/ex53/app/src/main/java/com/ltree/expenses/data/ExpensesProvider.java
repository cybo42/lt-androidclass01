package com.ltree.expenses.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;


/**
 * @author Course 2771 Development Team
 *
 */
public class ExpensesProvider extends ContentProvider {
	
	/** The DAO providing access to the SQLite database */
	private DAO mDAO;
    /**
     * UriMatcher used to determine which of the possible URL patterns is being used to address the ContentProvider
     * The URI matcher converts URL patterns to int values which are then used in switch statements
     */
    private static final UriMatcher sUriMatcher;
    /*
     * Constants used by the Uri matcher to choose an action based on the pattern
     * of the incoming URI
     */
    // The incoming URI matches the URI for a list of expenses
    private static final int EXPENSES = 1;

    // The incoming URI matches the URI for a single expense specified by ID
    private static final int EXPENSES_ID = 2;
    	
   
    static {
    	// Note that the static block is run only once during the application life-cycle
    	// Thus the setup is done once for all instances of the class
        /*
         * Creates and initializes the URI matcher
         */
        // Create a new instance
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add the pattern matching the URI ending with expenses
        // Used to route the request to operations not specifiying an expense Id value
        sUriMatcher.addURI(Expense.AUTHORITY, "expenses", EXPENSES);
        // Add the pattern matching the URI ending with expenses plus an integer
        // representing the ID of an expense
        sUriMatcher.addURI(Expense.AUTHORITY, "expenses/#", EXPENSES_ID);      

    }
        
	/** 
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 * @param uri The URI for the item to delete
	 * @param where The where string which may have place holders
	 * @param whereArgs the arguments for the where string
	 */

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
        int count;

        // Does the delete based on the incoming URI pattern.
        switch (sUriMatcher.match(uri)) {

            // If the incoming pattern matches the general pattern for expenses, does a delete
            // based on the incoming "where" columns and arguments.
            case EXPENSES:
            	count = mDAO.deleteExpenses(where, whereArgs);
            	break;
                // If the incoming URI matches a single expense ID, does the delete based on the
                // incoming data, but modifies the where clause to restrict it to the
                // particular expense ID.
            case EXPENSES_ID:
            	long expenseId = ContentUris.parseId(uri);
            	count = mDAO.deleteExpensesById(expenseId);
                break;

            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
			// Notify any observers that content has changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;

	}
	

	@Override
	/**
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 *	Returns the content type information to identify the data which will be returned from a particular URI
	 *  In our case, this will always be vnd.android.cursor.item/vnd.ltree.expenses.expense 
	 */
	public String getType(Uri uri) {
        /**
         * Chooses the MIME type based on the incoming URI pattern
         */
        switch (sUriMatcher.match(uri)) {
            // If the pattern is for expenses returns the expense ID content type.
            case EXPENSES:
                return Expense.ExpenseItem.CONTENT_TYPE;
            // If the pattern is for expense IDs, returns the expense ID content type.
            case EXPENSES_ID:
                return Expense.ExpenseItem.CONTENT_ITEM_TYPE;
            // If the URI pattern doesn't match any permitted patterns, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	/** 
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 * Inserts the supplied values at the location specified by the uri
	 */

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		       // Validates the incoming URI. Only the full provider URI is allowed for inserts.
	        if (sUriMatcher.match(uri) != EXPENSES) {
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }
	        // Hint where have the values come from?
	        long expenseId = mDAO.insertExpense(values);

	        // If the insert succeeded, the row ID exists.
	        if (expenseId > 0) {
	        	// As a row was inserted, observers need to be notified of the change...
	        	  
	            // Creates a URI with the expense ID pattern and the new row ID appended to it.
	            Uri expenseUri = ContentUris.withAppendedId(Expense.ExpenseItem.CONTENT_ID_URI_BASE, expenseId);
				
	            // Notifies observers registered against this provider that the data changed.
	            getContext().getContentResolver().notifyChange(expenseUri, null);
	            return expenseUri;
	        }

	        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
	        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	
	public boolean onCreate() {
		mDAO = new DAO(getContext());
		// Return true to indicate the ContentProvider is loaded OK
		return true;
	}


	@Override
	/**
	 * Queries the database and returns a cursor containing the results.
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 * @param projection The columns to return
	 * @param selection A filter declaring which rows to return
	 * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs
	 * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
	 */
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		   Cursor cursor = null;

	       /**
	        * Choose the projection and adjust the "where" clause based on URI pattern-matching.
	        */
	       switch (sUriMatcher.match(uri)) {
	           
	           case EXPENSES:
	        	   // If the incoming URI is for expenses, call the queryExpenses method on the DAO	        	   
	               cursor = mDAO.queryExpenses(projection, selection, selectionArgs, sortOrder);
	               break;
	           case EXPENSES_ID:
	               // If the incoming URI is for a single expense identified by its ID, call the queryExpenseById() method on the DAO
	        	   // Hint: use a ContentUris method
	        	   
	        	   long expenseId = ContentUris.parseId(uri);
	        	   
	        	   cursor = mDAO.queryExpenseById(expenseId, projection);
	               break;

	           default:
	               // If the URI doesn't match any of the known patterns, throw an exception.
	               throw new IllegalArgumentException("Unknown URI " + uri);
	       }
	       
	       // Tells the Cursor what URI to watch, so it knows when its source data changes
 	        cursor.setNotificationUri(getContext().getContentResolver(), uri);
 	        
	       return cursor;

	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
        int count;
        // Does the update based on the incoming URI pattern
        switch (sUriMatcher.match(uri)) {

            // If the incoming URI matches the general expenses pattern, does the update based on
            // the incoming data.
            case EXPENSES:

                // Does the update and returns the number of rows updated.
                count = mDAO.updateExpenses(values, where, whereArgs);
                break;

            // If the incoming URI matches a single expense ID, does the update based on the incoming
            // data, but modifies the where clause to restrict it to the particular expense ID.
            case EXPENSES_ID:
				long expenseId = ContentUris.parseId(uri);
				count = mDAO.updateExpenseById(expenseId, values);
                break;
            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

		getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows updated.
        return count;
	}

    /**
     * A test package can call this to get a handle to the database underlying Provider,
     * so it can insert test data into the database. 
     *
     * @return a handle to the database helper object for the provider's data.
     */
    public DatabaseHelper getDbHelperForTest() {
        return mDAO.getDbHelperForTest();
    }	
    
}
