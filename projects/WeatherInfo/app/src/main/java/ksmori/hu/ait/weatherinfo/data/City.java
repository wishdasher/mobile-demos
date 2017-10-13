package ksmori.hu.ait.weatherinfo.data;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {

    @PrimaryKey
    private String cityID;

    private String cityName;

    public City() {

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public City(String cityName) {
        this.cityName = cityName;

    }

    public String getCityID() {
        return cityID;
    }

}
