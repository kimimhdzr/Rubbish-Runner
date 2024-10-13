package com.example.trashrunner.DataBinding.Cache;

import com.example.trashrunner.DataBinding.Model.InfoModel;

import java.util.ArrayList;
import java.util.List;

public class InfoCache {
    private static InfoCache instance;
    private List<InfoModel> infosList;

    private InfoCache() {
        infosList = new ArrayList<>();
    }

    public static synchronized InfoCache getInstance() {
        if (instance == null) {
            instance = new InfoCache();
        }
        return instance;
    }

    public List<InfoModel> getinfosList() {
        return infosList;
    }

    public void setinfosList(List<InfoModel> newinfosList) {
        this.infosList.clear();
        this.infosList.addAll(newinfosList);
    }

    public void addPost(InfoModel newinfo) {
        infosList.add(0, newinfo); //new post to first index
    }

    public boolean isCacheEmpty() {
        return infosList.isEmpty();
    }

    public void clearCache() {
        infosList.clear();
    }
}