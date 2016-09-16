package com.ltree.expenses;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by user on 2/20/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String INPUT_DATE_MS = "Input Date in Millis";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = (Calendar) getArguments().getSerializable(INPUT_DATE_MS);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        //dateTextField.setText(ExpenseEntryFragment.dateFormatter.format(c.getTime()));
        updateOriginalDate(c);


    }

    /**
     * Method to communicate the modified date back to the Activity or Fragment which launched the dialog.
     * The code in here works very hard to try to find either a parent activity or a fragment which implements
     * DateUpdater. This only need to be this complex so that the same method will work in the various
     * configuration it is exposed to in our case-study environment.
     * @param c
     */
    private void updateOriginalDate(Calendar c) {
        DateUpdater dateUpdate = null;
        Activity parent = getActivity();
        if(parent instanceof DateUpdater) {
            // See if we are using Activities?
            dateUpdate = (DateUpdater)parent;
        } else {
            // Try the two possible fragments (dependent on layout).
            Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.frag_expense_details);
            if (null == frag) {
                // Must be in narrow view so the frag is frag_expense_list
                frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.frag_expense_list);
            }
            dateUpdate = (DateUpdater)frag;
        }
        dateUpdate.updateDate(c);
    }
}
