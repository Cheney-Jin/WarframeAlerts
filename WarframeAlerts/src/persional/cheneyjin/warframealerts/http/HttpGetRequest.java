package persional.cheneyjin.warframealerts.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpGetRequest {

	public StringBuffer doGet(String url) {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		try {
			URL localURL = new URL(url);
			URLConnection connection = localURL.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception( "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}

			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultBuffer;

	}
}
