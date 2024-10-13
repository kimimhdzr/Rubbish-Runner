package com.example.trashrunner.Main.Community.Services;

import android.content.Context;
import android.net.Uri;

import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NewPosts {
    private FirebaseFirestore db;
    CurrentUserSharedPreference userProfile;


    public NewPosts() {
        db = FirebaseFirestore.getInstance();
    }

    public void createPost(Context context, String content, List<String> selectimageUrls, PostCreationCallback callback) {
        userProfile = new CurrentUserSharedPreference(context);

        // Step 1: Upload the selected images and get the uploaded URLs
        uploadImages(selectimageUrls, new ImageUploadCallback() {
            @Override
            public void onUploadSuccess(List<String> uploadimageUrls) {
                // Notify that images were uploaded successfully
                callback.onImagesUploaded(true);

                // Step 2: Create the post using the uploaded image URLs
                Map<String, Object> sender = new HashMap<>();
                sender.put("email", userProfile.getEmail());
                sender.put("user_id", userProfile.getKeyId());
                sender.put("username", userProfile.getName());
                sender.put("profile_pic", userProfile.getProfilePic());

                // Create reactions
                Map<String, Object> reactions = new HashMap<>();
                reactions.put("comment_count", 0);
                reactions.put("like_count", 0);

                // Create the post data
                Map<String, Object> post = new HashMap<>();
                post.put("content", content);
                post.put("images", uploadimageUrls);  // Use the uploaded image URLs
                post.put("timestamp", FieldValue.serverTimestamp());
                post.put("sender", sender);
                post.put("reactions", reactions);

                // Step 3: Save the post to the Firestore 'posts' collection
                db.collection("posts")
                        .add(post)
                        .addOnSuccessListener(documentReference -> {
                            // Post creation successful
                            System.out.println("Post successfully written with ID: " + documentReference.getId());
                            callback.onPostCreated(true, uploadimageUrls);  // Trigger post creation callback
                        })
                        .addOnFailureListener(e -> {
                            // Post creation failed
                            System.err.println("Error creating post: " + e.getMessage());
                            callback.onPostCreated(false, uploadimageUrls);  // Trigger failure callback
                        });
            }

            @Override
            public void onUploadFailure() {
                // Notify that image upload failed
                callback.onImagesUploaded(false);
            }
        });
    }


    private void uploadImages(List<String> selectimageUrls, ImageUploadCallback callback) {
        List<String> uploadimageUrls = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0);  // To track how many images have been uploaded
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        if (selectimageUrls == null || selectimageUrls.isEmpty()){
            callback.onUploadSuccess(uploadimageUrls);  // All images uploaded successfully
            return;
        }
        int totalImages = selectimageUrls.size();
        for (int i = 0; i < totalImages; i++) {
            // Assuming you have a Firebase Storage reference
            String fileUri = selectimageUrls.get(i);
            StorageReference imageRef = storageRef.child("images/posts/" + userProfile.getKeyId() + "/" + UUID.randomUUID().toString());

            // Upload the image file to Firebase Storage
            imageRef.putFile(Uri.parse(fileUri))
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL once the image is uploaded
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            uploadimageUrls.add(uri.toString());

                            // Check if all images have been uploaded
                            if (uploadCount.incrementAndGet() == selectimageUrls.size()) {
                                callback.onUploadSuccess(uploadimageUrls);  // All images uploaded successfully
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors during upload
                        callback.onUploadFailure();  // Trigger the failure callback
                    });
        }
    }


}
