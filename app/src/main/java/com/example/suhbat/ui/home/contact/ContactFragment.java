package com.example.suhbat.ui.home.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suhbat.R;
import com.example.suhbat.domain.model.UserData;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    RecyclerView contactRecyclerView;
    ContactAdapter contactAdapter;
    DatabaseReference reference;
    List<UserData> list;
    UserPreferenceManager preferenceManager;

    public ContactFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        preferenceManager = new UserPreferenceManager(requireContext());
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    UserData data = ds.getValue(UserData.class);
                    if (data != null && !data.getKey().equals(preferenceManager.getKey())) {
                        list.add(data);
                    }
                }

                contactAdapter = new ContactAdapter(list);
                contactRecyclerView.setAdapter(contactAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initViews(View view) {
        contactRecyclerView = view.findViewById(R.id.contactRecyclerView);

    }
}