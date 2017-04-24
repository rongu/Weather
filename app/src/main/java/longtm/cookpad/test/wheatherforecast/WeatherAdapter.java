package longtm.cookpad.test.wheatherforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<DataForecast> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgWeather;
        TextView cityName;
        TextView tempTitle;
        TextView temp;
        TextView presTitle;
        TextView pressure;
        TextView humTitle;
        TextView humidity;
        TextView windTitle;
        TextView wind;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgWeather = (ImageView) itemView.findViewById(R.id.bImgWea);
            cityName = (TextView)itemView.findViewById(R.id.tDate1);
            tempTitle = (TextView)itemView.findViewById(R.id.tT1);
            temp = (TextView)itemView.findViewById(R.id.tTemp1);
            presTitle = (TextView)itemView.findViewById(R.id.tP1);
            pressure = (TextView)itemView.findViewById(R.id.tPressure1);
            humTitle = (TextView)itemView.findViewById(R.id.tH1);
            humidity = (TextView)itemView.findViewById(R.id.tHumidity1);
            windTitle = (TextView)itemView.findViewById(R.id.tW1);
            wind = (TextView)itemView.findViewById(R.id.tWind1);
            date = (TextView)itemView.findViewById(R.id.tDate1);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public WeatherAdapter(List<DataForecast> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather, parent, false);
        ViewGroup.LayoutParams layoutparams = view.getLayoutParams();

        layoutparams.height = parent.getMeasuredHeight() / 4;
        view.setLayoutParams(layoutparams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int id) {
        holder.cityName.setText(dataSet.get(id).city);
        holder.tempTitle.setText(dataSet.get(id).temTitle);
        holder.temp.setText(dataSet.get(id).temp);
        holder.humTitle.setText("Humidity:");
        holder.humidity.setText(dataSet.get(id).humidity);
        holder.presTitle.setText("Pressure:");
        holder.pressure.setText(dataSet.get(id).pressure);
        holder.windTitle.setText("Wind:");
        holder.wind.setText(dataSet.get(id).wind);
        holder.date.setText(dataSet.get(id).date);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
