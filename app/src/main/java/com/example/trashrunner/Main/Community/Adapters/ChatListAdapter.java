package com.example.trashrunner.Main.Community.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.DataBinding.RoomDB.Table.ChatModel;
import com.example.trashrunner.Main.Community.Services.ChatManager;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CardViewHolder> {

    private Context context;
    private List<ChatModel> chatList;
    CurrentUserSharedPreference userProfile;
    ChatManager chatManager;

    public ChatListAdapter(Context context,
                           List<ChatModel> chatList
    ) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_message_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ChatModel chat = chatList.get(position);

        userProfile = new CurrentUserSharedPreference(context);
        String role = userProfile.getRole();

        String name = "";
        if (role.equals("user")) {
            name = chat.getCompanyName();
        } else {
            name = chat.getUserName();
        }

        holder.nametxt.setText(name);
        holder.lastmessagetimestamptxt.setText(chat.getLastMessageTimestamp());
        holder.lastmessagetxt.setText(chat.getLastMessageContent());

        chatManager = ChatManager.getInstance();
        final String nameinner = name;
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatManager.setChatID(chat.getChatId());
                chatManager.setChatName(nameinner);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_to_chatFragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateChatList(List<ChatModel> newChatList) {
        this.chatList = newChatList;
        notifyDataSetChanged(); // Refresh the list
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView nametxt;
        TextView lastmessagetimestamptxt;
        TextView lastmessagetxt;
        MaterialCardView materialCardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            nametxt = itemView.findViewById(R.id.name);
            lastmessagetimestamptxt = itemView.findViewById(R.id.lastmessagetimestamp);
            lastmessagetxt = itemView.findViewById(R.id.lastmessage);
            materialCardView = itemView.findViewById(R.id.chatMaterialCard);
        }
    }
}

