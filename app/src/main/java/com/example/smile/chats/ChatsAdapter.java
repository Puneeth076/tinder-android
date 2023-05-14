package com.example.smile.chats;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smile.R;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsViewHolder> {
    private final List<ChatsObject> mChatsList;
    private final  Context mContext;

    public ChatsAdapter(List<ChatsObject> matchesList, Context context) {
        this.mChatsList = matchesList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_item, parent, false);
         RecyclerView.LayoutParams lp = new
         RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
         layoutView.setLayoutParams(lp);

        return new ChatsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        holder.mMessage.setText(mChatsList.get(position).getMessage());
        if(mChatsList.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#979595"));
        }

    }

    @Override
    public int getItemCount() {
        return this.mChatsList.size();
    }
}
