package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ShowToastActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, R.string.toast_popup, Toast.LENGTH_LONG).show();

	}

}
