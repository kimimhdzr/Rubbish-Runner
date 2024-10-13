package com.example.trashrunner.Main.Home.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashrunner.R;
import com.example.trashrunner.Main.MainActivity;
import com.example.trashrunner.Main.Home.Adapters.OngoingReportViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class ongoing_report extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OngoingReportViewPagerAdapter ongoingReportViewPagerAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_report, container, false);



        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);
        ongoingReportViewPagerAdapter = new OngoingReportViewPagerAdapter(this);
        viewPager2.setAdapter(ongoingReportViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



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