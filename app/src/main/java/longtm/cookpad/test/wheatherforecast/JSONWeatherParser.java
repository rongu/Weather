package longtm.cookpad.test.wheatherforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class JSONWeatherParser {

	public static Weather getWeather(String data) throws JSONException  {
		Weather weather = new Weather();

		// We create out JSONObject from the data
		JSONObject jObj = new JSONObject(data);
		
		// We start extracting the info
		Location loc = new Location();
		
		JSONObject coordObj = getObject("coord", jObj);
		loc.setLatitude(getFloat("lat", coordObj));
		loc.setLongitude(getFloat("lon", coordObj));
		
		JSONObject sysObj = getObject("sys", jObj);
		loc.setCountry(getString("country", sysObj));
		loc.setSunrise(getInt("sunrise", sysObj));
		loc.setSunset(getInt("sunset", sysObj));
		loc.setCity(getString("name", jObj));
		weather.location = loc;
		
		// We get weather info (This is an array)
		JSONArray jArr = jObj.getJSONArray("weather");
		
		// We use only the first value
		JSONObject JSONWeather = jArr.getJSONObject(0);
		weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
		weather.currentCondition.setDescr(getString("description", JSONWeather));
		weather.currentCondition.setCondition(getString("main", JSONWeather));
		weather.currentCondition.setIcon(getString("icon", JSONWeather));
		
		JSONObject mainObj = getObject("main", jObj);
		weather.currentCondition.setHumidity(getInt("humidity", mainObj));
		weather.currentCondition.setPressure(getInt("pressure", mainObj));
		weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
		weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
		weather.temperature.setTemp(getFloat("temp", mainObj));
		
		// Wind
		JSONObject wObj = getObject("wind", jObj);
		weather.wind.setSpeed(getFloat("speed", wObj));
		weather.wind.setDeg(getFloat("deg", wObj));
		
		// Clouds
		JSONObject cObj = getObject("clouds", jObj);
		weather.clouds.setPerc(getInt("all", cObj));
		
		// We download the icon to show
		
		
		return weather;
	}

	public static void getForecastWeather(String data) throws JSONException  {

		WeatherSingleton forecast = WeatherSingleton.getInstance();

		// We create out JSONObject from the data
		JSONObject jObj = new JSONObject(data);

		JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.JAPAN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		// We traverse all the array and parse the data
		for (int i=0; i < jArr.length(); i++) {
			JSONObject jDayForecast = jArr.getJSONObject(i);

			// Now we have the json object so we can extract the data
			DataForecast df = new DataForecast();
			// We retrieve the timestamp (dt)
            long timestamp = jDayForecast.getLong("dt");
			df.date = sdf.format(new Date(timestamp*1000));
			// Pressure & Humidity Wind
			df.pressure = "" +  String.valueOf(jDayForecast.getDouble("pressure")) + " hPa";;
			df.humidity = "" + String.valueOf(jDayForecast.getDouble("humidity")) + "%";
            df.wind = String.valueOf(getFloat("speed", jDayForecast)) + " mps " + String.valueOf(getFloat("deg", jDayForecast))  + "°";

            JSONObject jTempObj = jDayForecast.getJSONObject("temp");
            df.temp = " " + Math.round((float) jTempObj.getDouble("day") - 273.15) + "°C";

			JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
			JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
			String desc = getString("description", jWeatherObj);
            String cond = getString("main", jWeatherObj);
            df.temTitle = cond + "(" + desc + ")";
			String code = getString("icon", jWeatherObj);
			try {
                byte[] iconData = (new WeatherHttpClient()).getImage(code);
                df.img = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
			}
			catch (Exception ex)
			{
				// do something
				ex.printStackTrace();
			}

			forecast.lDataForecast.add(df);
		}
	}


	private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
		JSONObject subObj = jObj.getJSONObject(tagName);
		return subObj;
	}
	
	private static String getString(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getString(tagName);
	}

	private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
		return (float) jObj.getDouble(tagName);
	}
	
	private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getInt(tagName);
	}
	
}
