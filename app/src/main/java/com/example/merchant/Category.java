package com.example.merchant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

class Data  implements Parcelable {
    public int serviceId;
    public int serviceCategoryId;
    public String service;
    public String category;

    protected Data(Parcel in) {
        serviceId = in.readInt();
        serviceCategoryId = in.readInt();
        service = in.readString();
        category = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(serviceId);
        parcel.writeInt(serviceCategoryId);
        parcel.writeString(service);
        parcel.writeString(category);
    }
}

public class Category {
    public String responseCode;
    public String message;
    public List<Data> data;
}

