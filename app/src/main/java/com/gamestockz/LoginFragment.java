package com.gamestockz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginFragment extends Fragment {

    TextInputLayout mobile, password;
    Button login;
    TextView forget;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mobile = view.findViewById(R.id.mobile);
        password = view.findViewById(R.id.pass);
        login = view.findViewById(R.id.login);
        forget = view.findViewById(R.id.forgetpass);

//        This is for translation X axis
        mobile.setTranslationX(800);
        password.setTranslationX(800);
        forget.setTranslationX(800);
        login.setTranslationX(800);

//        This is for alpha animation
        mobile.setAlpha(v);
        password.setAlpha(v);
        forget.setAlpha(v);
        login.setAlpha(v);

//        This is for animation delay
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forget.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return view;
    }
}