package com.example.trashrunner.Main.Profile.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.Main.MainActivity;
import com.example.trashrunner.Main.Profile.Adapters.AppInfoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppInformation extends Fragment {
    private BottomNavigationView bottomNavigationView;
    ImageButton close_icon;
    RecyclerView recyclerView;
    List<String> QuestionsList, DetailsList;
    AppInfoAdapter appInfoAdapter;
    AppCompatImageView truckicon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_information, container, false);

        bottomNavigationView = ((MainActivity) getActivity()).findViewById(R.id.bottomNavView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        truckicon = view.findViewById(R.id.truckicon);
        Glide.with(this)
                .load(R.drawable.rent_service_image_1) // Replace with your drawable resource
                .into(truckicon);

        close_icon = view.findViewById(R.id.close_icon);
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        QuestionsList = new ArrayList<>(Arrays.asList(
                getString(R.string.question1),
                getString(R.string.question2),
                getString(R.string.question3),
                getString(R.string.question4),
                getString(R.string.question5),
                getString(R.string.question6),
                getString(R.string.question7),
                getString(R.string.question8),
                getString(R.string.question9),
                getString(R.string.question10),
                getString(R.string.question11),
                getString(R.string.question12)
        ));
        DetailsList = new ArrayList<>(Arrays.asList(
                getString(R.string.detail1),
                getString(R.string.detail2),
                getString(R.string.detail3),
                getString(R.string.detail4),
                getString(R.string.detail5),
                getString(R.string.detail6),
                getString(R.string.detail7),
                getString(R.string.detail8),
                getString(R.string.detail9),
                getString(R.string.detail10),
                getString(R.string.detail11),
                getString(R.string.detail12)
        ));

        recyclerView = view.findViewById(R.id.recyclerview);
        appInfoAdapter = new AppInfoAdapter(
                getContext(),
                QuestionsList,
                DetailsList
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(appInfoAdapter);


        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

//    public void cardviewclick(TextView details, LinearLayout linearLayout, CardView cardView) {
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expand(details, linearLayout);
//            }
//        });
//    }
//
//    public void expand(TextView details, LinearLayout linearLayout) {
//
//        linearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//
//        int v = (details.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
//        TransitionManager.beginDelayedTransition(linearLayout1, new AutoTransition());
//        details.setVisibility(v);
//    }
}