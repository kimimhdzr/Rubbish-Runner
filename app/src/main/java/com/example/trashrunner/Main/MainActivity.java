package com.example.trashrunner.Main;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    CurrentUserSharedPreference profileManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize profileManager (example, adjust as needed)
        profileManager = new CurrentUserSharedPreference(this);

        bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set up NavController from NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Set up BottomNavigationView with NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Call fetchUserData after user logs in
        fetchUserData();
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    String name = documentSnapshot.getString("name");
                                    String phone_num = documentSnapshot.getString("phone_num");
                                    String email = documentSnapshot.getString("email");
                                    String address = documentSnapshot.getString("address");
                                    String joinDate = documentSnapshot.getString("joinDate");
                                    String role = documentSnapshot.getString("role");
                                    String profile_pic = documentSnapshot.getString("profile_pic");
                                    String companyid = "";
                                    if (documentSnapshot.contains("company_id")) {
                                        companyid = documentSnapshot.get("company_id") != null ? documentSnapshot.getString("company_id") : "";
                                    }

                                    Log.d("FirestoreData", "Data: " + name + phone_num + email + role + joinDate + companyid + uid);

                                    // Save user data in SharedPreferences
                                    profileManager.saveUserProfile(
                                            name,
                                            email,
                                            phone_num,
                                            address,
                                            joinDate,
                                            role,
                                            uid,
                                            companyid,
                                            profile_pic);
                                } else {
                                    Log.d("MainActivity", "No such document");
                                }

                                Log.e("Profile", "state: " + "success");
                            } else {
                                Log.d("MainActivity", "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }
}
