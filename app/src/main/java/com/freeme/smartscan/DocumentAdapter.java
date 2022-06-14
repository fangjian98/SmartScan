package com.freeme.smartscan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freeme.smartscan.model.DocumentInfo;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<DocumentInfo> mDocInfos;
    private CustomItemClickListener mCustomItemClickListener;

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
            holder.docName.setText(doc.getDocName());
            holder.docDate.setText(doc.getDocDate());
            holder.docSize.setText(doc.getDocSize());
        }
    }

    @Override
    public int getItemCount() {
        return mDocInfos.size();
    }

    public void setCustomItemClickListener(CustomItemClickListener mCustomItemClickListener) {
        this.mCustomItemClickListener = mCustomItemClickListener;
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        public ImageView docIcon;
        public TextView docName;
        public TextView docDate;
        public TextView docSize;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            docIcon = itemView.findViewById(R.id.doc_icon);
            docName = itemView.findViewById(R.id.doc_name);
            docDate = itemView.findViewById(R.id.doc_date);
            docSize = itemView.findViewById(R.id.doc_size);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }
}



