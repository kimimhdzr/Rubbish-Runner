package com.example.trashrunner.DataBinding.RoomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trashrunner.DataBinding.RoomDB.Table.MessageModel2;

import java.util.List;

@Dao
public interface MessageDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<MessageModel2> messages);  // Inserts list of messages

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    LiveData<List<MessageModel2>> getMessagesForChat(String chatId);  // Retrieves messages for a specific chat
    @Query("DELETE FROM messages") // New method to delete all messages
    void deleteAllMessages();
}



