package lt.samples;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent bootIntent) {
		// TODO the receiver is commented out at the moment as it is annoying when you run the
		// other samples!
		// To enable it, just uncomment the startService call
		Intent intent = new Intent(context, SimpleIntentService.class);
		// REMOVE THE COMMENT context.startService(intent );
	}
}
