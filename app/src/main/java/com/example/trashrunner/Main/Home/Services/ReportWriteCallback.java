package com.example.trashrunner.Main.Home.Services;

public interface ReportWriteCallback {
    void onSuccess();  // Called when the report is successfully sent
    void onFailure(Exception e);  // Called when there's a failure
}
