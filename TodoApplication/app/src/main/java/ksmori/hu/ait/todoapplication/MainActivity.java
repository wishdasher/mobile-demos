package ksmori.hu.ait.todoapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;

    @BindView(R.id.etTodo)
    EditText etTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSave)
    public void savePressed(Button btn) {
        final View todo =
                getLayoutInflater().inflate(R.layout.todo, null);

        TextView tvTodo = (TextView) todo.findViewById(R.id.tvTodo);
        tvTodo.setText(etTodo.getText().toString());

        Button btnDel = (Button) todo.findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutContent.removeView(todo);
            }
        });
        layoutContent.addView(todo, 0);
    }
}
