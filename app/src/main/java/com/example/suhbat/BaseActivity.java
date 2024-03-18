package com.example.suhbat;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {


    DatabaseReference reference;
    UserPreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new UserPreferenceManager(this);
        reference = FirebaseDatabase.getInstance().getReference("users").child(preferenceManager.getKey());

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xato", "onStart: ");
        reference.child("lastTime").setValue("online");
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.child("lastTime").setValue("online");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.child("lastTime").setValue(getTime());

    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

}
