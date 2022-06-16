package com.freeme.smartscan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freeme.smartscan.model.CardInfo;
import com.freeme.smartscan.model.DocumentInfo;
import com.freeme.smartscan.utils.Constants;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DocumentInfo> mDocInfos;
    private CustomItemClickListener mCustomItemClickListener;

    private int showType = Constants.TYPE_SHOW_NORMAL;

    public DocumentAdapter(Context context, ArrayList<DocumentInfo> docInfos) {
        mInflater = LayoutInflater.from(context);
        mDocInfos = docInfos;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.document_info_item, null);
        return new DocumentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentInfo doc = mDocInfos.get(position);
        if(doc!=null){
            holder.docIcon.setImageResource(doc.getDocIcon());
            holder.docName.setText(doc.getDocName());
            holder.docDate.setText(doc.getDocDate());
            holder.docSize.setText(doc.getDocSize());

            holder.docItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCustomItemClickListener.onItemClick(v,position);
                }
            });

            holder.docItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCustomItemClickListener.onItemLongClick(v,position);
                    return true;
                }
            });

            if (showType == Constants.TYPE_SHOW_CHECK) {
                holder.docCheck.setVisibility(View.VISIBLE);
                holder.docCheck.setChecked(doc.getIsSelected());
            } else {
                holder.docCheck.setVisibility(View.GONE);
                doc.setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDocInfos.size();
    }

    public void setCustomItemClickListener(CustomItemClickListener mCustomItemClickListener) {
        this.mCustomItemClickListener = mCustomItemClickListener;
    }

    public void setShowType(int type) {
        showType = type;
    }

    public void setAllItemChecked(boolean isChecked) {
        if (mDocInfos != null && mDocInfos.size() != 0) {
            int count = mDocInfos.size();
            for (int i = 0; i < count; i++) {
                mDocInfos.get(i).setSelected(isChecked);
            }
            notifyItemRangeChanged(0, count);
        }
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout docItem;
        public ImageView docIcon;
        public TextView docName;
        public TextView docDate;
        public TextView docSize;
        public CheckBox docCheck;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            docItem =itemView.findViewById(R.id.doc_item);
            docIcon = itemView.findViewById(R.id.doc_icon);
            docName = itemView.findViewById(R.id.doc_name);
            docDate = itemView.findViewById(R.id.doc_date);
            docSize = itemView.findViewById(R.id.doc_size);
            docCheck = itemView.findViewById(R.id.doc_check);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }
}



