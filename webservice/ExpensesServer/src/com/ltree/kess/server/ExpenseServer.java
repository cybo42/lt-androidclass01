package com.ltree.kess.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A simple REST / JSON server which accepts a PUT containing an array of Expenses and stores them on the local file system
 * The URL for the PUT is exected to be http://localhost:8080/ExpensesServer/rest/ExpenseServer/putExpenses
 * The same array can then be retrieved from http://localhost:8080/ExpensesServer/rest/ExpenseServer/getExpenses using GET
 * 
 * The JSON Format should look like this:
 * {"expense":[{"description":"Description0","amount":"10.1","expenseDate":"1303492691292"},{"description":"Description1"...
 * 
 * 
 * @author Student
 *
 */

@Path ("/ExpenseServer")
public class ExpenseServer {
	private static final String SERVER_EXPENSES_FILE = "c:\\Course2771\\data\\expenses.dat";
	
	@Path("/getExpenses")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Returns the array of saved expenses.
	 * Be warned that if there are zero saved entries then an array with an empty Expense will be returned
	 * This is necessary to prevent weird behaviour with the JAXB JSON implementation.
	 * The client needs to check for an array with one Expense which has it's description set to null
	 */
	public Expense[] getExpenses()
	{
		
		Expense[] expenses = readExpenses();
		if(null == expenses || (expenses.length == 0)){
			expenses = new Expense[1];
			expenses[0] = new Expense();
		}
		return expenses;
	}
	
	@POST
	@Path("/putExpenses")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Saves the supplied array of expenses to local storage
	 */
	public Expense[]  putExpenses(Expense[] expenses){
		saveExpenses(expenses);
		return expenses;
	}
	
	// This method just to test if the configuration is working
	//
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "You have sent a text/plain request to the Expense Server. It needs to see JSON data.";
	}	

	private void saveExpenses(Expense[] expenses)
	{
		System.out.println("Writing " + expenses.length + " expenses to file");
		ObjectOutputStream oos = null;
		try{
			File f = new File(SERVER_EXPENSES_FILE);
			if(0 == expenses.length){
					// If no expenses then delete the file
				f.delete();
			} else {
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(expenses);
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if(null != oos){
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Expense[] readExpenses()
	{
		Expense[] expenses = null;
		ObjectInputStream ois = null;
		try{
			File f = new File(SERVER_EXPENSES_FILE);
			if(f.exists()){
				ois = new ObjectInputStream(new FileInputStream(f));
				
				expenses = (Expense[])ois.readObject();
			}
		} catch (IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(null != ois){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return expenses;
	}	
	
}

