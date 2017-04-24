package longtm.cookpad.test.wheatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private TextView cityText;
    private TextView tTempTitle;
    private TextView temp;
    private TextView tPresTitle;
    private TextView press;
    private TextView tWindTitle;
    private TextView wind;
    private TextView tHumTitle;
    private TextView hum;
    private ImageView imgView;
    private Button weather7Days;

    JSONObject data = null;
    JSONWeatherTask jsonTask;
    WeatherSingleton insWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insWeather = WeatherSingleton.getInstance();

        cityText = (TextView) findViewById(R.id.tCity);
        tTempTitle = (TextView) findViewById(R.id.tTempTitle);
        temp = (TextView) findViewById(R.id.tTemp);
        tHumTitle = (TextView) findViewById(R.id.tHumidityTitle);
        hum = (TextView) findViewById(R.id.tHumidity);
        tPresTitle = (TextView) findViewById(R.id.tPressureTitle);
        press = (TextView) findViewById(R.id.tPressure);
        tWindTitle = (TextView) findViewById(R.id.tWindTitle);
        wind = (TextView) findViewById(R.id.tWind);
        imgView = (ImageView) findViewById(R.id.bWeather);
        weather7Days = (Button)findViewById(R.id.bGoto7DayView);
        weather7Days.setVisibility(View.INVISIBLE);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String temp = place.getName().toString();
                Log.i(TAG, "Place: " + place.getName());
                insWeather.strCity = place.getName().toString();
                insWeather.lDataForecast.clear();

                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{temp});
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void goto7DayView(View v)
    {
        Intent intent = new Intent(this, Weather7Day.class);
        startActivity(intent);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = null;
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
            if(data != null && data != "") {
                try {
                    weather = new Weather();
                    weather = JSONWeatherParser.getWeather(data);

                    // Let's retrieve the icon
                    weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String dataForecast = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], "7"));
            if(dataForecast != null && dataForecast != "") {
                try {
                    JSONWeatherParser.getForecastWeather(dataForecast);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            if(weather != null) {
                if (weather.iconData != null && weather.iconData.length > 0) {
                    Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                    imgView.setImageBitmap(img);
                }

                cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
                tTempTitle.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
                tHumTitle.setText("Humidity:");
                hum.setText("" + weather.currentCondition.getHumidity() + "%");
                tPresTitle.setText("Pressure:");
                press.setText("" + weather.currentCondition.getPressure() + " hPa");
                tWindTitle.setText("Wind:");
                String strWind = weather.wind.getSpeed() + " mps " + weather.wind.getDeg() + "°";
                wind.setText(strWind);

                weather7Days = (Button)findViewById(R.id.bGoto7DayView);
                weather7Days.setVisibility(View.VISIBLE);
            }
            else
            {
                String msg = "Don't have data";
                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
