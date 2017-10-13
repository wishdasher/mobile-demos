package ksmori.hu.ait.weatherinfo.network;

import ksmori.hu.ait.weatherinfo.data.WeatherData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather")
    Call<WeatherData> getWeatherResults(@Query("q") String city, @Query("appid") String key, @Query("units") String units);

}
