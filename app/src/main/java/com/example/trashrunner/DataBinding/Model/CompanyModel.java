package com.example.trashrunner.DataBinding.Model;

public class CompanyModel {
    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_staff() {
        return company_staff;
    }

    public void setCompany_staff(String company_staff) {
        this.company_staff = company_staff;
    }

    // Common Info
    private String company_id;
    private String name;
    private String description;
    private String state;
    private double rating;

    // Contact Info
    private String company_staff;
    private String address;
    private String email;
    private String phone;

    // Container Info
    private String containerSize;
    private String containerColor;
    private String containerPrice;
    private String containerUsage;

    // Truck Info
    private String truckSize;
    private String truckModel;
    private String truckPrice;
    private String truckUsage;

    // Constructor
    public CompanyModel(String name, String description, String state, double rating,
                        String address, String email, String phone,
                        String containerSize, String containerColor, String containerPrice, String containerUsage,
                        String truckSize, String truckModel, String truckPrice, String truckUsage,
                        String company_id, String company_staff) {
        this.company_id = company_id;
        this.company_staff = company_staff;
        this.name = name;
        this.description = description;
        this.state = state;
        this.rating = rating;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.containerSize = containerSize;
        this.containerColor = containerColor;
        this.containerPrice = containerPrice;
        this.containerUsage = containerUsage;
        this.truckSize = truckSize;
        this.truckModel = truckModel;
        this.truckPrice = truckPrice;
        this.truckUsage = truckUsage;
    }

    // Getters for all fields
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public String getContainerColor() {
        return containerColor;
    }

    public String getContainerPrice() {
        return containerPrice;
    }

    public String getContainerUsage() {
        return containerUsage;
    }

    public String getTruckSize() {
        return truckSize;
    }

    public String getTruckModel() {
        return truckModel;
    }

    public String getTruckPrice() {
        return truckPrice;
    }

    public String getTruckUsage() {
        return truckUsage;
    }
}

