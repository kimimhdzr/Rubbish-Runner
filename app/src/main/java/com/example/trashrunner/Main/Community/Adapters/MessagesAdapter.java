package com.example.trashrunner.Main.Community.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.RoomDB.Table.MessageModel2;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_OUTGOING = 1;
    private static final int VIEW_TYPE_INCOMING = 2;
    private List<MessageModel2> messageList;
    private String currentUserRole; // You can get this from SharedPreferences

    public MessagesAdapter(List<MessageModel2> messageList, String currentUserRole) {
        this.messageList = messageList;
        this.currentUserRole = currentUserRole;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel2 message = messageList.get(position);
        // Check if the messageModel sender matches the current user role
        return message.getSender().equals(currentUserRole) ? VIEW_TYPE_OUTGOING : VIEW_TYPE_INCOMING;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OUTGOING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outgoing_message_item, parent, false);
            return new OutgoingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_message_item, parent, false);
            return new IncomingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageModel2 message = messageList.get(position);
        if (holder instanceof OutgoingViewHolder) {
            ((OutgoingViewHolder) holder).bind(message);
        } else {
            ((IncomingViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("MessagesAdapter", "Item count: " + messageList.size());  // Add this log
        return messageList.size();

    }

    public void setMessages(List<MessageModel2> newMessages) {
        this.messageList.clear();        // Clear the old messages
        this.messageList.addAll(newMessages);  // Add new messages
        notifyDataSetChanged();          // Notify the adapter that data has changed
    }
    // Optionally, if you want to append new messages instead of replacing
    public void appendMessages(List<MessageModel2> newMessages) {
        int previousSize = this.messageList.size();
        this.messageList.addAll(newMessages);  // Add new messages to the existing list
        notifyItemRangeInserted(previousSize, newMessages.size()); // Notify for new range
    }
    public void updateMessages(List<MessageModel2> newMessages) {
        messageList.clear();
        messageList.addAll(newMessages);
        notifyDataSetChanged();
    }


    // ViewHolder for outgoing messages
    public static class OutgoingViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timestamp;

        public OutgoingViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_outgoing_message);
            timestamp = itemView.findViewById(R.id.text_outgoing_time);
        }

        public void bind(MessageModel2 message) {
            messageText.setText(message.getContent()); // Set the actual messageModel text
            String formatdate = formatTimestamp(new Date(message.getTimestamp()));
            timestamp.setText(formatdate);
        }
    }

    // ViewHolder for incoming messages
    public static class IncomingViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timestamp;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_incoming_message);
            timestamp = itemView.findViewById(R.id.text_incoming_time);
        }

        public void bind(MessageModel2 message) {
            messageText.setText(message.getContent()); // Set the actual messageModel text
            String formatdate = formatTimestamp(new Date(message.getTimestamp()));
            timestamp.setText(formatdate);
        }
    }

    private static String formatTimestamp(Date timestamp) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long timeDifference = today.getTimeInMillis() - calendar.getTimeInMillis();
        long daysDifference = timeDifference / (24 * 60 * 60 * 1000);

        // If the timestamp is within the same day, return the time (e.g., "2 PM")
        if (daysDifference == 0) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return timeFormat.format(timestamp);  // e.g., "2:30 PM"
        }
        // If the timestamp was yesterday
        else if (daysDifference == 1) {
            return "Yesterday";
        }
        // If the timestamp is older than 1 day, return the date (e.g., "Sep 17, 2024")
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return dateFormat.format(timestamp);
        }
    }
}

