package com.freeme.smartscan.model;

public class DocumentInfo {

    private String docName;
    private String docDate;
    private String docSize;
    private int docIcon;
    private boolean docSelected;

    public DocumentInfo(String docName) {
        this.docName = docName;
    }

    public DocumentInfo(String docName, String docDate, String docSize, int docIcon, boolean isSelected) {
        this.docName = docName;
        this.docDate = docDate;
        this.docSize = docSize;
        this.docIcon = docIcon;
        this.docSelected = isSelected;
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

    public int getDocIcon() {
        return docIcon;
    }

    public void setDocIcon(int docIcon) {
        this.docIcon = docIcon;
    }

    public void setSelected(boolean isSelected) {
        docSelected = isSelected;
    }

    public boolean getIsSelected() {
        return docSelected;
    }
}
