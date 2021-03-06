package com.ltree.expenses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ltree.expenses.data.DAO;
import com.ltree.expenses.data.Expense;
import com.ltree.expenses.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ExpensesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "ExpenseListFragment"; // Changed for the Fragment
    private SimpleCursorAdapter mAdapter;
    private Uri mBaseUri;

    private ArrayAdapter<String> mExpensesAdapter;
    private ArrayList<String> mExpensesArrayList;
//    private ListView mList; // Not needed as we are now extending ListFragment which includes the list

    private DAO dao;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpensesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DONE 2 get the Intent associated with this Activity
        Intent intent =getActivity().getIntent();
        // set baseUri to the Uri specified with the launching Intent (as data)
        mBaseUri = intent.getData();
        // DONE 3 If no Data was passed in the launching Intent, then default it
        // to Expense.ExpenseItem.CONTENT_URI
        if (mBaseUri == null) {
            mBaseUri =Expense.ExpenseItem.CONTENT_URI;
        }

        // Call the queryExpenses method on the DAO you just created.
        // The first parameter is an array representing the columns you wish to
        // be returned
        // Use Expense.ExpenseItem.LIST_PROJECTION for this
        // We want to return all the expenses so selection and selectionArgs can both be null
        // DONE 4 Comment out this query from the last exercise which used the
        // DAO directly
        //Cursor cursor = dao.queryExpenses(Expense.ExpenseItem.LIST_PROJECTION, null, null);


        // The names of the cursor columns to display in the view, initialized
        // to the description column
        // Note the creation of these two arrays specifying the columns to be
        // displayed
        // and the resource ID's to use to display them
        // android.R.id.text1 is a built in resource to show simple text
        // DONE 5 Note that this is the same as when we used the DAO
        String[] dataColumns = { Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION } ;
        int[] viewIDs = { android.R.id.text1 };


        // Create a new SimpleCursorAdapter
        // Creates the backing adapter for the ListView.
        // DONE 6 Replace cursor with null
        mAdapter = new SimpleCursorAdapter(getActivity(), // The Context for the ListView
                android.R.layout.simple_list_item_single_choice,      // Points to the XML for a list item
                null,                      // The cursor to get items from
                dataColumns,						// The array of Strings holding the names of the data columns to display
                viewIDs,						// The array of resource ids used to display the data
                0
                // DONE 7 Add the flags parameter (0) to method call
        );

        // DONE 8 Initialize the LoaderManager
        getLoaderManager().initLoader(0, null, this);

        // TODO: Change Adapter to display your content
        setListAdapter(mAdapter);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
    // /* Done 10 - Remove the block comment around the three methods
    // implementing LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Done 11 modify the code to return a new CursorLoader. Note the
        // parameters
        // The first parameter is the URI for the ContentProvider which
        // The remaining parameters are the same as the ones passed into
        // dao.queryExpenses (which you just commented out earlier)
        // with the addition of a sort order parameter (ascending / descending)
        return new CursorLoader(getActivity(), mBaseUri, // Use the URI stored in the
                // Intent for this Activity
                Expense.ExpenseItem.LIST_PROJECTION, // Return the expense
                // description and
                // amount
                null, // No where clause, return all records.
                null, // No where clause, therefore no where column values.
                Expense.ExpenseItem.DEFAULT_SORT_ORDER // Use the default sort
                // order.
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Done 12 modify the code to swap the cursor on the adapter
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Done 13 modify the code to set cursor on the adapter to null
        mAdapter.swapCursor(null);

    }
}
