package lt.samples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpClientActivity extends Activity {
	private static final String TAG = "HttpRequestActivity";
	WebView webView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		webView = (WebView)findViewById(R.id.webView1);
		// TODO Set the IP address of localhost on the Windows machine where the service is running		
		String urlStr = "http://10.0.2.2:8080/index.jsp";
		// Note that network connections can only be made in a background thread
		// We are using an AsyncTask handler to launch a thread and handle the update
		try {
			new AsyncNetworkConnection().execute(new URL(urlStr));
		} catch (MalformedURLException e){
			webView.loadData("Excepetion: " + e.getMessage(), "text/html", "utf-8");
		}
	}



	class AsyncNetworkConnection extends AsyncTask<URL, String, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// Display the data in a web view
			webView.loadData(result, "text/html", "utf-8");
		}

		@Override
		protected String doInBackground(URL... arg0) {
			String results = null;
			try {
				results = fetchHTML(arg0[0]);
			} catch (Exception e) {
				String msg = getResources().getString(R.string.str_html_error, e.getMessage());
				Log.e(TAG, "Http connection error", e);
			}
			return results;
		}

		private String fetchHTML(URL serviceUrl) throws URISyntaxException, IOException, Exception
		{

			String result = "Nothing received! ";
			Log.i(TAG, "Attempting to contact service at: " + serviceUrl.toExternalForm());
			// 1 Open the connection
			HttpURLConnection urlConnection = (HttpURLConnection)serviceUrl.openConnection();

			try {
				//2 Check for the response
				int responseCode = urlConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					//3 Success: process the results from the input stream
					result = readStringFromStream(urlConnection.getInputStream());
				} else {
					//4 Failure: read the results from the error stream
					result = readStringFromStream(urlConnection.getErrorStream());
				}
				Log.i(TAG, "HTTP response code from service: " + responseCode);
			} catch (Exception e){
				Log.e(TAG, "Connection failed " + e.getMessage());
				result += "Connection failed " + e.getMessage();
			} finally {
				//5 Always disconnect
				urlConnection.disconnect();

			}

			return result;
		}

		/**
		 * Helper method to convert an input stream content toa String
		 * @param in
		 * @return
		 */
		private String readStringFromStream(InputStream in) {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {

				String nextLine = "";
				while ((nextLine = reader.readLine()) != null) {
					sb.append(nextLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
	}

}