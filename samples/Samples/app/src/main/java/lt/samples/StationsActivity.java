package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class StationsActivity extends Activity implements AdapterView.OnItemClickListener {

	ArrayList<String> mWeatherStationArrayList;
    ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.stations_list);
        mList = (ListView) findViewById(R.id.listView);
		ArrayList<String> weatherStationArrayList = getListData();
		// Create an Array adapter 
		ArrayAdapter<String> stationListAdapter = new ArrayAdapter<String>(getApplicationContext(),
							R.layout.list_item,	// Resource id of the TextView to display the data
							weatherStationArrayList);	// The array list to be displayed
		// Set the adapter into the list
		mList.setAdapter(stationListAdapter);
        mList.setOnItemClickListener(this);


	}

	/**
	 * List item click handler
	 */

    @Override
    public void onItemClick(AdapterView<?> theList, View view, int position, long id) {
        String station = (String)theList.getItemAtPosition(position);
        Toast toast = Toast.makeText(this, station, Toast.LENGTH_LONG);
        toast.show();
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)theList.getAdapter();
        arrayAdapter.insert("xxx", position);
    }

	private ArrayList<String> getListData(){
		//		Create an ArrayList of strings using the dummy station list defined in the resource file
		ArrayList<String> weatherStationArrayList = new ArrayList<String>();
		weatherStationArrayList.addAll(Arrays.asList(getResources().getStringArray(R.array.Airports)));  	
		return weatherStationArrayList;
	}

}
