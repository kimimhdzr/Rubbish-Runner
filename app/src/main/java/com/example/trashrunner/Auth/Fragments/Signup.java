package com.example.trashrunner.Auth.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.ProfileModel;
import com.example.trashrunner.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Signup extends Fragment {

    private TextView signinTxtView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText nameEditText, emailEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    AppCompatImageView truckicon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        signinTxtView = view.findViewById(R.id.signin);
        NavController navController = NavHostFragment.findNavController(this);
        signinTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_to_signin);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        addressEditText = view.findViewById(R.id.address);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirmpassword);
        signUpButton = view.findViewById(R.id.singupButton);

        truckicon = view.findViewById(R.id.truckicon);
        Glide.with(this)
                .load(R.drawable.rent_service_image_1) // Replace with your drawable resource
                .into(truckicon);

        signUpButton.setOnClickListener(v -> signUp());


        return view;
    }


    private void signUp() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Address is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }
        // Generate the current date as the join date
        String joinDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Create UserProfile with additional fields
                        ProfileModel userProfile = new ProfileModel(
                                name,
                                email,
                                "user", // Default role
                                "", // Default profile_pic (empty for now)
                                "",
                                address,
                                joinDate
                        );

                        // Save user info to Firestore
                        db.collection("users").document(user.getUid()).set(userProfile)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to save user info", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getContext(), "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}