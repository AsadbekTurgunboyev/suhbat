package com.example.suhbat.ui.login.data;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suhbat.R;
import com.example.suhbat.domain.model.UserData;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.example.suhbat.ui.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FillPersonFragment extends Fragment {

    TextInputLayout personNameEditText;
    MaterialButton buttonConfirm;

    FirebaseDatabase database;
    DatabaseReference reference;
    UserPreferenceManager preferenceManager;

    public FillPersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fill_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        preferenceManager = new UserPreferenceManager(requireContext());
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = personNameEditText.getEditText().getText().toString();
                if (name.isEmpty()){
                    return;
                }
                String phone = preferenceManager.getPhone();
                String key = reference.push().getKey();
                UserData data = new UserData(name,key,getTime(),"",phone,"online","");
                writeDatabase(data);
            }
        });
    }

    private void writeDatabase(UserData data) {
        reference.child(data.getKey()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    preferenceManager.saveKey(data.getKey());
                    preferenceManager.saveName(data.getName());
                    goHome();
                }
            }
        });
    }

    private void initViews(View view) {
        personNameEditText = view.findViewById(R.id.textInputLayout);
        buttonConfirm = view.findViewById(R.id.nextToVeritificationPage);
    }


    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss", Locale.getDefault());
       return sdf.format(new Date());
    }

    private void goHome() {
        Intent intent = new Intent(requireContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}