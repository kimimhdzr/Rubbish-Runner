package com.example.trashrunner.Main.Community.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Cache.PostCache;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.Main.Community.Adapters.ImagesGridAdapter;
import com.example.trashrunner.Main.Community.Adapters.PublicPostsAdapter;
import com.example.trashrunner.Main.Community.Services.NewPosts;
import com.example.trashrunner.DataBinding.Model.PostModel;
import com.example.trashrunner.Main.Community.Services.PostCreationCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommunityPublicFragment extends Fragment {

    RecyclerView post_recycler_view, images_recycler_view;
    PublicPostsAdapter publicPostsAdapter;
    FirebaseFirestore db;
    PostCache postCache;
    ShapeableImageView profile_image;
    Button post_button;
    CurrentUserSharedPreference userProfile;
    EditText context_edit_txt;
    NewPosts newPosts;
    List<String> selectimageUrls;
    LinearProgressIndicator linearProgressIndicator;
    ImageButton images_add_icon;
    ImagesGridAdapter imagesGridAdapter;
    AppBarLayout writepostcontainer;
    MaterialToolbar toolbar;
    private List<PostModel> postsList;
    // Constants
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_public, container, false);

        db = FirebaseFirestore.getInstance();

        //setup posts adapter for retrieved posts
        postsList = new ArrayList<>();
        post_recycler_view = view.findViewById(R.id.post_recycler_view);
        publicPostsAdapter = new PublicPostsAdapter(
                getContext(),
                postsList
        );
        post_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        post_recycler_view.setItemAnimator(new DefaultItemAnimator());
        post_recycler_view.setAdapter(publicPostsAdapter);

        //setup images adapter for selected images
        selectimageUrls = new ArrayList<>();

        images_recycler_view = view.findViewById(R.id.images_recycler_view);// Initialize the adapter and set it to the RecyclerView
        imagesGridAdapter = new ImagesGridAdapter(
                getContext(),
                selectimageUrls,
                2
        );
        images_recycler_view.setLayoutManager(new GridLayoutManager(
                getContext(),
                2)
        ); // 3 columns in the grid
        images_recycler_view.setAdapter(imagesGridAdapter);

        //attach the profile pic
        userProfile = new CurrentUserSharedPreference(getContext());
        profile_image = view.findViewById(R.id.profile_image);
        if (userProfile.getProfilePic() != null) {
            Glide.with(this)
                    .load(userProfile.getProfilePic()) // Replace with your drawable resource
                    .into(profile_image);
        }

        //collapse toolbar when scrolling animation
        writepostcontainer = view.findViewById(R.id.writepostcontainer);
        toolbar = view.findViewById(R.id.toolbar);
        writepostcontainer.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Animate the toolbar based on scroll offset
                float percentage = Math.abs(verticalOffset / (float) appBarLayout.getTotalScrollRange());

                // Example of changing alpha or scaling based on scroll
                appBarLayout.setAlpha(1 - percentage);
                appBarLayout.setScaleX(1 - 0.5f * percentage);
                appBarLayout.setScaleY(1 - 0.5f * percentage);
            }
        });



        // Fetch chats from Firestore
        postCache = PostCache.getInstance();  // Get the instance of PostCa
        if (postCache.isEmpty()) {
            Log.e("FetchPosts", "fetch new");
            fetchPosts();
        } else {
            Log.e("FetchPosts", "fetch cache");
            publicPostsAdapter.setPosts(postCache.getPostsList()); // Use cached posts3
        }

        //post button logic
        post_button = view.findViewById(R.id.post_button);
        context_edit_txt = view.findViewById(R.id.context_edit_txt);
        linearProgressIndicator = view.findViewById(R.id.linearprogressindicator);
        newPosts = new NewPosts();
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = context_edit_txt.getText().toString();
                if (TextUtils.isEmpty(context)) {
                    context_edit_txt.setError("It seems you forgot to write something");
                    return;
                }

                post_button.setEnabled(false);
                post_button.setAlpha(0.5f);
                linearProgressIndicator.setVisibility(View.VISIBLE);
                newPosts.createPost(
                        getContext(),
                        context,
                        selectimageUrls,
                        new PostCreationCallback() {
                            @Override
                            public void onPostCreated(boolean isSuccess, List<String> uploadimageUrls) {
                                post_button.setEnabled(true);
                                post_button.setAlpha(1.0f);
                                linearProgressIndicator.setVisibility(View.INVISIBLE);
                                context_edit_txt.setText("");
                                imagesGridAdapter.clearData();
                                selectimageUrls.clear();
                                if (isSuccess) {
                                    attachNewPost(context, uploadimageUrls);
                                    Log.e("Posts", "Post created successfully!");
                                } else {
                                    Log.e("Posts", "Failed to create post.");
                                }
                            }

                            @Override
                            public void onImagesUploaded(boolean isSuccess) {
                                if (isSuccess) {
                                    Log.e("Images", "Images uploaded successfully!");
                                } else {
                                    post_button.setEnabled(true);
                                    post_button.setAlpha(1.0f);
                                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                                    Log.e("Images", "Failed to upload pictures.");
                                }
                            }
                        }
                );
            }
        });


        //pick images logic
        images_add_icon = view.findViewById(R.id.images_add_icon);
        images_add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    openImagePicker();
                }
            }
        });

        return view;
    }

    public void attachNewPost(String description, List<String> uploadimageUrls) {
        String formattedtime = formatTimestamp(new Date());
        PostModel newpost = new PostModel(
                userProfile.getName(),
                userProfile.getEmail(),
                description,
                0,
                0,
                formattedtime,
                userProfile.getProfilePic(),
                uploadimageUrls
        );
        postCache.addPost(newpost);
        publicPostsAdapter.setPosts(postCache.getPostsList());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple image selection
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                // Handle multiple images
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        selectimageUrls.add(imageUri.toString()); // Store URI as String
                    }
                } else if (data.getData() != null) {
                    // Single image selected
                    Uri imageUri = data.getData();
                    selectimageUrls.add(imageUri.toString()); // Store URI as String
                }

                // Notify your adapter that the data has changed
                imagesGridAdapter.notifyDataSetChanged();
            }
        }
    }

    public void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the posts collection
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Order by timestamp descending
                .limit(10) // Limit to 10 posts
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PostModel> postsList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PostModel post = new PostModel();
                            // Extract post data

                            post.setName(document.getString("sender.username"));
                            post.setEmail(document.getString("sender.email"));
                            post.setContent(document.getString("content"));
                            Long commentCount = document.getLong("reactions.comment_count");
                            Long likeCount = document.getLong("reactions.like_count");
                            post.setCommentCount(commentCount != null ? commentCount.intValue() : 0);
                            post.setLikeCount(likeCount != null ? likeCount.intValue() : 0);
                            post.setProfilePic(document.getString("sender.profile_pic"));
                            List<String> images = (List<String>) document.get("images");
                            post.setImages(images != null ? images : new ArrayList<>());

                            Timestamp timestamp = document.getTimestamp("timestamp");
                            String formattedTime = formatTimestamp(timestamp.toDate());
                            post.setTimestamp(formattedTime);

                            postsList.add(post);
                            Log.e("Posts", document.getString("content"));

                        }
                        PostCache.getInstance().setPostsList(postsList);
                        publicPostsAdapter.setPosts(postsList); // Assuming your adapter has a setPosts method
                        Log.e("Posts", "state: " + "success");

                    } else {
                        // Handle errors
                        Log.e("FetchPosts", "Error getting documents: ", task.getException());
                        Log.e("Posts", "state: " + "fail");
                    }
                });
    }

    private String formatTimestamp(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long timeDifference = today.getTimeInMillis() - calendar.getTimeInMillis();
        long daysDifference = timeDifference / (24 * 60 * 60 * 1000);

        // If the timestamp is within the same day, return the time (e.g., "2 PM")
        if (daysDifference == 0) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return timeFormat.format(timestamp);  // e.g., "2:30 PM"
        }
        // If the timestamp was yesterday
        else if (daysDifference == 1) {
            return "Yesterday";
        }
        // If the timestamp is older than 1 day, return the date (e.g., "Sep 17, 2024")
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return dateFormat.format(timestamp);
        }
    }


}