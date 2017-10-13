package ksmori.hu.ait.weatherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import ksmori.hu.ait.weatherinfo.data.City;
import ksmori.hu.ait.weatherinfo.data.WeatherData;
import ksmori.hu.ait.weatherinfo.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    private City city;
    private static final String API_KEY = "798e423683650a301acb446dd02e1db9";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_ICON = "http://openweathermap.org/img/w/";
    private static final String ICON_EXT = ".png";
    private String mode = "metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getIntentExtras();

        final TextView tvData = (TextView) findViewById(R.id.tvData);
        final ImageView imgWeather = (ImageView) findViewById(R.id.imgWeather);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherData> call = weatherAPI.getWeatherResults(city.getCityName(), API_KEY, mode);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    String icon = response.body().getWeather().get(0).getIcon();
                    Log.e("HI", icon);
                    Glide.with(DetailsActivity.this).load(API_ICON + icon + ICON_EXT).into(imgWeather);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+1")); // give a timezone reference for formating (see comment at the bottom
                    String sunrise = sdf.format(new Date(response.body().getSys().getSunrise()*1000L));
                    String sunset = sdf.format(new Date(response.body().getSys().getSunset()*1000L));

                    tvData.setText(response.body().getName() + ", " + response.body().getSys().getCountry() + "\n\n"
                            + getString(R.string.lat_label) + response.body().getCoord().getLat() + "\n"
                            + getString(R.string.long_label) + response.body().getCoord().getLon() + "\n"
                            + getString(R.string.desc_label) + response.body().getWeather().get(0).getDescription() + "\n"
                            + getString(R.string.temp_label) + response.body().getMain().getTemp() + "\n"
                            + getString(R.string.humidity_label) + response.body().getMain().getHumidity() + "\n"
                            + getString(R.string.pressure_label) + response.body().getMain().getPressure() + "\n"
                            + getString(R.string.wind_label) + response.body().getWind().getSpeed() + "\n"
                            + getString(R.string.sunrise_label) + sunrise + "\n"
                            + getString(R.string.sunset_label) + sunset);
                } else {
                    tvData.setText(city.getCityName() + "city not found");
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                tvData.setText(t.getLocalizedMessage());
            }
        });
    }

    private void getIntentExtras() {
        if (getIntent().hasExtra(CityListActivity.KEY_CITY_ID)) {
            String cityID = getIntent().getStringExtra(CityListActivity.KEY_CITY_ID);
            city = getRealm().where(City.class)
                    .equalTo("cityID", cityID)
                    .findFirst();
        }
        if (getIntent().hasExtra(CityListActivity.KEY_UNIT)) {
            mode = getIntent().getStringExtra(CityListActivity.KEY_UNIT);
        }
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealm();
    }


}
