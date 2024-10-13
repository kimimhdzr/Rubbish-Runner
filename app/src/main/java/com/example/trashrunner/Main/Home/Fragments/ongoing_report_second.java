package com.example.trashrunner.Main.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.ReportModel;
import com.example.trashrunner.Main.Home.Adapters.ReportAdapter;

import java.util.ArrayList;
import java.util.List;


public class ongoing_report_second extends Fragment {


    RecyclerView recyclerView;
    ReportAdapter reportAdapter;
    List<ReportModel> ReportList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_report_second, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        ReportList = new ArrayList<>();

        // Set up the adapter
        reportAdapter = new ReportAdapter(
                getContext(),
                ReportList
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(reportAdapter);


        return view;
    }
}