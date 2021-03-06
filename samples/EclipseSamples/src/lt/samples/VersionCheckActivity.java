package lt.samples;

import android.app.Activity;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.os.Build;

public class VersionCheckActivity extends Activity {

	/**
	* @param savedInstanceState
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_me);
		
        findViewById(R.id.main_bt_clickme).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
					String version = "";
					switch(currentapiVersion){
					case VERSION_CODES.CUPCAKE:
						version = "CUPCAKE: May 2009: Android 1.5";
						break;
					case VERSION_CODES.DONUT:
						version = "DONUT	September 2009: Android 1.6";
						break;
					case VERSION_CODES.ECLAIR:
						version = "ECLAIR	November 2009: Android 2.0";
						break;
					case VERSION_CODES.ECLAIR_0_1:
						version = "ECLAIR_0_1	December 2009: Android 2.0.1";
						break;			
					case VERSION_CODES.ECLAIR_MR1:
						version = "ECLAIR_MR1	January 2010: Android 2.1";
						break;	
					case VERSION_CODES.FROYO:
						version = "FROYO	June 2010: Android 2.2";
						break;	
					case VERSION_CODES.GINGERBREAD:
						version = "GINGERBREAD	November 2010: Android 2.3";
						break;	
					case VERSION_CODES.GINGERBREAD_MR1:
						version = "GINGERBREAD_MR1	February 2011: Android 2.3.3";
						break;	
					case VERSION_CODES.HONEYCOMB:
						version = "HONEYCOMB	February 2011: Android 3.0";
						break;	
					case VERSION_CODES.HONEYCOMB_MR1:
						version = "Honeycomb 3.1";
						break;
					case VERSION_CODES.HONEYCOMB_MR2:
						version = "Honeycomb 3.21";
						break;
					case VERSION_CODES.ICE_CREAM_SANDWICH:
						version = "Ice Cream Sandwich 4.0";
						break;
						
					case VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
						version = "Ice Cream Sandwich Update 4.0.x";
						break;
						
					case VERSION_CODES.JELLY_BEAN:
						version = "Jellybean 4.1";
						break;
						
					case VERSION_CODES.JELLY_BEAN_MR1:
						version = "Jellybean update 4.2";
						break;
						
					default:
						version = "Version check failed - maybe this device is newer than our code?";
						
					}
					Toast tst = Toast.makeText(VersionCheckActivity.this, version, Toast.LENGTH_LONG);
					tst.show();
				} catch (Exception e) {
					Toast t= Toast.makeText(VersionCheckActivity.this, "Oops, this is a pre-Donut version!", Toast.LENGTH_LONG);
					t.show();
				}

				
			}
		});		
	}

}
