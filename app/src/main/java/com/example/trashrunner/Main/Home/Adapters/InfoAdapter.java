package com.example.trashrunner.Main.Home.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.InfoModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.CardViewHolder> {

    private static Context context;
    private List<InfoModel> InfosList;

    public InfoAdapter(
            Context context,
            List<InfoModel> InfosList
    ) {
        this.context = context;
        this.InfosList = InfosList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        InfoModel info = InfosList.get(position);

        holder.name_txt.setText(info.getName());
        holder.date_txt.setText(info.getDate());
        holder.froemail_txt.setText(info.getEmailfrom());
        holder.context_txt.setText(info.getContext());

        if (info.getProfilepic() != null) {
            Glide.with(context)
                    .load(info.getProfilepic())
                    .into(holder.profile_image); // Replace with your ImageView reference
        }    // Set the image in the ImageView
    }
    public void setInfos(List<InfoModel> newInfosList){
        InfosList.clear();
        InfosList.addAll(newInfosList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return InfosList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt;
        TextView date_txt;
        TextView froemail_txt;
        TextView context_txt;
        MaterialCardView postMaterialCard;
        ShapeableImageView profile_image;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            postMaterialCard = itemView.findViewById(R.id.postMaterialCard);
            name_txt = itemView.findViewById(R.id.name);
            date_txt = itemView.findViewById(R.id.datesent);
            froemail_txt = itemView.findViewById(R.id.froemail);
            context_txt = itemView.findViewById(R.id.context);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}

