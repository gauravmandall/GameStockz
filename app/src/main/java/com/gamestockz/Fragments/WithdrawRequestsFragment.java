package com.gamestockz.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamestockz.R;
import com.gamestockz.databinding.FragmentWithdrawRequestsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class WithdrawRequestsFragment extends BottomSheetDialogFragment {

    FragmentWithdrawRequestsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = binding.getRoot();



       return view;
    }
}