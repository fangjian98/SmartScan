package com.freeme.smartscan;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freeme.smartscan.model.DocumentInfo;
import com.freeme.smartscan.utils.Constants;
import com.freeme.smartscan.utils.FileIconHelper;
import com.freeme.smartscan.utils.FileUtil;
import com.freeme.smartscan.utils.LogUtil;
import com.freeme.smartscan.utils.MimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DocumentActivity extends Activity implements ActionMode.Callback,DocumentAdapter.CustomItemClickListener{

    private static String SCAN_DOCUMENT_DIR = "/storage/emulated/0/Documents/SmartScan/";
    private int mShowType = Constants.TYPE_SHOW_NORMAL;
    private int mSelectedCount = 0;

    private TextView mMultiHeaderTitle, mMultiHeaderSelectAll;

    private ActionMode mActionMode;
    private LinearLayout mActionModeHeader;

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

    @Override
    public void onItemClick(View v, int position) {

        DocumentInfo documentInfo = mDocInfos.get(position);
        String docName = documentInfo.getDocName();

        if (mShowType == Constants.TYPE_SHOW_NORMAL) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this, "com.freeme.smartscan.provider",new File(SCAN_DOCUMENT_DIR,docName));
            grantUriPermission(getPackageName(), uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String type = MimeUtil.guessMimeTypeFromExtension(FileUtil.getFileSuffix(docName));
            intent.setDataAndType(uri, type);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("IntentBuilder", "fail to view file, type: ");
            }
        } else if (mShowType == Constants.TYPE_SHOW_CHECK) {
            CheckBox checkBox = v.findViewById(R.id.doc_check);
            checkBox.toggle();

            if (checkBox.isChecked()) {
                documentInfo.setSelected(true);
                mSelectedCount += 1;
            } else {
                documentInfo.setSelected(false);
                mSelectedCount -= 1;
            }

            //update mViewTitle and mViewSelectall
            updateMultiHeader(mSelectedCount, mDocInfos);

        }

    }

    @Override
    public void onItemLongClick(View v, int position) {
        DocumentInfo documentInfo = mDocInfos.get(position);
        documentInfo.setSelected(true);
        mSelectedCount += 1;
        mShowType = Constants.TYPE_SHOW_CHECK;
        mDocumentAdapter.setShowType(Constants.TYPE_SHOW_CHECK);
        mDocumentAdapter.notifyDataSetChanged();
        startActionMode(this);

        updateMultiHeader(1, mDocInfos);
    }

    private void updateMultiHeader(int selectedCount, ArrayList<DocumentInfo> docInfos) {
        if (selectedCount > 1) {
            mMultiHeaderTitle.setText(getString(R.string.multi_select_titles, selectedCount));
        } else {
            mMultiHeaderTitle.setText(getString(R.string.single_select_title, selectedCount));
        }
        mMultiHeaderSelectAll.setText(selectedCount == docInfos.size()
                ? (getString(R.string.deselect_all))
                : (getString(R.string.select_all)));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_document_operation, menu);
        mActionModeHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.multi_select_header, null);
        mActionMode.setCustomView(mActionModeHeader);

        mMultiHeaderTitle = findViewById(R.id.tv_am_title);
        mMultiHeaderSelectAll = findViewById(R.id.tv_select_all);

        mMultiHeaderSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDocumentAdapter != null) {
                    if (mSelectedCount != mDocInfos.size()) {
                        mDocumentAdapter.setAllItemChecked(true);
                        mSelectedCount = mDocInfos.size();
                        mMultiHeaderTitle.setText(getString(R.string.multi_select_titles, mSelectedCount));
                        mMultiHeaderSelectAll.setText(getString(R.string.deselect_all));
                    } else {
                        mDocumentAdapter.setAllItemChecked(false);
                        mSelectedCount = 0;
                        mMultiHeaderTitle.setText(getString(R.string.single_select_title, mSelectedCount));
                        mMultiHeaderSelectAll.setText(getString(R.string.select_all));
                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_rename:
                Toast.makeText(this,"action rename",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(this,"action share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_delete:
                Toast.makeText(this,"action delete",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        if (mShowType == Constants.TYPE_SHOW_CHECK) {
            mShowType = Constants.TYPE_SHOW_NORMAL;
            mDocumentAdapter.setShowType(Constants.TYPE_SHOW_NORMAL);
            mDocumentAdapter.notifyDataSetChanged();
            //loadTodos(FILTER_TYPE, mId);
            mSelectedCount = 0;
        }
    }

    ArrayList<DocumentInfo> mDocInfos = new ArrayList<DocumentInfo>();

    private class LoadDocumentFileTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog mDialog;
        Context mContext;


        public LoadDocumentFileTask(Context context) {
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
            File fileDir = new File(SCAN_DOCUMENT_DIR);
            getAllFiles(fileDir);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //mDialog.cancel();

            mDocumentAdapter = new DocumentAdapter(mContext, mDocInfos);
            mDocumentAdapter.setCustomItemClickListener((DocumentAdapter.CustomItemClickListener) mContext);
            mRecyclerView.setAdapter(mDocumentAdapter);
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
                        String docDate = FileUtil.getFileLastModifiedTime(doc.lastModified());
                        String docSize = FileUtil.FormetFileSize(FileUtil.getFileSize(doc));
                        int docIcon = FileIconHelper.getFileIcon(FileUtil.getFileSuffix(docName));
                        DocumentInfo documentInfo = new DocumentInfo(docName, docDate, docSize, docIcon,false);
                        mDocInfos.add(documentInfo);
                    }
                }
            }
        }
    }
}