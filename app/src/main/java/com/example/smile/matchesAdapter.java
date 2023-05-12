package com.example.smile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class matchesAdapter extends RecyclerView.Adapter<matchesViewHolder> {

    List<matchesHistory> matchesHistories;
    Context context;

    public matchesAdapter(List<matchesHistory> matchesHistory, Context context1){
        this.matchesHistories = matchesHistory;
        this.context = context1;
    }

    @NonNull
    @Override
    public matchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        matchesViewHolder rmv = new matchesViewHolder((layoutView));
        return rmv;
    }

    @Override
    public void onBindViewHolder(@NonNull matchesViewHolder holder, int position) {

        holder.matchId.setText(matchesHistories.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
