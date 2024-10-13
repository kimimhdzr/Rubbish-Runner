package com.example.trashrunner.Main.Community.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Persistent.ChatViewModel;
import com.example.trashrunner.DataBinding.Persistent.CurrentUserSharedPreference;
import com.example.trashrunner.DataBinding.RoomDB.Table.MessageModel2;
import com.example.trashrunner.Main.Community.Adapters.MessagesAdapter;
import com.example.trashrunner.Main.Community.Services.ChatManager;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Fragment {


    CurrentUserSharedPreference userProfile;
    ImageButton close_icon;
    CollectionReference messagesCollection;  // Declare this here
    RecyclerView recyclerView;
    MessagesAdapter messagesAdapter;
    TextView headerTxtView;
    ChatManager chatManager;
    ImageButton button_send;
    EditText message_edit_txt;
    List<MessageModel2> messageList = new ArrayList<>();
    private ChatViewModel chatViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        userProfile = new CurrentUserSharedPreference(getContext());
        chatManager = ChatManager.getInstance();

        headerTxtView = view.findViewById(R.id.header);
        headerTxtView.setText(chatManager.getChatName());

        // Set up the adapter
        recyclerView = view.findViewById(R.id.recycler_view_messages);
        messagesAdapter = new MessagesAdapter(
                messageList,
                userProfile.getRole()
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //add animation to the cards
        recyclerView.setAdapter(messagesAdapter);

        message_edit_txt = view.findViewById(R.id.edit_text_message);
        button_send = view.findViewById(R.id.btn_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_text = message_edit_txt.getText().toString();
                String messageType = "text";
                if (TextUtils.isEmpty(input_text)) {
                    message_edit_txt.setError("Empty");
                    return;
                }
                chatManager.sendMessage(
                        getContext(),
                        chatManager.getChatID(),
                        input_text,
                        messageType
                );
                message_edit_txt.setText("");
            }
        });
        // Find the FAB and set the onClickListener to handle back navigation
        close_icon = view.findViewById(R.id.close_icon);
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger back navigation
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        String chatId = chatManager.getChatID();
        // Set up ViewModel
        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ChatViewModel(requireActivity().getApplication(), chatId);
            }
        }).get(ChatViewModel.class);
        chatViewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<List<MessageModel2>>() {
            @Override
            public void onChanged(List<MessageModel2> messages) {
                messagesAdapter.setMessages(messages);  // Update adapter with new messages
            }
        });
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        messagesCollection = firestore.collection("chats")
                .document(chatId)
                .collection("messages");
        // Fetch messages from Firestore and store in Room
        fetchMessagesFromFirestore(chatId);
        return view;
    }
    private void fetchMessagesFromFirestore(String chatId) {
        messagesCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e);
                return;
            }
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                List<MessageModel2> messagesToInsert = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    // Map Firestore document to MessageModel2
                    String messageId = document.getId();
                    String content = document.getString("content");
                    String sender = document.getString("sender");
                    Timestamp timestamp = document.getTimestamp("timestamp");
                    long timestamplong = timestamp != null ? timestamp.toDate().getTime() : 0; // Convert to Long

                    // Create a MessageModel2 object
                    MessageModel2 message = new MessageModel2(messageId, chatId, content, timestamplong, sender);
                    messagesToInsert.add(message);  // Add to list
                }
                // Insert all messages into the Room database
                chatViewModel.insertMessages(messagesToInsert);  // You might need to create a new insertMessages method in your ViewModel
            }
        });
    }
}