package com.example.trashrunner.Main.Home.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.CompanyModel;
import com.example.trashrunner.Main.Community.Services.ChatManager;
import com.example.trashrunner.Main.Community.Services.NewChat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.UUID;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.CardViewHolder> {
    private List<CompanyModel> companyList;

    private static Context context;
    private BottomNavigationView bottomNavigationView;
    private SparseBooleanArray expandedStateArray = new SparseBooleanArray();
    ChatManager chatManager;

    public RentAdapter(Context context,
                       List<CompanyModel> companyList
    ) {
        this.context = context;
        this.companyList = companyList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rent_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        CompanyModel company = companyList.get(position);
        //common view
        holder.company_name_txt.setText(company.getName());
        holder.company_detail_txt.setText(company.getDescription());
        String company_pricing_string = company.getContainerPrice() + " - " + company.getTruckPrice();
        holder.company_pricing_txt.setText(company_pricing_string);

        //contact info
        holder.addresstxt.setText(company.getAddress());
        holder.emailtxt.setText(company.getEmail());
        holder.phonetxt.setText(company.getPhone());

        //container info
        holder.containersizedb_txt.setText(company.getContainerSize());
        holder.containercolordb_txt.setText(company.getContainerColor());
        holder.containerpricedb_txt.setText(company.getContainerPrice());
        holder.containerusagedb_txt.setText(company.getContainerUsage());

        //truck info
        holder.trucksizedb_txt.setText(company.getTruckSize());
        holder.truckmodeldb_txt.setText(company.getTruckModel());
        holder.truckpricedb_txt.setText(company.getTruckPrice());
        holder.truckusagedb_txt.setText(company.getTruckUsage());

        double rating = company.getRating(); // Assuming this returns a double value.
        int roundedRating = (int) Math.round(rating); // Round the rating to nearest whole number.

        // Update star views based on the rounded rating
        holder.bindCompanyData(company);

        //expand function
        boolean isExpanded = expandedStateArray.get(position, false);
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        int currentPosition = holder.getAdapterPosition();
        holder.itemMaterialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the expanded state for the current position
                boolean expanded = expandedStateArray.get(currentPosition, false);
                expandedStateArray.put(currentPosition, !expanded); // Toggle the state

                // Notify item change for smooth expand/collapse
                notifyItemChanged(currentPosition);
            }
        });

        holder.messagenowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context); // Use 'getActivity()' if in a Fragment
                builder.setTitle("Confirmation")
                        .setMessage("Do you want to message now?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();

                            // Create an instance of NewChat
                            NewChat newChat = new NewChat();
                            String chatId = UUID.randomUUID().toString(); // Generate a unique chat ID
                            String companyId = company.getCompany_id(); // Replace with actual company ID
                            String companyName = company.getName(); // Replace with actual company name
                            String companyStaff = company.getCompany_staff(); // Replace with actual staff name

                            // Create the chat and navigate on success
                            newChat.createChat(context, chatId, companyId, companyName, companyStaff, createdChatId -> {
                                // Navigate to the ChatFragment using NavController
                                // Show a success message
                                Snackbar.make(v, "Chat initiated! You can continue the conversation in the Community section.", Snackbar.LENGTH_LONG).show();

                                Activity activity = (Activity) holder.itemView.getContext();
                                BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavView);
                                bottomNavigationView.setVisibility(View.VISIBLE); // Set to VISIBLE

                                NavController navController = Navigation.findNavController(v);
                                navController.navigate(R.id.navto_communityFragment); // Use the action ID to navigate
                            });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void updateData(List<CompanyModel> newCompanies) {
        companyList.clear();  // Clear old data
        companyList.addAll(newCompanies);  // Add new data
        notifyDataSetChanged();  // Notify the adapter to refresh the RecyclerView
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView company_name_txt, company_detail_txt, company_pricing_txt;
        Button messagenowbutton;
        TextView truck_provide_txt, container_provide_txt;
        TextView addresstxt, emailtxt, phonetxt;
        TextView containersizedb_txt, containercolordb_txt, containerpricedb_txt, containerusagedb_txt;
        TextView trucksizedb_txt, truckmodeldb_txt, truckpricedb_txt, truckusagedb_txt;
        ImageView star1, star2, star3, star4, star5, rent_profile_image;

        ConstraintLayout expandableLayout;
        MaterialCardView itemMaterialCard;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            //common view
            company_name_txt = itemView.findViewById(R.id.company_name_txt);
            company_detail_txt = itemView.findViewById(R.id.company_detail_txt);
            company_pricing_txt = itemView.findViewById(R.id.company_pricing_txt);
            truck_provide_txt = itemView.findViewById(R.id.truck_provide_txt);
            container_provide_txt = itemView.findViewById(R.id.container_provide_txt);

            //contact info
            addresstxt = itemView.findViewById(R.id.address);
            emailtxt = itemView.findViewById(R.id.email);
            phonetxt = itemView.findViewById(R.id.phone);

            //container info
            containersizedb_txt = itemView.findViewById(R.id.containersizedb);
            containercolordb_txt = itemView.findViewById(R.id.containercolordb);
            containerpricedb_txt = itemView.findViewById(R.id.containerpricedb);
            containerusagedb_txt = itemView.findViewById(R.id.containerusagedb);

            //truck info
            trucksizedb_txt = itemView.findViewById(R.id.trucksizedb);
            truckmodeldb_txt = itemView.findViewById(R.id.truckmodeldb);
            truckpricedb_txt = itemView.findViewById(R.id.truckpricedb);
            truckusagedb_txt = itemView.findViewById(R.id.truckusagedb);

            //expandable function
            expandableLayout = itemView.findViewById(R.id.expandablelayout);
            itemMaterialCard = itemView.findViewById(R.id.itemMaterialCard);

            //star image view
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);

            //company profile pic
            rent_profile_image = itemView.findViewById(R.id.rent_profile_image);
            messagenowbutton = itemView.findViewById(R.id.messagenowbutton);


        }

        public void bindCompanyData(CompanyModel company) {
            double rating = company.getRating(); // Assuming this returns a double value.
            int roundedRating = (int) Math.round(rating); // Round the rating to nearest whole number.

            // Update star views based on the rounded rating
            updateStarRating(roundedRating);
        }

        private void updateStarRating(int rating) {
            ImageView[] stars = new ImageView[]{
                    star1, star2, star3, star4, star5
            };
            // Get the colors from the current theme

            TypedValue activeStarValue = new TypedValue();
            TypedValue inactiveStarValue = new TypedValue();

            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondaryVariant, activeStarValue, true);
            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurface, inactiveStarValue, true);

            int activeStarColor = activeStarValue.data;
            int inactiveStarColor = inactiveStarValue.data;

            for (int i = 0; i < stars.length; i++) {
                if (i < rating) {
                    stars[i].setBackgroundTintList(ColorStateList.valueOf(activeStarColor));
                } else {
                    stars[i].setBackgroundTintList(ColorStateList.valueOf(inactiveStarColor));
                }
            }
        }
    }
}

