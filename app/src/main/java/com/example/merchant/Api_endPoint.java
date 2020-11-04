package com.example.merchant;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class Api_endPoint {
    public static String BASE_URL = " https://bmkservicesendpoints.herokuapp.com/api/v1/";
    //https://bmkservicesendpoints.herokuapp.com/api/v1/services?categoryId=1
    public static Api_endPoint.APIService postService = null;

    public static Api_endPoint.APIService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(Api_endPoint.APIService.class);
        }
        return postService;
    }

    public interface APIService {


        @GET("category")
        Call<Category> getLoginResponse(@HeaderMap Map<String, String> headers);

        @GET("services")
        Call<CategoryId> getCategoryIdResponse(@Query("categoryId") String categoryId, @HeaderMap Map<String, String> headers);


        @GET("portfolio")
        Call<ServiceOffered> getServicesOffered(@Query("merchantId") String categoryId, @HeaderMap Map<String, String> headers);


        @GET("services/all")
        Call<Category> getAllCategory(@HeaderMap Map<String, String> headers);

        @PUT("portfolio")
        Call<LoginResponse> putPortfolio(@HeaderMap Map<String, String> headers, @Body Portfolio portfolio);

    }
}
