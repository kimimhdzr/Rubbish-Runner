package com.example.trashrunner.DataBinding.RoomDB.Table;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages",
        foreignKeys = @ForeignKey(entity = ChatModel.class,
                parentColumns = "chatId",
                childColumns = "chatId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "chatId")})

public class MessageModel2 {
    public MessageModel2(@NonNull String messageId, @NonNull String chatId, String content, long timestamp, String sender) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    @PrimaryKey
    @NonNull
    private String messageId;
    @NonNull
    private String chatId;   // Foreign key to reference the chat
    private String content;
    private long timestamp;
    private String sender;

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
