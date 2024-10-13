package com.example.trashrunner.DataBinding.Cache;

import com.example.trashrunner.DataBinding.Model.CompanyBasicModel;

import java.util.ArrayList;
import java.util.List;

public class CompanyBasicCache {
    private static CompanyBasicCache instance;
    private List<CompanyBasicModel> companyList;

    private CompanyBasicCache() {
        companyList = new ArrayList<>();
    }

    public static synchronized CompanyBasicCache getInstance() {
        if (instance == null) {
            instance = new CompanyBasicCache();
        }
        return instance;
    }

    public List<CompanyBasicModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyBasicModel> companies) {
        this.companyList.clear();
        this.companyList.addAll(companies);
    }
    public void clearCache() {
        companyList.clear();
    }
}

