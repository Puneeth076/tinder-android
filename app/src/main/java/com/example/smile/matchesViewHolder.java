package com.example.smile;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smile.R;

public class matchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView matchId;
    public matchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        matchId = itemView.findViewById(R.id.matchId);
    }

    @Override
    public void onClick(View view) {

    }
}
