package com.example.merchant.merchantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Location  implements Parcelable, Serializable {
    protected Location(Parcel in) {
        locationId = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int locationId;
    public double lat;
    public double lng;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(locationId);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
