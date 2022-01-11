package com.mridul.MonthlyBudgetTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mridul.MonthlyBudgetTracker.DatabaseHelper;
import com.mridul.MonthlyBudgetTracker.R;

public class UpdateActivity extends AppCompatActivity {

    private ImageView backBtn;
    private EditText updatedTitle, updatedAmount;
    private TextView date;
    private FloatingActionButton updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        backBtn = findViewById(R.id.back_id);
        updatedTitle = findViewById(R.id.updateTitle_id);
        updatedAmount = findViewById(R.id.updateAmount_id);
        date = findViewById(R.id.date_id);
        updateBtn = findViewById(R.id.update_id);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        updatedTitle.setText(intent.getStringExtra("title"));
        updatedAmount.setText(intent.getStringExtra("amount"));
        date.setText("Date : " + intent.getStringExtra("date"));
        long income = intent.getLongExtra("income", 0);
        long expense = intent.getLongExtra("expense", 0);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textUtils(updatedTitle.getText().toString().trim(), updatedAmount.getText().toString().trim())) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(UpdateActivity.this);
                    databaseHelper.update(id, updatedTitle.getText().toString().trim(), updatedAmount.getText().toString().trim(), income, expense);

                    startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private boolean textUtils(String a, String b) {

        if (Long.parseLong(b) < 1)
            return false;

        return (!a.isEmpty() || !b.isEmpty());
    }
}