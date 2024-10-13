package com.example.trashrunner.Main.Home.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.trashrunner.R;
import com.example.trashrunner.Main.MainActivity;
import com.example.trashrunner.DataBinding.Model.CompanyModel;
import com.example.trashrunner.Main.Home.Adapters.RentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class rent_service extends Fragment {

    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    RentAdapter rentAdapter;
    private List<CompanyModel> CompanyList;
    FirebaseFirestore db;
    Spinner stateSpinner;
    private String selected_state = "All States";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rent_service, container, false);

        bottomNavigationView = ((MainActivity) getActivity()).findViewById(R.id.bottomNavView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        //common info
        CompanyList = new ArrayList<>();

        // Set up the adapter
        rentAdapter = new RentAdapter(
                getContext(),
                CompanyList
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(rentAdapter);

        db = FirebaseFirestore.getInstance();


        // Define the array of states
        String[] statesOfMalaysia = {
                "All States",
                "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan",
                "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah",
                "Sarawak", "Selangor", "Terengganu",
                "Kuala Lumpur", "Labuan", "Putrajaya"
        };
        stateSpinner = view.findViewById(R.id.state_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statesOfMalaysia
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selected_state = parentView.getItemAtPosition(position).toString();
                fetchCompaniesList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });



        return view;
    }

    public void fetchCompaniesList() {
        Query query;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (selected_state.equals("All States")) {
            // Fetch all companies, limited by 10
            query = db.collection("companies")
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .limit(10);
        } else {
            // Fetch companies from the selected state, limited by 10
            query = db.collection("companies")
                    .whereEqualTo("state", selected_state)
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .limit(10);
        }

        //reference to the companies list collection
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<CompanyModel> newCompanies = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Common Info
                                CompanyModel company = new CompanyModel(
                                        // common info
                                        document.getString("name"),
                                        document.getString("about"),
                                        document.getString("state"),
                                        document.getDouble("rating"),
                                        // contact info
                                        document.getString("contact_info.address"),
                                        document.getString("contact_info.email"),
                                        document.getString("contact_info.phone"),
                                        // container info
                                        document.getString("services.container.size"),
                                        document.getString("services.container.color"),
                                        document.getString("services.container.price"),
                                        document.getString("services.container.usage"),
                                        // truck info
                                        document.getString("services.truck.size"),
                                        document.getString("services.truck.model"),
                                        document.getString("services.truck.price"),
                                        document.getString("services.truck.usage"),
                                        document.getId(),
                                        document.getString("contact_info.company_staff")
                                );

                                String companyId = document.getId();
                                if (companyId != null) {
                                    newCompanies.add(company);
                                }
                            }
                            rentAdapter.updateData(newCompanies); // Notify adapter to update RecyclerView
                            Log.e("Booking", "state: " + "success");
                        } else {
                            // Handle errors
                            Log.e("Booking", "state: " + "fail");
                            Log.d("Firestore", "Error getting companies: ", task.getException());
                        }
                    }
                });
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