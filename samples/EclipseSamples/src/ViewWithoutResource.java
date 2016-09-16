

import lt.samples.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class ViewWithoutResource extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EditText edtTxt = new EditText(this);
        LayoutParams layParms = new 
        	LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        edtTxt.setLayoutParams(layParms);
        edtTxt.setText("11/11/11");
    }
}

/*
<EditText android:layout_height="wrap_content"
android:text="11/11/11" android:layout_width="match_parent" android:id="@+id/expEdt_et_date"/>

*/