package com.freeme.smartscan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freeme.smartscan.R;
import com.freeme.smartscan.model.CardInfo;

import java.io.File;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context mContext;
    private ArrayList<CardInfo> mCardInfos;

    public CardAdapter(Context context, ArrayList<CardInfo> cardInfos) {
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
        CardInfo cardInfo = mCardInfos.get(position);
        if (cardInfo != null) {
            Glide.with(mContext)
                    .load(new File(cardInfo.getCardPath()))
                    .placeholder(R.drawable.ic_launcher_foreground)//Glide使用placeholder占位图，最后加载出来的图片尺寸变成了占位图尺寸
                    .thumbnail(0.2f)
                    .into(holder.card);
        }
    }

    @Override
    public int getItemCount() {
        return mCardInfos != null ? mCardInfos.size() : 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView card;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.iv_card);
        }
    }
}
