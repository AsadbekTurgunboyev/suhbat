package com.example.suhbat;

import android.app.Application;

import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyApp extends Application {

    DatabaseReference reference;
    UserPreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();


    }
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

}
