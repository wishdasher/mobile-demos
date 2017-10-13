package ksmori.hu.ait.shoppinglistapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import ksmori.hu.ait.shoppinglistapplication.adapter.ItemRecyclerAdapter;
import ksmori.hu.ait.shoppinglistapplication.data.Item;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_NEW = 101;
    public static final int REQUEST_CODE_EDIT = 102;

    private ItemRecyclerAdapter itemRecyclerAdapter;
    private RecyclerView recyclerView;

    private CheckBox togglePurchased;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication) getApplication()).openRealm();
        setupToolbar();
        setupRecyclerView();
        setupTogglePurchased();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerItem);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        itemRecyclerAdapter = new ItemRecyclerAdapter(this, ((MainApplication) getApplication()).getRealmItem());
        recyclerView.setAdapter(itemRecyclerAdapter);
    }

    private void setupTogglePurchased() {
        togglePurchased = (CheckBox) findViewById(R.id.showPurchasedToggle);
        togglePurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (togglePurchased.isChecked()) {
                    itemRecyclerAdapter.showPurchased();
                } else {
                    itemRecyclerAdapter.hidePurchased();
                }
            }
        });
    }

    public boolean isToggleOn() {
        return togglePurchased.isChecked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent intentStart = new Intent(MainActivity.this, CreateItemActivity.class);
                startActivityForResult(intentStart, REQUEST_CODE_NEW);
                break;
            case R.id.action_delete_all:
                itemRecyclerAdapter.deleteAllItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication) getApplication()).closeRealm();
    }

    public void openEditActivity(int index, String itemID) {
        positionToEdit = index;
        Intent intent = new Intent(MainActivity.this, CreateItemActivity.class);
        intent.putExtra(KEY_ITEM_ID, itemID);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public void viewItemInformation(Item item) {
        String message =
                getString(R.string.categoryMsgLabel) + Item.Category.getCategoryFromIndex(item.getCategory()).toString() + "\n"
                        + getString(R.string.nameMsgLabel) + item.getName() + "\n"
                        + getString(R.string.descMsgLabel) + item.getDescription() + "\n"
                        + getString(R.string.priceMsgLabel) + item.getPrice() + "\n"
                        + getString(R.string.purchasedMsgLabel) + (item.isPurchased() ? getString(R.string.yes) : getString(R.string.no));
        new AlertDialog.Builder(this)
                .setTitle(R.string.viewDialogTitle)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_NEW) {
                    String itemID = data.getStringExtra(CreateItemActivity.KEY_ITEM_RETURN);
                    itemRecyclerAdapter.addItem(itemID);
                } else if (requestCode == REQUEST_CODE_EDIT) {
                    String itemID = data.getStringExtra(CreateItemActivity.KEY_ITEM_RETURN);
                    itemRecyclerAdapter.updateItem(itemID, positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, R.string.activityCancelled, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
