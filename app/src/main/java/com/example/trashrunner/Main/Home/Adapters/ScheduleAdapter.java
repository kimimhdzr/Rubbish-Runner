package com.example.trashrunner.Main.Home.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.trashrunner.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.TaskViewHolder> {

    private List<String> taskList;

    // Constructor to initialize the task list
    public ScheduleAdapter(List<String> taskList) {
        this.taskList = taskList;
    }

    // ViewHolder class
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.card_title);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Bind data to each item
        String task = taskList.get(position);
        holder.taskTitle.setText(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
