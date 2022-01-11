
package com.mridul.MonthlyBudgetTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mridul.MonthlyBudgetTracker.Adapters.Adapter;
import com.mridul.MonthlyBudgetTracker.DatabaseHelper;
import com.mridul.MonthlyBudgetTracker.Models.Model;
import com.mridul.MonthlyBudgetTracker.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton edit, addBtn, resetBtn, export;
    RecyclerView recyclerView;
    TextView total, totalIncome, totalExpense, resetText, addText;
    ArrayList<Model> data;
    Adapter adapter;

    DatabaseHelper databaseHelper;

    boolean vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView refresh = findViewById(R.id.refresh_id);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        resetText = findViewById(R.id.reset_text_id);
        addText = findViewById(R.id.add_text_id);
        addBtn = findViewById(R.id.add_id);
        resetBtn = findViewById(R.id.reset_id);
        edit = findViewById(R.id.edit_id);
        total = findViewById(R.id.total_id);
        totalIncome = findViewById(R.id.totalIncome_id);
        totalExpense = findViewById(R.id.totalExpense_id);
        resetText.setVisibility(View.GONE);
        addText.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);
        resetBtn.setVisibility(View.GONE);

        vis = false;

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vis) {
                    resetText.setVisibility(View.VISIBLE);
                    addText.setVisibility(View.VISIBLE);
                    addBtn.show();
                    resetBtn.show();

                    vis = true;
                } else {
                    resetText.setVisibility(View.GONE);
                    addText.setVisibility(View.GONE);
                    addBtn.hide();
                    resetBtn.hide();

                    vis = false;
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddEntryActivity.class));

                resetText.setVisibility(View.GONE);
                addText.setVisibility(View.GONE);
                addBtn.hide();
                resetBtn.hide();

                vis = false;
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Reset");
                builder.setMessage("Reset will cause lose all the transactions permanently \n \nAre you sure you want to reset?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        if (databaseHelper.deleteAll()) {
                            Toast.makeText(MainActivity.this, "Reset successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                resetText.setVisibility(View.GONE);
                addText.setVisibility(View.GONE);
                addBtn.hide();
                resetBtn.hide();

                vis = false;
            }
        });

        LinearLayout l1 = findViewById(R.id.linearLayout1_id);
        LinearLayout l2 = findViewById(R.id.linearLayout2_id);

        data = new ArrayList<>();
        adapter = new Adapter(MainActivity.this, data, true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.recyclerView_id);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllTransactions();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                data.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(4), cursor.getLong(5)));
            }
        }

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeExpenseActivity.class);
                intent.putExtra("from", "income");
                startActivity(intent);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeExpenseActivity.class);
                intent.putExtra("from", "expense");
                startActivity(intent);
            }
        });

        totalIncome.setText("₹" + databaseHelper.getTotalIncome());

        totalExpense.setText("₹" + databaseHelper.getTotalExpense());

        total.setText("₹" + databaseHelper.getTotal());
    }
}