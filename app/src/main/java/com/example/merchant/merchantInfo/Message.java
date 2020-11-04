package com.example.merchant.merchantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Message implements Parcelable, Serializable {
    protected Message(Parcel in) {
        merchantId = in.readInt();
        merchantName = in.readString();
        merchantType = in.readString();
        distance = in.readInt();
        email = in.readString();
        phoneNumber = in.readString();
        averageRating = in.readDouble();
        totalRatings = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public List<ImageArr> getImageArr() {
        return imageArr;
    }

    public void setImageArr(List<ImageArr> imageArr) {
        this.imageArr = imageArr;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public int merchantId;
    public String merchantName;
    public String merchantType;
    public List<ImageArr> imageArr;
    public int distance;
    public String email;
    public String phoneNumber;
    public Location location;
    public double averageRating;
    public int totalRatings;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(merchantId);
        parcel.writeString(merchantName);
        parcel.writeString(merchantType);
        parcel.writeInt(distance);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeDouble(averageRating);
        parcel.writeInt(totalRatings);
    }
}