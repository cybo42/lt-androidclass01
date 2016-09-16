package com.ltree.expenses;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Arrays;


public class ExpensesListActivity extends AppCompatActivity {
    private static final String TAG = "ExpenseListActivity";
    private ArrayAdapter<String> mExpensesAdapter;
    private ArrayList<String> mExpensesArrayList;
    private ListView mList;

    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mList = (ListView)findViewById(R.id.expLst_listView);

        // TODO 10 Create an instance of the DAO
        // Hint: use "this" as the Context parameter
        dao = new DAO(this);

        // TODO 11 Call the queryExpenses method on the DAO you just created.
        // The first parameter is an array representing the columns you wish to be returned
        // Use Expense.ExpenseItem.LIST_PROJECTION for this
        // We want to return all the expenses so selection and selectionArgs can both be null

        Cursor cursor = dao.queryExpenses(Expense.ExpenseItem.LIST_PROJECTION, null, null);


        // The names of the cursor columns to display in the view, initialized
        // to the description column
        // TODO Note the creation of these two arrays specifying the columns to be displayed
        // and the resource ID's to use to display them
        // android.R.id.text1 is a built in resource to show simple text
        String[] dataColumns = { Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION } ;
        int[] viewIDs = { android.R.id.text1 };


        // TODO Create a new SimpleCursorAdapter
        // Creates the backing adapter for the ListView.
        @SuppressWarnings("deprecation") // We will fix this with a loader later
        SimpleCursorAdapter adapter
                = new SimpleCursorAdapter(
                this,                             // The Context for the ListView
                android.R.layout.simple_list_item_single_choice,      // Points to the XML for a list item
                cursor,                      // The cursor to get items from
                dataColumns,						// The array of Strings holding the names of the data columns to display
                viewIDs						// The array of resource ids used to display the data
        );

        // TODO 14 set the new adapter as the adapter on mList

        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new ListClickHandler());

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
        Toast.makeText(this, "addExpenseItem", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, ExpenseEntryActivity.class);
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
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String expense = mExpensesArrayList.get(position);
            Intent i = new Intent(ExpensesListActivity.this, ExpenseEntryActivity.class);
            i.putExtra(Constants.EXPENSE_DESCRIPTION, expense);
            startActivity(i);
        }
    }


    private ArrayList<String> getExpensesArrayList() {
//		Create an ArrayList of strings using the dummy data defined
//      at the end of this class
        ArrayList<String> expensesArrayList = new ArrayList<String>();
        expensesArrayList.addAll(Arrays.asList(EXPENSES));
        return expensesArrayList;

    }


    // Dummy data
    static final String[] EXPENSES = new String[]{"Trip to NYC on QM2", "Venice on Orient Express", "Beijing on Trans-Sibera Express"};

//Leave this alone until the end of the exercise
//  @Override
//  public boolean onContextItemSelected(MenuItem item) {
//
//      // The data from the menu item.
//      AdapterView.AdapterContextMenuInfo info;
//
//      /*
//       * Gets the extra info from the menu item. When an expense in the Expense list is long-pressed, a
//       * context menu appears. The menu items for the menu automatically get the data
//       * associated with the note that was long-pressed. The data comes from the provider that
//       * backs the list.
//       *
//       */
//      try {
//          // Casts the data object in the item into the type for AdapterView objects.
//          info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//      } catch (ClassCastException e) {
//
//          // If the object can't be cast, logs an error
//          Log.e(TAG, "bad menuInfo", e);
//
//          // Triggers default processing of the menu item.
//          return false;
//      }
//
//      Log.i(TAG, "ID to delete=" + info.id);
//	      switch (item.getItemId()) {
//	      case R.id.delete:
//	    	  dao.deleteExpensesById((int)info.id);
//	    	  	// Note, cursor.requery is actually deprecated.
//	    	    // We are using it here as a quick and dirty intermediate solution before we add a ContentProvider to the solution
//	    	 ((SimpleCursorAdapter)mList.getAdapter()).getCursor().requery();
//	        return true;
//	      default:
//	        return super.onContextItemSelected(item);
//	      }
//  }


}
