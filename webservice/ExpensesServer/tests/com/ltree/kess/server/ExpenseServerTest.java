package com.ltree.kess.server;


import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExpenseServerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test invokes the REST server and checks that an array of expense items is returned.
	 */
	public void testGetExpenses() throws Exception {
//		ClientConfig config = new DefaultClientConfig();
//		Client client = Client.create(config);
//		WebResource service = client.resource(getServerURI());
//		String jsonResult = service.path("rest").path("ExpenseServer").path("getExpenses").accept(
//				MediaType.APPLICATION_JSON).get(String.class);		
		
		Expense[] expenses = new Expense[0];
		JSONArray jExpenses = expenseArrayToJSON(expenses);
		JSONObject j = new JSONObject();
		j.put("expense", jExpenses);
		// Put an empty expense array onto the server
		putToWebService(j.toString(), getServerURIForPut());		

		String jsonResult = getFromWebService();
        JSONObject jObject = new JSONObject(jsonResult);
        Assert.assertNotNull(jObject);
        
        // Something odd happens on the server if we pass empty Expense arrays.
        // Instead, there will always be a single element in the Array.
        // The Server then sends that back as a single JSON object, not an array of one!
        JSONObject jExpense = jObject.getJSONObject("expense");
        Assert.assertNotNull(jExpense);
        // As we put no expenses, that's what we expect back!

		
        // Setup for subtest 3 - create 3 expenses
		expenses = createTestExpenses();
		jExpenses = expenseArrayToJSON(expenses);
		j = new JSONObject();
		j.put("expense", jExpenses);
		putToWebService(j.toString(), getServerURIForPut());        
		
		// Now fetch them
		jsonResult = getFromWebService();
        jObject = new JSONObject(jsonResult);
        Assert.assertNotNull(jObject);
        
        jExpenses = jObject.getJSONArray("expense");
        Assert.assertNotNull(jExpenses);
        Assert.assertEquals(3, jExpenses.length());
        for(int i=0; i < jExpenses.length(); i++){
        	jExpense = jExpenses.getJSONObject(i);
        	Assert.assertNotNull(jExpense);
        	Assert.assertNotNull(jExpense.getString("description"));
        	Assert.assertNotNull(jExpense.getDouble("amount"));
        	Assert.assertNotNull(jExpense.getLong("expenseDate"));
        }
		
	}
	
	
	@Test
	/**
	 * Test sending an aaray of expenses to the server.
	 */
	public void testPutExpenses() throws Exception {
		try{
			Expense[] expenses = createTestExpenses();
			JSONArray jExpenses = expenseArrayToJSON(expenses);
			JSONObject j = new JSONObject();
			j.put("expense", jExpenses);
			putToWebService(j.toString(), getServerURIForPut());
			
			// Now read back and see what we got
			String jsonResult = getFromWebService();
	        JSONObject jObject = new JSONObject(jsonResult);
	        Assert.assertNotNull(jObject);
	        
	        jExpenses = jObject.getJSONArray("expense");
	        Assert.assertNotNull(jExpenses);
	        Assert.assertEquals(3, jExpenses.length());
		
			
		} catch (Exception e){
			e.printStackTrace();
			Assert.fail("Client Exception");
		}
	}	
	
	@SuppressWarnings("unused")
	private static URI getServerURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/ExpensesServer").build();
	}	
	
	private static URI getServerURIForPut() {
		return UriBuilder.fromUri(
				"http://localhost:8080/ExpensesServer/rest/ExpenseServer/putExpenses").build();
	}	
	
	private static URI getServerURIForGet() {
		return UriBuilder.fromUri(
				"http://localhost:8080/ExpensesServer/rest/ExpenseServer/getExpenses").build();
	}		
	
	private static final Expense[] createTestExpenses(){
		Expense[] expenses = new Expense[3];
		for(int i=0; i < expenses.length; i++){
			expenses[i] = new Expense("Description" + i, 10.1 + (10 * i), new Date().getTime());
		}
		return expenses;
	}
	
	private static final JSONObject expenseToJSON(Expense e) throws JSONException{
		JSONObject jObj = new JSONObject();
		jObj.put("description", e.description);
		jObj.put("amount", e.amount);
		jObj.put("expenseDate", e.expenseDate);
		return jObj;
	}
	
	private static final JSONArray expenseArrayToJSON(Expense[] expenses)  throws JSONException{
		JSONArray jArray = new JSONArray();
		System.out.println("Converting  " + expenses.length + " expenses to JSON");
		for(Expense e: expenses){
			jArray.put(expenseToJSON(e));
		}
		return jArray;
	}
	/**
	 * Very simple HttpClient based method to fetch data using Get
	 * @return
	 * @throws Exception
	 */
    public String  getFromWebService() throws Exception{  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpGet getRequest = new HttpGet(getServerURIForGet());
        getRequest.addHeader("deviceId", "Android");  
        getRequest.addHeader("Content-Type", MediaType.APPLICATION_JSON);
        ResponseHandler<String> handler = new BasicResponseHandler();   
        String result = httpclient.execute(getRequest, handler);   
        System.out.println("RECEIVED: " + result);
        httpclient.getConnectionManager().shutdown(); 
        return result;
 
    } // end callWebService()  	

	/**
	 * Very simple HttpClient based method to send data using Post
	 * @return
	 * @throws Exception
	 */

//    public void putToWebService(String msgBody) throws Exception{  
//        DefaultHttpClient httpclient = new DefaultHttpClient();  
//        HttpPut putRequest = new HttpPut(getServerURIForPut());
//        putRequest.setEntity(new StringEntity(msgBody));
//        putRequest.addHeader("deviceId", "Android");  
//        putRequest.addHeader("Content-Type", MediaType.APPLICATION_JSON);
//        ResponseHandler<String> handler = new BasicResponseHandler();   
//        String result = httpclient.execute(putRequest, handler);   
//        httpclient.getConnectionManager().shutdown();  
// 
//    } // end callWebService()  
	
    public void putToWebService(String msgBody, URI uri) throws Exception{  
///    	Log.i(TAG, "Attempting to contact service at: " + uri);
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPut putRequest = new HttpPut(uri);
        putRequest.setEntity(new StringEntity(msgBody));
        putRequest.addHeader("deviceId", "Android");  
        putRequest.addHeader("Content-Type", "application/json");
        ResponseHandler<String> handler = new BasicResponseHandler();   
        @SuppressWarnings("unused")
		String result = httpclient.execute(putRequest, handler);   
///        Log.i(TAG, "Put to Service. Result: " + result);
        httpclient.getConnectionManager().shutdown();  
 
    } // end callWebService()     
    
    
}
