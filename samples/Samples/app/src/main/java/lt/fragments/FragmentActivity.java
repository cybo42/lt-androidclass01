package lt.fragments;

import lt.samples.R;
import android.app.Activity;
import android.os.Bundle;

public class FragmentActivity extends Activity {

	/**
	* @param savedInstanceState
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity_layout);
	}

}
