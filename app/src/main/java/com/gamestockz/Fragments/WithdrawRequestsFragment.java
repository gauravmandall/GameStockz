package com.gamestockz.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gamestockz.Adapters.WithdrawAdapter;
import com.gamestockz.R;
import com.gamestockz.data.modals.UserWithdrawRequests;
import com.gamestockz.databinding.FragmentWithdrawRequestsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WithdrawRequestsFragment extends BottomSheetDialogFragment {


    FragmentWithdrawRequestsBinding binding;
    ArrayList<UserWithdrawRequests> userWithdrawRequests;
    WithdrawAdapter withdrawAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWithdrawRequestsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        String mobile = getActivity().getIntent().getStringExtra("mobilewithdraw");

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firestore = FirebaseFirestore.getInstance();
        userWithdrawRequests = new ArrayList<UserWithdrawRequests>();
        withdrawAdapter = new WithdrawAdapter(getActivity(), userWithdrawRequests);
        binding.recyclerView.setAdapter(withdrawAdapter);
        EventChangeListner(mobile);

        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog() {

        Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_withdraw_requests);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

    }

    private void EventChangeListner(String mobile) {

        firestore.collection("Users").document(mobile).collection("Withdraw Requests").orderBy("Numeric Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        userWithdrawRequests.add(dc.getDocument().toObject(UserWithdrawRequests.class));

                    }
                    if (dc.getType() == DocumentChange.Type.MODIFIED) {

//                        userWithdrawRequests.get(dc.getDocument().toObject(UserWithdrawRequests.class));

                    }

                    withdrawAdapter.notifyDataSetChanged();

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }


            }
        });

    }

}