package lt.samples;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StationListActivity extends ListActivity {

	ArrayList<String> mWeatherStationArrayList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		ArrayList<String> weatherStationArrayList = getListData();
		// Create an Array adapter 
		ArrayAdapter<String> stationListAdapter = new ArrayAdapter<String>(getApplicationContext(),
							R.layout.list_item,	// Resource id of the TextView to display the data  
							weatherStationArrayList);	// The array list to be displayed
		// Set the adapter into the list
		setListAdapter(stationListAdapter);		
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
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)l.getAdapter();
		arrayAdapter.insert("xxx", position);

		
	}	
	
	private ArrayList<String> getListData(){
		//		Create an ArrayList of strings using the dummy station list defined in the resource file
		ArrayList<String> weatherStationArrayList = new ArrayList<String>();
		weatherStationArrayList.addAll(Arrays.asList(getResources().getStringArray(R.array.Airports)));  	
		return weatherStationArrayList;
	}

}
