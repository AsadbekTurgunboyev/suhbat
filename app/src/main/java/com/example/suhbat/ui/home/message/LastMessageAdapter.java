package com.example.suhbat.ui.home.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.suhbat.R;
import com.example.suhbat.domain.model.ChatModel;
import com.example.suhbat.domain.model.LastMessageModel;
import com.example.suhbat.domain.model.UserData;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.example.suhbat.ui.chat.ChatActivity;
import com.example.suhbat.ui.home.contact.ContactAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LastMessageAdapter extends RecyclerView.Adapter<LastMessageAdapter.LastViewHolder> {

    String[] color = {"#49A355","#D97C57","#B85378","#4DA8BD","#3D95BA"};



    List<LastMessageModel> list;
    int countMessage = 0;

    public LastMessageAdapter(List<LastMessageModel> list, int count) {
        this.list = list;
        countMessage = count;
    }

    @NonNull
    @Override
    public LastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);
        return new LastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastViewHolder holder, int position) {
        holder.bind(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LastViewHolder extends RecyclerView.ViewHolder {
        TextView personName, personPhone,txtAvatar, messageCount;
        CircleImageView avatar;
        public LastViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.textView);
            personPhone = itemView.findViewById(R.id.txtPhone);
            txtAvatar = itemView.findViewById(R.id.txtAvata);
            avatar = itemView.findViewById(R.id.circleImageView);
            messageCount = itemView.findViewById(R.id.textCount);
        }

        public void bind(LastMessageModel userData, int pos){
            if (countMessage == 0){
                messageCount.setVisibility(View.GONE);
            }else {
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(String.valueOf(countMessage));
            }
            personPhone.setText(userData.getMessage());
            personName.setText(userData.getName());

            ShapeDrawable ovalDrawable = new ShapeDrawable(new OvalShape());

            ovalDrawable.getPaint().setColor(Color.parseColor(color[pos % color.length]));
            txtAvatar.setBackground(ovalDrawable);
            txtAvatar.setText(getUserNameLetter(userData.getName()));
            if (userData.getAvatarUrl().isEmpty()){
                txtAvatar.setVisibility(View.VISIBLE);
                avatar.setVisibility(View.GONE);
            }else {
                txtAvatar.setVisibility(View.GONE);
                avatar.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(userData.getAvatarUrl()).into(avatar);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    intent.putExtra("userdata",list.get(getAdapterPosition()).getData());
                    intent.putExtra("color",color[pos % color.length]);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public String getUserNameLetter(String name){
            return name.substring(0,2);
        }
    }
}
