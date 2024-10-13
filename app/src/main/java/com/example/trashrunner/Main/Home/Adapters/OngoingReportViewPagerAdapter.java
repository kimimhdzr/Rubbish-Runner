package com.example.trashrunner.Main.Home.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trashrunner.Main.Home.Fragments.ongoing_report;
import com.example.trashrunner.Main.Home.Fragments.ongoing_report_first;
import com.example.trashrunner.Main.Home.Fragments.ongoing_report_second;

public class OngoingReportViewPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragments;

    public OngoingReportViewPagerAdapter(@NonNull ongoing_report fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{new ongoing_report_first(), new ongoing_report_second()};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    // Method to get fragment based on position
    public Fragment getFragment(int position) {
        return fragments[position];
    }
}