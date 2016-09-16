package com.ltree.expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ExpenseEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_form);
		
		
		
		// TODO 12 Add a Call to findViewById () to get a reference to the
        // description EditText field in the View.  The Id to use is R.id.expEdt_et_description
        // Store the reference in mDescription



        // TODO 13 Add code to get a reference to the Intent which started the Activity
        // Intent intent =



        // TODO 14 On the Intent, call getStringExtra() to locate the Extra data
        // sent with the Intent. (This is the data you added as Constants.EXPENSE_DESCRIPTION).
        // Save the string as a variable called description.



        // TODO 15 Populate the View with this data by calling
        // mDescription.setText() and passing the data you have retrieved in step 14.
    }


}
