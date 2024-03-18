package com.example.suhbat.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suhbat.R;
import com.example.suhbat.domain.model.ChatModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    List<ChatModel> list;
    String myKey;
    int SENDER_ID = 1;
    int RECEIVER_ID = 0;
    private DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats");

    public ChatAdapter(List<ChatModel> list, String myKey) {
        this.list = list;
        this.myKey = myKey;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_ID){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sender_chat_buble,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiver_chat_buble,parent,false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessage());
        ChatModel model = list.get(position);
        if (!model.isReadStatus() && !model.getSenderKey().equals(myKey)){
            messagesRef.child(model.getChatKey()).child(model.getMessageKey()).child("readStatus").setValue(true);
        }
        holder.time.setText(getTime(list.get(position).getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView message, time;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.messageTxt);
            time = itemView.findViewById(R.id.timeMessageTxt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSenderKey().equals(myKey)){
            return SENDER_ID;
        }else {
            return RECEIVER_ID;
        }
    }

    public String getTime(String time){
        String[] a = time.split("_");
        return a[1].substring(0,5);
    }
}
