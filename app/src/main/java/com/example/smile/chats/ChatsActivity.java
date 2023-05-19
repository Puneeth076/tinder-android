package com.example.smile.chats;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.smile.MainActivity;
import com.example.smile.R;
import com.example.smile.matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mChatsAdapter;
    String currentUserid, matchId, matchName,matchImage, chatId;
    TextView  mMatchName;
    RecyclerView recyclerView;
    EditText inputMessages;
    RecyclerView.LayoutManager mMatchesLayoutManager;
    ZegoSendCallInvitationButton audio_call, vedio_call;
    ImageButton sendMessage;
    CircleImageView profileImage;
    DatabaseReference mDatabaseUser, mDatabaseChat;
    private final ArrayList<ChatsObject> resultMatches = new ArrayList<ChatsObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_activity);

        matchId = getIntent().getExtras().getString("matchId");
        matchName = getIntent().getExtras().getString("matchName");
        matchImage = getIntent().getExtras().getString("matchProfileImage");
        recyclerView = findViewById(R.id.recycleView);
        mMatchName = findViewById(R.id.senderName);
        profileImage = findViewById(R.id.senderImage);
        inputMessages = findViewById(R.id.message);
        sendMessage = findViewById(R.id.sendMessage);
        audio_call = findViewById(R.id.audio_call);
        vedio_call = findViewById(R.id.vedio_call);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        currentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid).child("connections").child("matches").child(matchId).child("chatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("chats");
        mMatchesLayoutManager = new LinearLayoutManager(this);
        mChatsAdapter = new ChatsAdapter(getDatasetMatches(), this);
        recyclerView.setLayoutManager(mMatchesLayoutManager);
        recyclerView.setAdapter(mChatsAdapter);
        getChatId();
        recyclerView.post(new Runnable() {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            final int lastItemPosition = adapter.getItemCount() - 1;
            @Override
            public void run() {
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);
                }
            }
        });
        mMatchName.setText(matchName);
        Glide.with(getApplication()).load(matchImage)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.img1)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(profileImage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToUser();
            }
        });

        Application application = getApplication(); // Android's application context
        long appID = 1551381463;   // yourAppID
        String appSign ="6a86ef106db4ca593bc80622e03914e0b38db2284269f3552585e6b733a991b5";  // yourAppSign
        String userID = currentUserid; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = currentUserid;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);

        if(matchId != null){
            setVoiceCal(matchId, matchName);
            setVedioCall(matchId, matchName);
        }
    }

    private void sendMessageToUser() {
        String sendMessage = inputMessages.getText().toString();

        if(!sendMessage.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map<String, String> newMessage = new HashMap<>();
            newMessage.put("createdBy", currentUserid);
            newMessage.put("message", sendMessage);
            newMessageDb.setValue(newMessage);
        }
        inputMessages.setText(null);
    }

    private void getChatId(){


        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = Objects.requireNonNull(snapshot.getValue()).toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();


                }
                else {
                    Toast.makeText(ChatsActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("message").getValue()!=null){
                        message = Objects.requireNonNull(dataSnapshot.child("message").getValue()).toString();
                    }
                    if(dataSnapshot.child("createdBy").getValue()!=null){
                        createdByUser = Objects.requireNonNull(dataSnapshot.child("createdBy").getValue()).toString();
                    }

                    if(message!=null && createdByUser!=null){
                        boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserid)){
                            currentUserBoolean = true;
                        }
                        ChatsObject newMessage = new ChatsObject(message, currentUserBoolean);
                        resultMatches.add(newMessage);
                        mChatsAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private List<ChatsObject> getDatasetMatches() {
        return resultMatches;
    }

    public void goToMatchesPage(View view){
        Intent intent = new Intent(ChatsActivity.this, MatchesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setVoiceCal(String targetUserId, String targetUserName){
        audio_call.setIsVideoCall(false);
        audio_call.setResourceID("zego_uikit_call");
        audio_call.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserId,targetUserName)));
    }

    public void setVedioCall(String targetUserId, String targetUserName){
        vedio_call.setIsVideoCall(true);
        vedio_call.setResourceID("zego_uikit_call");
        vedio_call.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserId,targetUserName)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}