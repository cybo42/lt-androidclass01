package com.ltree.expenses;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ltree.expenses.data.Expense;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ExpenseEntryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DateUpdater {

    private static final String TAG = "ExpenseEntryActivity";
    public static final int CAM_REQUEST_CODE = 103;
    protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");


    private Button mSaveButton;
    private TextView mDescription;
    private TextView mAmount;
    private ImageButton mReceiptButton;
    private ImageView mReceiptImg;
    private File mReceiptFilePath;

    private TextView mDateText;
    private Calendar mExpenseDate; // Keep a calendar in sync with the date text for efficiency


    /**
     * Fields for managing the date control
     */


    private long mExpenseId; // Expense item associated with this activity

    private Cursor mCursor;
    private Uri mBaseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");
        setContentView(R.layout.expenses_form);

        // Set up references to the view items and instantiate a Calendar to hold the actual date


        mSaveButton = (Button) findViewById(R.id.expEdt_bt_save);
        mDescription = (EditText) findViewById(R.id.expEdt_et_description);
        mAmount = (TextView) findViewById(R.id.expEdt_et_amount);
        mReceiptButton = (ImageButton) findViewById(R.id.expEdt_ib_receipt);
        mReceiptImg = (ImageView) findViewById(R.id.expEdt_im_receipt);
        mDateText = (TextView) findViewById(R.id.expEdt_et_date);
        mExpenseDate = Calendar.getInstance();

        Log.v(TAG, "Expense ID = " + mExpenseId);

        mSaveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // To save the data, just call the finish() method to close the Activity
                finish();
            }
        });

        registerDatePickerHandler();
        // TODO 21 Set up the on click listener for the receipt button (use the class
        // you just created
        mReceiptButton.setOnClickListener(new ReceiptButtonListener());

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


    private void registerDatePickerHandler() {
        View v1 = findViewById(R.id.expEdt_et_date);
        v1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putSerializable(DatePickerFragment.INPUT_DATE_MS, mExpenseDate);
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        // TODO 61 call the local helper method saveExpenseItem()
        saveExpenseItem();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        mBaseUri = getIntent().getData();
        try {
            mExpenseId = ContentUris.parseId(mBaseUri);
        } catch (java.lang.NumberFormatException e) {
            mExpenseId = Constants.EXPENSE_ITEM_UNDEFINED;
        }

        // If loading an existing expense, load a cursor from the provided URI
        if (Constants.EXPENSE_ITEM_UNDEFINED != mExpenseId) {
            // Done 41 Initialise the Loader
            getLoaderManager().initLoader(0, null, this);

        }
    }


    private void saveExpenseItem() {

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


        if (Constants.EXPENSE_ITEM_UNDEFINED == mExpenseId) {
            // As the expenseItem was set to -1, create a new expense item

            // Done 72 Call the insert method to add the new item to the content provider
            // The values are in values. Save the returned Uri in newItemUri
            // mBaseUri is the content provider Uri
            Uri newItemUri = getContentResolver().insert(mBaseUri, values);
            // Get the id of the new expense from the uri returned
            mExpenseId = ContentUris.parseId(newItemUri);

        } else {
            // Done 73 Call the update method on the ContentResolver to update the data with the
            // modified form elements (use null as the where clause parameters)
            // Important: use the provider URL stored mBaseUri as it represents the
            // expense item associated with this Activity instance
            // Use null for the where clause and selection arguments
            getContentResolver().update(getIntent().getData(), values, null, null);
        }
    }

    @Override
    public void updateDate(Calendar date) {
        mDateText.setText(dateFormatter.format(new Date(date.getTimeInMillis())));
        mExpenseDate = date;
    }

    @Override
    protected void onStart() {
        // Done Auto-generated method stub
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        // Done Auto-generated method stub
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        // Done Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Done 45 modify the code to return a new CursorLoader. Note the parameters
        // The first parameter is the URI for the ContentProvider which is stored in member field mBaseUri"
        // The remaining parameters are the same as the ones passed into dao.queryExpenses (which you just commented out earlier)
        // with the addition of a sort order parameter (ascending / descending)
        return new CursorLoader(this,
                mBaseUri,                // Use the URI stored in the Intent for this Activity
                Expense.ExpenseItem.FULL_PROJECTION,    // Return the expense description and amount
                null,                                    // No where clause, return all records.
                null,                                    // No where clause, therefore no where column values.
                Expense.ExpenseItem.DEFAULT_SORT_ORDER  // Use the default sort order.
        );

        // TODO 46 Delete the next line
        //return null;
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

    // TODO 1 Declare a class called ReceiptButtonListener which implements View.OnClickListener
    public class ReceiptButtonListener implements View.OnClickListener {


        // TODO 2 Implement the onClick method
        @Override
        public void onClick(View v) {

            // TODO 3 Create an explicit Intent to start the Camera app (MediaStore.ACTION_IMAGE_CAPTURE)
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Set up receiptFile to specify where the image should be stored
            // Need to pass in the ID of the expenseItem so we can
            // create the file name here.
            // TODO 4 Get the File object representing the External Public Pictures area
            // Hint: use a static method on Environment we discussed in Chapter 5
            // Hint2: Environment defines a constant to specify the location of the PICTURES directory
            File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // TODO 5 append the "receipts" path to the directory
            // Hint: you are just creating a sub-directory called receipts
            File receiptsDir = new File(baseDir, "receipts");

            // Create the directories if they do not yet exist
            if (!receiptsDir.exists()) {
                // TODO 7 Create the directories if they do not yet exist
                // Embed the creation in the if test so a failure gets logged
                // Hint: you need the method on File which makes all the required directories
                if (!receiptsDir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory to save receipts");
                    return;  // There is no point continuing if we can't save the file
                }
            }
            // TODO 8 add the Id of the current expense into the file name
            // Hint: mExpenseId
            mReceiptFilePath = new File(receiptsDir, "receipt" + mExpenseId + ".jpg");
            Log.i(TAG, "Capturing recipt and storing in : " + mReceiptFilePath.getAbsolutePath());
            // Now convert the fileName into a Provider URI so it can be written
            // to.
            // TODO 9 Convert the receipt file path to a URI
            Uri uri = Uri.fromFile(mReceiptFilePath);
            // TODO 10 Put the uri of the file into the Intent as Extra data with a name of MediaStore.EXTRA_OUTPUT
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            // TODO 11 Put the Extra data MediaStore.EXTRA_VIDEO_QUALITY as 1 (high quality)
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            // TODO 12 Use the Intent you created to start the Activity using startActivityForResult
            // Set the resultCode to be CAM_REQUEST_CODE
            // Note: starting the Activity using startActivityForResult will cause the onActivityResult
            // method below to be called on completion of the sub-activity
            startActivityForResult(intent,CAM_REQUEST_CODE );
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check the resultCode is OK and
        // correlate the requestCode
        if (requestCode == CAM_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                //use imageUri here to access the image
                // Process result
                Drawable camImage = BitmapDrawable.createFromPath(mReceiptFilePath.getAbsolutePath());
                mReceiptImg.setImageDrawable(camImage);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
