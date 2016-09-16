package lt.samples;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ListContactsActivity extends Activity {

	/**
	* @param savedInstanceState
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_me);
		
		Button clickMe = (Button)findViewById(R.id.main_bt_clickme);
		clickMe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Uri uri = ContactsContract.Contacts.CONTENT_URI;
		        String[] projection = new String[] {
		                ContactsContract.Contacts._ID,
		                ContactsContract.Contacts.DISPLAY_NAME
		        };
		        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		        Cursor c = managedQuery(uri, projection, null, null, sortOrder);
		        while(c.moveToNext()){
		        	Toast.makeText(ListContactsActivity.this, c.getString(1), Toast.LENGTH_SHORT).show();
		        }			
			}
		});
		
	}

}
