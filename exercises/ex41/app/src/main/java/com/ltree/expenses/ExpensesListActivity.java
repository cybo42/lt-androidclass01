package com.ltree.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class ExpensesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
        switch(id){
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
//        Toast toast = Toast.makeText(this, "adding expense", Toast.LENGTH_LONG);
//        toast.show();
        Intent addExpense = new Intent(this, ExpenseEntryActivity.class);
        startActivity(addExpense);
    }

    private void startSync() {
        // Code to start the service removed for this exercise so the menu operation can be more
        // clearly seen
        Intent startSyncService = new Intent(this,
                SyncService.class);
        startService(startSyncService);

        Toast toast = Toast.makeText(this, "starting sync", Toast.LENGTH_LONG);
        toast.show();
    }
}
