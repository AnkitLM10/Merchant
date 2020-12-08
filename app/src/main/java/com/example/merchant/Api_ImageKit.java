package com.example.merchant;

import com.example.merchant.pojo.ImageKitResponse;
import com.example.merchant.pojo.Signature;
import com.example.merchant.pojo.imageKitPost;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public class Api_ImageKit {
    public static String BASE_URL = "https://upload.imagekit.io/api/v1/files/";
    //https://bmkservicesendpoints.herokuapp.com/api/v1/services?categoryId=1
    public static Api_ImageKit.APIService postService = null;

    public static Api_ImageKit.APIService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(Api_ImageKit.APIService.class);
        }
        return postService;
    }

    public interface APIService {
        @FormUrlEncoded
        @POST("upload")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<ImageKitResponse> getUploadedImage(@Field("file") String file,
                                                @Field("fileName") String fileName,
                                                @Field("tags") String tags,
                                                @Field("signature") String signature,
                                                @Field("publicKey") String publicKey,
                                                @Field("token") String token,
                                                @Field("expire") long expire,
                                                @Field("folder") String folder
                                                );

//        @Multipart
//        @POST("upload")
//        @Headers({"Content-Type: application/x-www-form-urlencoded"})
//        Call<ImageKitResponse> getUploadedImage(
//                @Body RequestBody file
//        );


    }
}