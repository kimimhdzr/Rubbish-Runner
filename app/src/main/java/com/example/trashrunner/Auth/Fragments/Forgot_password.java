package com.example.trashrunner.Auth.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trashrunner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends Fragment {

    private EditText emailEditTxt;

    private FirebaseAuth mAuth;
    private TextView signinTxt;
    private Button resetButton;
    private ImageButton signinarrowImgButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        signinTxt = view.findViewById(R.id.signintxt);
        signinarrowImgButton = view.findViewById(R.id.signinimg);
        emailEditTxt = view.findViewById(R.id.email);
        resetButton = view.findViewById(R.id.resetButton);
        mAuth = FirebaseAuth.getInstance();


        NavController navController = NavHostFragment.findNavController(this);
        signinTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_to_signin);
            }
        });
        signinarrowImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_to_signin);
            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditTxt.getText().toString().trim();

                if (email.isEmpty()) {
                    emailEditTxt.setError("Email is required");
                    emailEditTxt.requestFocus();
                    return;
                }

                // Send password reset email
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.nav_to_signin);
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return view;
    }
}