package longtm.cookpad.test.wheatherforecast;

import java.util.ArrayList;
import java.util.List;

public class WeatherSingleton {

    public String strCity;
    public List<DataForecast> lDataForecast;

    private static final WeatherSingleton instance = new WeatherSingleton();
    public static WeatherSingleton getInstance()
    {
        return instance;
    }

    WeatherSingleton()
    {
        strCity = "";
        lDataForecast = new ArrayList<DataForecast>() ;
    }
}
