package com.example.trashrunner.Main.Home.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trashrunner.Main.Home.HomeFragment;
import com.example.trashrunner.Main.Home.HomeInfoFragment;
import com.example.trashrunner.Main.Home.HomeMenuFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragments;

    public HomeViewPagerAdapter(@NonNull HomeFragment fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{new HomeMenuFragment(), new HomeInfoFragment()};
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