package com.example.trashrunner.DataBinding.RoomDB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.example.trashrunner.DataBinding.RoomDB.DAO.ChatDAO;
import com.example.trashrunner.DataBinding.RoomDB.DAO.MessageDAO;
import com.example.trashrunner.DataBinding.RoomDB.Table.ChatModel;
import com.example.trashrunner.DataBinding.RoomDB.Table.MessageModel2;

@Database(entities = {ChatModel.class, MessageModel2.class}, version = 6, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatDAO chatDao();

    public abstract MessageDAO messageDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "chat_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}

