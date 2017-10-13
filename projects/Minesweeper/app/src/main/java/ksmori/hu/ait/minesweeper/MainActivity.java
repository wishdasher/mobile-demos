package ksmori.hu.ait.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ksmori.hu.ait.minesweeper.model.MinesweeperModel;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SIZE = "SIZE";
    public static final String KEY_MIMES = "NUM_MINES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        final EditText etSizeValue = (EditText) findViewById(R.id.etSizeValue);
        final EditText etMineValue = (EditText) findViewById(R.id.etMineValue);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(etSizeValue.getText().toString());
                int numMines = Integer.parseInt(etMineValue.getText().toString());
                if (numMines >= size * size) {
                    Toast.makeText(getApplicationContext(), R.string.tooManyMinesMsg, Toast.LENGTH_SHORT).show();
                } else if (size >= 15) {
                    Toast.makeText(getApplicationContext(), R.string.sizeTooLargeMsg, Toast.LENGTH_SHORT).show();
                } else if (size < 1 || numMines < 0){
                    Toast.makeText(getApplicationContext(), R.string.badPuzzleMsg, Toast.LENGTH_SHORT).show();
                } else {
                        MinesweeperModel.createCustomInstance(size, numMines);
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra(KEY_SIZE, size);
                        intent.putExtra(KEY_MIMES, numMines);
                        startActivity(intent);
                    }
            }
        });
    }
}
