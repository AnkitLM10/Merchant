package com.example.merchant;

import com.example.merchant.pojo.Signature;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class Api_Upload {
    public static String BASE_URL = " https://commonutil.herokuapp.com/file/";
    //https://bmkservicesendpoints.herokuapp.com/api/v1/services?categoryId=1
    public static Api_Upload.APIService postService = null;

    public static Api_Upload.APIService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(Api_Upload.APIService.class);
        }
        return postService;
    }

    public interface APIService {


        @GET("upload/signature")
        Call<Signature> getSignature(@HeaderMap Map<String, String> headers);


    }
}