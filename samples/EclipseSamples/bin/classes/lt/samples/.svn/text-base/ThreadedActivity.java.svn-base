package lt.samples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ThreadedActivity extends Activity {
	
	private ProgressBar mProgressBar;
	private TextView mTextViewResult;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threads_layout);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mTextViewResult = (TextView)findViewById(R.id.tv_result);
        findViewById(R.id.main_bt_clickme).setOnClickListener(new ThreadClickListener());
        findViewById(R.id.bt_async_task).setOnClickListener(new AsyncTaskClickListener());
    }
    
    


	private class AsyncTaskClickListener implements View.OnClickListener
    {
		@Override
		public void onClick(View v) {
			((TextView)findViewById(R.id.tv_result)).setText("Running Async Task");
			new UpdateServerTask().execute("http://server/update");
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
    private class UpdateServerTask extends AsyncTask<String, Integer, Double> {
    	private static final String TAG = "UpdateServerTask";

    	@Override
        protected Double doInBackground(String... urls) {
            return updateServer(urls[0]);
        }
        
        @Override
        protected void onPostExecute(Double result) {
        	mTextViewResult.setText("Done: Result=" + result);
        }
        
		@Override
		protected void onProgressUpdate(Integer... progress) {
			mProgressBar.setProgress(progress[0]);
		}        
        
        private Double updateServer(String url){
        	double k=0;
    		for(int i=1; i <= 100; i++){
    			double j=100;
    			k = j/i;
    			j = k + i;
    			try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.i(TAG, "Thread interrupted");
				}
				publishProgress(i);
    		}
    		return k;
        }
    }
    
    private class ThreadClickListener implements View.OnClickListener
    {
		@Override
		public void onClick(View v) {
			mTextViewResult.setText("Running a Thread - expect a failure!");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// Do something for a few moments
					takeYourTime();
						// Updating from this thread violates the Android threading model
					mTextViewResult.setText("Done");					
				}
				
			}).start();
		}    	
    }
    
    /**
     * Simulate doing some real work
     */
    private double takeYourTime() {
    	double k=0;
		for(int i=1; i < 100000; i++){
			double j=100;
			k = j/i;
			j = k + i;
		}
		return k;
	}
    
    
}
