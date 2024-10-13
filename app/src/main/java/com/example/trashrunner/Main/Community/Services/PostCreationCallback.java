package com.example.trashrunner.Main.Community.Services;

import java.util.List;

public interface PostCreationCallback {
    void onPostCreated(boolean isSuccess, List<String> uploadimageUrls);
    void onImagesUploaded(boolean isSuccess);
}
