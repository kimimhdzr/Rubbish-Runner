package com.example.trashrunner.Main.Home.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.CardViewHolder> {

    private Context context;
    private List<String> cardTitleList;
    private List<Integer> imageList;

    public ServiceAdapter(Context context, List<String> cardTitleList, List<Integer> imageList) {
        this.context = context;
        this.cardTitleList = cardTitleList;
        this.imageList = imageList; // Initialize image list
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        String cardTitle = cardTitleList.get(position);
        holder.cardTitleTxtView.setText(cardTitle);

        Integer imageResourceId = imageList.get(position); // Get drawable resource ID
        // Use Glide to load the image from the drawable resource
        Glide.with(context)
                .load(imageResourceId)  // Load drawable resource ID
//                .placeholder(R.drawable.placeholder) // Optional placeholder
//                .error(R.drawable.error_image)       // Optional error image
                .into(holder.servicesImgView);       // Set the image in the ImageView

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPosition = holder.getAdapterPosition();
                // Get the NavController from the Fragment or Activity context
                NavController navController = Navigation.findNavController(view);

                // Navigate to the corresponding fragment based on the card position
                switch (currentPosition) {
                    case 0:
                        String url = "https://www.mbi.gov.my/perkhidmatan?view=article&id=263&catid=2";
                        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        // Create an AlertDialog builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(context); // Use 'getActivity()' if in a Fragment
                        builder.setTitle("Confirmation")
                                .setMessage("This will direct you to Ipoh Majlis Bandaraya Website?")
                                .setPositiveButton("Got it", (dialog, which) -> {
                                    // Check if there's an activity available to handle the intent
                                    if (openLinkIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                                        view.getContext().startActivity(openLinkIntent);
                                    } else {
                                        // Optional: Handle the case where no browser is available
                                        Toast.makeText(context, "No browser found to open the link", Toast.LENGTH_SHORT).show();
                                    }

                                    dialog.dismiss();
                                })
                                .setNegativeButton("Return", (dialog, which) -> {
                                    dialog.dismiss();
                                });

                        // Create and show the AlertDialog
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        // Navigate to viewscheduleFragment
//                        navController.navigate(R.id.nav_to_viewscheduleFragment);
                        break;
                    case 1:
                        // Navigate to reportFragment
                        navController.navigate(R.id.nav_to_reportFragment);
                        break;
                    case 2:
                        // Navigate to ongoingreportFragment
                        navController.navigate(R.id.nav_to_ongoingreportFragment);
                        break;
                    case 3:
                        // Navigate to rentbinFragment
                        navController.navigate(R.id.nav_to_rentFragment);
                        break;
                    case 4:
                        // Navigate to supportFragment
                        navController.navigate(R.id.nav_to_supportFragment);
                        break;
                    default:
                        // Handle default case or error
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardTitleList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView cardTitleTxtView;
        AppCompatImageView servicesImgView;
        MaterialCardView materialCardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitleTxtView = itemView.findViewById(R.id.card_title);
            materialCardView = itemView.findViewById(R.id.serviceMaterialCard);
            servicesImgView = itemView.findViewById(R.id.service_image);
        }
    }
}
