package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class InternalFileActivity extends Activity {
	private static final String TAG = "InternalFileActivity";
	
	private static final String FILE_NAME="DoNow51.txt";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.main_bt_clickme).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				writeFile();
				readFile();
				
			}
		});
    }
    
    private void writeFile()
    {
    	PrintWriter pw = null;
    	try {
    		// TODO Open the file defined by FILE_NAME for output as a PRIVATE file 
    		// HINT Use openFileOutput(String name, int mode). Mode is MODE_PRIVATE
    		// This relates to the slide titled Internal Storage 
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			pw = new PrintWriter(fos);
			// TODO add a text string to print (use your name perhaps)
			pw.println("Hello Joe");
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found", e);
			e.printStackTrace();
		} finally {
			if(null != pw){
				// TODO Close the PrintWriter
				pw.close();
			}
		}
    }
    
    private void readFile()
    {
    	BufferedReader reader = null;
    	try {
    		// TODO Open the FILE_NAME file for reading
    		// HINT: Look at the slide on Internal Storage
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader( new InputStreamReader((fis)));
			// TODO Read a line of text from the file 
			// HINT: reader.readLine();
			String fileText = reader.readLine();
			Toast toast = Toast.makeText(this, fileText, Toast.LENGTH_LONG);
			toast.show();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found", e);
		} catch (IOException e) {
			Log.e(TAG, "IO Error", e);
		} finally {
			if(null != reader){
				try {
					// TODO Close the reader
					reader.close();
				} catch (IOException e) {
					Log.e(TAG, "IO Error closing stream", e);
				}
			}
		}
    }
    
}