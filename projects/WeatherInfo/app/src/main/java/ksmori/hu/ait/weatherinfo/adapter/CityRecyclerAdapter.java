package ksmori.hu.ait.weatherinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import ksmori.hu.ait.weatherinfo.CityListActivity;
import ksmori.hu.ait.weatherinfo.R;
import ksmori.hu.ait.weatherinfo.data.City;

public class CityRecyclerAdapter
        extends RecyclerView.Adapter<CityRecyclerAdapter.ViewHolder> {

    private List<City> cityList;
    private Context context;

    private Realm realm;

    public CityRecyclerAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
        RealmResults<City> cityResult =
                realm.where(City.class).findAll();
        cityList = new ArrayList<>();

        for (int i = 0; i < cityResult.size(); i++) {
            cityList.add(cityResult.get(i));
        }


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvCity.setText(cityList.get(position).getCityName());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCity(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CityListActivity)context).openDetailsActivity(
                        cityList.get(holder.getAdapterPosition()).getCityID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void addCity(String cityName) {
        realm.beginTransaction();
        City newCity = realm.createObject(City.class, UUID.randomUUID().toString());
        newCity.setCityName(cityName);

        realm.commitTransaction();

        cityList.add(newCity);
        notifyItemInserted(cityList.size());
    }

    public void deleteCity(int position) {
        realm.beginTransaction();
        cityList.get(position).deleteFromRealm();
        realm.commitTransaction();
        cityList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCity;
        private Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCity = (TextView) itemView.findViewById(R.id.tvCityName);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
