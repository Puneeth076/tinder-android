package com.example.smile.matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

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

public class MatchesActivity extends AppCompatActivity {

    public RecyclerView.Adapter mMatchesAdapter;
    String currentUserid;
    private final ArrayList<MatchesObject> resultMatches = new ArrayList<MatchesObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches2);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        currentUserid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        RecyclerView.LayoutManager mMatchesLayoutManager = new LinearLayoutManager(this);
        mMatchesAdapter = new MatchesAdapter(getDatasetMatches(), this);
        recyclerView.setLayoutManager(mMatchesLayoutManager);
        recyclerView.setAdapter(mMatchesAdapter);
        getUserMatchId();


    }

    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists()){
                    for (DataSnapshot match : snapshot.getChildren()){
                        fetchMatchesInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchMatchesInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     String userId = snapshot.getKey();

                     String name="";
                     String profileImage = "";

                     if(snapshot.child("name").getValue() != null){
                         name = snapshot.child("name").getValue().toString();
                     }
                    if(snapshot.child("profileImage").getValue() != null){
                        profileImage = snapshot.child("profileImage").getValue().toString();
                    }

                    MatchesObject obj = new MatchesObject(userId, name, profileImage);
                    resultMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                    Log.e("Error", "Error matchesActivity");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private List<MatchesObject> getDatasetMatches() {
        return resultMatches;
    }
}