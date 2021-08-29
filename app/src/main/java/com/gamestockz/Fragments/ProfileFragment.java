package com.gamestockz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gamestockz.Activities.OnRechargeActivity;
import com.gamestockz.Activities.WithdrawActivity;
import com.gamestockz.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    MaterialButton recharge, withdraw;
    TextView nameshow,balance;
    ListView listView;
    TextView mobiledb;
    String[] options={"Profile","setting","Invite code","Log out"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        nameshow = v.findViewById(R.id.nameShow);
        balance = v.findViewById(R.id.balance);
        recharge=v.findViewById(R.id.rechargeBtn);
        withdraw=v.findViewById(R.id.withdrawBtn);
        listView=v.findViewById(R.id.lstView);
        mobiledb=v.findViewById(R.id.mobile);
        ArrayList<String> arrayList= new ArrayList<>();
        arrayList.add("Profile");
        arrayList.add("Withdraw Funds");
        arrayList.add("Show Transaction History");
        arrayList.add("Invite Code");
        arrayList.add("Setting");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        arrayList.add("Logout");
        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,arrayList);
        listView.setAdapter(arrayAdapter);



        Intent intent=getActivity().getIntent();
        String name=intent.getStringExtra("name");
        String wallet=intent.getStringExtra("wallet");
        String mobile=intent.getStringExtra("mobile");
        // name=intent.getStringExtra("name");
        nameshow.setText(name);
        balance.setText(wallet);
        mobiledb.setText(mobile);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Users").child(mobile).child("wallet");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String walletf=snapshot.getValue(String.class);
                balance.setText(walletf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5=new Intent(getActivity(), OnRechargeActivity.class);
                intent5.putExtra("mobile2",mobile);
                startActivity(intent5);



            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                startActivity(intent);
            }
        });






        return  v;
    }
}