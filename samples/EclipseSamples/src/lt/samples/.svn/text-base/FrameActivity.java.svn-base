package lt.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FrameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        
        Button yellBut = (Button) findViewById(R.id.frm_bt_yell);
        yellBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View img = findViewById(R.id.frm_img_yell);
				int visibility = img.getVisibility();
				switch(visibility){
				case View.VISIBLE:
					img.setVisibility(View.INVISIBLE);
					break;
				case View.INVISIBLE:
					img.setVisibility(View.VISIBLE);
					break;
				}				
			}
		});
    }
}