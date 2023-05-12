package com.example.smile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter matchesAdapter;
    RecyclerView.LayoutManager matchesLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        matchesLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(matchesLayoutManager);
        matchesAdapter = new matchesAdapter(getDataSetMatches(), this);
        recyclerView.setAdapter(matchesAdapter);

        for (int i=0;i<100;i++){

        matchesHistory obj = new matchesHistory("Person ID"+i);
        resultsMatches.add(obj);
        }
        matchesAdapter.notifyDataSetChanged();
    }

    public ArrayList<matchesHistory> resultsMatches = new ArrayList<matchesHistory>();
    private List<matchesHistory> getDataSetMatches() {
        return resultsMatches;

    }
}