package com.example.trashrunner.Main.Profile.Adapters;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;


import com.example.trashrunner.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.CardViewHolder> {

    private Context context;
    private List<String> QuestionsList;
    private List<String> DetailsList;

    public AppInfoAdapter(
            Context context,
            List<String> QuestionsList,
            List<String> DetailsList) {
        this.context = context;
        this.QuestionsList = QuestionsList;
        this.DetailsList = DetailsList;
    }

    @NonNull
    @Override
    public AppInfoAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appinfoitem, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoAdapter.CardViewHolder holder, int position) {

        holder.question.setText(QuestionsList.get(position));
        holder.detail.setText(DetailsList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

                int state = (holder.detail.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(holder.linearLayout, new AutoTransition());
                holder.detail.setVisibility(state);
            }
        });
    }

    @Override
    public int getItemCount() {
        return QuestionsList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView question, detail;
        MaterialCardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            question = itemView.findViewById(R.id.question);
            detail = itemView.findViewById(R.id.detail);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}


