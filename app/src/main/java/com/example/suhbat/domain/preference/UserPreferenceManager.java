package com.example.suhbat.domain.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferenceManager {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public UserPreferenceManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }
    public void savePhone(String phone){
        editor.putString("phone",phone).apply();
    }

    public String getPhone(){
        return preferences.getString("phone",null);
    }

    public void saveKey(String key){
        editor.putString("key",key).apply();
    }

    public void saveName(String name){
        editor.putString("name",name).apply();
    }

    public String getName(){
        return preferences.getString("name","");
    }

    public String getKey(){
        return preferences.getString("key",null);
    }
}
