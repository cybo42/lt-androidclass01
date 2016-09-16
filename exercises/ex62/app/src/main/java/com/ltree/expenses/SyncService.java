package com.ltree.expenses;


import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ltree.expenses.data.Expense;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SyncService extends IntentService {
    private static final String TAG="SyncService";
	private static int NOTIFICATION_ID = 1;

    public SyncService() {
        super("SyncService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Processing Started");

		/** Default action is to save to the Web service using JSON */
		boolean exportToCsvFile = false;
		// Create a notification that the service started OK
		createNotification();

		Log.i(TAG, "Service Started");

        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of expenses.
        if (intent.getData() == null) {
            intent.setData(Expense.ExpenseItem.CONTENT_URI);
        }

        Bundle extras = intent.getExtras();
        if(null != extras){
        	// TODO 31 Note that the flag indicating what action the service should take is retrieved here (nothing to change)
        	exportToCsvFile = extras.getBoolean(Constants.EXPENSE_EXTRA_EXPORT_TO_CSV);

        }

        // TODO 32 Note the expense data is being retrieved from the Content Provider as a cursor
		ContentResolver resolver =getContentResolver();
        Cursor cursor = resolver.query(
            	intent.getData(), // Use the default content URI for the provider.
                Expense.ExpenseItem.FULL_PROJECTION, //PROJECTION,                       // Return the note ID and title for each note.
                null,                             // No where clause, return all records.
                null,                             // No where clause, therefore no where column values.
                Expense.ExpenseItem.DEFAULT_SORT_ORDER  // Use the default sort order.
            );

        // TODO 33 Call the helper method in Expense.Helper which converts the cursor to an array of expenses
        Expense[] expenses = Expense.Helper.getExpensesFromCursor(cursor);


        if(exportToCsvFile){
    		// Work out where to write the file (from the Intent Extras)
            String fileName = resolveFileNameForExpenses(intent);
            // TODO 34 pass the array of expenses to the writeExpensesToCsvFile helper method
         // You will work on this method next
            writeExpensesToCsvFile(fileName, expenses);
        }
        if(null != cursor){
        		// Close the cursor to release any resources
        	cursor.close();
        }
		Log.i(TAG, "Processing finished");
	}


	private boolean writeExpensesToCsvFile(String fileName, Expense[] expenses)
	{
		boolean success = false;
		PrintWriter pw = null;

		// TODO 35 Get the state of the external storage (remove the null!)
		// HINT: See chapter 5
		String state = Environment.getExternalStorageState();

		// TODO 36 Test that the state is Environment.MEDIA_MOUNTED (accessible and writable)
		// Hint: don't forget that state is a String (use equals() to compare)
		if(state.equals(Environment.MEDIA_MOUNTED )){
			try {
				// TODO 37 Get the File object representing the Public Downloads area
				// Hint: use a method on Environment
				File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

				// Ensure the directories in the path are actually created
				baseDir.mkdirs();


				// TODO 38 Create the file name we will use to write the CSV file
				// by appending the fileName to the baseDir
				// Hint: fileName is a parameter to the method you are working in
				File fullFileName = new File(baseDir, fileName);



				Log.d(TAG, "Outputting csv to: " + fullFileName.getAbsolutePath());
				// TODO 39 Create a new PrintWriter to write the file (use the fullFileName )
				pw = new PrintWriter(fullFileName);


				// TODO 40 Pass the expenses array to Expense.Helper.expenseArrayToCsv then
				// use the PrintWriter to write the data to the CSV file
				// Hint: you may need to convert the returned StringBuilder to a string
				pw.write(Expense.Helper.expenseArrayToCsv(expenses).toString());

				success = true;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "Failed to save expenses in service. Details:", e);
			} finally {
				if(null != pw){
					// 41 TODO close the PrintWriter
                    pw.close();
				}
			}
		}
		return success;
	}

	/**
	 * Helper method to set either a default file name or use one passed in with the intent
	 * @param intent
	 * @return the file name to write to
	 */
	private static String resolveFileNameForExpenses(Intent intent) {
		String fileName = Constants.DEFAULT_CSV_FILENAME; // set a default file name
        Bundle extras = intent.getExtras();
        if(null != extras ){
	        String tmpFName = extras.getString(Constants.EXPENSE_EXTRA_FILENAME);
	        if(null != tmpFName){
	        	fileName = tmpFName;
	        }
        }
		return fileName;
	}

    /**
     * Creates a simple notification to indicate that the service has started
     */
    private void createNotification() {
        String msgSvcStarted = getResources().getString(R.string.svc_str_started);
        // Add an Intent to the Notification. NB: Using a Dummy Activity which does nothing when selected
        Intent notificationIntent = new Intent(this, DummyActivity.class);
        // Create the PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        // Get the NotificationManager
        NotificationManager notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Create a Notification
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        // Set the icon, ticker text and notification time
        notiBuilder.setTicker(msgSvcStarted).setSmallIcon(R.drawable.ic_app)
                // Instruct the notification to clear it's self when clicked
                .setAutoCancel(true)
                        // Specify the title, text and PendingIntent for the notification
                .setContentText(msgSvcStarted)
                .setContentTitle(msgSvcStarted)
                .setContentIntent(contentIntent);
        // Get the notification to use
        Notification notification = notiBuilder.build();

        // Fire the notification
        notManager.notify(NOTIFICATION_ID, notification);

    }

    /**
     * Dummy class to allow a notification to be created which does not actually do anything!
     */
    class DummyActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            finish();
        }
    }
}
