package lt.fragments;

import android.R;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

// TODO 1 Change the base class to be ListActivity
// Fix imports as required
public class ExpensesListFragment extends ListFragment {
	
	// TODO 2  Add new fields here
	private ArrayAdapter<String> mExpensesAdapter;
	private ArrayList<String> mExpensesArrayList;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO 3 Remove the next line as ListActivity sets up the Content View automatically
        // Removed setContentView(R.layout.main);
        
        // TODO 4 call getExpensesArrayList() here
        // (It's a member of this class)
        mExpensesArrayList = getExpensesArrayList(); 
        
        // TODO 5 Create an ArrayAdapter<String> and assign it to mExpensesAdapter
        // Parameters for the constructor: 
        //	 this � the Context, 
        //   R.layout.expenses_list_item � the ID of the layout, 
        //   mExpensesArrayList � the ArrayList of dummy data
        
        mExpensesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, mExpensesArrayList); 
        
        // TODO 6 Call setListAdapter() passing the newly created ArrayAdapter as the adapter to use
        
        setListAdapter(mExpensesAdapter);
    }
    
	
	private ArrayList<String> getExpensesArrayList(){
//		Create an ArrayList of strings using the dummy data defined
//      at the end of this class  
		ArrayList<String> expensesArrayList = new ArrayList<String>();
		expensesArrayList.addAll(Arrays.asList(EXPENSES));  
		return expensesArrayList;
		
	}
	
	
	// Dummy data
	static final String[] EXPENSES = new String[] {"Trip to NYC on QM2", "Venice on Orient Express", "Beijing on Trans-Sibera Express"};	
}