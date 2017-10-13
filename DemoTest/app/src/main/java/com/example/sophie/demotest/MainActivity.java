package com.example.sophie.demotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etData = (EditText) findViewById(R.id.etData);
        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the the 2nd activity
                Intent intentShowDetails = new Intent();
                intentShowDetails.setClass(MainActivity.this, DetailsActivity.class);
                startActivity(intentShowDetails);
            }
        });
    }
}
