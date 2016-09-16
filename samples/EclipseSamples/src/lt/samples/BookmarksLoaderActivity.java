package lt.samples;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.ContactsContract.Contacts;
import android.widget.SimpleCursorAdapter;

public class BookmarksLoaderActivity extends ListActivity
	implements LoaderManager.LoaderCallbacks<Cursor>
{
	private String[] mProjection;
	private SimpleCursorAdapter mAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProjection = new String[] { Browser.BookmarkColumns._ID,
				Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL };
		String[] displayFields = new String[] { Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL };
		int[] displayViews = new int[] { R.id.text1, R.id.text2 };

		mAdapter = new SimpleCursorAdapter(this, R.layout.two_item_list,
									null, displayFields, displayViews,0);
		setListAdapter(mAdapter);
		
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, android.provider.Browser.BOOKMARKS_URI,
        		mProjection, null, null, null );
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
		
	}

}
