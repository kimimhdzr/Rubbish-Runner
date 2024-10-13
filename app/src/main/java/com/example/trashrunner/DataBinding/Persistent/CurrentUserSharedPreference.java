package com.example.trashrunner.DataBinding.Persistent;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUserSharedPreference {
    private static final String PREF_NAME = "UserProfile";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone_num";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_JOIN_DATE = "joinDate";
    private static final String KEY_ROLE = "role";
    private static final String KEY_ID = "id";
    private static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_PROFILE_PIC = "profile_pic";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CurrentUserSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save user data
    public void saveUserProfile(String name, String email, String phone_num, String address, String joinDate, String role, String id, String cid, String profile_pic) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone_num);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_JOIN_DATE, joinDate);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_COMPANY_ID, cid);
        editor.putString(KEY_PROFILE_PIC, profile_pic);
        editor.apply(); // Save changes asynchronously
    }

    // Retrieve user data
    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public String getJoinDate() {
        return sharedPreferences.getString(KEY_JOIN_DATE, "");
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "");
    }

    public String getKeyId() {
        return sharedPreferences.getString(KEY_ID, "");
    }

    public String getKeyCompanyId() {
        return sharedPreferences.getString(KEY_COMPANY_ID, "");
    }

    public String getProfilePic() {
        return sharedPreferences.getString(KEY_PROFILE_PIC, "");
    }


    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void setPhone(String phone_num) {
        editor.putString(KEY_PHONE, phone_num);
        editor.apply();
    }

    public void setAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public void setJoinDate(String joinDate) {
        editor.putString(KEY_JOIN_DATE, joinDate);
        editor.apply();
    }

    public void setRole(String role) {
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public void setKeyId(String id) {
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public void setCompanyId(String company_id) {
        editor.putString(KEY_COMPANY_ID, company_id);
        editor.apply();
    }

    public void setProfilePic(String profile_pic) {
        editor.putString(KEY_PROFILE_PIC, profile_pic);
        editor.apply();
    }
    // Clear user data (e.g., on logout)
    public void clearProfileData() {
        editor.clear();
        editor.apply();
    }
}
