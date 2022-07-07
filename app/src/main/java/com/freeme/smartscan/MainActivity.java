package com.freeme.smartscan;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.freeme.smartscan.fragment.FunCardFragment;
import com.freeme.smartscan.fragment.FunDocumentFragment;
import com.freeme.smartscan.fragment.FunRecognitionFragment;
import com.freeme.smartscan.fragment.FunScanFragment;
import com.freeme.smartscan.fragment.FunTranslateFragment;
import com.freeme.smartscan.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintlayout;
    private TextView tv_result;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView btnCancel;
    private ImageView btnShot;
    private RadioGroup mFunMemu;

    private Fragment mDocumentFragment;
    private Fragment mTranslateFragment;
    private Fragment mScanFragment;
    private Fragment mRecognitionFragment;
    private Fragment mCardFragment;

    public static final int REQUEST_CODE_PHOTO = 0X1113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        constraintlayout = findViewById(R.id.constraintlayout);
        tv_result = findViewById(R.id.tv_result);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintlayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        btnShot = findViewById(R.id.btn_shot);
        mFunMemu = findViewById(R.id.fun_menu);
        mFunMemu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String index = radioButton.getTag().toString();
                android.util.Log.e("fangjian","index="+index);
                replaceFragment(index);
            }
        });

        setCancelOperation();

        if(savedInstanceState == null){
            replaceFragment("fun_scan");
        }
    }

    public void replaceFragment(String index) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = null;
        switch (index) {
            case Constants.FRAGMENT_DOCUMENT_TAG:
                if (mDocumentFragment == null) {
                    mDocumentFragment = new FunDocumentFragment();
                }
                currentFragment = mDocumentFragment;
                break;
            case Constants.FRAGMENT_TRANSLATE_TAG:
                if (mTranslateFragment == null) {
                    mTranslateFragment = new FunTranslateFragment();
                }
                currentFragment = mTranslateFragment;
                break;
            case Constants.FRAGMENT_SCAN_TAG:
                if (mScanFragment == null) {
                    mScanFragment = new FunScanFragment(this);
                }
                currentFragment = mScanFragment;
                break;
            case Constants.FRAGMENT_RECOGNITION_TAG:
                if (mRecognitionFragment == null) {
                    mRecognitionFragment = new FunRecognitionFragment();
                }
                currentFragment = mRecognitionFragment;
                break;
            case Constants.FRAGMENT_CARD_TAG:
                if (mCardFragment == null) {
                    mCardFragment = new FunCardFragment();
                }
                currentFragment = mCardFragment;
                break;
            default:
                currentFragment = new FunScanFragment(this);

        }
        mTransaction.replace(R.id.frame_layout, currentFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    private void setCancelOperation(){
        btnCancel = findViewById(R.id.iv_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    /**
     * Handle the return results from the album.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(MainActivity.this, bitmap, new HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create());
                if (hmsScans != null && hmsScans.length > 0 && hmsScans[0] != null && !TextUtils.isEmpty(hmsScans[0].getOriginalValue())) {
                    //Intent intent = new Intent();
                    //intent.putExtra(SCAN_RESULT, hmsScans[0]);
                    //setResult(RESULT_OK, intent);
                    //MainActivity.this.finish();
                    HmsScan hmsScan = hmsScans[0];
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    tv_result.setText(hmsScan.getOriginalValue());
                    //Toast.makeText(MainActivity.this,"hmsScan="+hmsScan.getOriginalValue()+" ScanType="+hmsScan.getScanType(),Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showResult(HmsScan hmsScan){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        tv_result.setText(hmsScan.getOriginalValue());
    }
}