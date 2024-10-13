package com.example.trashrunner.Main.Profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.Auth.AuthActivity;
import com.example.trashrunner.DataBinding.RoomDB.AppDatabase;
import com.example.trashrunner.DataBinding.Cache.InfoCache;
import com.example.trashrunner.DataBinding.Persistent.ChatViewModel;
import com.example.trashrunner.DataBinding.Cache.PostCache;
import com.example.trashrunner.DataBinding.Cache.ReportCache;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.DataBinding.Cache.CompanyBasicCache;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ImageView profilebackgroundImage;
    private ShapeableImageView profileImage;
    private FirebaseAuth mAuth;
    private MaterialCardView Logout;
    private FirebaseFirestore db;
    private TextView profileNameTxt, joinDateTxt, phoneTxt, emailTxt, addressTxt;
    private CurrentUserSharedPreference profileManager; // Use ProfileManager to manage shared preferences
    private AppDatabase appDatabase; // Your Room Database instance
    MaterialCardView editProfileContainer, appiInfoContainer, supportContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Initialize views
        profileNameTxt = view.findViewById(R.id.ProfileNameTxt);
        joinDateTxt = view.findViewById(R.id.JoinDateTxt);
        phoneTxt = view.findViewById(R.id.linearLayout1).findViewById(R.id.phoneText);
        emailTxt = view.findViewById(R.id.linearLayout2).findViewById(R.id.emailText);
        addressTxt = view.findViewById(R.id.linearLayout3).findViewById(R.id.addressText);
        profileImage = view.findViewById(R.id.profileImage);
        editProfileContainer = view.findViewById(R.id.MaterialCardView4);
        appiInfoContainer = view.findViewById(R.id.MaterialCardView1);
        supportContainer = view.findViewById(R.id.MaterialCardView3);


        // Initialize ProfileManager
        profileManager = new CurrentUserSharedPreference(getContext());// Load user data from SharedPreferences
        setUserDataFromSharedPrefs();

        profilebackgroundImage = view.findViewById(R.id.ProfileBackgroundImage);
        Glide.with(this)
                .load(R.drawable.forest_landscape) // Replace with your drawable resource
                .into(profilebackgroundImage);

        profileImage = view.findViewById(R.id.profileImage);
        if (profileManager.getProfilePic() != null) {
            Glide.with(this)
                    .load(profileManager.getProfilePic())
                    .into(profileImage); // Replace with your ImageView reference
        }

        //logout
        Logout = view.findViewById(R.id.MaterialCardView2);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // Use 'getActivity()' if in a Fragment
                builder.setTitle("Confirmation")
                        .setMessage("Do you want to logout now?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            signOut();
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        appDatabase = Room.databaseBuilder(
                getContext(),
                AppDatabase.class,
                "chat_database"
        ).build();

        editProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navto_editProfile);
            }
        });
        appiInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navto_appinfo);
            }
        });
        supportContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_to_supportFragment);
//                Snackbar.make(v, "This feature is coming soon. Stay tuned for future updates!", Snackbar.LENGTH_LONG).show();
            }
        });




//
//        TypedValue typedValue = new TypedValue();
//        requireContext().getTheme().resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true);
//        int backgroundColor = typedValue.data;
//        requireActivity().getWindow().setStatusBarColor(backgroundColor);

        return view;
    }

    // Set profile data from SharedPreferences
    private void setUserDataFromSharedPrefs() {
        profileNameTxt.setText(profileManager.getName());
        phoneTxt.setText(formatPhoneNumber(profileManager.getPhone()));
        emailTxt.setText(profileManager.getEmail());
        addressTxt.setText(profileManager.getAddress());
        joinDateTxt.setText(profileManager.getJoinDate());
    }

    public static String formatPhoneNumber(String phoneNumber) {

        if (phoneNumber.isEmpty()) {
            phoneNumber = "01xxxxxxxx";
        }
        // Remove all spaces from the input
        phoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Ensure the phone number starts with '0'
        if (!phoneNumber.startsWith("0")) {
            throw new IllegalArgumentException("Phone number should start with 0");
        }

        String formattedPhoneNumber = "+(60)-" + phoneNumber.substring(1, 3) + " " +
                phoneNumber.substring(3, 7) + " " +
                phoneNumber.substring(7);

        return formattedPhoneNumber;
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();

        profileManager.clearProfileData();

        CompanyBasicCache.getInstance().clearCache();
        InfoCache.getInstance().clearCache();
        PostCache.getInstance().clearCache();
        ReportCache.getInstance().clearCache();

        ChatViewModel.clearAllPreferences();
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.chatDao().clearTable(); // Call the clear method
                // If you have multiple tables, call clearTable for each DAO
                Log.d("Database", "Chat table cleared");
            }
        }).start();
        // Clear messages from the Room database
        clearMessagesData();

        // Optionally, redirect the user to the login screen
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void clearMessagesData() {
        // Clear messages from Room database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getContext());
            db.messageDao().deleteAllMessages(); // Clear all messages from Room
            Log.d("Database", "All messages cleared from Room");
        }).start();
    }
}