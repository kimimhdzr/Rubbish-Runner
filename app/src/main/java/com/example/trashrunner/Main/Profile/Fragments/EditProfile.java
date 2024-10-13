package com.example.trashrunner.Main.Profile.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.Main.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends Fragment {
    TextView uploadphotoViewTxt;
    EditText nameEditTxt, emailEditTxt, phoneEditTxt, addressEditTxt;
    public Uri selectedimage;
    ShapeableImageView profileImg;
    ImageButton close_iconImgBtn;
    MaterialButton saveButton;
    CurrentUserSharedPreference userprofile;
    private BottomNavigationView bottomNavigationView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        nameEditTxt = view.findViewById(R.id.name);
        emailEditTxt = view.findViewById(R.id.email);
        phoneEditTxt = view.findViewById(R.id.phone);
        addressEditTxt = view.findViewById(R.id.address);
        profileImg = view.findViewById(R.id.profileImage);
        //click
        uploadphotoViewTxt = view.findViewById(R.id.uploadphototxt);
        close_iconImgBtn = view.findViewById(R.id.close_icon);
        saveButton = view.findViewById(R.id.SaveButton);

        bottomNavigationView = ((MainActivity) getActivity()).findViewById(R.id.bottomNavView);

        userprofile = new CurrentUserSharedPreference(getContext());// Load user data from SharedPreferences

        nameEditTxt.setHint(userprofile.getName());
        emailEditTxt.setHint(userprofile.getEmail());
        phoneEditTxt.setHint(userprofile.getPhone());
        addressEditTxt.setHint(userprofile.getAddress());


        if (userprofile.getProfilePic() != null) {
            Glide.with(this)
                    .load(userprofile.getProfilePic())
                    .into(profileImg);
        }

        uploadphotoViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        close_iconImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameEditTxt.getText().toString().trim();
                String emailString = emailEditTxt.getText().toString().trim();
                String phoneString = phoneEditTxt.getText().toString().trim();
                String addressString = addressEditTxt.getText().toString().trim();

                if (TextUtils.isEmpty(nameString)
                        && TextUtils.isEmpty(emailString)
                        && TextUtils.isEmpty(emailString)
                        && TextUtils.isEmpty(emailString)
                        && imageUri == null
                ) {
                    //no change
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.navto_profileFragment);
                } else {
                    uploadImageToServer(nameString, emailString, phoneString, addressString);
                }


            }
        });

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData(); // Get the image URI
            profileImg.setImageURI(imageUri); // Set the selected image to the ImageView
            // You can call a method here to upload the image to your server or Firebase
            selectedimage = imageUri;
        }
    }

    private void uploadImageToServer(final String name, final String email, final String phone, final String address) {
        // Implement your image upload logic here
        // For example, using Firebase Storage or your backend service
        if (selectedimage != null) {
            String uid = userprofile.getKeyId();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference profileimageref = storageReference.child("images/profiles/" + uid + ".jpg");

            profileimageref.putFile(selectedimage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileimageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    updateUserProfile(userprofile.getKeyId(), name, email, phone, address, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Edit Profile", "Fail to upload profile image");
                        }
                    });
        } else {
            Log.e("Edit Profile", "No image selected");
            updateUserProfile(userprofile.getKeyId(), name, email, phone, address, null);
        }
    }
    private void updateUserProfile(String uid, String name, String email, String phone, String address, String imageUrl) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Create a map to store updated fields
        Map<String, Object> updatedData = new HashMap<>();

        if (!TextUtils.isEmpty(name)) {
            updatedData.put("name", name);
        }
        if (!TextUtils.isEmpty(email)) {
            updatedData.put("email", email);
        }
        if (!TextUtils.isEmpty(phone)) {
            updatedData.put("phone_num", phone);
        }
        if (!TextUtils.isEmpty(address)) {
            updatedData.put("address", address);
        }
        if (imageUrl != null) {
            updatedData.put("profile_pic", imageUrl);
        }

        // Update user document in Firestore
        firestore.collection("users").document(uid)
                .update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!TextUtils.isEmpty(name)) {
                            userprofile.setName(name);
                        }
                        if (!TextUtils.isEmpty(email)) {
                            userprofile.setEmail(email);
                        }
                        if (!TextUtils.isEmpty(phone)) {
                            userprofile.setPhone(phone);
                        }
                        if (!TextUtils.isEmpty(address)) {
                            userprofile.setAddress(address);
                        }
                        if (imageUrl != null) {
                            userprofile.setProfilePic(imageUrl);
                        }
                        Snackbar.make(getView(), "Profile updated successfully!", Snackbar.LENGTH_LONG).show();
                        // Navigate back to profile after successful update
                        if (bottomNavigationView != null) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.navto_profileFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Edit Profile", "Failed to update profile: " + e.getMessage());
                    }
                });
    }


}