package com.example.trashrunner.Main.Home;

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
import com.example.trashrunner.DataBinding.Cache.InfoCache;
import com.example.trashrunner.DataBinding.Model.InfoModel;
import com.example.trashrunner.Main.Home.Adapters.InfoAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeInfoFragment extends Fragment {

    RecyclerView recyclerView;
    InfoAdapter infoAdapter;
    private List<InfoModel> InfoList;
    InfoCache infoCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_info, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        // Set up the adapter
        InfoList = new ArrayList<>();
        infoAdapter = new InfoAdapter(
                getContext(),
                InfoList
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(infoAdapter);


        infoCache = InfoCache.getInstance();
        if (infoCache.isCacheEmpty()) {
            Log.e("Fetch Infos", "fetch new");
            fetchInfosFromFirestore();
        } else {
            Log.e("Fetch Infos", "fetch cache");
            infoAdapter.setInfos(infoCache.getinfosList());
        }

        return view;
    }
    // Fetch reports from Firestore
    private void fetchInfosFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("infos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the cache before adding new data
                        List<InfoModel> InfosList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            InfoModel Info = new InfoModel();

                            Info.setName(document.getString("username"));
                            Info.setEmailfrom(document.getString("email_from"));
                            Info.setProfilepic(document.getString("profile_pic"));
                            Info.setContext(document.getString("context"));

                            Timestamp timestamp = document.getTimestamp("timestamp");
                            String formattedTime = formatTimestamp(timestamp);
                            Info.setDate(formattedTime);

                            InfosList.add(Info);
                        }
                        InfoCache.getInstance().setinfosList(InfosList);
                        infoAdapter.setInfos(InfosList);
                        infoAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the data
                        Log.e("Info Retrieve", "State : " + "success");
                    } else {
                        // Handle Firestore fetch failure
                        Log.e("Info Retrieve", "State : " + "fail");
                        Toast.makeText(getContext(), "Failed to fetch reports from Firestore", Toast.LENGTH_SHORT).show();
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