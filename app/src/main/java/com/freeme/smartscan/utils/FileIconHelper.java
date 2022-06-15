package com.freeme.smartscan.utils;

import java.util.HashMap;

import com.freeme.smartscan.R;

public class FileIconHelper {

    private static final HashMap<String, Integer> fileExtToIcons = new HashMap<>();

    //document type
    static {
        addItem(new String[]{"doc", "docx"}, R.drawable.file_icon_word);
        addItem(new String[]{"ppt", "pptx"}, R.drawable.file_icon_ppt);
        addItem(new String[]{"xsl", "xlsx", "xls"}, R.drawable.file_icon_exc);
        addItem(new String[]{"pdf"}, R.drawable.file_icon_pdf);
    }

    private static void addItem(String[] exts, int resId) {
        if (exts != null) {
            for (String ext : exts) {
                fileExtToIcons.put(ext.toLowerCase(), resId);
            }
        }
    }

    public static int getFileIcon(String ext) {
        Integer i;
        i = fileExtToIcons.get(ext.toLowerCase());
        if (i != null) {
            return i;
        } else {
            return R.drawable.file_icon_default;
        }
    }
}
