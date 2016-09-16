/**
 * General constants class supporting the Expenses Application
 * @author Course 2771 Development Team
 *
 */

package com.ltree.expenses;

public class Constants {
	
	public static final int REQUEST_CODE_TAKE_PICTURE = 1002;
	public static final int REQUEST_PERMISSIONS = 42;

	private Constants(){}
	
	public static final String EXPENSE_ARRAY = "expenseArrayData";
	public static final String EXPENSE_ENTRY = "expenseEntry";
	
	public static final String EXPENSE_EXTRA_FILENAME = "com.ltree.expenses.filename";
	/** If true,causes the service to save to a local file */
	public static final String EXPENSE_EXTRA_EXPORT_TO_CSV = "export_to_csv";

	
	/** Preferences */
	public static final String PREF_SERVER_PUT_URL = "sync_server_put_url";
	public static final String PREF_SERVER_GET_URL = "sync_server_get_url";
	public static final String EXPENSE_ID = "expense_id";
	public static final long EXPENSE_ITEM_UNDEFINED = -1;
	
	/** keys for Intent extras for the simple approach taken in exercise 4.2 */
	public static final String EXPENSE_DESCRIPTION = "expense_description";
	public static final String EXPENSE_AMOUNT = "expense_amount";
	public static final String EXPENSE_DATE = "expense_date";
	
	
	public static final int REQUEST_CODE_EDIT = 1;
	protected static final int DATE_DIALOG_ID = 100;
	public static final int DELETE_CONFIRM_DIALOG_ID = 2;
	
	public static final String DEFAULT_CSV_FILENAME = "expenses.csv";
	public static final String BASE_URI = "expenseUri";
	public static final String EXPENSE_FRAG_TAG = "tag_expenses_frag";

	
}
