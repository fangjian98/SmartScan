package com.freeme.smartscan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CardInfo  implements Parcelable {

    private String cardPath;

    public CardInfo(String cardPath) {
        this.cardPath = cardPath;
    }

    protected CardInfo(Parcel in) {
        cardPath = in.readString16NoHelper();
    }

    public static final Creator<CardInfo> CREATOR = new Creator<CardInfo>() {
        @Override
        public CardInfo createFromParcel(Parcel in) {
            return new CardInfo(in);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };

    public String getCardPath() {
        return cardPath;
    }

    public void setCardPath(String cardPath) {
        this.cardPath = cardPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardPath);
    }
}
