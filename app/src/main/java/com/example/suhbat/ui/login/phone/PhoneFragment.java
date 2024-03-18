package com.example.suhbat.ui.login.phone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suhbat.R;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {

    TextInputLayout edtPhone;
    MaterialButton buttonContinue;
    UserPreferenceManager preferenceManager;


    public PhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        preferenceManager = new UserPreferenceManager(requireContext());


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getPrefixText() + Objects.requireNonNull(edtPhone.getEditText()).getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("phone",phone);
                preferenceManager.savePhone(phone);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragNavHost);
                navController.navigate(R.id.codeFragment,bundle);

            }
        });


    }

    private void initViews(View view) {
    edtPhone = view.findViewById(R.id.edtPhoneNumber);
    buttonContinue = view.findViewById(R.id.buttonContinue);
    }



}