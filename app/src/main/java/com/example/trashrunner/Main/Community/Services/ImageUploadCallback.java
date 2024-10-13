package com.example.trashrunner.Main.Community.Services;

import java.util.List;

public interface ImageUploadCallback {
    void onUploadSuccess(List<String> uploadimageUrls);  // Pass the uploaded image URLs
    void onUploadFailure();
}

