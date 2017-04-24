package longtm.cookpad.test.wheatherforecast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String IMG_URL = "http://openweathermap.org/img/w/";
	private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=";
	private static String APP_ID = "&APPID=de70fb0bd44dd7cd5f7acf442ea2d83b";

	
	public String getWeatherData(String location) {
		HttpURLConnection con = null ;
		InputStream is = null;

		try {
			con = (HttpURLConnection) ( new URL(BASE_URL + location + APP_ID)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			
			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (  (line = br.readLine()) != null )
				buffer.append(line + "\r\n");
			
			is.close();
			con.disconnect();
			return buffer.toString();
	    }
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;

	}

	public String getForecastWeatherData(String location, String sForecastDayNum) {
		HttpURLConnection con = null;
		InputStream is = null;
		int forecastDayNum = Integer.parseInt(sForecastDayNum);

		try {

			// Forecast
			String url = BASE_FORECAST_URL + location;
			url = url + "&cnt=" + forecastDayNum + APP_ID;
			con = (HttpURLConnection) (new URL(url)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			StringBuffer buffer1 = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
			String line1 = null;
			while ((line1 = br1.readLine()) != null)
				buffer1.append(line1 + "\r\n");

			is.close();
			con.disconnect();

			System.out.println("Buffer [" + buffer1.toString() + "]");
			return buffer1.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;
	}


	public byte[] getImage(String code) {
		HttpURLConnection con = null ;
		InputStream is = null;
		try {
//			con = (HttpURLConnection) ( new URL(IMG_URL + code +".png")).openConnection();
//			con.setRequestMethod("GET");
//			con.setDoInput(true);
//			con.setDoOutput(true);
//			con.connect();

			URL imageUrl = new URL(IMG_URL + code +".png");
			HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ( is.available() > 0 && is.read(buffer) != -1)
				baos.write(buffer);

			return baos.toByteArray();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;
		
	}
}
