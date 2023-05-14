package com.example.smile.matches;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smile.R;
import com.example.smile.chats.ChatsActivity;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;

    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);


        mMatchId = itemView.findViewById(R.id.matchesId);
        mMatchName = itemView.findViewById(R.id.matchName);
        mMatchImage = itemView.findViewById(R.id.img_profile);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(view.getContext(), ChatsActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        b.putString("matchName", mMatchName.getText().toString());
        b.putString("matchProfileImage", mMatchImage.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
