package com.ltree.donow52;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.widget.SimpleCursorAdapter;

public class BookmarkActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		
		// TODO Modify the projection to select _ID, TITLE and URL
		// Hint: all the column names are defined in Browser.BookmarkColumns
		String[] projection = new String[] { Browser.BookmarkColumns._ID,Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL };
		
		String[] displayFields = new String[] { Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL };
		int[] displayViews = new int[] { R.id.text1, R.id.text2 };

		// TODO Specify the URI to retrieve browser bookmarks
		// Hint from the previous slide, it is android.provider.Browser.BOOKMARKS_URI
		Cursor cur = getContentResolver().query(android.provider.Browser.BOOKMARKS_URI /* replace the null */,
												projection, null, 
												null, null);
		// We will examine the deprecation issue later in this chapter
		setListAdapter(new SimpleCursorAdapter(this, 
												R.layout.two_item_list,
												cur, 
												displayFields, 
												displayViews));
	}
}