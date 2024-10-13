package com.example.trashrunner.Main.Home.Services;
import android.content.Context;

import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportWriter {

    private FirebaseFirestore db;
    CurrentUserSharedPreference userProfile;

    public ReportWriter() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void writeReport(Context context, String companyId, String companyName, String companyEmail,
                            double latitude, double longitude,
                            String title, String description, List<String> imageUrls,
                            ReportWriteCallback callback) {

        userProfile = new CurrentUserSharedPreference(context);

        // Create participants map
        Map<String, Object> participants = new HashMap<>();
        participants.put("user_id", userProfile.getKeyId());
        participants.put("username", userProfile.getName());
        participants.put("company_id", companyId);
        participants.put("company_name", companyName);
        participants.put("email_from", userProfile.getEmail());
        participants.put("email_to", companyEmail);
        participants.put("profile_pic", userProfile.getProfilePic());

        // Create location map
        Map<String, Object> location = new HashMap<>();
        location.put("latitude", latitude);
        location.put("longitude", longitude);

        // Create details map
        Map<String, Object> details = new HashMap<>();
        details.put("description", description);
        details.put("title", title);
        details.put("images", imageUrls);

        // Create report data map
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("participants", participants);
        reportData.put("location", location);
        reportData.put("details", details);
        reportData.put("timestamp", FieldValue.serverTimestamp());

        // Generate a new report ID and save to Firestore
        db.collection("trash_reports")
                .add(reportData)
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
