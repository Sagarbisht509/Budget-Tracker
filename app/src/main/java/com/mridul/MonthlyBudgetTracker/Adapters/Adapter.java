package com.mridul.MonthlyBudgetTracker.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mridul.MonthlyBudgetTracker.Activities.UpdateActivity;
import com.mridul.MonthlyBudgetTracker.DatabaseHelper;
import com.mridul.MonthlyBudgetTracker.Models.Model;
import com.mridul.MonthlyBudgetTracker.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<Model> allTransactions;
    Boolean fromMain;

    public Adapter(Context context, ArrayList<Model> allTransactions, Boolean fromMain) {
        this.context = context;
        this.allTransactions = allTransactions;
        this.fromMain = fromMain;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model model = allTransactions.get(position);

        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        if (model.getIncome() == 0) {
            holder.amount.setText("-" + model.getAmount());
        } else {
            holder.amount.setText("+" + model.getAmount());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (fromMain) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete this ");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            if (databaseHelper.delete(model.getId()) > 0) {
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                allTransactions.remove(model);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
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
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromMain) {
                    Intent intent = new Intent(context, UpdateActivity.class);

                    intent.putExtra("id", model.getId());
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("amount", model.getAmount());
                    intent.putExtra("date", model.getDate());
                    intent.putExtra("income", model.getIncome());
                    intent.putExtra("expense", model.getExpense());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTransactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_id);
            amount = itemView.findViewById(R.id.amount_id);
            date = itemView.findViewById(R.id.date_id);
        }
    }
}
