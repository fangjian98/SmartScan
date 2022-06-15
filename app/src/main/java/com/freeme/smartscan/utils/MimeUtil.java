package com.freeme.smartscan.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeUtil {

    private static final Map<String, String> extensionToMimeTypeMap = new HashMap<>();

    static {
        add("application/msword", "doc");
        add("application/msword", "dot");
        add("application/msword", "docs");
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");
        add("application/vnd.ms-excel", "xls");
        add("application/vnd.ms-excel", "xlt");
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx");
        add("application/vnd.ms-powerpoint", "ppt");
        add("application/vnd.ms-powerpoint", "pot");
        add("application/vnd.ms-powerpoint", "pps");
        add("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        add("application/vnd.openxmlformats-officedocument.presentationml.template", "potx");
        add("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
        add("application/pdf", "pdf");
    }

    private static void add(String mimeType, String extension) {
        extensionToMimeTypeMap.put(extension, mimeType);
    }

    public static String guessMimeTypeFromExtension(String extension) {
        String mimeType = extensionToMimeTypeMap.get(extension);
        return mimeType != null ? mimeType : "*/*";
    }
}
