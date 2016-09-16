package com.ltree.expenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExpenseEntryActivity extends Activity {
	// TODO Make a note of the value of TAG
	private static final String TAG="ExpenseEntryActivity";
	

	private EditText mDescription;
	private Button mSaveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "onCreate");
		
		setContentView(R.layout.expenses_form);
		
		mSaveButton = (Button)findViewById(R.id. expEdt_bt_save );
		
		// Call to findViewById () to get a reference to the 
		// description EditText field in the View.  The Id to use is R.id.expEdt_et_description 
		// Store the reference in mDescription  (You will have to down-cast the reference)
		mDescription = (EditText)findViewById(R.id.expEdt_et_description);

		
		// Get a reference to the Intent which started the Activity
		Intent intent = getIntent(); 
		
		
		
		// On the Intent, call getStringExtra() to locate the Extra data
		// sent with the Intent. (This is the data you added as Constants.EXPENSE_DESCRIPTION).
		// Save the string as a variable called description.
		String description = intent.getStringExtra(Constants.EXPENSE_DESCRIPTION);
		
		
		// Populate the View with this data by calling 
		// mDescription.setText() and passing the data you have retrieved in step 14.
		mDescription.setText(description);
		
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Toast t = Toast.makeText(ExpenseEntryActivity.this, "Saving", Toast.LENGTH_LONG);
				t.show();
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {

		super.onPause();
		// TODO Add a Log.i statement here. Use TAG and "onPause" as the arguments
        Log.i(TAG, "onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	
	@Override
	protected void onRestart() {
		super.onResume();
		Log.i(TAG, "onRestart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}


	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}

}
