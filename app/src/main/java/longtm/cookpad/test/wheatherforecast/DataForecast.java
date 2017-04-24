package longtm.cookpad.test.wheatherforecast;

import android.graphics.Bitmap;


public class DataForecast {
    public Bitmap img;
    public String city, temTitle, temp, humidity, pressure, wind, date;

    public DataForecast()
    {
        city= temTitle=temp= humidity =pressure =wind = date = "";
    }
}
