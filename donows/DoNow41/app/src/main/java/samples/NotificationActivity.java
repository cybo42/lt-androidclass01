package samples;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ltree.android.snippets.R;

public class NotificationActivity extends Activity {
	@SuppressWarnings("unused")
	private static final String TAG = "NotificationActivity";
	private static int SAMPLE_NOTIFICATION = 1;
//	private final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_layout);
		
		// Add an onClick event handler to the button
		findViewById(R.id.not_bt_createNot).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Context context = NotificationActivity.this;
				// Step 1 Create the information for the notification
				int icon = R.drawable.emo_im_yelling;      		// icon to display when showing the notification
				CharSequence notiTickerText = "A NOTIFICATION!";    // text to display when notification first occurs
				long notiTime = System.currentTimeMillis();         // notification time

				CharSequence notiTitle = "A NOTIFICATION!";  // message title displayed in notification screen
				CharSequence notiContent = "I'm trying to tell you something!";      // detailed message text

					// Step 2, create an Intent which will launch an Activity in out application
				Intent notificationIntent = new Intent(context, NotificationActivity.class);
					// Convert it into a Pending Intent which gives it full privileges 
					// within our application (as if it came from us)
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				// Step 3, create a Notification and add the the PendingIntent to it
				// Set up the notification
				Notification.Builder notiBuilder = new Notification.Builder(context);
				// Set the icon, ticker text and notification time
				notiBuilder.setSmallIcon(icon).setTicker(notiTickerText).setWhen(notiTime)
				// Instruct the notification to clear it's self when clicked
					.setAutoCancel(true)
				// Specify the title, text and PendingIntent for the notification
					.setContentText(notiContent).setContentTitle(notiTitle).setContentIntent(pendingIntent);
				// Build the notification to use
				Notification notification = notiBuilder.build();	
				
				// Step 4: Obtain a reference to the NotificationManager and use it to issue the Intent
				// Obtain a reference to the NotificationManager 
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
				// Use the NotificationManager to issue the notification
				mNotificationManager.notify(++SAMPLE_NOTIFICATION, notification);

			}
			
		});
		

	}

	
}
