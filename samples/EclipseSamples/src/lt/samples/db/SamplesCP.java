package lt.samples.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class SamplesCP extends ContentProvider {
	/**
	 * UriMatcher used to determine which of the possible URL patterns is being
	 * used to address the ContentProvider The URI matcher converts URL patterns
	 * to int values which are then used in switch statements
	 */
	private static final UriMatcher sUriMatcher;
	private static final String AUTHORITY = "com.ltree.weather";
	private static final int STATIONS = 1;
	private static final int STATION_ID = 2;
	private static final int ID_PATH_POSITION = 1;

	private DAO mDAO;

	static {
		// Note that the static block is run only once during the application
		// life-cycle
		// Thus the setup is done once for all instances of the class
		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// Add the pattern matching the URI ending with stations
		// Used to route the request to operations not specifiying an station Id
		// value
		sUriMatcher.addURI(AUTHORITY, "stations", STATIONS);
		// Add the pattern matching the URI ending with stations plus an integer
		// representing the ID of an station
		sUriMatcher.addURI(AUTHORITY, "station/#", STATION_ID);

	}

	/**
	 * Chooses the MIME type based on the incoming URI pattern
	 */

	@Override
	public String getType(Uri uri) {
		// TODO MAtch on the url
		switch (sUriMatcher.match(uri)) {
		// If the pattern is for stations returns the returns the station ID
		// content type.
		case STATIONS:
			return "vnd.android.cursor.item/vnd.learningtree.station";
			// If the pattern is for station IDs, returns the station ID content
			// type.
		case STATION_ID:
			return "vnd.android.cursor.dir/vnd.learningtree.station";
			// If the URI pattern doesn't match any permitted patterns, throws
			// an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count;

        // Does the delete based on the incoming URI pattern.
        switch (sUriMatcher.match(uri)) {

            // If the incoming pattern matches the general pattern for stations, does a delete
            // based on the incoming "where" columns and arguments.
            case STATIONS:
            	count = mDAO.deleteStations(where, whereArgs);
            	break;
                // If the incoming URI matches a single station ID, does the delete based on the
                // incoming data, but modifies the where clause to restrict it to the
                // particular station ID.
            case STATION_ID:
            	int stationId = Integer.parseInt(uri.getPathSegments().get(ID_PATH_POSITION));
            	count = mDAO.deleteStationById(stationId);
                break;

            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        /*Gets a handle to the content resolver object for the current context, and notifies it
         * that the incoming URI changed. The object passes this along to the resolver framework,
         * and observers that have registered themselves for the provider are notified.
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;
    }

	/**
	 * Inserts a new item
	 * 
	 * @return the Uri of the new item
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Validates the incoming URI. Only the full provider URI without an _ID
		// is allowed for Inserts
		if (sUriMatcher.match(uri) != STATIONS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// Insert the data using the DAO
		long rowId = mDAO.insertStation(values);

		// If the insert succeeded, the row ID exists.
		if (rowId > 0) {
			// Creates a URI with the station ID pattern and the new row ID
			// appended to it.
			Uri stationUri = ContentUris.withAppendedId(
					Uri.parse("content://com.ltree.weather/stations"), rowId);
			// Notifies observers registered against this provider that the data
			// changed.
			getContext().getContentResolver().notifyChange(stationUri, null);
			return stationUri;
		}
		// If the insert didn't succeed, then the rowID is <= 0. Throws an
		// exception.
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDAO = new DAO(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor cursor = null;

		// Decide if the uri is for a single station or multiple
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for stations, chooses the Stations projection
		case STATIONS:
			cursor = mDAO.queryStations(projection, selection, selectionArgs,
					sortOrder);
			break;
		// If the incoming URI is for a single station identified by its ID
		case STATION_ID:
			int _ID = Integer.parseInt(uri.getPathSegments().get(
					ID_PATH_POSITION));
			cursor = mDAO.queryStationById(_ID, projection);
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw 
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// Tells the Cursor which URI to watch, so it knows when its source data
		// changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        int count;
        // Does the update based on the incoming URI pattern
        switch (sUriMatcher.match(uri)) {

            // If the incoming URI matches the general stations pattern, does the update based on
            // the incoming data.
            case STATIONS:

                // Does the update and returns the number of rows updated.
                count = mDAO.updateStations(values, where, whereArgs);
                break;

            // If the incoming URI matches a single station ID, does the update based on the incoming
            // data, but modifies the where clause to restrict it to the particular station ID.
            case STATION_ID:
                // From the incoming URI, get the station ID
                int stationId = Integer.parseInt(uri.getPathSegments().get(ID_PATH_POSITION));
                count = mDAO.updateStationById(stationId, values);
                break;
            // If the incoming pattern is invalid, throws an exception.
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /*Gets a handle to the content resolver object for the current context, and notifies it
         * that the incoming URI changed. The object passes this along to the resolver framework,
         * and observers that have registered themselves for the provider are notified.
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows updated.
        return count;
    }

}
