package lt.samples;

import lt.fragments.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SamplesSelectionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.samples_selection);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create an instance of the Menu inflater
	    MenuInflater inflater = getMenuInflater();
	    // Inflate the menu
	    inflater.inflate(R.menu.samples_menu, menu);
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
	    case R.id.menu_bookmarks:
	    	startActivity(new Intent(this, BookmarksActivity.class));
	        return true;		 
	    case R.id.menu_bookmarks_loader:
	    	startActivity(new Intent(this, BookmarksLoaderActivity.class));
	        return true;
	    case R.id.menu_camera_launcher:
	    	startActivity(new Intent(this, CameraLauncherActivity.class));
	        return true;	
	    case R.id.menu_contacts:
	    	startActivity(new Intent(this, ListContactsActivity.class));
	    	return true;
	    case R.id.menu_dialog:
	    	startActivity(new Intent(this, DialogActivity.class));
	        return true;
	    case R.id.menu_database:
	    	startActivity(new Intent(this, DatabaseActivity.class));
	        return true;
	    case R.id.menu_db_in_list_view:
	    	startActivity(new Intent(this, ShowDatabaseInListActivity.class));
	        return true;		        
	    case R.id.menu_event_handler:
	    	startActivity(new Intent(this, EventHandlerActivity.class));
	        return true;	
	    case R.id.menu_fragments:
	    	startActivity(new Intent(this, FragmentActivity.class));
	    	return true;
	    case R.id.menu_gallery:
	    	startActivity(new Intent(this, GalleryActivity.class));
	        return true;	
	    case R.id.menu_frame:
	    	startActivity(new Intent(this, FrameActivity.class));
	        return true;		        
	    case R.id.menu_home:
	    	startActivity(new Intent(this, HomeActivity.class));
	        return true;	
	    case R.id.menu_menu:
	    	startActivity(new Intent(this, MenuActivity.class));
	        return true;	
	    case R.id.menu_notification:
	    	startActivity(new Intent(this, NotificationActivity.class));
	        return true;	
	    case R.id.menu_toast:
	    	startActivity(new Intent(this, ShowToastActivity.class));
	        return true;
	    case R.id.menu_stations_list:
	    	// process the menu action
	    	startActivity(new Intent(this, StationListActivity.class));
	        return true;	
	        
	    case R.id.menu_stations_list_cp:
	    	// process the menu action
	    	Intent i = new Intent(this, StationListCPActivity.class );
	    	startActivity(i);
	        return true;	
	    case R.id.menu_simple_service:    
			startService(new Intent(this, SimpleIntentService.class));
			return true;
	    case R.id.menu_threads:
	    	startActivity(new Intent(this, ThreadedActivity.class));
	    	return true;
	    case R.id.menu_json_test:
	    	startActivity(new Intent(this, JSONActivity.class));
	    	return true;
	    case R.id.menu_http_client:
	    	startActivity(new Intent(this, HttpRequestActivity.class));
	    	return true;	
	    case R.id.menu_versions:
	    	startActivity(new Intent(this, VersionCheckActivity.class));
	    	return true;	    	
	    case R.id.menu_widget_examples:
	    	startActivity(new Intent(this, WidgetExamples.class));
	    	return true;
	    	
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}		
}
