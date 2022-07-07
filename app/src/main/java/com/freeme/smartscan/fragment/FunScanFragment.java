package com.freeme.smartscan.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.freeme.smartscan.MainActivity;
import com.freeme.smartscan.R;
import com.freeme.smartscan.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.io.IOException;

public class FunScanFragment extends Fragment {

    private FrameLayout frameLayout;
    private RemoteView remoteView;
    private ImageView settingsBtn;
    private ImageView imgBtn;
    private ImageView flushBtn;
    int mScreenWidth;
    int mScreenHeight;
    //The width and height of scan_view_finder is both 240 dp.
    final int SCAN_FRAME_SIZE = 240;

    private int[] img = {R.drawable.ic_flashlight_on, R.drawable.ic_flashlight_off};
    private static final String TAG = "DefinedActivity";

    //Declare the key. It is used to obtain the value returned from Scan Kit.
    public static final String SCAN_RESULT = "scanResult";
    public static final int REQUEST_CODE_PHOTO = 0X1113;

    private ConstraintLayout constraintlayout;
    private ImageView btnCancel;
    private TextView tv_result_titile;
    private TextView tv_result;
    private RecyclerView recycleview;

    private BottomSheetBehavior bottomSheetBehavior;

    private Context mContext;

    public FunScanFragment() {
        // Required empty public constructor
    }

    public FunScanFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fun_sacn, container, false);

        settingsBtn = view.findViewById(R.id.settings_img);
        btnCancel = view.findViewById(R.id.iv_cancel);
        imgBtn = view.findViewById(R.id.img_btn);


        //bottomSheetBehavior
        constraintlayout = view.findViewById(R.id.constraintlayout);
        tv_result_titile = view.findViewById(R.id.tv_result_titile);
        tv_result = view.findViewById(R.id.tv_result);
        recycleview = view.findViewById(R.id.recycleview);
        setCancelOperation();

        bottomSheetBehavior = BottomSheetBehavior.from(constraintlayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Bind the camera preview screen.
        frameLayout = view.findViewById(R.id.rim);

        //1. Obtain the screen density to calculate the viewfinder's rectangle.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        //2. Obtain the screen size.
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);

        //3. Calculate the viewfinder's rectangle, which in the middle of the layout.
        //Set the scanning area. (Optional. Rect can be null. If no settings are specified, it will be located in the middle of the layout.)
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;


        //Initialize the RemoteView instance, and set callback for the scanning result.
        remoteView = new RemoteView.Builder().setContext(getActivity()).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        // When the light is dim, this API is called back to display the flashlight switch.
        flushBtn = view.findViewById(R.id.flush_btn);
        remoteView.setOnLightVisibleCallback(new OnLightVisibleCallBack() {
            @Override
            public void onVisibleChanged(boolean visible) {
                if(visible){
                    flushBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        // Subscribe to the scanning result callback event.
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                //Check the result.
                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                    //Intent intent = new Intent();
                    //intent.putExtra(SCAN_RESULT, result[0]);
                    //setResult(RESULT_OK, intent);
                    //MainActivity.this.finish();
                    HmsScan hmsScan = result[0];
                    //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    //tv_result.setText(hmsScan.getOriginalValue());
                   ( (MainActivity) requireActivity()).showResult(hmsScan);
                    //Toast.makeText(MainActivity.this,"hmsScan="+hmsScan.getOriginalValue()+" ScanType="+hmsScan.getScanType(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Load the customized view to the activity.
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(remoteView, params);
        // Set the settings, photo scanning, and flashlight operations.
        setSettingsOperation();
        setPictureScanOperation();
        setFlashOperation();


        return view;
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    private void setPictureScanOperation() {
        //imgBtn = findViewById(R.id.img_btn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                ((MainActivity)mContext).startActivityForResult(pickIntent, REQUEST_CODE_PHOTO);
            }
        });
    }

    private void setFlashOperation() {
        flushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remoteView.getLightStatus()) {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[1]);
                } else {
                    remoteView.switchLight();
                    flushBtn.setImageResource(img[0]);
                }
            }
        });
    }

    private void setSettingsOperation() {
        //settingsBtn = findViewById(R.id.settings_img);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SettingsActivity.class));
            }
        });
    }

    private void setCancelOperation(){
        //btnCancel = findViewById(R.id.iv_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    @Override
    public void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        remoteView.onStop();
    }

    /**
     * Handle the return results from the album.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
                HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(mContext, bitmap, new HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create());
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
}