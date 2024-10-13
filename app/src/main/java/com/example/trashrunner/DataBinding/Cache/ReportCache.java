package com.example.trashrunner.DataBinding.Cache;

import com.example.trashrunner.DataBinding.Model.ReportModel;

import java.util.ArrayList;
import java.util.List;

public class ReportCache {
    private static ReportCache instance;
    private List<ReportModel> ReportList;

    // Private constructor to prevent instantiation
    private ReportCache() {
        ReportList = new ArrayList<>();
    }

    // Get the singleton instance
    public static synchronized ReportCache getInstance() {
        if (instance == null) {
            instance = new ReportCache();
        }
        return instance;
    }

    public List<ReportModel> getReportList() {
        return ReportList;
    }

    public void setReportList(List<ReportModel> ReportList) {
        this.ReportList.clear();
        this.ReportList.addAll(ReportList);
    }

    // Check if cache is empty
    public boolean isCacheEmpty() {
        return ReportList.isEmpty();
    }

    // Clear the cache (useful when logging out or refreshing)
    public void clearCache() {
        ReportList.clear();
    }
}

