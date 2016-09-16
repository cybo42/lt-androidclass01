package com.ltree.expenses.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

// NOTE: Lots of this is auto-generated when you create the class with the Eclipse wizard
// NOTE: Don't forget to register this in the manifest
public class ExpensesProvider extends ContentProvider {
	
	private DAO mDAO;
	
    /**
     * A UriMatcher instance
     */
    private static final UriMatcher sUriMatcher;
    
 
    
    /*
     * Constants used by the Uri matcher to choose an action based on the pattern
     * of the incoming URI
     */
    // The incoming URI matches the Expenses URI pattern
    private static final int EXPENSES = 1;

    // The incoming URI matches the Note ID URI pattern
    private static final int EXPENSES_ID = 2;
    
    /**
     * Static initializer block gets run just once for the Class
     */
    static {

        /*
         * Creates and initializes the URI matcher
         */
        // Create a new instance
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        // Add a pattern that routes URIs terminated with "expenses" to a NOTES operation
        sUriMatcher.addURI(Expense.AUTHORITY, "expenses", EXPENSES);

        // Add a pattern that routes URIs terminated with "expenses" plus an integer
        // to a expense ID operation
        sUriMatcher.addURI(Expense.AUTHORITY, "expenses/#", EXPENSES_ID);
        

    }
    
    
    
    /**
     * {@link android.content.ContentResolver#getType(Uri)}.
     * Returns the MIME data type of the URI given as a parameter.
     *
     * @param uri The URI whose MIME type is desired.
     * @return The MIME type of the URI.
     * @throws IllegalArgumentException if the incoming URI pattern is invalid.
     */
    @Override
    public String getType(Uri uri) {

        /**
         * Chooses the MIME type based on the incoming URI pattern
         */
        switch (sUriMatcher.match(uri)) {
            // If the pattern is for expenses returns the returns the expense ID content type.
            case EXPENSES:
                return Expense.ExpenseItem.CONTENT_TYPE;
            // If the pattern is for expense IDs, returns the expense ID content type.
            case EXPENSES_ID:
                return Expense.ExpenseItem.CONTENT_TYPE;
            // If the URI pattern doesn't match any permitted patterns, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
     }
    

    /**
     * {@link android.content.ContentResolver#delete(Uri, String, String[])}.
     * @return If a "where" clause is used, the number of rows affected is returned, otherwise
     * 0 is returned. To delete all rows and get a row count, use "1" as the where clause.
     * @throws IllegalArgumentException if the incoming URI pattern is invalid.
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
            	int expenseId = Integer.parseInt(uri.getPathSegments().get(Expense.ExpenseItem.EXPENSE_ID_PATH_POSITION));
            	count = mDAO.deleteExpensesById(expenseId);
                break;

            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        /*Gets a handle to the content resolver object for the current context, and notifies it
         * that the incoming URI changed. The object passes this along to the resolver framework,
         * and observers that have registered themselves for the provider are notified.
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;
    }



	@Override
	public Uri insert(Uri uri, ContentValues values) {
	       // Validates the incoming URI. Only the full provider URI is allowed for inserts.
        if (sUriMatcher.match(uri) != EXPENSES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long rowId = mDAO.insertExpense(values);

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            // Creates a URI with the expense ID pattern and the new row ID appended to it.
            Uri expenseUri = ContentUris.withAppendedId(Expense.ExpenseItem.CONTENT_ID_URI_BASE, rowId);

            // Notifies observers registered against this provider that the data changed.
            getContext().getContentResolver().notifyChange(expenseUri, null);
            return expenseUri;
        }

        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub

	    mDAO = new DAO(getContext());   
		return false;
	}

	  /**

	    * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}.
	    * Queries the database and returns a cursor containing the results.
	    *
	    * @return A cursor containing the results of the query. The cursor exists but is empty if
	    * the query returns no results or an exception occurs.
	    * @throws IllegalArgumentException if the incoming URI pattern is invalid.
	    */
	   @Override
	   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	           String sortOrder) {

		   Cursor cursor = null;

	       /**
	        * Choose the projection and adjust the "where" clause based on URI pattern-matching.
	        */
	       switch (sUriMatcher.match(uri)) {
	           // If the incoming URI is for expenses, chooses the Expenses projection
	           case EXPENSES:
	               cursor = mDAO.queryExpenses(projection, selection, selectionArgs, sortOrder);
	               break;

	           /* If the incoming URI is for a single expense identified by its ID, chooses the
	            * expense ID projection, and appends "_ID = <expenseID>" to the where clause, so that
	            * it selects that single expense
	            */
	           case EXPENSES_ID:
	        	   int expenseId = Integer.parseInt(uri.getPathSegments().get(Expense.ExpenseItem.EXPENSE_ID_PATH_POSITION));
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


    /**
     * {@link android.content.ContentResolver#update(Uri,ContentValues,String,String[])}
     * @return The number of rows updated.
     * @throws IllegalArgumentException if the incoming URI pattern is invalid.
     */
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

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
                // From the incoming URI, get the expense ID
                int expenseId = Integer.parseInt(uri.getPathSegments().get(Expense.ExpenseItem.EXPENSE_ID_PATH_POSITION));
                count = mDAO.updateExpenseById(expenseId, values);
                break;
            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /*Gets a handle to the content resolver object for the current context, and notifies it
         * that the incoming URI changed. The object passes this along to the resolver framework,
         * and observers that have registered themselves for the provider are notified.
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows updated.
        return count;
    }

	
    /**
     * A test package can call this to get a handle to the database underlying NotePadProvider,
     * so it can insert test data into the database. The test case class is responsible for
     * instantiating the provider in a test context; {@link android.test.ProviderTestCase2} does
     * this during the call to setUp()
     *
     * @return a handle to the database helper object for the provider's data.
     */
    public DatabaseHelper getDbHelperForTest() {
        return mDAO.getDbHelperForTest();
    }
}
