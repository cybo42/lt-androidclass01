package lt.samples;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DialogActivity extends Activity {

	private static final String TAG = "DialogActivity";
	private static final int DLG_SHOW_ALERT_ID = 0;
	private static final int DLG_SHOW_PROGRESS_ID = 1;

	WorkerThread worker;
	ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);

		// Add an onClick event handler to the button
		findViewById(R.id.dlg_bt_createProgDlg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialog(DLG_SHOW_PROGRESS_ID);

					}

				});

		// Add an onClick event handler to the button
		findViewById(R.id.dlg_bt_createAlertDlg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialog(DLG_SHOW_ALERT_ID);

					}

				});

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DLG_SHOW_ALERT_ID:
			// call helper to create the alert Dialog
			dialog = buildAlertDialog();
			break;
		case DLG_SHOW_PROGRESS_ID:
			// call helper to create the progress Dialog
			dialog = showProgressDialog();
			break;
		default: // Don't recognize the dialog id. return null
			dialog = null;
		}
		// Return the new dialog. Android will display it
		return dialog;
	}


	private Dialog buildAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				//.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast toast = Toast.makeText(
										getApplicationContext(),
										R.string.str_yes_pressed,
										Toast.LENGTH_LONG);
								toast.show();
								// Dialog will automatically be dismissed
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast toast = Toast.makeText(getApplicationContext(),
								R.string.str_no_pressed, Toast.LENGTH_LONG);
						toast.show();
							// Calling cancel will dismiss the dialog and call 
							// any registered onCancelListener
						dialog.cancel();
					}
				});
		return builder.create();
	}

	private Dialog showProgressDialog() {
		Dialog dialog;
		progressDlg = new ProgressDialog(DialogActivity.this);
		progressDlg.setMessage(getString(R.string.str_progress_dlg_message));
		progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog = progressDlg;
		return dialog;
	}

	/**
	 * This method is called after the dialog is created but before it is
	 * displayed To simulate long-running behavior, it is being used to launch a
	 * background thread
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DLG_SHOW_PROGRESS_ID:
			progressDlg.setProgress(0);
			worker = new WorkerThread(handler);
			worker.start();
		}
	}

	/*
	 * IMPORTANT! IMPORTANT! IMPORTANT! IMPORTANT! IMPORTANT! IMPORTANT!
	 * IMPORTANT!
	 * 
	 * The code from this point on is simulating the behavior of a long -running
	 * process Threading will be discussed later in the course.
	 */

	/**
	 * A Handler instance used for communication between the worker thread and
	 * the Activity
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			progressDlg.setProgress(total);
			if (total >= 100) {
				dismissDialog(DLG_SHOW_PROGRESS_ID);
				worker.suicide = true;
			}
		}
	};

	/**
	 * A Thread used to simulate a long-running background process
	 * 
	 * @author Course 2771 Development Team
	 * 
	 */
	private class WorkerThread extends Thread {
		Handler mHandler;
		boolean suicide = false;
		int total;

		WorkerThread(Handler h) {
			mHandler = h;
		}

		public void run() {
			total = 0;
			while (!suicide) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e(TAG, "Thread Interrupted");
				}
				Message msg = mHandler.obtainMessage();
				msg.arg1 = total;
				mHandler.sendMessage(msg);
				total++;
			}
		}

	}
}
