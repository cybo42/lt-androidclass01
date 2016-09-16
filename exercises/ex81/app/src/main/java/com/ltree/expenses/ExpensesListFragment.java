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

import com.ltree.expenses.data.Expense;

public class ExpensesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ExpenseListFragment";
	private SimpleCursorAdapter mAdapter;
	private Uri mBaseUri;

    public ExpensesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mBaseUri = intent.getData();

        if (mBaseUri == null) {
            mBaseUri = Expense.ExpenseItem.CONTENT_URI;
        }

        String[] dataColumns = { Expense.ExpenseItem.COLUMN_NAME_DESCRIPTION } ;
        int[] viewIDs = { android.R.id.text1 };

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_single_choice,
                null,
                dataColumns,
                viewIDs,
                0
        );

        getLoaderManager().initLoader(0, null, this);
        setListAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), mBaseUri,
                Expense.ExpenseItem.LIST_PROJECTION,
                null,
                null,
                Expense.ExpenseItem.DEFAULT_SORT_ORDER
        );
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
