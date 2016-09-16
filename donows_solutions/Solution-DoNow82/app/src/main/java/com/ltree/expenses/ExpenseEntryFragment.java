package com.ltree.expenses;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltree.expenses.data.Expense;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ExpenseEntryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, DateUpdater {

    private static final String TAG = "ExpenseEntryFragment";
    public static final int CAM_REQUEST_CODE = 103;

    private Button mSaveButton;
    private TextView mDescription;
    private TextView mAmount;
    private TextView mDateText;
    private Calendar mExpenseDate; // Keep a calendar in sync with the date text for efficiency

    private ImageButton mReceiptButton;
    private ImageView mReceiptImg;
    private File mReceiptFilePath;

    private long mExpenseId; // Expense item associated with this activity
    private Uri mExpenseUri;
    private Uri mBaseUri;
    private Cursor mCursor;
    private FragmentActivity mParentActivity;

    public boolean mDontPopFragment;


    protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
    private boolean flShowTrash;

    /**
     * Create a new instance of ExpenseEntryFragment, providing "expenseId" as
     * an argument. expenseId is the id of the expense associated with this
     * fragment
     */
    // TODO complete the declaration of a static method called newInstance
    // The method parameter is long expenseId
    // The return type is ExpenseEntryFragment
    static ExpenseEntryFragment newInstance(long expenseId, Uri baseUri) {

        // TODO Create a new instance of ExpenseEntryFragment
        ExpenseEntryFragment f = new ExpenseEntryFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        // TODO Add the expenseId as an argument called "expenseId" to the
        // bundle
        // Note: this is the best way of passing arguments into the new Fragment
        args.putLong(Constants.EXPENSE_ID, expenseId);
        // SAve the base URI in the bundle
        args.putParcelable(Constants.BASE_URI, baseUri);
        // TODO Call f.setArguments passing in the bundle
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  get the expenseId argument and store in mExpenseId
        mBaseUri = getArguments().getParcelable(Constants.BASE_URI);
        mExpenseId = getArguments().getLong(Constants.EXPENSE_ID);
        mExpenseUri = ContentUris.withAppendedId(mBaseUri, mExpenseId);
        mExpenseDate = Calendar.getInstance();
        Log.i(TAG, "onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.expenses_form, container, false);

        mSaveButton = (Button) v.findViewById(R.id.expEdt_bt_save);
        mDescription = (EditText) v.findViewById(R.id.expEdt_et_description);
        mAmount = (TextView) v.findViewById(R.id.expEdt_et_amount);
        mReceiptButton = (ImageButton) v.findViewById(R.id.expEdt_ib_receipt);
        mReceiptImg = (ImageView) v.findViewById(R.id.expEdt_im_receipt);
        mDateText = (TextView) v.findViewById(R.id.expEdt_et_date);

        // Set up references to the view items and instantiate a Calendar to hold the actual date
        mExpenseDate = Calendar.getInstance();


        mSaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "Save button clicked");
                // To save the data, just call the finish() method to close the Activity
                saveExpenseItem();
            }
        });

        registerDatePickerHandler(v);
        // TODO 21 Set up the on click listener for the receipt button (use the class
        // you just created
        mReceiptButton.setOnClickListener(new ReceiptButtonListener());

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParentActivity = getActivity();


    }

    private void loadExpenseDataFromCursor() {
        /*
         * mCursor is initialized, since onCreate() always precedes onResume for
		 * any running process. This tests that it's not null, since it should
		 * always contain data.
		 */
        if (mCursor != null) {
            // Re-query in case something changed while paused
            // Done 51 remove the comments
            // and then requery the cursor by moving the cursor to the first row

            if (mCursor.moveToFirst()) {
                // populate the dialog fields

                // Done 52 Note how the view elements are populated from the cursor content
                mDescription.setText(mCursor.getString(0));
                mAmount.setText(mCursor.getString(1));

				/* The next section fetches the date as a string from the database and
				 * then converts to a GregorianCalendar to populate the DatePicker
				 */
                try {
                    // Convert the stored date to Calendar
                    // Done 53 Add the code to retrieve the data from the cursor
                    //The date is stored as a String and it's column index is 2
                    long lDate = Long.parseLong(mCursor.getString(2));
                    // Update the mExpenseDate calendar with the new date value
                    mExpenseDate.setTimeInMillis(lDate);
                    // Done 54 Note how we update the date text to reflect the stored date
                    mDateText.setText(dateFormatter.format(mExpenseDate.getTime()));
                } catch (NumberFormatException e) {
                    Log.i(TAG, "Date not loaded");
                }
            }
        }
    }


    private void registerDatePickerHandler(View layout) {
        View v1 = layout.findViewById(R.id.expEdt_et_date);
        v1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putSerializable(DatePickerFragment.INPUT_DATE_MS, mExpenseDate);
                newFragment.setArguments(args);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        // TODO 61 call the local helper method saveExpenseItem()
        saveExpenseItem();


    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        

        // If loading an existing expense, load a cursor from the provided URI
        if (Constants.EXPENSE_ITEM_UNDEFINED != mExpenseId) {
            // Done 41 Initialise the Loader
            getLoaderManager().initLoader(0, null, this);

        }
    }


    private void saveExpenseItem() {

        if (Constants.EXPENSE_ITEM_UNDEFINED == mExpenseId) {
            // As the expenseItem was set to -1, create a new expense item
            // Insert a new (empty) expense item into the database (Hint: the
            // Uri for the ContentProvider is in the Intent)
            mExpenseUri = getActivity().getContentResolver().insert(
                    getActivity().getIntent().getData(), null);
            // Get the id of the new expense from the uri returned
            mExpenseId = ContentUris.parseId(mExpenseUri);

        }

        ContentValues values = new ContentValues();

        // Adds map entries for the user-controlled fields in the map
        // Note that we are not checking the field content here but the provider
        // code does do the checking
        // Get the date as a String representing mS value

        values.put(Expense.ExpenseItem.COLUMN_NAME_EXPENSE_DATE, mExpenseDate.getTimeInMillis() + "");

        values.put(Expense.ExpenseItem.COLUMN_NAME_AMOUNT, mAmount.getText()
                .toString());

        // Done 71 create a new element in the values object with a key of Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION
        // and the value from the mDescription view element (see the lines above for a template)
        values.put(Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION, mDescription.getText().toString());


        // Done 73 Call the update method on the ContentResolver to update the data with the
        // modified form elements (use null as the where clause parameters)
        // Important: use the provider URL stored mExpenseUri as it represents the
        // expense item associated with this Activity instance
        // Use null for the where clause and selection arguments
        mParentActivity.getContentResolver().update(mExpenseUri, values, null, null);
        // Dispose of this fragment
//        if (!mDontPopFragment) {
//            getActivity().getSupportFragmentManager().popBackStack();
//        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Done 45 modify the code to return a new CursorLoader. Note the parameters
        // The first parameter is the URI for the ContentProvider which is stored in member field mBaseUri"
        // The remaining parameters are the same as the ones passed into dao.queryExpenses (which you just commented out earlier)
        // with the addition of a sort order parameter (ascending / descending)
        return new CursorLoader(mParentActivity,
                mExpenseUri,                // Use the URI stored in the Intent for this Activity
                Expense.ExpenseItem.FULL_PROJECTION,    // Return the expense description and amount
                null,                                    // No where clause, return all records.
                null,                                    // No where clause, therefore no where column values.
                Expense.ExpenseItem.DEFAULT_SORT_ORDER  // Use the default sort order.
        );

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursor = data;
        // Done 47 Call loadExpenseDataFromCursor();
        loadExpenseDataFromCursor();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursor = null;

    }

    @Override
    public void updateDate(Calendar date) {
        mDateText.setText(dateFormatter.format(new Date(date.getTimeInMillis())));
        mExpenseDate = date;
    }

    public void showTrash(boolean showTrash) {
        flShowTrash = showTrash;

    }

    //  Declare a class called ReceiptButtonListener which implements View.OnClickListener
    public class ReceiptButtonListener implements View.OnClickListener {


        //  Implement the onClick method
        @Override
        public void onClick(View v) {

            // Create an explicit Intent to start the Camera app (MediaStore.ACTION_IMAGE_CAPTURE)
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Set up receiptFile to specify where the image should be stored
            // Need to pass in the ID of the expenseItem so we can
            // create the file name here.
            // Get the File object representing the External Public Pictures area
            // Hint: use a static method on Environment we discussed in Chapter 5
            // Hint2: Environment defines a constant to specify the location of the PICTURES directory
            File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // append the "receipts" path to the directory
            // Hint: you are just creating a sub-directory called receipts
            File receiptsDir = new File(baseDir, "receipts");

            // Create the directories if they do not yet exist
            if (!receiptsDir.exists()) {
                //  Create the directories if they do not yet exist
                // Embed the creation in the if test so a failure gets logged
                // Hint: you need the method on File which makes all the required directories
                if (!receiptsDir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory to save receipts");
                    return;  // There is no point continuing if we can't save the file
                }
            }
            //  add the Id of the current expense into the file name
            // Hint: mExpenseId
            mReceiptFilePath = new File(receiptsDir, "receipt" + mExpenseId + ".jpg");
            Log.i(TAG, "Capturing recipt and storing in : " + mReceiptFilePath.getAbsolutePath());
            // Now convert the fileName into a Provider URI so it can be written
            // to.
            //  Convert the receipt file path to a URI
            Uri uri = Uri.fromFile(mReceiptFilePath);
            //  Put the uri of the file into the Intent as Extra data with a name of MediaStore.EXTRA_OUTPUT
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            // Put the Extra data MediaStore.EXTRA_VIDEO_QUALITY as 1 (high quality)
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            mDontPopFragment = true;// Prevent the fragment being closed when the pause method is called.
            // Use the Intent you created to start the Activity using startActivityForResult
            // Set the resultCode to be CAM_REQUEST_CODE
            // Note: starting the Activity using startActivityForResult will cause the onActivityResult
            // method below to be called on completion of the sub-activity
            startActivityForResult(intent, CAM_REQUEST_CODE);
        }


    }

}
