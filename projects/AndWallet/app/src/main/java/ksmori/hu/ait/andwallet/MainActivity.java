package ksmori.hu.ait.andwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String INCOME = "income";
    public static final String OUTCOME = "outcome";

    @BindView(R.id.layoutContent) LinearLayout layoutContent;

    @BindView(R.id.tvTitle) TextView textViewTitle;
    @BindView(R.id.tvAmount) TextView textViewAmount;

    @BindView(R.id.tvBalance) TextView textViewBalance;

    @BindView(R.id.btnToggle) ToggleButton toggleButton;
    @BindView(R.id.btnSave) Button saveButton;
    @BindView(R.id.btnDeleteAll) Button deleteButton;

    @BindView(R.id.activity_main) View mainLayout;

    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balance = 0;
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SummaryActivity.class);
        int income = getTotalIncome();
        int outcome = income - balance;
        intent.putExtra(INCOME, income);
        intent.putExtra(OUTCOME, outcome);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private int getTotalIncome() {
        int income = 0;
        for (int i = 0; i < layoutContent.getChildCount(); i++) {
            View child = layoutContent.getChildAt(i);
            int amount = Integer.valueOf(
                    ((TextView) child.findViewById(R.id.tvAmount)).getText().toString());
            if (amount > 0) {
                income += amount;
            }
        }
        return income;
    }

    @OnClick(R.id.btnSave)
    public void savePressed() {
        String title = textViewTitle.getText().toString();
        String amount = textViewAmount.getText().toString();
        if (title.isEmpty() || amount.isEmpty()) {
            showEmptyError();
            return;
        }

        final View lineItem = getLayoutInflater().inflate(R.layout.line_item, null);
        ImageView imageView = (ImageView) lineItem.findViewById(R.id.thumbnail);
        if (!toggleButton.isChecked()) {
            imageView.setImageResource(R.drawable.green_up);
            balance += Integer.valueOf(amount);
        } else {
            imageView.setImageResource(R.drawable.red_down);
            balance -= Integer.valueOf(amount);
            amount = getString(R.string.minus) + amount;
        }

        ((TextView) lineItem.findViewById(R.id.tvTitle)).setText(title);
        ((TextView) lineItem.findViewById(R.id.tvAmount)).setText(amount);
        layoutContent.addView(lineItem);
        textViewBalance.setText(getString(R.string.balance) + balance);
    }

    private void showEmptyError() {
        final Snackbar snackbar = Snackbar.make(mainLayout, "Fields may not be empty", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Okay", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @OnClick(R.id.btnDeleteAll)
    public void deleteAll() {
        layoutContent.removeAllViews();
        balance = 0;
    }
}
