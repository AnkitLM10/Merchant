package com.example.merchant;

import java.util.List;

public class CategoryId {
    public String responseCode;
    public String message;
    public List<CategoryIdData> data;

    public String getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public List<CategoryIdData> getData() {
        return data;
    }
}

 class CategoryIdData{
    public int serviceId;
    public int serviceCategoryId;
    public String service;

     public int getServiceId() {
         return serviceId;
     }

     public int getServiceCategoryId() {
         return serviceCategoryId;
     }

     public String getService() {
         return service;
     }
 }