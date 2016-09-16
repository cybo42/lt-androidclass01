package lt.samples;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraLauncherActivity extends Activity {

	private static final String TAG="CameraLauncherActivity";
	
	public static final int CAM_REQUEST_CODE = 103;
	private Button launchButton;
	private ImageView img;
	 // Holds the location of the receipt file we are capturing
	private File receiptFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_launcher_layout);
		
		launchButton = (Button)findViewById(R.id.camLnch_bt_launch);
		img = (ImageView)findViewById(R.id.camLnch_bt_img);
		
		launchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CameraLauncherActivity.this, CameraEmulatorActivity.class);

				// Set up receiptFile to specify where the image should be stored
				// Need to pass in the ID of the expenseItem so we can
				// create the file name here.
				// TODO Get the File object representing the External Public Pictures area
				File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); 
				// TODO append the "receipts" path to the directory
				File receiptsDir = new File(baseDir, "receipts");
				// TODO Create the directories if they do not yet exist
				if(!receiptsDir.exists()){
					// TODO Create the directories if they do not yet exist
					// Embed the creation in the if test so a failure gets logged
					if(!receiptsDir.mkdirs()){
						Log.e(TAG, "Failed to create directory to save receipts");
						return;  // There is not point continuing if we can't save the file
					}					
				}
				receiptFile = new File(receiptsDir, "receipt.jpg");
				Log.i(TAG, "Capturing recipt and storing in : " + receiptFile.getAbsolutePath());			
				// Now convert the fileName into a Provider URI so it can be written
				// to.
				// TODO Convert the file path to a URI
				Uri uri = Uri.fromFile(receiptFile);
				// TODO Add the uri to the Intent as Extra data with a name of MediaStore.EXTRA_OUTPUT
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				// TODO Set the Extra data MediaStore.EXTRA_VIDEO_QUALITY to 1 (high quality)
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				
				startActivityForResult(intent, CAM_REQUEST_CODE);
				
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
	 	// Check the resultCode is OK and 
		// correlate the requestCode
	    if (resultCode == Activity.RESULT_OK && 
	    		requestCode == CAM_REQUEST_CODE) {

            // Extract data from the resultIntent here if there is any
	    	// Process the photograph
	    	Drawable camImage = BitmapDrawable.createFromPath(receiptFile.getAbsolutePath());
	    	img.setImageDrawable(camImage);	        	

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
        }
	}
	
}
