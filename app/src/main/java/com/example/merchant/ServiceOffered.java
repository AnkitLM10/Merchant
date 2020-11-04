package com.example.merchant;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

class Service {
    public int serviceId;
    public int serviceCategoryId;
    public String service;
}

class ServiceDataList implements Parcelable {
    public String category;
    public int itemId;
    public int price;
    public int discountPercentage;
    public Service service;

    protected ServiceDataList(Parcel in) {
        category = in.readString();
        itemId = in.readInt();
        price = in.readInt();
        discountPercentage = in.readInt();
    }

    public static final Creator<ServiceDataList> CREATOR = new Creator<ServiceDataList>() {
        @Override
        public ServiceDataList createFromParcel(Parcel in) {
            return new ServiceDataList(in);
        }

        @Override
        public ServiceDataList[] newArray(int size) {
            return new ServiceDataList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeInt(itemId);
        parcel.writeInt(price);
        parcel.writeInt(discountPercentage);
    }
}

public class ServiceOffered {
    public String responseCode;
    public String message;
    public List<ServiceDataList> data;
}

