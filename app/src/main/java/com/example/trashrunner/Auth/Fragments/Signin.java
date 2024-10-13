package com.example.trashrunner.Auth.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends Fragment {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button loginButton;
    private TextView signupTxtView;
    private TextView forgotpassTxtView;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;
    AppCompatImageView truckicon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);


        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);
        signupTxtView = view.findViewById(R.id.signup);
        passwordToggle = view.findViewById(R.id.passwordToggle);
        truckicon = view.findViewById(R.id.truckicon);


        Glide.with(this)
                .load(R.drawable.rent_service_image_1) // Replace with your drawable resource
                .into(truckicon);


        NavController navController = NavHostFragment.findNavController(this);
        signupTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_to_signup);
            }
        });
        forgotpassTxtView = view.findViewById(R.id.forgotpassword);
        forgotpassTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_to_forgotpassword);
            }
        });


        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24);
                } else {
                    // Show password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24);
                }
                // Move cursor to the end of the text
                password.setSelection(password.getText().length());
                isPasswordVisible = !isPasswordVisible;
            }
        });


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (TextUtils.isEmpty(emailText)) {
                email.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(passwordText)) {
                password.setError("Password is required");
                return;
            }

            // Sign in with Firebase
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            // Navigate to another activity (e.g., MainActivity)
                        } else {
                            Toast.makeText(getContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}