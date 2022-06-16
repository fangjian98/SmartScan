package com.freeme.smartscan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freeme.smartscan.model.CardInfo;
import com.freeme.smartscan.model.Info;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context mContext;
    private ArrayList<Info> mCardInfos;

    public CardAdapter(Context context, ArrayList<Info> cardInfos) {
        this.mContext = context;
        this.mCardInfos = cardInfos;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_info_item, null);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardInfo cardInfo = (CardInfo) mCardInfos.get(position);
        if (cardInfo != null) {
            Glide.with(mContext)
                    .load(cardInfo.getCardPath())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .thumbnail(0.2f)
                    .into(holder.card);
        }
    }

    @Override
    public int getItemCount() {
        return mCardInfos.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView card;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.iv_card);
        }
    }
}
