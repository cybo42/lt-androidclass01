package com.ltree.expenses.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;


public class ExpensesProviderTest extends ProviderTestCase2<ExpensesProvider> {
	
	@SuppressWarnings("unused")
	private static String TAG = "ExpensesProviderTest";



    // Contains a reference to the mocked content resolver for the provider under test.
    private MockContentResolver mMockResolver;

    // Contains an SQLite database, used as test data
    private SQLiteDatabase mDb;
    

	
    /*
     * Constructor for the test case class.
     * Calls the super constructor with the class name of the provider under test and the
     * authority name of the provider.
     */
	public ExpensesProviderTest() {
		super(ExpensesProvider.class, Expense.AUTHORITY);
	}
	
    /*
     * Sets up the test environment before each test method. Creates a mock content resolver,
     * gets the provider under test, and creates a new database for the provider.
     */
    @Override
    protected void setUp() throws Exception {
        // Calls the base class implementation of this method.
        super.setUp();

        // Gets the resolver for this test.
        mMockResolver = getMockContentResolver();

        /*
         * Gets a handle to the database underlying the provider. Gets the provider instance
         * created in super.setUp(), gets the DatabaseOpenHelper for the provider, and gets
         * a database object from the helper.
         */
        mDb = getProvider().getDbHelperForTest().getWritableDatabase();
    }

