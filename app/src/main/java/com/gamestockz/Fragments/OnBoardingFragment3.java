package com.gamestockz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gamestockz.Activities.SignupActivity;
import com.gamestockz.R;

import org.jetbrains.annotations.NotNull;

public class OnBoardingFragment3 extends Fragment {

    TextView skip;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding3, container, false);

        skip=root.findViewById(R.id.skip3);

        skip.setOnClickListener(v -> {
            Intent intent1 = new Intent(getActivity(), SignupActivity.class);
            startActivity(intent1);
        });

        return root;
    }
}
