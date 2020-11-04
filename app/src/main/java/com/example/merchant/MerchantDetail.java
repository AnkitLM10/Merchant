package com.example.merchant;

import java.util.ArrayList;
import java.util.List;

public class MerchantDetail {

    String merchantName = "";
    String merchantType = "";
    List<String> images;
    String email;
    String phoneNumber;
    String password;
    Double lat;
    Double lng;

    public MerchantDetail(String merchantName, String merchantType, String email, String phoneNumber, String lat, String lng, List<String> img, String password) {
        this.merchantName = merchantName;
        this.merchantType = merchantType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
        images = new ArrayList<>();
        images.addAll(img);
        this.password = password;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "MerchantDetail{" +
                "merchantName='" + merchantName + '\'' +
                ", merchantType='" + merchantType + '\'' +
                ", images=" + images +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}

