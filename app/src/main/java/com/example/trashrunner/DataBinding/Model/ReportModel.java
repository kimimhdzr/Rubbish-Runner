package com.example.trashrunner.DataBinding.Model;

public class ReportModel {
    private String name;
    private String emailfrom;
    private String emailto;
    private String context;
    private String date;
    private String profilepic;

    public ReportModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailfrom() {
        return emailfrom;
    }

    public void setEmailfrom(String emailfrom) {
        this.emailfrom = emailfrom;
    }

    public String getEmailto() {
        return emailto;
    }

    public void setEmailto(String emailto) {
        this.emailto = emailto;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

}
