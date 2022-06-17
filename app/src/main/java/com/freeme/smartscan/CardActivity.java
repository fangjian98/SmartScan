package com.freeme.smartscan;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import com.freeme.smartscan.adapter.TabsAdapter;
import com.freeme.smartscan.fragment.BuscardFragment;
import com.freeme.smartscan.fragment.CardFragment;
import com.freeme.smartscan.model.CardInfo;
import com.freeme.smartscan.utils.LogUtil;
import com.freeme.smartscan.view.CustomViewPager;
import com.freeme.smartscan.view.FreemeTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CardActivity extends FragmentActivity {

    private final static String SCAN_CARD_DIR = "/storage/emulated/0/Pictures/SmartScan/";
    private final static String SCAN_BUSINESSCARD_DIR = "/storage/emulated/0/Pictures/Screenshots/";

    private CustomViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    private ArrayList<CardInfo> mCardInfos = new ArrayList<>();
    private ArrayList<CardInfo> mBusinessCardInfos = new ArrayList<>();
    private Bundle cardArgs = new Bundle();
    private Bundle busCardArgs = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        initViewPager();

        //LoadDocumentFileTask
        LoadCardTask loadCardTask = new LoadCardTask(this);
        loadCardTask.execute();
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mViewPager, new ArrayList<String>() {{
            add(getString(R.string.card_identity));
            add(getString(R.string.card_bussiness));
        }});

        mTabsAdapter.addTab(null, CardFragment.class, cardArgs);
        mTabsAdapter.addTab(null, BuscardFragment.class, busCardArgs);
        mViewPager.setAdapter(mTabsAdapter);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            FreemeTabLayout tabs = (FreemeTabLayout) View.inflate(this,
                    R.layout.freeme_layout_tab, null);
            tabs.setupWithViewPager(mViewPager);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER_HORIZONTAL);
            actionBar.setCustomView(tabs, lp);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private class LoadCardTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog mDialog;
        Context mContext;

        public LoadCardTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mDialog = ProgressDialog.show(mContext, "提示", "奋力寻找中……");
            // mDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            getAllFiles(SCAN_CARD_DIR, mCardInfos);
            getAllFiles(SCAN_BUSINESSCARD_DIR, mBusinessCardInfos);

            cardArgs.putParcelableArrayList("list",mCardInfos);
            busCardArgs.putParcelableArrayList("list",mBusinessCardInfos);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // mDialog.cancel();

            // put bundle after fragment init,it's useless,move these code to  doInBackground
            // cardArgs.putParcelableArrayList("list",mCardInfos);
            // busCardArgs.putParcelableArrayList("list",mBusinessCardInfos);
        }

        private void getAllFiles(String dir, ArrayList<CardInfo> infos) {
            File root = new File(dir);
            File[] files = root.listFiles();
            LogUtil.i("files=" + Arrays.toString(files));
            if (files != null) {
                for (File doc : files) {
                    if (doc.isDirectory()) {
                        getAllFiles(doc.getAbsolutePath(),infos);
                    } else {
                        String docName = doc.getName();
                        LogUtil.i("filename=" + docName);
                        CardInfo cardInfo = new CardInfo(dir + docName);
                        infos.add(cardInfo);
                    }
                }
            }
        }
    }
}