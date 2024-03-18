package com.example.suhbat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.suhbat.R;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.example.suhbat.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    UserPreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new UserPreferenceManager(getApplicationContext());
        if (preferenceManager.getKey() == null){
            setContentView(R.layout.activity_login);
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }



    }
}