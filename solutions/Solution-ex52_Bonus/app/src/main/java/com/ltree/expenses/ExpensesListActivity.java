package com.ltree.expenses;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ltree.expenses.data.DAO;
import com.ltree.expenses.data.Expense;

import java.util.ArrayList;

// TODO 1 modify the class declaration to implement LoaderManager.LoaderCallbacks<Cursor>
public class ExpensesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @SuppressWarnings("unused")
    private static final String TAG = "ExpenseListActivity";
	private SimpleCursorAdapter mAdapter;
	private Uri mBaseUri;

    private ArrayAdapter<String> mExpensesAdapter;
    private ArrayList<String> mExpensesArrayList;
    private ListView mList;

    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mList = (ListView)findViewById(R.id.expLst_listView);

		// TODO 2 get the Intent associated with this Activity
		Intent intent =getIntent();
		// set baseUri to the Uri specified with the launching Intent (as data)
		mBaseUri = intent.getData();
		// TODO 3 If no Data was passed in the launching Intent, then default it
		// to Expense.ExpenseItem.CONTENT_URI
		if (mBaseUri == null) {
			mBaseUri =Expense.ExpenseItem.CONTENT_URI;
		}

		// Call the queryExpenses method on the DAO you just created.
		// The first parameter is an array representing the columns you wish to
		// be returned
		// Use Expense.ExpenseItem.LIST_PROJECTION for this
        // We want to return all the expenses so selection and selectionArgs can both be null
        // TODO 4 Comment out this query from the last exercise which used the
        // DAO directly
        //Cursor cursor = dao.queryExpenses(Expense.ExpenseItem.LIST_PROJECTION, null, null);


		// The names of the cursor columns to display in the view, initialized
		// to the description column
		// Note the creation of these two arrays specifying the columns to be
		// displayed
		// and the resource ID's to use to display them
		// android.R.id.text1 is a built in resource to show simple text
		// TODO 5 Note that this is the same as when we used the DAO
        String[] dataColumns = { Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION } ;
        int[] viewIDs = { android.R.id.text1 };


		// Create a new SimpleCursorAdapter
		// Creates the backing adapter for the ListView.
		// TODO 6 Replace cursor with null
		mAdapter = new SimpleCursorAdapter(this, // The Context for the ListView
                android.R.layout.simple_list_item_single_choice,      // Points to the XML for a list item
                null,                      // The cursor to get items from
                dataColumns,						// The array of Strings holding the names of the data columns to display
                viewIDs,						// The array of resource ids used to display the data
                0
		// TODO 7 Add the flags parameter (0) to method call
        );

        // TODO 8 Initialize the LoaderManager
        getLoaderManager().initLoader(0, null, this);

        // set the new adapter as the list adapter
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new ListClickHandler());

        registerForContextMenu(mList);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sync:
                startSync();
                return true;
            case R.id.menu_add:
                addExpenseItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void addExpenseItem() {
        Intent i = new Intent(this, ExpenseEntryActivity.class);
        i.setData(mBaseUri);
        startActivity(i);
    }


    private void startSync() {
        Toast.makeText(this, "startSync", Toast.LENGTH_LONG).show();

        // Code to start the service removed for this exercise so the menu operation can be more
        // clearly seen
        Intent startSyncService = new Intent(this,
                SyncService.class);
        startService(startSyncService);
    }


    private class ListClickHandler implements AdapterView.OnItemClickListener {

        /**
         * Handle a list item click
         *
         * @param parent
         *            The List View where the click happened
         * @param v
         *            the View item that was clicked
         * @param position
         *            the position of the clicked item in the list
         * @param id
         *            row id (_id value) of the item that was clicked
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent launchingIntent = new Intent(ExpensesListActivity.this, ExpenseEntryActivity.class);
            // When launching the ExpenseEntryActivity, we must supply the URI of
            // the data within the ContentProvider
            // Do this by adding it as the Data of the Intent
            // As a list item has been clicked, we know the id of the item that was
            // clicked
            // Simply append that to the main CP URI

            // TODO 21 Create a Content Provider URI for the item to edit
            // HINT: you need to append the id of the item to edit which has been
            // passed in
            // as the id parameter into this method to the content provider URI (mBaseUri)

            Uri itemUri = ContentUris.withAppendedId(mBaseUri , id);

            // TODO 22 Set the data of the launchingIntent to be the Uri of the
            // expense item
            // that is to be edited
            launchingIntent.setData(itemUri);

            // TODO 23 Remove the comment
            startActivity(launchingIntent);
        }
    };


//Leave this alone until the end of the exercise
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// The data from the menu item.
		AdapterView.AdapterContextMenuInfo info;

      /*
       * Gets the extra info from the menu item. When an expense in the Expense list is long-pressed, a
       * context menu appears. The menu items for the menu automatically get the data
       * associated with the note that was long-pressed. The data comes from the provider that
       * backs the list.
       *
       */
      try {
          // Casts the data object in the item into the type for AdapterView objects.
          info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      } catch (ClassCastException e) {

          // If the object can't be cast, logs an error
          Log.e(TAG, "bad menuInfo", e);

          // Triggers default processing of the menu item.
          return false;
      }
		// Appends the selected expense ID to the data URI
		Uri expenseUri = ContentUris.withAppendedId(mBaseUri, info.id);
		Log.i(TAG, "ID to delete=" + info.id);
		switch (item.getItemId()) {
		case R.id.delete:
			deleteExpenseItem(expenseUri);
	        return true;
		case R.id.add:
			addExpenseItem();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}



	private void deleteExpenseItem(Uri expenseUri) {
		// TODO Bonus
		getContentResolver().delete(expenseUri, null, null);
	}


	// TODO 10 - Remove the block comment around the three methods
	// implementing LoaderManager.LoaderCallbacks<Cursor>
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO 11 modify the code to return a new CursorLoader. Note the
		// parameters
		// The first parameter is the URI for the ContentProvider which
		// The remaining parameters are the same as the ones passed into
		// dao.queryExpenses (which you just commented out earlier)
		// with the addition of a sort order parameter (ascending / descending)
		return new CursorLoader(this, mBaseUri, // Use the URI stored in the
												// Intent for this Activity
				Expense.ExpenseItem.LIST_PROJECTION, // Return the expense
														// description and
														// amount
				null, // No where clause, return all records.
				null, // No where clause, therefore no where column values.
				Expense.ExpenseItem.DEFAULT_SORT_ORDER // Use the default sort
														// order.
		);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO 12 modify the code to swap the cursor on the adapter
		mAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO 13 modify the code to set cursor on the adapter to null
		mAdapter.swapCursor(null);

	}

}
