package com.freeme.smartscan;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.freeme.smartscan.model.DocumentInfo;
import com.freeme.smartscan.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DocumentActivity extends Activity {

    private RecyclerView mRecyclerView;
    private DocumentAdapter mDocumentAdapter;
    private LoadDocumentFileTask documentFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //LoadDocumentFileTask
        documentFileTask = new LoadDocumentFileTask(this);
        documentFileTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        documentFileTask.cancel(true);
    }

    private class LoadDocumentFileTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog mDialog;
        Context mContext;
        ArrayList<DocumentInfo> mDocInfos = new ArrayList<DocumentInfo>();

        public LoadDocumentFileTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mDialog = ProgressDialog.show(mContext, "提示", "奋力寻找中……");
            //mDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            File fileDir = new File("/storage/emulated/0/Pictures/Screenshots");
            getAllFiles(fileDir);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //mDialog.cancel();

            mDocumentAdapter = new DocumentAdapter(mContext, mDocInfos);
            mRecyclerView.setAdapter(mDocumentAdapter);
        }

        private void getAllFiles(File root) {
            File files[] = root.listFiles();
            android.util.Log.e("fangjian", "files=" + files);
            if (files != null) {
                for (File doc : files) {
                    if (doc.isDirectory()) {
                        getAllFiles(doc);
                    } else {
                        try {
                            if (doc.getCanonicalPath().contains(".jpg")) {
                                String docName = doc.getName();
                                android.util.Log.e("fangjian", "filename=" + docName);
                                String docDate = FileUtil.getFileLastModifiedTime(doc.lastModified());
                                String docSize = FileUtil.FormetFileSize(FileUtil.getFileSize(doc));
                                DocumentInfo documentInfo = new DocumentInfo(docName, docDate, docSize);
                                mDocInfos.add(documentInfo);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}