package com.example.trashrunner.Main.Home.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.Main.MainActivity;
import com.example.trashrunner.DataBinding.Cache.CompanyBasicCache;
import com.example.trashrunner.DataBinding.Model.CompanyBasicModel;
import com.example.trashrunner.Main.Home.Adapters.CompanySpinnerAdapter;
import com.example.trashrunner.Main.Home.Services.ReportWriteCallback;
import com.example.trashrunner.Main.Home.Services.ReportWriter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class report extends Fragment {


    ImageButton close_icon, send_icon, images_add_icon;
    EditText subject_edit_text, location_edit_txt, body_edit_txt;
    Spinner sender_spinner;
    ChipGroup recipient_chip_group;
    FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    List<String> companyNameList;
    ReportWriter reportWriter;
    ProgressBar progressCircle;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 200;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private List<Uri> imageSelectUriList = new ArrayList<>();
    private List<String> imageUploadUrlsList = new ArrayList<>();
    private String companynameChoosen, companyidChoosen, companyemailChoosen;
    CurrentUserSharedPreference userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        close_icon = view.findViewById(R.id.close_icon);
        send_icon = view.findViewById(R.id.send_icon);
        images_add_icon = view.findViewById(R.id.images_add_icon);
        subject_edit_text = view.findViewById(R.id.subject_edit_text);
        location_edit_txt = view.findViewById(R.id.location_edit_txt);
        body_edit_txt = view.findViewById(R.id.body_edit_txt);
        sender_spinner = view.findViewById(R.id.sender_spinner);
        recipient_chip_group = view.findViewById(R.id.recipient_chip_group);
        bottomNavigationView = ((MainActivity) getActivity()).findViewById(R.id.bottomNavView);
        fab = view.findViewById(R.id.fab);
        companyNameList = new ArrayList<>();
        reportWriter = new ReportWriter();
        userProfile = new CurrentUserSharedPreference(getContext());


        // Fetch and populate the spinner with company names
        fetchCompanies();

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


        progressCircle = view.findViewById(R.id.progress_circle);
        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = subject_edit_text.getText().toString().trim();
                String location = location_edit_txt.getText().toString().trim();
                String description = body_edit_txt.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    subject_edit_text.setError("Please enter title");
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    subject_edit_text.setError("Please include location");
                    return;
                }
                if (TextUtils.isEmpty(title)) {
                    subject_edit_text.setError("Please specify the problems");
                    return;
                }

                send_icon.setVisibility(View.GONE);      // Hide the send icon
                progressCircle.setVisibility(View.VISIBLE); // Show the progress circle
                send_icon.setClickable(false);           // Disable the button
                uploadImages(title, description);

            }
        });

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        // Find the FAB and set the onClickListener to handle back navigation
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        return view;
    }

    // Fetch company data from Firestore and set the Spinner adapter
    private void fetchCompanies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<CompanyBasicModel> cachedCompanies = CompanyBasicCache.getInstance().getCompanyList();

        if (!cachedCompanies.isEmpty()) {
            // Use cached data
            Log.e("FetchCompanies", "fetch cache");
            setupSpinner(cachedCompanies);
            return;
        }

        Log.e("FetchCompanies", "fetch new");
        // Replace "your_document_id" with the actual document ID
        db.collection("companies_basic_info_compile").document("UXXF2Fo67Vyxze4ifMzZ").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> companyMap = (Map<String, Object>) document.get("company_summaries");
                                List<CompanyBasicModel> companyList = new ArrayList<>();

                                for (Map.Entry<String, Object> entry : companyMap.entrySet()) {
                                    String companyId = entry.getKey();
                                    Map<String, Object> companyInfo = (Map<String, Object>) entry.getValue();

                                    String name = (String) companyInfo.get("name");
                                    String state = (String) companyInfo.get("state");
                                    String email = (String) companyInfo.get("email");

                                    // Create Company object and add to list
                                    CompanyBasicModel company = new CompanyBasicModel(companyId, name, state, email);
                                    companyList.add(company);
                                }
                                Log.d("Report", "Retrieve List of companies info");
                                // Update cache with new data
                                CompanyBasicCache.getInstance().setCompanyList(companyList);

                                // Populate Spinner with the retrieved company list
                                setupSpinner(companyList);
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void uploadImages(String title, String description) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        if (imageSelectUriList == null || imageSelectUriList.isEmpty()) {
            writeReport(title, description);
            return;
        }
        // Counter to keep track of completed uploads
        int totalImages = imageSelectUriList.size();
        AtomicInteger uploadCount = new AtomicInteger(0); // Using AtomicInteger to handle the count safely

        for (int i = 0; i < totalImages; i++) {
            Uri file = imageSelectUriList.get(i);
            StorageReference imageRef = storageRef.child("images/reports/" + userProfile.getKeyId() + "/" + file.getLastPathSegment());

            imageRef.putFile(file).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUploadUrlsList.add(uri.toString());

                    // Increment the upload count
                    if (uploadCount.incrementAndGet() == totalImages) {
                        // All images uploaded, now write the report
                        writeReport(title, description);
                    }
                }).addOnFailureListener(e -> {
                    // Handle failure to get the download URL
                    Log.e("ImageUpload", "Failed to get download URL: " + e.getMessage());
                });
            }).addOnFailureListener(e -> {
                // Handle failed uploads
                Log.e("ImageUpload", "Failed to upload image: " + e.getMessage());
            });
        }
    }

    private void writeReport(String title, String description) {
        reportWriter.writeReport(
                getContext(), companyidChoosen, companynameChoosen, companyemailChoosen,
                34.052235, -118.243683,
                title, description,
                imageUploadUrlsList,
                new ReportWriteCallback() {
                    @Override
                    public void onSuccess() {

                        // Report successfully sent, revert the UI back to the send icon
                        send_icon.setVisibility(View.VISIBLE);   // Show the send icon
                        progressCircle.setVisibility(View.GONE); // Hide the progress circle
                        send_icon.setClickable(true);            // Re-enable the button
                        // Reset UI elements on success
                        imageSelectUriList.clear();
                        imageUploadUrlsList.clear();
                        recipient_chip_group.removeAllViews();
                        subject_edit_text.setText("");
                        location_edit_txt.setText("");
                        body_edit_txt.setText("");
                        // Show a success message
                        Snackbar.make(getView(), "Report successfully sent!", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Failed to send report, revert the UI back to the send icon
                        send_icon.setVisibility(View.VISIBLE);   // Show the send icon
                        progressCircle.setVisibility(View.GONE); // Hide the progress circle
                        send_icon.setClickable(true);            // Re-enable the button

                        // Handle failure (optional)
                        Snackbar.make(getView(), "Failed to send report: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }


    // Set up the Spinner with an ArrayAdapter
    private void setupSpinner(List<CompanyBasicModel> companies) {
        CompanySpinnerAdapter adapter = new CompanySpinnerAdapter(getActivity(), companies);
        sender_spinner.setAdapter(adapter);

        sender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CompanyBasicModel selectedCompany = (CompanyBasicModel) parent.getItemAtPosition(position);
                companyidChoosen = selectedCompany.getId();
                companynameChoosen = selectedCompany.getName();
                companyemailChoosen = selectedCompany.getEmail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // Enable multiple image selection
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
                        imageSelectUriList.add(imageUri);
                        addChipForImage(imageUri);
                    }
                } else if (data.getData() != null) {
                    // Single image selected
                    Uri imageUri = data.getData();
                    imageSelectUriList.add(imageUri);
                    addChipForImage(imageUri);
                }
            }
        }
    }

    private void addChipForImage(Uri imageUri) {
        Chip chip = new Chip(getContext());
        chip.setText(imageUri.getLastPathSegment());  // You can customize this text
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipient_chip_group.removeView(chip);  // Remove the chip on close icon click
            }
        });
        recipient_chip_group.addView(chip);  // Add the chip to the ChipGroup
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set up the enter and exit transitions for this fragment
            setExitTransition(new TransitionSet()
                    .addTransition(new ChangeBounds())
                    .addTransition(new ChangeImageTransform())
                    .addTransition(new ChangeTransform()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}