package com.gamestockz.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.gamestockz.Fragments.GameSectionFragment;
import com.gamestockz.Fragments.MeeshoFragment;
import com.gamestockz.Fragments.ProfileFragment;
import com.gamestockz.R;
import com.google.firebase.auth.FirebaseAuth;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class DashboardActivity extends AppCompatActivity {
    private MeowBottomNavigation bnv_Main;
    FirebaseAuth auth;

    SharedPreferences preferences;
    public static final String LOGIN_CHECK = "MyLoginCheck";
    public static final String KEY_MOBILE = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initElements();

    }
    private void initElements(){

        preferences = getSharedPreferences(LOGIN_CHECK, MODE_PRIVATE);

        bnv_Main=findViewById(R.id.bnv_Main);
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.ic_game_stockz));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.ic_person));
        bnv_Main.show(2,true);
        replace(new GameSectionFragment());
        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        replace(new MeeshoFragment());
                        break;
                    case 2:
                        replace(new GameSectionFragment());
                        break;
                    case 3:
                        replace(new ProfileFragment());
                        break;
                }
                return null;
            }

        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("mobile", preferences.getString(KEY_MOBILE, null));
        fragment.setArguments(bundle);
        transaction.replace(R.id.frags,fragment);
        transaction.commit();

    }

}