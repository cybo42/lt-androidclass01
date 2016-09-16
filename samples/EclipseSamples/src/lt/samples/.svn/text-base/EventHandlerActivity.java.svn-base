package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EventHandlerActivity extends Activity implements View.OnClickListener {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_handler_layout);
		View v = findViewById(R.id.evt_but_createEvent);
		v.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Toast toast = Toast.makeText(this, R.string.toast_popup, Toast.LENGTH_LONG);
		toast.show();
		
	}

}
