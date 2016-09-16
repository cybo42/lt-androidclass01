package lt.fragments;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import lt.samples.R;
import lt.samples.R.id;
import lt.samples.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// TODO change the base class to Fragment
public class ExpenseEntryFragment extends Fragment {

	private static final String TAG = "ExpenseEntryActivity";
	public static final int CAM_REQUEST_CODE = 103;

	private DatePicker mDatePicker;
	private Button mSaveButton;
	private TextView mDescription;
	private TextView mAmount;
	@SuppressWarnings("unused")
	private TextView mExpenseDate;
	private ImageButton mReceiptButton;
	private ImageView mReceiptImg;
	private File mReceiptFilePath;

	private long mExpenseId; // Expense item associated with this activity
	private Uri mUri;
	private Cursor mCursor;

	/**
	 * Create a new instance of ExpenseEntryFragment, providing "expenseId" as
	 * an argument. expenseId is the id of the expense associated with this
	 * fragment
	 */
	// TODO complete the declaration of a static method called newInstance
	// The method parameter is long expenseId
	// The return type is ExpenseEntryFragment
	static ExpenseEntryFragment newInstance(long expenseId) {

		// TODO Create a new instance of ExpenseEntryFragment
		ExpenseEntryFragment f = new ExpenseEntryFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		// TODO Add the expenseId as an argument called "expenseId" to the
		// bundle
		// Note: this is the best way of passing arguments into the new Fragment
		args.putLong("expenseId", expenseId);
		// TODO Call f.setArguments passing in the bundle
		f.setArguments(args);

		return f;
	}

	@Override
	/**
	 * New onCreate implementation
	 * Saves the expenseId from the bundle into an instance variable mExpenseId
	 */
	// TODO Note that this is a new onCreate method
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	// TODO Note that the body of this method is the body of the onCreate()
	// method
	// in the ExpenseEntryActivity
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(TAG, "onCreateView");
		// TODO Delete the next line. The resource needs to be inflated! ...

		// TODO Use the inflater parameter to inflate the layout for this
		// fragment
		View v = inflater.inflate(R.layout.expenses_form, container, false);
		// Set up references to the view items.

		// TODO Fix the errors by prefix findViewById with v. (the new view)
		mDatePicker = (DatePicker) v.findViewById(R.id.expEdt_dp_date);
		mSaveButton = (Button) v.findViewById(R.id.expEdt_bt_save);
		// TODO Note that we've done the rest for you
		mDescription = (EditText) v.findViewById(R.id.expEdt_et_description);
		mAmount = (TextView) v.findViewById(R.id.expEdt_et_amount);

		// TODO return v which is the reference to the view just created
		return v;
	}

	
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		mDescription.setText("Dummy description");
		mAmount.setText("10.99");
	}


}
