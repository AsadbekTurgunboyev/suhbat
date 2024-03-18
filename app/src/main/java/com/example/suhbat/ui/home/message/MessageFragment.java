package com.example.suhbat.ui.home.message;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suhbat.R;
import com.example.suhbat.domain.model.ChatModel;
import com.example.suhbat.domain.model.LastMessageModel;
import com.example.suhbat.domain.model.StoryModel;
import com.example.suhbat.domain.model.UserData;
import com.example.suhbat.domain.preference.UserPreferenceManager;
import com.example.suhbat.ui.home.story.ClickStory;
import com.example.suhbat.ui.home.story.StoryAdapter;
import com.example.suhbat.ui.home.story.StoryViewActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MessageFragment extends Fragment implements ClickStory {

    RecyclerView storyRecyclerView, lastMessageRecy;

    StoryAdapter storyAdapter;
    UserPreferenceManager preferenceManager;
    FirebaseDatabase database;
    DatabaseReference reference, lastMessageReference, userReference,chatReference;
    List<StoryModel> storyModels;
    List<LastMessageModel> lastMessageModelList;


    private final ActivityResultLauncher<Intent> startForProfileImageResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK) {
                        //Image Uri will not be null for RESULT_OK
                        Uri fileUri = data.getData();
                        if (fileUri != null) {
                            writeDatabaseInStorage(fileUri);
                            Log.d("tekshirish", "onActivityResult: " + fileUri);
//                            mProfileUri = fileUri;
//                            viewBinding.mainImage.setImageURI(fileUri);
                        }
                    } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
                        Toast.makeText(
                                requireContext(),
                                com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(requireContext(), "Bekor qilindi!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void writeDatabaseInStorage(Uri fileUri) {
        String fileName = UUID.randomUUID().toString();
        StorageReference reference = FirebaseStorage.getInstance().getReference("story").child(fileName);
        reference.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            writeToDatabase(uri);
                        }
                    });
                }
            }
        });

    }

    private void writeToDatabase(Uri uri) {
        String myKey = preferenceManager.getKey();
        StoryModel model = new StoryModel(preferenceManager.getName(), uri.toString(), myKey, false);

        reference.child(myKey).setValue(model);
    }

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        preferenceManager = new UserPreferenceManager(requireContext());
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("storyAction");
        lastMessageReference = database.getReference("lastmessages");
        userReference = database.getReference("users");
        chatReference = database.getReference("chats");
        storyModels = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    StoryModel model = ds.getValue(StoryModel.class);
                    if (!model.getStoryKey().equals(preferenceManager.getKey())) {
                        storyModels.add(model);
                    }
                }
                storyAdapter = new StoryAdapter(storyModels, MessageFragment.this);
                storyRecyclerView.setAdapter(storyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lastMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastMessageModelList = new ArrayList<>();

                for (DataSnapshot ds: snapshot.getChildren()){
                    ChatModel model = ds.getValue(ChatModel.class);
                    if(model.getSenderKey().equals(preferenceManager.getKey()) || model.getReceiverKey().equals(preferenceManager.getKey())){
                        String friendKey;
                        if (model.getReceiverKey().equals(preferenceManager.getKey())){
                            friendKey = model.getSenderKey();
                        }else {
                            friendKey = model.getReceiverKey();
                        }
                        userReference.child(friendKey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                lastMessageModelList.clear();
                                if (snapshot.exists()){

                                    UserData data = snapshot.getValue(UserData.class);
                                    LastMessageModel lastMessageModel = new LastMessageModel(model.getMessageKey(),model.getTimeStamp(),model.getChatKey(),model.getMessage(),data.getName(),data.getAvatarUrl());
                                    lastMessageModel.setData(data);
                                    lastMessageModelList.add(lastMessageModel);

                                }

                                chatReference.child(model.getChatKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int count = 0;
                                        for (DataSnapshot d:snapshot.getChildren()){
                                            ChatModel chatModel = d.getValue(ChatModel.class);
                                            if (chatModel != null && !chatModel.isReadStatus() && !chatModel.getSenderKey().equals(preferenceManager.getKey())){
                                                count ++;
                                            }
                                        }
                                        lastMessageRecy.setAdapter(new LastMessageAdapter(lastMessageModelList,count ));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initViews(View view) {

        storyRecyclerView = view.findViewById(R.id.story_recy);
        lastMessageRecy = view.findViewById(R.id.lastMessageRecy);
    }

    @Override
    public void buttonAddStory() {
        ImagePicker.with(requireActivity()).cameraOnly().cropSquare().maxResultSize(1080, 1080).createIntent(new Function1<Intent, Unit>() {
            @Override
            public Unit invoke(Intent intent) {
                startForProfileImageResult.launch(intent);
                return null;
            }
        });
    }

    @Override
    public void showStory(int position, List<StoryModel> list) {
        Intent intent = new Intent(requireContext(), StoryViewActivity.class);
        intent.putParcelableArrayListExtra("list", new ArrayList<Parcelable>(list));
        intent.putExtra("pos", position);
        startActivity(intent);
    }
}