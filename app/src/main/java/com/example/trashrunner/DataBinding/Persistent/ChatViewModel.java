package com.example.trashrunner.DataBinding.Persistent;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.trashrunner.DataBinding.RoomDB.AppDatabase;
import com.example.trashrunner.DataBinding.RoomDB.DAO.MessageDAO;
import com.example.trashrunner.DataBinding.RoomDB.Table.MessageModel2;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private MessageDAO messageDao;
    private LiveData<List<MessageModel2>> messages;
    private static SharedPreferences sharedPreferences;  // To store the last message timestamp
    private static final String PREFS_NAME = "chat_prefs";  // Name for SharedPreferences
    private static final String KEY_LAST_TIMESTAMP_PREFIX = "last_timestamp_"; // Key prefix for storing timestamps

    public ChatViewModel(@NonNull Application application, String chatId) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);  // Assuming you have a Room DB instance
        messageDao = db.messageDao();
        messages = messageDao.getMessagesForChat(chatId);
        // Initialize SharedPreferences
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    public LiveData<List<MessageModel2>> getMessages() {
        return messages;
    }
    // Insert message method
    public void insertMessage(MessageModel2 message) {
        // Use a background thread for database operations
        new Thread(() -> {
            List<MessageModel2> messagesToInsert = new ArrayList<>();
            messagesToInsert.add(message);
            messageDao.insertMessages(messagesToInsert);
        }).start();
    }
    public void insertMessages(List<MessageModel2> messages) {
        new Thread(() -> {
            messageDao.insertMessages(messages);  // Insert list of messages
        }).start();
    }
    // Get last message timestamp for a specific chat
    public long getLastMessageTimestamp(String chatId) {
        return sharedPreferences.getLong(KEY_LAST_TIMESTAMP_PREFIX + chatId, 0);
    }

    // Update last message timestamp for a specific chat
    public void updateLastMessageTimestamp(String chatId, long timestamp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LAST_TIMESTAMP_PREFIX + chatId, timestamp);
        editor.apply();
    }
    // Clear all preferences
    public static void clearAllPreferences() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all preferences
            editor.apply(); // Apply the changes
        } else {
            Log.e("ChatViewModel", "SharedPreferences is null.");
        }
    }


}

