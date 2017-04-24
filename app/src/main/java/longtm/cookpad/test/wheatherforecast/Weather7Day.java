package longtm.cookpad.test.wheatherforecast;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Weather7Day extends Activity {
    private WeatherSingleton inst;

    private static RecyclerView recyclerView;
    private WeatherAdapter mAdapter;
    private List<DataForecast> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather7_day);

        inst = WeatherSingleton.getInstance();
        mAdapter = new WeatherAdapter(listData);

        TextView strCity = (TextView)findViewById(R.id.tCity1);
        strCity.setText(inst.strCity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(mAdapter);

        prepareWeatherData();
    }

    void prepareWeatherData()
    {

        for(int i= 0; i < inst.lDataForecast.size(); i++)
        {
            DataForecast tem = new DataForecast();
            tem = inst.lDataForecast.get(i);
            listData.add(tem);
        }
        mAdapter.notifyDataSetChanged();
    }
}