    /*
     *  This method is called after each test method, to clean up the current fixture. Since
     *  this sample test case runs in an isolated context, no cleanup is necessary.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }	
    
    
    /*
     * Tests the provider's publicly available URIs. If the URI is not one that the provider
     * understands, the provider should throw an exception. It also tests the provider's getType()
     * method for each URI, which should return the MIME type associated with the URI.
     */
    public void testUriAndGetType() {
        // Tests the MIME type for the expenses table URI.
        String mimeType = mMockResolver.getType(Expense.ExpenseItem.CONTENT_URI);
        assertEquals(Expense.ExpenseItem.CONTENT_TYPE, mimeType);

        // Creates a URI with a pattern for expense item ids. The id doesn't have to exist.
        Uri expenseIdUri = ContentUris.withAppendedId(Expense.ExpenseItem.CONTENT_ID_URI_BASE, 1);

        // Gets the expense ID URI MIME type.
        mimeType = mMockResolver.getType(expenseIdUri);
        assertEquals(Expense.ExpenseItem.CONTENT_ITEM_TYPE, mimeType);

        // Tests an invalid URI. This should throw an IllegalArgumentException.
        mimeType = mMockResolver.getType(DataTestHelper.INVALID_URI);
    }
    
    

    
    /*
     * Tests the provider's public API for querying data in the table, using the URI for
     * a dataset of records.
     */
    public void testQueriesOnExpensesUri() {
        // Defines a projection of column names to return for a query
        final String[] TEST_PROJECTION = {
            Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION,
            Expense.ExpenseItem.COLUMN_NAME_AMOUNT,
            Expense.ExpenseItem.COLUMN_NAME_EXPENSE_DATE
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
        Cursor cursor = mMockResolver.query(
            Expense.ExpenseItem.CONTENT_URI,  // the URI for the main data table
            null,                       // no projection, get all columns
            null,                       // no selection criteria, get all records
            null,                       // no selection arguments
            null                        // use default sort order
        );

         // Asserts that the returned cursor contains 4 records (the dummy entries)
        assertEquals(4, cursor.getCount());

         // Query subtest 2.
         // If the table contains records, the returned cursor from a query should contain records.

        // Inserts the test data into the provider's underlying data source
        DataTestHelper.insertData(mDb);

        // Gets all the columns for all the rows in the table
        cursor = mMockResolver.query(
            Expense.ExpenseItem.CONTENT_URI,  // the URI for the main data table
            null,                       // no projection, get all columns
            null,                       // no selection criteria, get all records
            null,                       // no selection arguments
            null                        // use default sort order
        );

        // Asserts that the returned cursor contains the same number of rows as the size of the
        // test data array.
        assertEquals(DataTestHelper.TEST_EXPENSES.length + 4, cursor.getCount());

        // Query subtest 3.
        // A query that uses a projection should return a cursor with the same number of columns
        // as the projection, with the same names, in the same order.
        Cursor projectionCursor = mMockResolver.query(
              Expense.ExpenseItem.CONTENT_URI,  // the URI for the main data table
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
        assertEquals(TEST_PROJECTION[2], projectionCursor.getColumnName(2));

        // Query subtest 4
        // A query that uses selection criteria should return only those rows that match the
        // criteria. Use a projection so that it's easy to get the data in a particular column.
        projectionCursor = mMockResolver.query(
            Expense.ExpenseItem.CONTENT_URI, // the URI for the main data table
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
    public void testQueriesOnExpensesIdUri() {
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

      // Constructs a URI that matches the provider's expenses id URI pattern, using an arbitrary
      // value of 1 as the expense ID.
      Uri expenseIdUri = ContentUris.withAppendedId(Expense.ExpenseItem.CONTENT_ID_URI_BASE, 1);

      // Queries the table with the expenses ID URI. This should return an empty cursor.
      Cursor cursor = mMockResolver.query(
          expenseIdUri, // URI pointing to a single record
          null,      // no projection, get all the columns for each record
          null,      // no selection criteria, get all the records in the table
          null,      // no need for selection arguments
          null       // default sort, by ascending title
      );

      // Asserts that the cursor is null.
      assertEquals(1,cursor.getCount());

      // Query subtest 2.
      // Tests that a query against a table containing records returns a single record whose ID
      // is the one requested in the URI provided.

      // Inserts the test data into the provider's underlying data source.
      DataTestHelper.insertData(mDb);

      // Queries the table using the URI for the full table.
      cursor = mMockResolver.query(
          Expense.ExpenseItem.CONTENT_URI, // the base URI for the table
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

      // Builds a URI based on the provider's content ID URI base and the saved expense ID.
      expenseIdUri = ContentUris.withAppendedId(Expense.ExpenseItem.CONTENT_ID_URI_BASE, inputExpenseId);

      // Queries the table using the content ID URI, which returns a single record with the
      // specified expense ID, matching the selection criteria provided.
      cursor = mMockResolver.query(expenseIdUri, // the URI for a single expense
          EXPENSE_ID_PROJECTION,                 // same projection, get ID and title columns
          SELECTION_COLUMNS,                  // same selection, based on title column
          SELECTION_ARGS,                     // same selection arguments, title = "Expense1"
          SORT_ORDER                          // same sort order returned, by title, ascending
      );

      // Asserts that the cursor contains only one row.
      assertEquals(1, cursor.getCount());

      // Moves to the cursor's first row, and asserts that this did not fail.
      assertTrue(cursor.moveToFirst());

      // Asserts that the expense ID passed to the provider is the same as the expense ID returned.
      assertEquals(inputExpenseId, cursor.getInt(0));
      
      // SUBTEST - repeat the above test but with no Selection Args specified.
      // Should still return a single row as specified by the expenseIdUri      
      
      // Queries the table using the content ID URI, which returns a single record with the
      // specified expense ID, matching the selection criteria provided.
      cursor = mMockResolver.query(expenseIdUri, // the URI for a single expense
          EXPENSE_ID_PROJECTION,                 // same projection, get ID and title columns
          null,                  // same selection, based on title column
          null,                     // same selection arguments, title = "Expense1"
          SORT_ORDER                          // same sort order returned, by title, ascending
      );


      
      // Asserts that the cursor contains only one row.
      assertEquals(1, cursor.getCount());

      // Moves to the cursor's first row, and asserts that this did not fail.
      assertTrue(cursor.moveToFirst());

      // Asserts that the expense ID passed to the provider is the same as the expense ID returned.
      assertEquals(inputExpenseId, cursor.getInt(0));
      
      
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
        Uri rowUri = mMockResolver.insert(
            Expense.ExpenseItem.CONTENT_URI,  // the main table URI
            expense.getContentValues()     // the map of values to insert as a new record
        );

        // Parses the returned URI to get the expense ID of the new expense. The ID is used in subtest 2.
        long expenseId = ContentUris.parseId(rowUri);

        // Does a full query on the table. Since insertData() hasn't yet been called, the
        // table should only contain the record just inserted.
        Cursor cursor = mMockResolver.query(
        	Expense.ExpenseItem.CONTENT_URI, // the main table URI
            null,                      // no projection, return all the columns
            null,                      // no selection criteria, return all the rows in the model
            null,                      // no selection arguments
            null                       // default sort order
        );

        // Asserts that there should be only 1 record (in addition to the 4 auto-created ones).
        assertEquals(5, cursor.getCount());

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
            rowUri = mMockResolver.insert(Expense.ExpenseItem.CONTENT_URI, values);
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
        int rowsDeleted = mMockResolver.delete(
            Expense.ExpenseItem.CONTENT_URI, // the base URI of the table
            SELECTION_COLUMNS,         // select based on the title column
            SELECTION_ARGS             // select title = "Expense0"
        );

        // Assert that the deletion did not work. The number of deleted rows should be zero.
        assertEquals(0, rowsDeleted);

        // Subtest 2.
        // Tries to delete an existing record. Repeats the previous subtest, but inserts data first.

        // Inserts data into the model.
        DataTestHelper.insertData(mDb);

        // Uses the same parameters to try to delete the row with title "Expense0"
        rowsDeleted = mMockResolver.delete(
            Expense.ExpenseItem.CONTENT_URI, // the base URI of the table
            SELECTION_COLUMNS,         // same selection column, "title"
            SELECTION_ARGS             // same selection arguments, title = "Expense0"
        );

        // The number of deleted rows should be 1.
        assertEquals(1, rowsDeleted);

        // Tests that the record no longer exists. Tries to get it from the table, and
        // asserts that nothing was returned.

        // Queries the table with the same selection column and argument used to delete the row.
        Cursor cursor = mMockResolver.query(
            Expense.ExpenseItem.CONTENT_URI, // the base URI of the table
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
        int rowsUpdated = mMockResolver.update(
            Expense.ExpenseItem.CONTENT_URI,  // the URI of the data table
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
        rowsUpdated = mMockResolver.update(
            Expense.ExpenseItem.CONTENT_URI,   // The URI of the data table
            values,                      // the same map of updates
            SELECTION_COLUMNS,            // same selection, based on the title column
            selectionArgs                // same selection argument, to select "title = Expense1"
        );

        // Asserts that only one row was updated. The selection criteria evaluated to
        // "title = Expense1", and the test data should only contain one row that matches that.
        assertEquals(1, rowsUpdated);

    }

}
