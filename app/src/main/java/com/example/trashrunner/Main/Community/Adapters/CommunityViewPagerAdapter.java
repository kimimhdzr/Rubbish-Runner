package com.example.trashrunner.Main.Community.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.trashrunner.Main.Community.CommunityFragment;
import com.example.trashrunner.Main.Community.Fragments.CommunityPersonalFragment;
import com.example.trashrunner.Main.Community.Fragments.CommunityPublicFragment;

public class CommunityViewPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragments;

    public CommunityViewPagerAdapter(@NonNull CommunityFragment fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[]{new CommunityPublicFragment(), new CommunityPersonalFragment()};
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