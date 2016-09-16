package com.ltree.expenses.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ltree.expenses.ExpenseEntryActivity;

public class ExpenseEntryActivityTest extends
		ActivityInstrumentationTestCase2<ExpenseEntryActivity> {

	private ExpenseEntryActivity mActivity;
	private TextView mTV_Description;
	private EditText mET_Description;
	private TextView mTV_Date;
	private EditText mET_Date;
	private TextView mTV_Amount;
	private EditText mET_Amount;
	private ImageButton mBT_Receipt;
	private Button mTV_Save;

	public ExpenseEntryActivityTest() {
		super("com.ltree.expenses", ExpenseEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		
		mTV_Date = (TextView) mActivity.findViewById(com.ltree.expenses.R.id.expEdt_tv_date);
		mET_Date = (EditText)mActivity.findViewById(com.ltree.expenses.R.id.expEdt_et_date);
		
		mTV_Description = (TextView) mActivity.findViewById(com.ltree.expenses.R.id.expEdt_tv_description);
		mET_Description = (EditText)mActivity.findViewById(com.ltree.expenses.R.id.expEdt_et_description);

		mTV_Amount = (TextView) mActivity.findViewById(com.ltree.expenses.R.id.expEdt_tv_amount);
		mET_Amount = (EditText)mActivity.findViewById(com.ltree.expenses.R.id.expEdt_et_amount);
		
		mBT_Receipt = (ImageButton) mActivity.findViewById(com.ltree.expenses.R.id.expEdt_ib_receipt);
		mTV_Save = (Button) mActivity.findViewById(com.ltree.expenses.R.id.expEdt_bt_save);		
		

	}

	public void testPreconditions() {
		assertNotNull(mTV_Description);
		assertNotNull( mET_Description);
		assertNotNull(mTV_Date);
		assertNotNull(mET_Date);
		assertNotNull(mTV_Amount);
		assertNotNull(mET_Amount);
		assertNotNull(mBT_Receipt);
		assertNotNull(mTV_Save);
	}

	public void testLabels(){
		assertEquals(mTV_Description.getText(), mActivity.getString(com.ltree.expenses.R.string.expEdt_str_description));
		assertEquals(mTV_Date.getText(), mActivity.getString(com.ltree.expenses.R.string.expEdt_str_date));
		assertEquals(mTV_Amount.getText(), mActivity.getString(com.ltree.expenses.R.string.expEdt_str_amount));
	}
	
}

