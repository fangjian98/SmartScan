package com.freeme.smartscan.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freeme.smartscan.CardAdapter;
import com.freeme.smartscan.R;
import com.freeme.smartscan.model.CardInfo;
import com.freeme.smartscan.model.Info;
import com.freeme.smartscan.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class BuscardFragment extends Fragment {

    private static String SCAN_CARD_DIR = "/storage/emulated/0/Pictures/Screenshots/";
    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private Context mContext;

    public BuscardFragment() {
        // Required empty public constructor
    }
    public BuscardFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //CardAdapter cardAdapter = new CardAdapter(mContext, mCardInfos);
        //mRecyclerView.setAdapter(cardAdapter);

        //LoadDocumentFileTask
        LoadCardTask loadCardTask = new LoadCardTask(getActivity());
        loadCardTask.execute();
        return view;
    }

    ArrayList<Info> mCardInfos = new ArrayList<>();

    private class LoadCardTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog mDialog;
        Context mContext;

        public LoadCardTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // = ProgressDialog.show(mContext, "提示", "奋力寻找中……");
            //mDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            File fileDir = new File(SCAN_CARD_DIR);
            getAllFiles(fileDir);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //mDialog.cancel();

            mCardAdapter = new CardAdapter(mContext, mCardInfos);
            mRecyclerView.setAdapter(mCardAdapter);
        }

        private void getAllFiles(File root) {
            File[] files = root.listFiles();
            LogUtil.i("files=" + Arrays.toString(files));
            if (files != null) {
                for (File doc : files) {
                    if (doc.isDirectory()) {
                        getAllFiles(doc);
                    } else {
                        String docName = doc.getName();
                        LogUtil.i("filename=" + docName);
                        CardInfo cardInfo = new CardInfo(SCAN_CARD_DIR + docName);
                        mCardInfos.add(cardInfo);
                    }
                }
            }
        }
    }
}