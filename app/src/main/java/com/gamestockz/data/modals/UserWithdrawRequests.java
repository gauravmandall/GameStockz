package com.gamestockz.data.modals;

public class UserWithdrawRequests {
    String Amount, Date, Status;

    public UserWithdrawRequests(){}

    public UserWithdrawRequests(String amount, String date, String status) {
        Amount = amount;
        Date = date;
        Status = status;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
