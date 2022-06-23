package com.freeme.smartscan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freeme.smartscan.R;
import com.freeme.smartscan.adapter.CardAdapter;
import com.freeme.smartscan.model.CardInfo;
import com.freeme.widget.FreemeEmptyView;

import java.util.ArrayList;

public class BuscardFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FreemeEmptyView mEmptyView;
    private CardAdapter mCardAdapter;
    private Context mContext;
    private ArrayList<CardInfo> mCardInfos = new ArrayList<>();

    public BuscardFragment() {
        // Required empty public constructor
        this.mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardInfos = getArguments().getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        mRecyclerView = view.findViewById(R.id.recycle_view);
        mEmptyView = view.findViewById(R.id.empty_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCardAdapter = new CardAdapter(getActivity(), mCardInfos);
        mRecyclerView.setAdapter(mCardAdapter);

        showEmptyView(mCardInfos.size() <= 0);

        return view;
    }

    private void showEmptyView(boolean isShow) {
        mEmptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}