package com.freeme.smartscan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtil {

    private static final String mformatType = "yyyy/MM/dd HH:mm:ss";

    public static String getFileLastModifiedTime(long time) {
        Calendar cal = Calendar.getInstance();
        //long time = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(mformatType);
        cal.setTimeInMillis(time);

        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }


    /**
     　　* 获取指定文件大小
     　　* @param f
     　　* @return
     　　* @throws Exception
     */
    public static long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }
    /**
     　　* 转换文件大小
     　　* @param fileS
     　　* @return
     */
    public static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
