package ksmori.hu.ait.weatherinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import ksmori.hu.ait.weatherinfo.adapter.CityRecyclerAdapter;

public class CityListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_CITY_ID = "city_id";
    public static final String KEY_UNIT = "key_unit";
    private static final String IMPERIAL_MODE = "imperial";
    private static final String METRIC_MODE = "metric";
    private CityRecyclerAdapter adapter;
    private RecyclerView recyclerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ((MainApplication)getApplication()).openRealm();

        setUpUI();
        setupRecyclerView();
    }

    private void setUpUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCityDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        recyclerCity = (RecyclerView) findViewById(R.id.recyclerCity);
        recyclerCity.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCity.setLayoutManager(layoutManager);

        adapter = new CityRecyclerAdapter(this,
                ((MainApplication)getApplication()).getRealm());
        recyclerCity.setAdapter(adapter);
    }

    private void showAddCityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_city_text);

        final EditText etCityText = new EditText(this);
        builder.setView(etCityText);

        builder.setPositiveButton(R.string.add_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = etCityText.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(CityListActivity.this, R.string.empty_err_msg, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.addCity(text);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void openDetailsActivity(String cityID) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(KEY_CITY_ID, cityID);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleUnit);
        if (toggleButton.isChecked()) {
            intent.putExtra(KEY_UNIT, METRIC_MODE);
        } else {
            intent.putExtra(KEY_UNIT, IMPERIAL_MODE);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /**if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean showAbout = false;

        if (id == R.id.add_city) {
            showAddCityDialog();
        } else if (id == R.id.about) {
            showAbout = true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (showAbout) {
            Toast.makeText(this, R.string.developer_msg, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }

}
