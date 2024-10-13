package com.example.trashrunner.Main.Community.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.RoomDB.AppDatabase;
import com.example.trashrunner.DataBinding.RoomDB.DAO.ChatDAO;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.DataBinding.RoomDB.Table.ChatModel;
import com.example.trashrunner.Main.Community.Adapters.ChatListAdapter;
import com.example.trashrunner.Main.Community.Services.TimestampCallback;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CommunityPersonalFragment extends Fragment {

    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    private List<ChatModel> chatList2;
    FirebaseFirestore db;
    CurrentUserSharedPreference userProfile;
    private AppDatabase dbInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_personal, container, false);

        userProfile = new CurrentUserSharedPreference(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);

        chatList2 = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(
                getContext(),
                chatList2
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatListAdapter);

        db = FirebaseFirestore.getInstance();
        dbInstance = AppDatabase.getDatabase(getContext());
        syncWithFirebase();

        // Observe Room database for chat changes and update UI
        observeChatUpdates();

        return view;
    }

    public void syncWithFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AppDatabase dbInstance = AppDatabase.getDatabase(getContext());
        String uid = userProfile.getKeyId();
        String role = userProfile.getRole();
        String field = "";

        if (role.equals("user")) {
            field = "participants.user_id";
        } else if (role.equals("admin")) {
            field = "participants.company_id";
        } else if (role.equals("staff")) {
            field = "participants.company_staff";
        } else {
            // Handle the case where the role is not recognized
            Log.e("FetchChats", "Unrecognized user role: " + role);
        }
        final String field2 = field;

        // Fetch the last message timestamp asynchronously
        getLastMessageTimestamp(dbInstance, uid, lastupdated -> {

            Query query;
            if (lastupdated == 0L) {
                // First sync: retrieve all chats for the user (without timestamp filter)
                query = db.collection("chats").whereEqualTo(field2, uid);
            } else {
                // Subsequent syncs: retrieve only chats with updated messages
                query = db.collection("chats")
                        .whereEqualTo(field2, uid)
                        .whereGreaterThan("last_message.timestamp", new Date(lastupdated));
            }

            // Once the timestamp is fetched, sync chats from Firebase
            query.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Use a background thread for database insertion
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        String chatId = documentSnapshot.getId();
                                        String companyId = (String) documentSnapshot.get("participants.company_id");
                                        String companyName = (String) documentSnapshot.get("participants.company_name");
                                        String companyStaff = (String) documentSnapshot.get("participants.company_staff");
                                        String userId = (String) documentSnapshot.get("participants.user_id");
                                        String userName = (String) documentSnapshot.get("participants.user_name");
                                        String lastMessageId = (String) documentSnapshot.get("last_message.message_id");
                                        String lastMessageContent = (String) documentSnapshot.get("last_message.content");
                                        String lastMessageSender = (String) documentSnapshot.get("last_message.sender");
                                        String status = (String) documentSnapshot.get("status");

                                        Timestamp createdAt = documentSnapshot.getTimestamp("participants.created_at");
                                        Timestamp lastMessageTimestampinner = documentSnapshot.getTimestamp("last_message.timestamp");

                                        long lastupdatedinner = lastMessageTimestampinner != null ? lastMessageTimestampinner.toDate().getTime() : 0; // Convert to Long

                                        String createdAtformattedTime = formatTimestamp(createdAt.toDate());
                                        String lastMessageTimestampinnerformattedTime = formatTimestamp(lastMessageTimestampinner.toDate());


                                        // Create a ChatEntity instance
                                        ChatModel chat = new ChatModel();
                                        chat.setChatId(chatId);
                                        chat.setCompanyId(companyId);
                                        chat.setCompanyName(companyName);
                                        chat.setCompanyStaff(companyStaff);
                                        chat.setUserId(userId);
                                        chat.setUserName(userName);
                                        chat.setCreatedAt(createdAtformattedTime);
                                        chat.setLastMessageId(lastMessageId);
                                        chat.setLastMessageContent(lastMessageContent);
                                        chat.setLastMessageTimestamp(lastMessageTimestampinnerformattedTime);
                                        chat.setLastMessageSender(lastMessageSender);
                                        chat.setStatus(status);
                                        chat.setLastupdated(lastupdatedinner);


                                        AppDatabase dbRoom = Room.databaseBuilder(getContext(), AppDatabase.class, "my-database-name").build();
                                        ChatDAO chatDao = dbRoom.chatDao();
                                        dbInstance.chatDao().insertChat(chat); // Insert or update chat in Room
                                        Log.e("ChatList", "fetch new");
                                    }
                                });
                            } else {
                                Log.d("Firebase", "No new chats to sync.");
                            }
                        } else {
                            Log.w("Firebase", "Error getting chats.", task.getException());
                        }
                    });
        });
    }

    private void observeChatUpdates() {
        String uid = userProfile.getKeyId();

        // Observe the chat updates based on the user role and update the RecyclerView
        dbInstance.chatDao().getAllChats(uid).observe(getViewLifecycleOwner(), newChats -> {
            chatListAdapter.updateChatList(newChats);
        });
    }


    // Fetch the last message timestamp from Room
    private void getLastMessageTimestamp(AppDatabase dbInstance, String uid, TimestampCallback callback) {
        // Query Room database asynchronously
        Executors.newSingleThreadExecutor().execute(() -> {
            Long lastTimestamp = dbInstance.chatDao().getLastUpdated(uid);
            if (lastTimestamp == null || lastTimestamp == 0L) {
                // If Room is empty, return 0L to indicate no local chats
                callback.onTimestampFetched(0L);
            } else {
                callback.onTimestampFetched(lastTimestamp);
            }
        });
    }


//    public long getLastMessageUpdate(AppDatabase db) {
//        // Retrieve the most recent updatedAt timestamp for messages
//        List<MessageModel2> messages = db.messageDao().getAllMessages();
//        return messages.isEmpty() ? 0 : messages.stream().mapToLong(MessageModel2::getUpdatedAt).max().orElse(0);
//    }

    private String formatTimestamp(Date timestamp) {
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