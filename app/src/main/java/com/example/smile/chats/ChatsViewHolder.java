package com.example.smile.chats;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smile.R;

public class ChatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;

    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        Log.e("Error in holder", "Error");
        mMatchId = itemView.findViewById(R.id.matchesId);
        mMatchName = itemView.findViewById(R.id.matchName);
        mMatchImage = itemView.findViewById(R.id.img_profile);
    }

    @Override
    public void onClick(View view) {

    }
}
