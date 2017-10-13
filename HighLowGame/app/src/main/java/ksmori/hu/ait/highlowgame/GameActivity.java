package ksmori.hu.ait.highlowgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int random;
    private final String KEY_RAND = "RANDOM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_RAND)) {
            random = savedInstanceState.getInt(KEY_RAND);
        } else {
            generateNewRandom();
        }


        final EditText etGuess = (EditText) findViewById(R.id.etGuess);
        Button btnGuess = (Button) findViewById(R.id.btnGuess);
        final TextView tvStatus = (TextView) findViewById(R.id.tvStatus);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etGuess.getText().toString().equals("")) {
                    int myNumber = Integer.parseInt(etGuess.getText().toString());
                    if (random == myNumber) {
                        tvStatus.setText("You have won!");
                        startActivity(new Intent(GameActivity.this, ResultActivity.class));

                    } else if (random < myNumber) {
                        tvStatus.setText("Your number is higher.");
                    } else if (random > myNumber) {
                        tvStatus.setText("Your number is lower.");
                    }
                } else {
                    etGuess.setError("This field should not be empty!");
                }
            }
        });
    }

    private void generateNewRandom() {
        random = new Random(System.currentTimeMillis()).nextInt(2);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_RAND, random);
    }
}
