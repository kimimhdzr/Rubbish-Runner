package com.example.trashrunner.Main.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.ReportModel;
import com.example.trashrunner.DataBinding.Cache.ReportCache;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.Main.Home.Adapters.ReportAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ongoing_report_first extends Fragment {

    RecyclerView recyclerView;
    ReportAdapter reportAdapter;
    CurrentUserSharedPreference userProfile;
    ReportCache reportCache;
    private List<ReportModel> ReportList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_report_first, container, false);

        userProfile = new CurrentUserSharedPreference(getContext());

        // Set up the adapter
        ReportList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        reportAdapter = new ReportAdapter(
                getContext(),
                ReportList
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(reportAdapter);

        // Check if cache is empty
        reportCache = ReportCache.getInstance();
        if (reportCache.isCacheEmpty()) {
            Log.e("FetchReports", "fetch new");
            fetchReportsFromFirestore();
        } else {
            Log.e("FetchReports", "fetch cache");
            reportAdapter.setReports(reportCache.getReportList());
        }

        return view;
    }

    // Fetch reports from Firestore
    private void fetchReportsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = "";
        String role = userProfile.getRole();
        String field = "";
        if (role.equals("user")) {
            field = "participants.user_id";
            uid = userProfile.getKeyId();
            Log.e("Report", "role: " + role + " | uid: " + uid);
        } else if (role.equals("admin") || role.equals("staff")) {
            field = "participants.company_id";
            uid = userProfile.getKeyCompanyId();
            Log.e("Report", "role: " + role + " | cid: " + uid);
        } else {
            // Handle the case where the role is not recognized
            Log.e("FetchChats", "Unrecognized user role: " + role);
        }
        db.collection("trash_reports")
                .whereEqualTo(field, uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the cache before adding new data
                        List<ReportModel> ReportList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReportModel report = new ReportModel();

                            report.setName(document.getString("participants.username"));
                            report.setEmailfrom(document.getString("participants.email_from"));
                            report.setEmailto(document.getString("participants.email_to"));
                            report.setProfilepic(document.getString("participants.profile_pic"));
                            report.setContext(document.getString("details.description"));

                            Timestamp timestamp = document.getTimestamp("timestamp");
                            String formattedTime = formatTimestamp(timestamp);
                            report.setDate(formattedTime);

                            ReportList.add(report);
                        }
                        ReportCache.getInstance().setReportList(ReportList);
                        reportAdapter.setReports(ReportList);
                        reportAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the data
                        Log.e("Report Retrieve", "State : " + "success");
                    } else {
                        // Handle Firestore fetch failure
                        Log.e("Report Retrieve", "State : " + "fail");
                        Toast.makeText(getContext(), "Failed to fetch reports from Firestore", Toast.LENGTH_SHORT).show();
                        Log.e("ongoing_report_first", "Error fetching reports", task.getException());
                    }
                });
    }

    private String formatTimestamp(Timestamp firestoreTimestamp) {
        // Convert Firestore Timestamp to Date
        Date timestamp = firestoreTimestamp.toDate();

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