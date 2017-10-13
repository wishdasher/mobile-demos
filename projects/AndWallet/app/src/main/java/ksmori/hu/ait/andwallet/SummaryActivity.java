package ksmori.hu.ait.andwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryActivity extends AppCompatActivity{

    @BindView(R.id.incomeSummary) TextView tvIncome;
    @BindView(R.id.outcomeSummary) TextView tvOutcome;
    @BindView(R.id.balanceSummary) TextView tvBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        int income = getIntent().getIntExtra(MainActivity.INCOME, 0);
        int outcome = getIntent().getIntExtra(MainActivity.OUTCOME, 0);

        ButterKnife.bind(this);

        tvIncome.setText(getString(R.string.incomeLabel) + income);
        tvOutcome.setText(getString(R.string.outcomeLabel) + outcome);
        tvBalance.setText(getString(R.string.balanceLabel) + (income - outcome));

    }
}
