package com.ltree.expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;


public class ExpenseEntryActivity extends AppCompatActivity {

    private EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_form);



        // TODO 12 Add a Call to findViewById () to get a reference to the
        // description EditText field in the View.  The Id to use is R.id.expEdt_et_description
        // Store the reference in mDescription

        mDescription = (EditText)findViewById(R.id.expEdt_et_description);

        // TODO 13 Add code to get a reference to the Intent which started the Activity
        Intent intent = getIntent();



        // TODO 14 On the Intent, call getStringExtra() to locate the Extra data
        // sent with the Intent. (This is the data you added as Constants.EXPENSE_DESCRIPTION).
        // Save the string as a variable called description.
        String description = intent.getStringExtra(Constants.EXPENSE_DESCRIPTION);


        // TODO 15 Populate the View with this data by calling
        // mDescription.setText() and passing the data you have retrieved in step 14.
        mDescription.setText(description);

    }



}
