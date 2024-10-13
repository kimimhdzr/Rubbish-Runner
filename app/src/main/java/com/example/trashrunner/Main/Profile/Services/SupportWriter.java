package com.example.trashrunner.Main.Profile.Services;

import android.content.Context;

import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportWriter {
    private FirebaseFirestore db;
    CurrentUserSharedPreference userProfile;

    public SupportWriter() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void writeSupport(Context context,
                             String title, String description,
                             String selectedSupport,
                             List<String> imageUrls,
                             SupportWriterCallback callback) {

        userProfile = new CurrentUserSharedPreference(context);

        // Create participants map
        Map<String, Object> participants = new HashMap<>();
        participants.put("user_id", userProfile.getKeyId());
        participants.put("username", userProfile.getName());
        participants.put("email_from", userProfile.getEmail());
        participants.put("profile_pic", userProfile.getProfilePic());


        // Create details map
        Map<String, Object> details = new HashMap<>();
        details.put("description", description);
        details.put("title", title);
        details.put("images", imageUrls);
        details.put("support", selectedSupport);

        // Create report data map
        Map<String, Object> supportData = new HashMap<>();
        supportData.put("participants", participants);
        supportData.put("details", details);
        supportData.put("timestamp", FieldValue.serverTimestamp());

        // Generate a new report ID and save to Firestore
        db.collection("supports")
                .add(supportData)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                    callback.onSuccess();

                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    callback.onFailure(e);
                });
    }
}
