package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

public class MenuActivity extends Activity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create an instance of the Menu inflater
	    MenuInflater inflater = getMenuInflater();
	    // Inflate the menu
	    inflater.inflate(R.menu.menu, menu);
	    // Return true if the menu inflated OK
	    return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Get the Id of the selected menu item
	    switch (item.getItemId()) {
	    case R.id.menu_refresh:
	        // process the menu action
	        return true;
	    case R.id.menu_add:
	    	// process the menu action
	        return true;	
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}	
	
	
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
        // The data from the menu item.
        @SuppressWarnings("unused")
		AdapterView.AdapterContextMenuInfo info;

        try {
            // Casts the data object in the item into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
        	
        }

      switch (item.getItemId()) {
	    case R.id.menu_refresh:
	        // process the menu action
	        return true;
	    case R.id.menu_add:
	    	// process the menu action
	        return true;	     
	      default:
	        return super.onContextItemSelected(item);
	      }
    }	
}
