package com.example.smile.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smile.R;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsViewHolder> {
    private final List<ChatsObject> mChatsList;
    private final Context context;

    public ChatsAdapter(List<ChatsObject> matchesList, Context context) {
        this.mChatsList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.matches_items, parent, false);
         RecyclerView.LayoutParams lp = new
         RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
         ViewGroup.LayoutParams.WRAP_CONTENT);
         layoutView.setLayoutParams(lp);

        return new ChatsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mChatsList.size();
    }
}
