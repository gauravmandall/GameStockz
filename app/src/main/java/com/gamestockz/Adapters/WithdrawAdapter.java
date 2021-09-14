package com.gamestockz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamestockz.R;
import com.gamestockz.data.modals.UserWithdrawRequests;

import java.util.ArrayList;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserWithdrawRequests> userWithdrawRequestsArrayList;

    public WithdrawAdapter(Context context, ArrayList<UserWithdrawRequests> userWithdrawRequestsArrayList) {
        this.context = context;
        this.userWithdrawRequestsArrayList = userWithdrawRequestsArrayList;
    }

    @NonNull
    @Override
    public WithdrawAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawAdapter.MyViewHolder holder, int position) {

        UserWithdrawRequests userWithdrawRequests = userWithdrawRequestsArrayList.get(position);

        holder.Amount.setText(userWithdrawRequests.getAmount());
        holder.Date.setText(userWithdrawRequests.getDate());

        holder.Status.setText(userWithdrawRequests.getStatus());

    }

    @Override
    public int getItemCount() {
        return userWithdrawRequestsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Amount, Date, Status;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Amount = itemView.findViewById(R.id.amount);
            Date = itemView.findViewById(R.id.date);
            Status = itemView.findViewById(R.id.status);

        }
    }

}
