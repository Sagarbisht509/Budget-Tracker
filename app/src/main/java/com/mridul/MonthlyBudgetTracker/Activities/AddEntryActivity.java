package com.mridul.MonthlyBudgetTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mridul.MonthlyBudgetTracker.DatabaseHelper;
import com.mridul.MonthlyBudgetTracker.R;

import java.util.Calendar;

public class AddEntryActivity extends AppCompatActivity {

    EditText title, amount;
    Button incomeBtn, expenseBtn;

    TextView date;
    ImageView backBtn, cal;

    String d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        title = findViewById(R.id.title_id);
        amount = findViewById(R.id.amount_id);
       // calendar = findViewById(R.id.date_id);
        incomeBtn = findViewById(R.id.income_id);
        expenseBtn = findViewById(R.id.Expense_id);
        backBtn = findViewById(R.id.back_id);
        cal = findViewById(R.id.cal_id);
        date = findViewById(R.id.date_id);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayYear = calendar.get(Calendar.YEAR);

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String selectedDate =  dayOfMonth + " " + month + " " + year ;
                        date.setText(selectedDate);
                        d = selectedDate;
                    }
                },todayYear,todayMonth,todayDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        //date.setText(d);

//        DateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
//        Date date = new Date();
//        String d = dateFormat.format(date);
//
//        calendar.setText(d);

        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils(title.getText().toString().trim(), amount.getText().toString().trim())) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(AddEntryActivity.this);

                    databaseHelper.addTransactions(title.getText().toString().trim(), amount.getText().toString().trim(), d, "income");
                    startActivity(new Intent(AddEntryActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddEntryActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils(title.getText().toString().trim(), amount.getText().toString().trim())) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(AddEntryActivity.this);

                    databaseHelper.addTransactions(title.getText().toString().trim(), amount.getText().toString().trim(), d, "expense");
                    startActivity(new Intent(AddEntryActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddEntryActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean utils(String spendOn, String amu) {

        if (Long.parseLong(amu) < 1)
            return false;

        return !spendOn.isEmpty() && !amu.isEmpty();
    }
}