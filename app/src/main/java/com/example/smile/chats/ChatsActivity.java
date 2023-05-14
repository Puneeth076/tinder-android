package com.example.smile.chats;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.smile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {

    public RecyclerView.Adapter mChatsAdapter;
    String currentUserid, matchId, matchName,matchImage;
    TextView  mMatchName;
    EditText inputMessages;
    ImageButton sendMessage;
    CircleImageView profileImage;
    private final ArrayList<ChatsObject> resultMatches = new ArrayList<ChatsObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_activity);

        matchId = getIntent().getExtras().getString("matchId");
        matchName = getIntent().getExtras().getString("matchName");
        matchImage = getIntent().getExtras().getString("matchProfileImage");
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        mMatchName = findViewById(R.id.senderName);
        profileImage = findViewById(R.id.senderImage);
        inputMessages = findViewById(R.id.message);
        sendMessage = findViewById(R.id.sendMessage);


        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        currentUserid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        RecyclerView.LayoutManager mMatchesLayoutManager = new LinearLayoutManager(this);
        mChatsAdapter = new ChatsAdapter(getDatasetMatches(), this);
        recyclerView.setLayoutManager(mMatchesLayoutManager);
        recyclerView.setAdapter(mChatsAdapter);

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
    }

    private void sendMessageToUser() {

    }

    private List<ChatsObject> getDatasetMatches() {
        return resultMatches;
    }

}