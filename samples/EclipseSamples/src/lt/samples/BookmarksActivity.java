package lt.samples;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.widget.SimpleCursorAdapter;

public class BookmarksActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		super.onResume();
		String[] projection = new String[] { Browser.BookmarkColumns._ID,
				Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL };
		String[] displayFields = new String[] { Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL };
		int[] displayViews = new int[] { R.id.text1, R.id.text2 };

		Cursor cur = getContentResolver().query(
				android.provider.Browser.BOOKMARKS_URI, /* replace the null */
				projection, null, null, null);
		
		setListAdapter(new SimpleCursorAdapter(this, R.layout.two_item_list,
				cur, displayFields, displayViews, 0));
	}

}
