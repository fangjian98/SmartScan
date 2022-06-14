package com.freeme.smartscan.model;

public class DocumentInfo {

    private String docName;
    private String docDate;
    private String docSize;

    public DocumentInfo(String docName) {
        this.docName = docName;
    }

    public DocumentInfo(String docName, String docDate, String docSize) {
        this.docName = docName;
        this.docDate = docDate;
        this.docSize = docSize;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getDocSize() {
        return docSize;
    }

    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }
}
