package com.mridul.MonthlyBudgetTracker.Models;

public class Model {

    String id,title,amount,date;
    long income,expense;

    public Model(String id, String title, String amount, String date, long income, long expense) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getExpense() {
        return expense;
    }

    public void setExpense(long expense) {
        this.expense = expense;
    }
}
