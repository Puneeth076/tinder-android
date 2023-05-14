package com.example.smile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smile.matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    cards cards_data[];
    private com.example.smile.arrayAdapter arrayAdapter;
    private int i;
    Drawable drawable;

    DatabaseReference userDb;
    FirebaseAuth mAuth;
    ImageView imageView;

    FirebaseUser mUser;
    ListView listView;
    List<cards> rowItems;

    ImageView matches;
    String currentUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.profileImage);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        currentUid = mUser.getUid();
        matches = findViewById(R.id.matchesImage);
        checkUserGender();
        rowItems = new ArrayList<cards>();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)

                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                 cards obj = (cards) dataObject;
                 String userId = obj.getUserId();
                 userDb.child(userId).child("connections").child("nope").child(currentUid).setValue(true);
                makeToast(MainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                userDb.child(userId).child("connections").child("yope").child(currentUid).setValue(true);
                isConnectionMatches(userId);
                makeToast(MainActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(MainActivity.this, "Clicked!");
            }
        });

    }

    private void isConnectionMatches(String userId) {
        DatabaseReference currentUsersConnectionDb = userDb.child(currentUid).child("connections").child("yope").child(userId);
        currentUsersConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(MainActivity.this, "New connection", Toast.LENGTH_LONG).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("chats").push().getKey();

                    userDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUid).child("chatId").setValue(key);

                    userDb.child(currentUid).child("connections").child("matches").child(snapshot.getKey()).child("chatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    String userGender, notUserGender;
    public void checkUserGender(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getKey().equals(user.getUid())){
                    if(snapshot.exists()){
                        if (snapshot.child("gender") != null){
                            userGender = snapshot.child("gender").getValue().toString();
                            notUserGender = "Female";
                            switch (userGender){
                                case "Male":
                                    notUserGender = "Female";
                                    break;
                                case "Female":
                                    notUserGender="Male";
                                    break;
                            }
                            getOppositeGenderUsers();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getOppositeGenderUsers(){


        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("gender").getValue() != null) {
                    if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUid) && !snapshot.child("connections").child("yope").hasChild(currentUid) && snapshot.child("gender").getValue().toString().equals(notUserGender)) {
                        String userProfile = "default";


                        if (!snapshot.child("profileImage").getValue().toString().equals("default")) {

                            userProfile = snapshot.child("profileImage").getValue().toString();
                        }
                        cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), userProfile);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}