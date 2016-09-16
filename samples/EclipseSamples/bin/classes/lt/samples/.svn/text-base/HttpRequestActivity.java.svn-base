package lt.samples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class HttpRequestActivity extends Activity {
    private static final String TAG = "HttpRequestActivity";


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_request_layout);

    	try {
			String html = fetchHTML();
			WebView webView = (WebView)findViewById(R.id.webView1);
			webView.loadData(html, "text/html", "utf-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    private String fetchHTML() throws URISyntaxException, ClientProtocolException, IOException, Exception
    {
    	DefaultHttpClient httpclient = null;
		// TODO Set the IP address of localhost on the Windows machine where the service is running		
		String urlStr = "http://10.0.2.2:8080/index.jsp";    	
   	 	// Create a new URI for the service (the urlStr)
		URI serviceUri = new URI(urlStr);			
    	Log.i(TAG, "Attempting to contact service at: " + urlStr);
		// Use the client to execute a request passing the HttpPut and ResponseHandler as parameters 
		String result;
		try {
			// Create a new HttpGet instance around the supplied service uri
			HttpGet getRequest = new HttpGet(serviceUri);
			// Create a response handler (BasicResponseHandler)
			ResponseHandler<String> handler = new BasicResponseHandler();
			// Create an instance of the DefaultHttpClient
			httpclient = new DefaultHttpClient(); 
			// Execute the request
			result = httpclient.execute(getRequest, handler);   
			Log.i(TAG, "Put to Service. Result: " + result);                
		} catch (Exception e) {
			throw e;
		} finally {
			// Shutdown the HttpClient
			if(null != httpclient){
				httpclient.getConnectionManager().shutdown();			
			}
		}
        return result;
    }
}
