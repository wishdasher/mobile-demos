package ksmori.hu.ait.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import ksmori.hu.ait.minesweeper.model.MinesweeperModel;
import ksmori.hu.ait.minesweeper.view.MinesweeperView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final int size = getIntent().getIntExtra(MainActivity.KEY_SIZE, MinesweeperModel.DEFAULT_SIZE);
        final int numMines = getIntent().getIntExtra(MainActivity.KEY_MIMES, MinesweeperModel.DEFAULT_MINES);

        final MinesweeperView gameView = (MinesweeperView) findViewById(R.id.minesweeperView);
        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.btnToggle);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gameView.setDigMode(false);
                } else {
                    gameView.setDigMode(true);
                }
            }
        });

        Button btnRestart = (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton.setChecked(false);
                gameView.restart(size, numMines);
            }
        });
    }

    public void updateMinesLeft(int n) {
        ((TextView) findViewById(R.id.numMinesLeftField)).setText(getString(R.string.minesLeftLabel) + n);
    }
}
