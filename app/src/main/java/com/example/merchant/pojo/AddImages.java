package com.example.merchant.pojo;

import java.util.List;

public class AddImages {
    public List<String> imageUrl;

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "AddImages{" +
                "imageUrl=" + imageUrl +
                '}';
    }
}

