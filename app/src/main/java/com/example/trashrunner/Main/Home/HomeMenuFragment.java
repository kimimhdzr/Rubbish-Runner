package com.example.trashrunner.Main.Home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashrunner.R;
import com.example.trashrunner.Main.Home.Services.CustomItemDecoration;
import com.example.trashrunner.Main.Home.Adapters.ServiceAdapter;

import java.util.Arrays;
import java.util.List;


public class HomeMenuFragment extends Fragment {

    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    List<String> cardTitleList;
    List<Integer> imageList;
//    List<String> cardDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Set up the GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Generate some dummy card data
        cardTitleList = Arrays.asList("View Schedule", "Report Trash Issues", "Track Issue Status", "Book Waste Service");
        imageList = Arrays.asList(R.drawable.view_schedule_image, R.drawable.check_report_image_1, R.drawable.check_report_image_2, R.drawable.rent_service_image_1);


        // Set up the adapter
        serviceAdapter = new ServiceAdapter(getContext(), cardTitleList, imageList);
        recyclerView.addItemDecoration(new CustomItemDecoration(5)); // add spacing between cards
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(serviceAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set up the enter and exit transitions for this fragment
            setEnterTransition(new TransitionSet()
                    .addTransition(new ChangeBounds())
                    .addTransition(new ChangeImageTransform())
                    .addTransition(new ChangeTransform()));
        }
    }
}