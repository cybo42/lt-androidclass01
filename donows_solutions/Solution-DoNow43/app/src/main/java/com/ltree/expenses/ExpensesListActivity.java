package com.ltree.expenses;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ExpensesListActivity extends ListActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "ExpenseListActivity";

	private ArrayAdapter<String> mExpensesAdapter;
	private ArrayList<String> mExpensesArrayList;

	private int mEditId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpExpensesArrayList();
		mExpensesAdapter = new ArrayAdapter<String>(this,
				R.layout.expenses_list_item, mExpensesArrayList);

		setListAdapter(mExpensesAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
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

	// 5
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mEditId = (int)id;
		String expense = mExpensesArrayList.get((int)id);
		
		Intent intent = new Intent(this, ExpenseEntryActivity.class);		
		intent.putExtra(Constants.EXPENSE_DESCRIPTION, expense); 
		startActivityForResult(intent, Constants.REQUEST_CODE_EDIT);//
		
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		super.onActivityResult(requestCode, resultCode, intent);
  
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE_EDIT) {
        	String expenseDescription = intent.getStringExtra(Constants.EXPENSE_DESCRIPTION);
        	mExpensesAdapter.remove(mExpensesAdapter.getItem(mEditId));
        	mExpensesAdapter.insert(expenseDescription, mEditId);

        }
    }	
	

	private void addExpenseItem() {

		Intent intent = new Intent(this, ExpenseEntryActivity.class);
		startActivity(intent);

	}

	private void startSync() {
		Toast toast = Toast.makeText(this, "startSync", Toast.LENGTH_LONG);
		toast.show();

		Intent startSyncService = new Intent(this, SyncService.class);
		startService(startSyncService);

	}

	
	private void setUpExpensesArrayList(){
//		Create an ArrayList of strings using the dummy data defined
//      at the end of this class  
		mExpensesArrayList = new ArrayList<String>();
		mExpensesArrayList.addAll(Arrays.asList(EXPENSES));  	
		
	}
	
	// Dummy data

	static final String[] EXPENSES = new String[] { "Trip to NYC on QM2",
			"Venice on Orient Express", "Beijing on Trans-Sibera Express" };
}