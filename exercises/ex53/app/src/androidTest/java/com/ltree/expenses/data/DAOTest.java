package com.ltree.expenses.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;


public class DAOTest extends AndroidTestCase {
	
	private static String TAG = "DAOTest";

    // Contains an SQLite database, used as test data
    private SQLiteDatabase mDb;
    
    private DAO mDAO;
    

    /*
     * Constructor for the test case class.
     * Calls the super constructor with the class name of the provider under test and the
     * authority name of the provider.
     */
	public DAOTest() {
		super();
	}
	
    /*
     * Sets up the test environment before each test method. Creates a mock content resolver,
     * gets the provider under test, and creates a new database for the provider.
     */
    @Override
    protected void setUp() throws Exception {
        // Calls the base class implementation of this method.
        super.setUp();

        // Sets up a test instance of the DAO (using in-memory DB)
        mDAO = new DAO(getContext(), true);
        /*
         * Gets a handle to the database underlying the provider. Gets the provider instance
         * created in super.setUp(), gets the DatabaseOpenHelper for the provider, and gets
         * a database object from the helper.
         */
        mDb = mDAO.getDbHelperForTest().getWritableDatabase();
    }

    /*
     *  This method is called after each test method, to clean up the current fixture. Since
     *  this sample test case runs in an isolated context, no cleanup is necessary.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }	
    
    
  
    

    /**
     * Helper to get the first valid expense ID from the database
     * @return
     */
    private int getValidExpenseItemId()
    {
        final String[] TEST_PROJECTION = {
                Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION,
                Expense.ExpenseItem.COLUMN_NAME_AMOUNT
            };    	
    	Cursor c = mDb.query(Expense.ExpenseItem.TABLE_NAME, TEST_PROJECTION, null, null, null, null, null);
    	c.moveToFirst();
    	return c.getInt(0);
    	
    }
    
    /*
     * Tests the provider's public API for querying data in the table, using the URI for
     * a dataset of records.
     */
    public void testQueryExpenses() {
        // Defines a projection of column names to return for a query
        final String[] TEST_PROJECTION = {
            Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION,
            Expense.ExpenseItem.COLUMN_NAME_AMOUNT
        };

        // Defines a selection column for the query. When the selection columns are passed
        // to the query, the selection arguments replace the placeholders.
        final String TITLE_DESCRIPTION = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " = " + "?";

        // Defines the selection columns for a query.
        final String SELECTION_COLUMNS =
        	TITLE_DESCRIPTION + " OR " + TITLE_DESCRIPTION + " OR " + TITLE_DESCRIPTION;

         // Defines the arguments for the selection columns.
        final String[] SELECTION_ARGS = { "Description0", "Description1", "Description5" };

         // Defines a query sort order
        final String SORT_ORDER = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " ASC";

        // Query subtest 1.
        // If there are no records in the table, the returned cursor from a query should be empty.
        Cursor cursor = mDAO.queryExpenses( 
            null,                       // no projection, get all columns
            null,                       // no selection criteria, get all records
            null,                       // no selection arguments
            null                        // use default sort order
        );

         // Asserts that the returned cursor contains no records
        assertEquals(0, cursor.getCount());

         // Query subtest 2.
         // If the table contains records, the returned cursor from a query should contain records.

        // Inserts the test data into the provider's underlying data source
        DataTestHelper.insertData(mDb);

        // Gets all the columns for all the rows in the table
        cursor = mDAO.queryExpenses(
            null,                       // no projection, get all columns
            null,                       // no selection criteria, get all records
            null,                       // no selection arguments
            null                        // use default sort order
        );

        // Asserts that the returned cursor contains the same number of rows as the size of the
        // test data array.
        assertEquals(DataTestHelper.TEST_EXPENSES.length, cursor.getCount());

        // Query subtest 3.
        // A query that uses a projection should return a cursor with the same number of columns
        // as the projection, with the same names, in the same order.
        Cursor projectionCursor = mDAO.queryExpenses(
              TEST_PROJECTION,            // get the title, expense, and mod date columns
              null,                       // no selection columns, get all the records
              null,                       // no selection criteria
              null                        // use default the sort order
        );

        // Asserts that the number of columns in the cursor is the same as in the projection
        assertEquals(TEST_PROJECTION.length, projectionCursor.getColumnCount());

        // Asserts that the names of the columns in the cursor and in the projection are the same.
        // This also verifies that the names are in the same order.
        assertEquals(TEST_PROJECTION[0], projectionCursor.getColumnName(0));
        assertEquals(TEST_PROJECTION[1], projectionCursor.getColumnName(1));

        // Query subtest 4
        // A query that uses selection criteria should return only those rows that match the
        // criteria. Use a projection so that it's easy to get the data in a particular column.
        projectionCursor = mDAO.queryExpenses(
            TEST_PROJECTION,           // get the title, expense, and mod date columns
            SELECTION_COLUMNS,         // select on the title column
            SELECTION_ARGS,            // select titles "expense0", "expense1", or "expense5"
            SORT_ORDER                 // sort ascending on the title column
        );

        // Asserts that the cursor has the same number of rows as the number of selection arguments
        assertEquals(SELECTION_ARGS.length, projectionCursor.getCount());

        int index = 0;

        while (projectionCursor.moveToNext()) {

            // Asserts that the selection argument at the current index matches the value of
            // the title column (column 0) in the current record of the cursor
            assertEquals(SELECTION_ARGS[index], projectionCursor.getString(0));

            index++;
        }

        // Asserts that the index pointer is now the same as the number of selection arguments, so
        // that the number of arguments tested is exactly the same as the number of rows returned.
        assertEquals(SELECTION_ARGS.length, index);

    }    

