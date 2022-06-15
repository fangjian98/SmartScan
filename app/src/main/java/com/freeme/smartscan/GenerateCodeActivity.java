package com.freeme.smartscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.freeme.smartscan.utils.FileUtil;
import com.freeme.smartscan.utils.MimeUtil;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class GenerateCodeActivity extends Activity {

    private static final String TAG = "GenerateCodeActivity" ;
    //Define a view.
    private EditText inputContent;
    private ImageView barcodeImage;
    private String content;
    private int width, height;
    private Bitmap resultImage;
    private int type = 0;
    private int margin = 1;
    private int color = Color.BLACK;
    private int background = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        inputContent = findViewById(R.id.barcode_content);

        //Set the barcode default type,margin,color and background color.
        type = HmsScan.QRCODE_SCAN_TYPE;
        margin = 1;
        color = Color.BLACK;
        background = Color.WHITE;
    }

    /**
     * Generate a barcode.
     */
    public void generateCodeBtnClick(View v) {
        content = inputContent.getText().toString();

        //QR code default width and height
        width = 500;
        height = 500;

        //Set the barcode content.
        if (content.length() <= 0) {
            Toast.makeText(this, "Please input content first!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //Generate the barcode.
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator().setBitmapMargin(margin).setBitmapColor(color).setBitmapBackgroundColor(background).create();
            resultImage = ScanUtil.buildBitmap(content, type, width, height, options);
            //barcodeImage.setImageBitmap(resultImage);

            showGenerateCodeDialog(resultImage);

        } catch (WriterException e) {
            Toast.makeText(this, "Parameter Error!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save the barcode.
     */
    public void saveCodeBtnClick() {
        if (resultImage == null) {
            Toast.makeText(GenerateCodeActivity.this, "Please generate barcode first!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String fileName = System.currentTimeMillis() + ".jpg";
            String storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+File.separator+"SmartScan";
            android.util.Log.e("Fangjian","storePath="+storePath);
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean isSuccess = resultImage.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            //send Broadcast to notify photo album
            Uri uri = Uri.fromFile(file);
            GenerateCodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {

                //Share QR code using MediaStore API
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                Uri shareUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.putExtra(Intent.EXTRA_STREAM, shareUri);
                startActivity(Intent.createChooser(intent,getString(R.string.action_share)));*/

                //Share QR code using FileProvider
                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri shareUri = FileProvider.getUriForFile(this, "com.freeme.smartscan.provider",file);
                grantUriPermission(getPackageName(), shareUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, shareUri);
                startActivity(Intent.createChooser(intent,getString(R.string.action_share)));

                //Toast.makeText(GenerateCodeActivity.this, "Barcode has been saved locally", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GenerateCodeActivity.this, "Barcode save failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.w(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(GenerateCodeActivity.this, "Unkown Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGenerateCodeDialog(Bitmap codeImage) {
        View codeDialog = LayoutInflater.from(this).inflate(R.layout.generate_code_dialog, null);
        barcodeImage = codeDialog.findViewById(R.id.barcode_image);
        barcodeImage.setImageBitmap(codeImage);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.generate_qr_code_title)
                .setPositiveButton(R.string.action_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCodeBtnClick();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(codeDialog);
        alertDialog.show();
    }
}