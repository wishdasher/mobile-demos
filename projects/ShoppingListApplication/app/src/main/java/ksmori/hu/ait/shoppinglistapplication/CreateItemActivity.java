package ksmori.hu.ait.shoppinglistapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import io.realm.Realm;
import ksmori.hu.ait.shoppinglistapplication.data.Item;

public class CreateItemActivity extends AppCompatActivity {

    public static final String KEY_ITEM_RETURN = "KEY_ITEM_RETURN";
    private Spinner spnCategory;
    private EditText etName;
    private EditText etDescription;
    private EditText etPrice;

    private Item itemToEdit = null;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        setupUI();

        if (getIntent().getSerializableExtra(MainActivity.KEY_ITEM_ID) != null) {
            initEdit();
            edit = true;
        }
    }

    private void setupUI() {
        spnCategory = (Spinner) findViewById(R.id.spnEditCategory);
        spnCategory.setAdapter(new ArrayAdapter<Item.Category>(this, android.R.layout.simple_spinner_item, Item.Category.values()));
        etName = (EditText) findViewById(R.id.etEditName);
        etDescription = (EditText) findViewById(R.id.etEditDescription);
        etPrice = (EditText) findViewById(R.id.etEditPrice);

        Button btnSave = (Button) findViewById(R.id.btnEditSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void initCreate() {
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();

    }

    private void initEdit() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_ITEM_ID);
        itemToEdit = getRealm().where(Item.class)
                .equalTo("itemID", itemID)
                .findFirst();

        if (itemToEdit != null) {
            spnCategory.setSelection(itemToEdit.getCategory());
            etName.setText(itemToEdit.getName());
            etDescription.setText(itemToEdit.getDescription());
            etPrice.setText("" + itemToEdit.getPrice());
        }
    }

    private void saveItem() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError(getString(R.string.emptyNameError));
        } else {
            if (!edit) {
                initCreate();
            }
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            itemToEdit.setCategory(spnCategory.getSelectedItemPosition());
            itemToEdit.setName(etName.getText().toString().trim());
            itemToEdit.setDescription(etDescription.getText().toString());
            if (!etPrice.getText().toString().isEmpty()) {
                itemToEdit.setPrice(Double.parseDouble(etPrice.getText().toString()));
            } else {
                itemToEdit.setPrice(0);
            }
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_ITEM_RETURN, itemToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            CreateItemActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmItem();
    }

}
