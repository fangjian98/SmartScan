package com.freeme.smartscan.model;

public class CardInfo extends Info{

    private String cardPath;

    public CardInfo(String cardPath) {
        this.cardPath = cardPath;
    }

    public String getCardPath() {
        return cardPath;
    }

    public void setCardPath(String cardPath) {
        this.cardPath = cardPath;
    }
}
