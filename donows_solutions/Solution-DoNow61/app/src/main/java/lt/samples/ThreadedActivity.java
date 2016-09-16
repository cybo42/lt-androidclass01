package lt.samples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedActivity extends Activity {





	@SuppressWarnings("unused")
	private static final String TAG = "ThreadedActivity";
	
	private ProgressBar mProgressBar;
	private Button butAsync, butThread;
	private TextView mTvResults;
	ExecutorService mExecutor = null;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threads_layout);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mTvResults = (TextView)findViewById(R.id.tv_result);
        butThread = (Button)findViewById(R.id.main_bt_clickme);
        butThread.setOnClickListener(new ThreadClickListener());
        butAsync = (Button)findViewById(R.id.bt_async_task);
        butAsync.setOnClickListener(new AsyncTaskClickListener());
    }
    
	private class ThreadClickListener implements View.OnClickListener {
		private static final String TAG = "ThreadClickListener";

		/**
		 * Handler for the Simple Thread button
		 */
		@Override
		public void onClick(View v) {
			// Update the UI so that something is clearly happening
			butThread.setEnabled(false);
			mProgressBar.setProgress(0);
			mTvResults.setText("Running Square Roots Task in background thread. See results in Log Cat");

				// Used post delayed so the user actually sees the button disabled and the 
				// initial messages for 600mS
			v.postDelayed(new Runnable() {

				public void run() {
					// executor is an instance of ExecutorService
					mExecutor = Executors.newSingleThreadExecutor();
					// Create and then run a thread
					mExecutor.execute(new Runnable() {

						@Override
						public void run() {
							int lineCount = 0;
							// Do something for a few moments

							for (int i = 0; i < 100; i++) {
								// Calculate the square root of i * 4 (* 4 just
								// makes it a bit slower)
								double result = root(i * 4);

								Log.i(TAG, "Root of " + (i * 4) + " is " + result);

								// TODO Remove the comment from the line below
								mTvResults.append("Root of " + (i * 4) + " is " + result);

							}
						} // run
					}); // runnable
				}// run
			}, 600); // postDelayed
			
			
			// Post a 2nd job so that the Thread button is reenabled after 3 seconds
			// This is very inelegant and much easier with AsyncTask
			v.postDelayed(new Runnable(){
				public void run(){
					butThread.setEnabled(true);
				}
			},3000);
		}
	}




	private class AsyncTaskClickListener implements View.OnClickListener
    {
		/**
		 * Handler for the AsyncTask button
		 */
		@Override
		public void onClick(View v) {
			// Update the UI so that something is clearly happening
			butAsync.setEnabled(false);
			mProgressBar.setProgress(0);	
			((TextView)findViewById(R.id.tv_result)).setText("Running Square Roots Async Task");
			
			// Used post delayed so the user actually sees the button disabled and the 
			// initial messages for 600mS			
			v.postDelayed(new Runnable(){

				@Override
				public void run() {
				
					// TODO Remove the comments from the line below - this code launches the AsyncTask
					new SquareRootTask().execute("Calculating Square Roots");
				}
				
			},1000);
			
		}    	
    }    
    
    /**
     * 
     * @author Course 2771 Development Team
     *
     * The 3 parameters
     * Params, the type of the parameters sent to the task upon execution.
	 * Progress, the type of the progress units published during the background computation.
     * Result, the type of the result of the background computation.
     */
    private class SquareRootTask extends AsyncTask<String, String, String> {
    	@SuppressWarnings("unused")
		private static final String TAG = "UpdateServerTask";
    	private int percentComplete = 0;

    	@Override
    	/**
    	 * Performs the background processing. 
    	 * @param msg - Ignored in this example
    	 */
        protected String doInBackground(String... msg) {
			for(int i=0; i < 25; i++){
				// Calculate the square root of i * 4 (* 4 just makes it a bit slower)
				double result = root(i * 4);
				publishProgress("Root of " + (i * 4) + " is " + result + "\n");
			}        	
    		return "Finished";
        }
        
        @Override
        protected void onPostExecute(String result) {
        	mTvResults.append("Done: Result=" + result);
        	butAsync.setEnabled(true);
        }
        
		@Override
		protected void onProgressUpdate(String... progress) {
			// Output the latest results
			mTvResults.append(progress[0]);
			// Update a progress bar to make it more interesting!
			mProgressBar.setProgress(percentComplete += 4);
		}        

    }
    

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mExecutor != null){
				// Shutdown the Executor service if it is running
			mExecutor.shutdown();
		}
	}    
   
    /**
     * Calculate the square root of the supplied int value
     * @param num - the number to calculate the root for
     * @return
     */
    public static double root(int num) {
    	if (num < 0)
    		throw new IllegalArgumentException("Negative integers don't have (real) square roots");
    	double precision = 0.00000001, guess = num/2., change = num/4.; 
    	while ((guess*guess - num > precision) || (num - guess*guess > precision)) {		
    		guess += (guess * guess > num) ? -change : change;
    		change /= 2;
 		
    	}
    	return guess;
    }    
    
}
