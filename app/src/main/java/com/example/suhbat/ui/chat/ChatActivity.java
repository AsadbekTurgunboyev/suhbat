package com.example.suhbat.ui.chat;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.suhbat.BaseActivity;
import com.example.suhbat.R;
import com.example.suhbat.domain.model.ChatModel;
import com.example.suhbat.domain.model.UserData;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends BaseActivity implements TextWatcher {

    TextView personName;
    UserData data;
    CircleImageView avatar;
    EditText typingEdt;
    RecyclerView chatRecyclerView;

    ImageView buttonMicrophone, buttonSend;
    UserPreferenceManager preferenceManager;

    TextView lastTimeTxt;
    TextView txtAvatar;
    DatabaseReference chatReference, lastReference;
    String friendKey, myKey, chatKey;
    List<ChatModel> chatModelList;
    DatabaseReference userReference;
    String[] color = {"#49A355", "#D97C57", "#B85378", "#4DA8BD", "#3D95BA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        data = getIntent().getParcelableExtra("userdata");

        preferenceManager = new UserPreferenceManager(this);
        chatReference = FirebaseDatabase.getInstance().getReference("chats");
        userReference = FirebaseDatabase.getInstance().getReference("users");
        lastReference = FirebaseDatabase.getInstance().getReference("lastmessages");
        setPersonData(data);


        friendKey = data.getKey();
        myKey = preferenceManager.getKey();

        userReference.child(friendKey).child("lastTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time = snapshot.getValue(String.class);
                if (time.equals("online")){
                    lastTimeTxt.setText("online");
                }else {
                    lastTimeTxt.setText("oxirgi marta " + getLastTime(time));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (friendKey.compareTo(myKey) > 0) {
            chatKey = friendKey + myKey;
        } else {
            chatKey = myKey + friendKey;
        }

        chatReference.child(chatKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModelList = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ChatModel model = ds.getValue(ChatModel.class);
                    chatModelList.add(model);
                }
                chatRecyclerView.setAdapter(new ChatAdapter(chatModelList,preferenceManager.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {


        String messageKey = chatReference.push().getKey();
        String message = typingEdt.getText().toString();
        if (message.isEmpty()) {
            return;
        }


        ChatModel chatModel = new ChatModel(chatKey, messageKey, message, myKey, friendKey, getTime(),false);
        chatReference.child(chatKey).child(messageKey).setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    lastReference.child(chatKey).setValue(chatModel);
                    typingEdt.setText("");

                }
            }
        });
    }

    private void setPersonData(UserData data) {
        personName.setText(data.getName());
        typingEdt.addTextChangedListener(this);
        ShapeDrawable ovalDrawable = new ShapeDrawable(new OvalShape());
        String color = getIntent().getStringExtra("color");
        ovalDrawable.getPaint().setColor(Color.parseColor(color));
        txtAvatar.setBackground(ovalDrawable);
        txtAvatar.setText(getUserNameLetter(data.getName()));
        if (data.getAvatarUrl().isEmpty()) {
            txtAvatar.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.GONE);
        } else {
            txtAvatar.setVisibility(View.GONE);
            avatar.setVisibility(View.VISIBLE);
            Glide.with(this).load(data.getAvatarUrl()).into(avatar);
        }
    }

    private void initViews() {
        personName = findViewById(R.id.personName);
        avatar = findViewById(R.id.circleImageView);
        txtAvatar = findViewById(R.id.txtAvata);
        buttonSend = findViewById(R.id.buttonSend);
        buttonMicrophone = findViewById(R.id.buttonMicrophone);
        typingEdt = findViewById(R.id.edtTyping);
        chatRecyclerView = findViewById(R.id.recyclerView);
        lastTimeTxt = findViewById(R.id.lastTime);

    }

    public String getUserNameLetter(String name) {
        return name.substring(0, 2);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0) {
            buttonMicrophone.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        } else {
            buttonMicrophone.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        }
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
    public String getLastTime(String time){
        String[] a = time.split("_");
        return a[1].substring(0,5);
    }
}
