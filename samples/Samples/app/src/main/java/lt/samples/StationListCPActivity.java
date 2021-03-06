package lt.samples;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import lt.samples.db.DAO;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class StationListCPActivity extends ListActivity {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
    public static final String[] LIST_PROJECTION =
        new String[] {
            DAO.COL_ID,
            DAO.COL_NAME,
            DAO.COL_UPDATED
    };  	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		createListData();
		Intent intent = getIntent();
		
        if (intent.getData() == null) {
            intent.setData(Constants.CONTENT_URI);
        }		
		
        Cursor cursor = managedQuery(
            	intent.getData(), 						// Use the default content URI for the provider.
                LIST_PROJECTION,    					// Return the expense description and amount
                null,                             		// No where clause, return all records.
                null,                             		// No where clause, therefore no where column values.
                DAO.DEFAULT_SORT_ORDER  // Use the default sort order.
            );   
        
		
        String[] dataColumns = { DAO.COL_NAME } ;
        int[] viewIDs = { android.R.id.text1 };		
		
        SimpleCursorAdapter adapter
        = new SimpleCursorAdapter(
                  this,                             // The Context for the ListView
                  R.layout.list_item,      // Points to the XML for a list item
                  cursor,                           // The cursor to get items from
                  dataColumns,						// The array of Strings holding the names of the data columns to display	
                  viewIDs							// The array of resource ids used to display the data 
          );		
		// Set the adapter into the list
		setListAdapter(adapter);		
	}

	/**
	 * List item click handler
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Retrieve the item associated with the click from the list
		String station = (String)l.getItemAtPosition(position);
		Toast toast = Toast.makeText(this, station, Toast.LENGTH_LONG);
		toast.show();

		
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create an instance of the Menu inflater
	    MenuInflater inflater = getMenuInflater();
	    // Inflate the menu
	    inflater.inflate(R.menu.station_list_cp_menu, menu);
	    // Return true if the menu inflated OK
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Get the Id of the selected menu item
	    switch (item.getItemId()) {
	    case R.id.toast_stations:
	        // process the toast menu action
	    	toastStations();
	        return true;	
    	default:
    		return super.onOptionsItemSelected(item);
    	}	    
	}
	
	private void toastStations() {
        Uri uri = Constants.CONTENT_URI;
        String[] projection = new String[] {
                "_ID",
                "stationName",
                "lastUpdatedDate"
        };
        Cursor c = getContentResolver().query(uri, projection, null, null, null);
        while(c.moveToNext()){
        	Toast.makeText(this, c.getString(1), Toast.LENGTH_SHORT).show();
        }		
        c.close();
	}	
	
	
	private void createListData(){
		
		Cursor c = getContentResolver().query(Constants.CONTENT_URI, LIST_PROJECTION, null, null, null);
		
		if(0 == c.getCount()){
			// CP is empty so populate it
			String[] stations = getResources().getStringArray(R.array.Airports);
			for(String station : stations){
				ContentValues values = new ContentValues();
				values.put(DAO.COL_NAME, station);
				values.put(DAO.COL_UPDATED, sdf.format(GregorianCalendar.getInstance().getTime()));
				getContentResolver().insert(Constants.CONTENT_URI, values);
			}
		}
	}
}
