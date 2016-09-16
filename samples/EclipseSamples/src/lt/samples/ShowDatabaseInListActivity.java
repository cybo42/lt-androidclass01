package lt.samples;

import lt.samples.db.DatabaseHelper;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowDatabaseInListActivity extends ListActivity {


	private static final String TAG = "ShowDatabaseInListActivity";

	private DatabaseHelper mDbHelper;

	private SQLiteDatabase mDb;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DatabaseHelper(this);

		
		Toast.makeText(this, "If you don't see any data, run the 'Station List (CP) activity and try again", Toast.LENGTH_LONG).show();
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mDb.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDb = mDbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("WeatherStations");
		
		String[] projection = new String[] { "_id", "stationName", "lastUpdatedDate" }; 

		Cursor cursor = qb.query(mDb, projection, null, null, null, null, null);

		String[] fromFields = new String[] { "stationName", "lastUpdatedDate" };
		int[] toViews = new int[] { R.id.text1, R.id.text2 };

		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.two_item_list,
											cursor, fromFields, toViews);
		setListAdapter(cursorAdapter);		
	}

}
