package com.example.trashrunner.Main.Home.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashrunner.R;
import com.example.trashrunner.Main.MainActivity;
import com.example.trashrunner.Main.Home.Adapters.ScheduleAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class viewschedule extends Fragment {
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<String> taskList;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewschedule, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the task list and adapter
        taskList = new ArrayList<>();
        // Adding some dummy tasks for demonstration
        taskList.add("Taman Aman");
        taskList.add("Taman Kerjaya");
        taskList.add("Taman Tunku Majid");
        taskList.add("Jalan Jitra");

        scheduleAdapter = new ScheduleAdapter(taskList);
        recyclerView.setAdapter(scheduleAdapter);

        bottomNavigationView = ((MainActivity)getActivity()).findViewById(R.id.bottomNavView);
        if(bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.GONE);
        }

        // Find the FAB and set the onClickListener to handle back navigation
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger back navigation
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
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

        if(bottomNavigationView!=null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}