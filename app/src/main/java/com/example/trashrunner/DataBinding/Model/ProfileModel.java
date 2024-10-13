package com.example.trashrunner.DataBinding.Model;

public class ProfileModel {
    public String name;
    public String email;
    public String role;
    public String profile_pic;
    public String phone_num;
    public String address;
    private String joinDate;

    public ProfileModel() { }

    public ProfileModel(
            String name,
            String email,
            String role,
            String profile_pic,
            String phone_num,
            String address,
            String joinDate) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.profile_pic = profile_pic;
        this.phone_num = phone_num;
        this.address = address;
        this.joinDate = joinDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfilePic() { return profile_pic; }
    public void setProfilePic(String profile_pic) { this.profile_pic = profile_pic; }

    public String getPhone() { return phone_num; }
    public void setPhone(String phone_num) { this.phone_num = phone_num; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
}

