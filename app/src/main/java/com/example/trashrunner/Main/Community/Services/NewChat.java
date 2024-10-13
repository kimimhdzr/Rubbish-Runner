package com.example.trashrunner.Main.Community.Services;

import android.content.Context;
import android.util.Log;

import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.Main.Home.Services.ChatCreationCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewChat {
    private FirebaseFirestore db;

    public NewChat() {
        db = FirebaseFirestore.getInstance();
    }

    public void createChat(Context context, String chatId, String companyId, String companyName, String companyStaff, ChatCreationCallback callback) {
        CurrentUserSharedPreference userProfile = new CurrentUserSharedPreference(context);

        // Create participants using a HashMap
        Map<String, Object> participants = new HashMap<>();
        participants.put("user_id", userProfile.getKeyId());
        participants.put("user_name", userProfile.getName());
        participants.put("company_id", companyId);
        participants.put("company_name", companyName);
        participants.put("company_staff", companyStaff);
        participants.put("created_at", FieldValue.serverTimestamp());

        // Create last message using a HashMap
        String messageId = UUID.randomUUID().toString();
        Map<String, Object> lastMessage = new HashMap<>();
        lastMessage.put("message_id", messageId);
        lastMessage.put("content", "Hi, may I know more about your company service pricing");
        lastMessage.put("timestamp", FieldValue.serverTimestamp());
        lastMessage.put("sender", "user");

        // Create the chat document using HashMap
        Map<String, Object> chat = new HashMap<>();
        chat.put("participants", participants);
        chat.put("last_message", lastMessage);
        chat.put("messageModels", new ArrayList<>());  // Initialize as an empty list

        // Write the chat document to Firestore
        db.collection("chats")
                .document(chatId)
                .set(chat)
                .addOnSuccessListener(aVoid -> {
                    // Chat created successfully, call the callback
                    Log.d("NewChat", "Chat successfully created with ID: " + chatId);
                    callback.onChatCreated(chatId);
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.w("NewChat", "Error creating chat", e);
                });
    }

}

