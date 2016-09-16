package lt.samples;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * Simple service to run to prove the BOOT_COMPLETED receiver works.
 * @author mjrw
 *
 */
public class SimpleIntentService extends IntentService {

	private Handler mHandler;

	public SimpleIntentService(){
		super("SimpleIntentService");
		mHandler = new Handler();
	}
	@Override
	protected void onHandleIntent(Intent arg0) {
		
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mHandler.post(new DisplayToast("I'm still here and to kill me you will have to use settings | apps..."));
			
		}

	}
	
	class DisplayToast implements Runnable {
		private String msg;
		public DisplayToast(String msg){
			this.msg = msg;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Toast.makeText(SimpleIntentService.this, msg, Toast.LENGTH_SHORT).show();
		}
		
	}

}
