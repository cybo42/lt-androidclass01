package com.ltree.expenses.data;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;


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
		// TODO Auto-generated constructor stub
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
        final String[] SELECTION_ARGS = { "Description5", "Description1", "Description0" };

         // Defines a query sort order
        final String SORT_ORDER = Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION + " ASC";

        // Query subtest 1.
        // If there are no records in the table, the returned cursor from a query should be empty.
        Cursor cursor = mDAO.queryExpenses( 
            null,                       // no projection, get all columns
            null,                       // no selection criteria, get all records
            null,                       // no selection arguments
            null                       // sort order
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
            null                       // sort order
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
              null                       // sort order
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
            null
        );

        // Query subtest 4
        // A query that uses selection criteria should return only those rows that match the
        // criteria. Use a projection so that it's easy to get the data in a particular column.
        projectionCursor = mDAO.queryExpenses(
            TEST_PROJECTION,           // get the title, expense, and mod date columns
            SELECTION_COLUMNS,         // select on the title column
            SELECTION_ARGS,             // select titles "expense0", "expense1", or "expense5"
            null
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

    
    
}
