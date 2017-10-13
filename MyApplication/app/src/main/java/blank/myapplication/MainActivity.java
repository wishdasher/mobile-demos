package blank.myapplication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout layoutRoot = (LinearLayout) findViewById(R.id.activity_main);

        final EditText editText = (EditText) findViewById(R.id.editTextField);
        Button btnTime = (Button) findViewById(R.id.btnTime);
        final TextView tv = (TextView) findViewById(R.id.tvStatus);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MAIN_TAG", "onClick called");

                String time = getString(R.string.date_display_text) + (new Date(System.currentTimeMillis())).toString();
                Toast.makeText(MainActivity.this, time, Toast.LENGTH_LONG).show();
                tv.setText(editText.getText().toString());
                Snackbar.make(layoutRoot, time, Snackbar.LENGTH_LONG).show();

            }
        });


    }
}
