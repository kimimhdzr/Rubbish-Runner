package com.example.trashrunner.DataBinding.RoomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.trashrunner.DataBinding.RoomDB.Table.ChatModel;

import java.util.List;

@Dao
public interface ChatDAO {
    // Query to fetch all chats for a given user based on the role (userId could be user, company, or staff)
    @Query("SELECT * FROM chats WHERE userId = :userId OR companyId = :userId OR companyStaff = :userId")
    LiveData<List<ChatModel>> getAllChats(String userId);

    // Insert a chat into the Room database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat(ChatModel chat);

    // Query to get the last message timestamp for synchronization
    @Query("SELECT MAX(lastupdated) FROM chats WHERE userId = :userId OR companyId = :userId OR companyStaff = :userId")
    Long getLastUpdated(String userId);
    @Query("DELETE FROM chats")
    void clearTable();


}