    /*
     * Tests queries against the provider, using the expense id URI. This URI encodes a single
     * record ID. The provider should only return 0 or 1 record.
     */
    public void testQueryExpenseById() {
      // Defines the selection column for a query. The "?" is replaced by entries in the
      // selection argument array
      final String SELECTION_COLUMNS = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " = " + "?";

      // Defines the argument for the selection column.
      final String[] SELECTION_ARGS = { "Description1" };

      // A sort order for the query.
      final String SORT_ORDER = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " ASC";

      // Creates a projection includes the expense id column, so that expense id can be retrieved.
      final String[] EXPENSE_ID_PROJECTION = {
           Expense.ExpenseItem.COLUMN_NAME_ID,                 // The Expenses class extends BaseColumns,
                                              // which includes _ID as the column name for the
                                              // record's id in the data model
           Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION};  // The expense's title

      // Query subtest 1.
      // Tests that a query against an empty table returns null.

      

      // Queries the table with an expense ID of 1. Nothing expected to be returned
      Cursor cursor = mDAO.queryExpenseById(
          1, 		// The id of the expense to be fetched
          null      // no projection, get all the columns for each record
      );

      Log.i(TAG, "Rows = " + cursor.getCount());
      // Asserts that the cursor is null.
      assertEquals(0,cursor.getCount());

      // Query subtest 2.
      // Tests that a query against a table containing records returns a single record whose ID
      // is the one requested in the URI provided.

      // Inserts the test data into the provider's underlying data source.
      DataTestHelper.insertData(mDb);
      
      // Gets all the columns for all the rows in the table (paranoid test to make sure insertData() actually worked.
      cursor = mDAO.queryExpenses(
          null,                       // no projection, get all columns
          null,                       // no selection criteria, get all records
          null,                       // no selection arguments
          null                        // use default sort order
      );

      // Asserts that the returned cursor contains the same number of rows as the size of the
      // test data array.
      assertEquals(DataTestHelper.TEST_EXPENSES.length, cursor.getCount());      

      // Queries the table using the URI for the full table. (just to get a valid expense id)
      cursor = mDAO.queryExpenses(
          EXPENSE_ID_PROJECTION,        // returns the ID and title columns of rows
          SELECTION_COLUMNS,         // select based on the title column
          SELECTION_ARGS,            // select title of "Expense1"
          SORT_ORDER                 // sort order returned is by title, ascending
      );

      // Asserts that the cursor contains only one row.
      assertEquals(1, cursor.getCount());

      // Moves to the cursor's first row, and asserts that this did not fail.
      assertTrue(cursor.moveToFirst());

      // Saves the record's expense ID.
      int inputExpenseId = cursor.getInt(0);

      Log.i(TAG, "Expense ID: " + inputExpenseId);

      // Queries the table using the expense Id, which returns a single record with the
      // specified expense ID
      cursor = mDAO.queryExpenseById(inputExpenseId,//the id of a  single expense
          EXPENSE_ID_PROJECTION                 	// same projection, get ID and title columns
      );

      // Asserts that the cursor contains only one row.
      assertEquals(1, cursor.getCount());

      // Moves to the cursor's first row, and asserts that this did not fail.
      assertTrue(cursor.moveToFirst());

      // Asserts that the expense ID passed to the provider is the same as the expense ID returned.
      assertEquals(inputExpenseId, cursor.getInt(0));
      
      // SUBTEST - repeat the above test but with no Selection Args specified.
      // Should still return a single row as specified by the expenseIdUri      
      
      // Queries the table with the expenses ID URI. This should return an empty cursor.
      // using an arbitrary value of 1 as the expense ID.
      cursor = mDAO.queryExpenseById(
    	  inputExpenseId, 		// The id of the expense to be fetched
    	  null      // no projection, get all the columns for each record
      );


      
      // Asserts that the cursor contains only one row.
      assertEquals(1, cursor.getCount());

      // Moves to the cursor's first row, and asserts that this did not fail.
      assertTrue(cursor.moveToFirst());
      int idCol = cursor.getColumnIndexOrThrow(Expense.ExpenseItem.COLUMN_NAME_ID);
      // Asserts that the expense ID passed to the provider is the same as the expense ID returned.
      assertEquals(inputExpenseId, cursor.getInt(idCol));
      
      
    }
    
    
    /*
     *  Tests inserts into the data model.
     */
    public void testInserts() {
        // Creates a new expense instance with ID of 30.
        Expense expense = new Expense(
            "Expense Description", // the note's title
            300.0f, // the expense's content
            DataTestHelper.START_DATE + DataTestHelper.ONE_WEEK_MILLIS * 3
        );


        // Insert subtest 1.
        // Inserts a row using the new expense instance.
        // No assertion will be done. The insert() method either works or throws an Exception
        long expenseId = mDAO.insertExpense(
            expense.getContentValues()     // the map of values to insert as a new record
        );


        // Does a full query on the table. Since insertData() hasn't yet been called, the
        // table should only contain the record just inserted.
        Cursor cursor = mDAO.queryExpenses(
            null,                      // no projection, return all the columns
            null,                      // no selection criteria, return all the rows in the model
            null,                      // no selection arguments
            null                       // default sort order
        );

        // Asserts that there should be only 1 record.
        assertEquals(1, cursor.getCount());

        // Moves to the first (and only) record in the cursor and asserts that this worked.
        assertTrue(cursor.moveToFirst());

        // Since no projection was used, get the column indexes of the returned columns
        int descriptionIndex = cursor.getColumnIndex(Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION);
        int amountIndex = cursor.getColumnIndex(Expense.ExpenseItem.COLUMN_NAME_AMOUNT);

        // Tests each column in the returned cursor against the data that was inserted, comparing
        // the field in the expenseInfo object to the data at the column index in the cursor.
        assertEquals(expense.description, cursor.getString(descriptionIndex));
        assertEquals(expense.amount, cursor.getDouble(amountIndex));

        // Insert subtest 2.
        // Tests that we can't insert a record whose id value already exists.

        // Defines a ContentValues object so that the test can add a expense ID to it.
        ContentValues values = expense.getContentValues();

        // Adds the expense ID retrieved in subtest 1 to the ContentValues object.
        values.put(Expense.ExpenseItem._ID, (int) expenseId);

        // Tries to insert this record into the table. This should fail and drop into the
        // catch block. If it succeeds, issue a failure message.
        try {
        	expenseId = mDAO.insertExpense(values);
            fail("Expected insert failure for existing record but insert succeeded.");
        } catch (Exception e) {
          // succeeded, so do nothing.
        }
    }    
    
    
    /*
     * Tests deletions from the data model.
     */
    public void testDeletes() {
        // Subtest 1.
        // Tries to delete a record from a data model that is empty.

        // Sets the selection column to "description"
        final String SELECTION_COLUMNS = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " = " + "?";

        // Sets the selection argument "Description3"
        final String[] SELECTION_ARGS = { "Description3" };

        // Tries to delete rows matching the selection criteria from the data model.
        int rowsDeleted = mDAO.deleteExpenses(
            SELECTION_COLUMNS,         // select based on the title column
            SELECTION_ARGS             // select title = "Expense0"
        );

        // Assert that the deletion did not work. The number of deleted rows should be zero.
        assertEquals(0, rowsDeleted);

        // Subtest 2.
        // Tries to delete an existing record. Repeats the previous subtest, but inserts data first.

        // Inserts data into the model.
        DataTestHelper.insertData(mDb);

        // Tries to delete rows matching the selection criteria from the data model.
        rowsDeleted = mDAO.deleteExpenses(
            SELECTION_COLUMNS,         // select based on the title column
            SELECTION_ARGS             // select title = "Expense0"
        );

        // The number of deleted rows should be 1.
        assertEquals(1, rowsDeleted);

        // Tests that the record no longer exists. Tries to get it from the table, and
        // asserts that nothing was returned.

        // Queries the table with the same selection column and argument used to delete the row.
        Cursor cursor = mDAO.queryExpenses(
            null,                      // no projection, return all columns
            SELECTION_COLUMNS,         // select based on the title column
            SELECTION_ARGS,            // select title = "Expense0"
            null                       // use the default sort order
        );

        // Asserts that the cursor is empty since the record had already been deleted.
        assertEquals(0, cursor.getCount());
    }
    /*
     * Tests updates to the data model.
     */
    public void testUpdates() {
        // Selection column for identifying a record in the data model.
        final String SELECTION_COLUMNS = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " = " + "?";

        // Selection argument for the selection column.
        final String[] selectionArgs = { "Description4" };

        // Defines a map of column names and values
        ContentValues values = new ContentValues();

        // Subtest 1.
        // Tries to update a record in an empty table.

        // Sets up the update by putting the "amount" column and a value into the values map.
        values.put(Expense.ExpenseItem.COLUMN_NAME_AMOUNT, "99.9f");

        // Tries to update the table
        int rowsUpdated = mDAO.updateExpenses(
            values,                     // a map of the updates to do (column title and value)
            SELECTION_COLUMNS,           // select based on the title column
            selectionArgs               // select "title = Expense1"
        );

        // Asserts that no rows were updated.
        assertEquals(0, rowsUpdated);

        // Subtest 2.
        // Builds the table, and then tries the update again using the same arguments.

        // Inserts data into the model.
        DataTestHelper.insertData(mDb);

        //  Does the update again, using the same arguments as in subtest 1.
        rowsUpdated = mDAO.updateExpenses(
                values,                     // a map of the updates to do (column title and value)
                SELECTION_COLUMNS,           // select based on the title column
                selectionArgs               // select "title = Expense1"
            );

        // Asserts that only one row was updated. The selection criteria evaluated to
        // "title = Expense1", and the test data should only contain one row that matches that.
        assertEquals(1, rowsUpdated);

    }
    
    
    /*
     * Tests updates to the data model.
     */
    public void testUpdateById() {
        // Selection column for identifying a record in the data model.
        final String SELECTION_COLUMNS = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " = " + "?";

        // Selection argument for the selection column.
        final String[] selectionArgs = { "Description4" };

        // Defines a map of column names and values
        ContentValues values = new ContentValues();

      
        
        // Subtest 1.
        // Tries to update a record in an empty table.
        int expenseId = 1;
        // Sets up the update by putting the "amount" column and a value into the values map.
        values.put(Expense.ExpenseItem.COLUMN_NAME_AMOUNT, "99.9f");

        // Tries to update the table
        int rowsUpdated = mDAO.updateExpenseById(
       		expenseId,
            values                     // a map of the updates to do (column title and value)
        );

        // Asserts that no rows were updated.
        assertEquals(0, rowsUpdated);

        // Subtest 2.
        // Builds the table, and then tries the update again using the same arguments.

        DataTestHelper.insertData(mDb);
        // Get the ID of an Expense from the database (depends on Query methods working)
        expenseId = getValidExpenseItemId();

        Log.i(TAG, "Expense ID: " + expenseId);  

        //  Does the update again, using the same arguments as in subtest 1.
        rowsUpdated = mDAO.updateExpenses(
                values,                     // a map of the updates to do (column title and value)
                SELECTION_COLUMNS,           // select based on the title column
                selectionArgs               // select "title = Expense1"
            );

        // Asserts that only one row was updated. The selection criteria evaluated to
        // "title = Expense1", and the test data should only contain one row that matches that.
        assertEquals(1, rowsUpdated);

    }     
    

}
