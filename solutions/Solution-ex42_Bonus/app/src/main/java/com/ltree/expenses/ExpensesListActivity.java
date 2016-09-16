package com.ltree.expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class ExpensesListActivity extends AppCompatActivity {

    // TODO 1  Add 3 new fields here
    private ArrayAdapter<String> mExpensesAdapter;
    private ArrayList<String> mExpensesArrayList;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // TODO 2 Uncomment and add code to to locate the ListView with an id of expLst_listView
        // Assign the result to mList

        mList = (ListView)findViewById(R.id.expLst_listView);

        // TODO 3 call getExpensesArrayList() here
        // (It's a member of this class)
        mExpensesArrayList =getExpensesArrayList();

        // TODO 4 Create an ArrayAdapter<String> and assign it to mExpensesAdapter
        // Parameters for the constructor:
        //	 this – the Context,
        //   android.R.layout.simple_list_item_single_choice – the ID of the layout,
        //   mExpensesArrayList – the ArrayList of dummy data

        mExpensesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mExpensesArrayList);

        // TODO 5 Call setAdapter() passing the newly created ArrayAdapter as the adapter to use

        mList.setAdapter(mExpensesAdapter);

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
    };


    private ArrayList<String> getExpensesArrayList() {
//		Create an ArrayList of strings using the dummy data defined
//      at the end of this class
        ArrayList<String> expensesArrayList = new ArrayList<String>();
        expensesArrayList.addAll(Arrays.asList(EXPENSES));
        return expensesArrayList;

    }


    // Dummy data
    static final String[] EXPENSES = new String[]{"Trip to NYC on QM2", "Venice on Orient Express", "Beijing on Trans-Sibera Express"};
}
