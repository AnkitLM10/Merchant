package com.example.merchant.merchantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ImageArr  implements Parcelable, Serializable {
    public ImageArr(Parcel in) {
        imageId = in.readInt();
        imageAddress = in.readString();
    }

    public static final Creator<ImageArr> CREATOR = new Creator<ImageArr>() {
        @Override
        public ImageArr createFromParcel(Parcel in) {
            return new ImageArr(in);
        }

        @Override
        public ImageArr[] newArray(int size) {
            return new ImageArr[size];
        }
    };

    public ImageArr() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public int imageId;
    public String imageAddress;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeString(imageAddress);
    }
}


