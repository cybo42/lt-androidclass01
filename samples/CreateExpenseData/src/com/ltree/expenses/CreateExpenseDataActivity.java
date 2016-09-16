package com.ltree.expenses;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ltree.expenses.data.Expense;

public class CreateExpenseDataActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button but = (Button)findViewById(R.id.button1);
        but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				populateExpenses();
			}

		});
    }
    

	private void populateExpenses() {
		Calendar cal = Calendar.getInstance();
		Expense exp = new Expense("Night in 6 star hotel", 22222.99, cal.getTimeInMillis() );
		getContentResolver().insert(Expense.ExpenseItem.CONTENT_URI, exp.getContentValues());
		
		cal.roll(Calendar.DAY_OF_MONTH, -3);
		exp = new Expense("Lunch at the Restaurant at the End of the Universe", 9999.99, cal.getTimeInMillis() );
		getContentResolver().insert(Expense.ExpenseItem.CONTENT_URI, exp.getContentValues());

		//cal.roll(Calendar.DAY_OF_MONTH, -23);
		exp = new Expense("Travel to Restaurant at the End of the Universe", 132221.99, cal.getTimeInMillis() );
		getContentResolver().insert(Expense.ExpenseItem.CONTENT_URI, exp.getContentValues());
		
		((TextView)findViewById(R.id.results)).setText("Sample expenses are now in the content provider");
		
	}    
}