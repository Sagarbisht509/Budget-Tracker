package com.mridul.MonthlyBudgetTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mridul.MonthlyBudgetTracker.Adapters.Adapter;
import com.mridul.MonthlyBudgetTracker.DatabaseHelper;
import com.mridul.MonthlyBudgetTracker.Models.Model;
import com.mridul.MonthlyBudgetTracker.R;

import java.util.ArrayList;

public class IncomeExpenseActivity extends AppCompatActivity {

    ArrayList<Model> data;
    Adapter adapter;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);

        ImageView back = findViewById(R.id.back_id);
        TextView from = findViewById(R.id.from_id);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_id);

        Intent intent = getIntent();
        from.setText(intent.getStringExtra("from"));

        data = new ArrayList<>();
        adapter = new Adapter(IncomeExpenseActivity.this, data, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(IncomeExpenseActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(IncomeExpenseActivity.this);
        if(from.getText().toString().trim().equals("income")) {
            Cursor cursor = databaseHelper.getAllIncome();

            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    data.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4), cursor.getLong(5)));
                }
            }
        } else {
            Cursor cursor = databaseHelper.getAllExpense();

            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    data.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4), cursor.getLong(5)));
                }
            }
        }
    }
}