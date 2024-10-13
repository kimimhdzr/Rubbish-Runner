package com.example.trashrunner.Main.Community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.trashrunner.R;
import com.example.trashrunner.Main.Community.Adapters.CommunityViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CommunityViewPagerAdapter communityViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);
        communityViewPagerAdapter = new CommunityViewPagerAdapter(this);
        viewPager2.setAdapter(communityViewPagerAdapter);

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


//
//        TypedValue typedValue = new TypedValue();
//        requireContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
//        requireContext().getTheme().resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true);
//        int colorPrimaryVariant = typedValue.data;
//        int backgroundColor = typedValue.data;
//        requireActivity().getWindow().setStatusBarColor(colorPrimaryVariant);
//        requireActivity().getWindow().setNavigationBarColor(backgroundColor);


        return view;
    }
}