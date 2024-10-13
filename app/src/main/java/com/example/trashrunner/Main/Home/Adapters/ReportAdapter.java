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
import com.example.trashrunner.DataBinding.Model.ReportModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.CardViewHolder> {

    private Context context;
    private List<ReportModel> ReportList;

    public ReportAdapter(
            Context context,
            List<ReportModel> ReportList) {
        this.context = context;
        this.ReportList = ReportList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ReportModel report = ReportList.get(position);

        holder.name_txt.setText(report.getName());
        holder.date_txt.setText(report.getDate());
        holder.toemail_txt.setText(report.getEmailto());
        holder.froemail_txt.setText(report.getEmailfrom());
        holder.context_txt.setText(report.getContext());

        if (report.getProfilepic() != null) {
            Glide.with(context)
                    .load(report.getProfilepic())
                    .into(holder.profile_image); // Replace with your ImageView reference
        }
    }
    public void setReports(List<ReportModel> newReports){
        ReportList.clear();
        ReportList.addAll(newReports);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ReportList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt, date_txt, toemail_txt, froemail_txt, context_txt;
        MaterialCardView postMaterialCard;
        ShapeableImageView profile_image;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            postMaterialCard = itemView.findViewById(R.id.postMaterialCard);
            name_txt = itemView.findViewById(R.id.name);
            date_txt = itemView.findViewById(R.id.datesent);
            froemail_txt = itemView.findViewById(R.id.froemail);
            toemail_txt = itemView.findViewById(R.id.toemail);
            context_txt = itemView.findViewById(R.id.context);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}

