package com.example.trashrunner.Main.Profile.Services;

public interface SupportWriterCallback {
    void onSuccess();  // Called when the report is successfully sent
    void onFailure(Exception e);  // Called when there's a failure
}
