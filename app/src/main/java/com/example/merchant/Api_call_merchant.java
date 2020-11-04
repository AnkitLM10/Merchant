package com.example.merchant;

import com.example.merchant.merchantInfo.MerchantInfo;
import com.example.merchant.pojo.AddImages;
import com.example.merchant.pojo.retrieveAllFeedback;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

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

public class Api_call_merchant {
    public static String BASE_URL = "https://bmkmerchant.herokuapp.com/";

    public static Api_call_merchant.APIService postService = null;

    public static Api_call_merchant.APIService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(Api_call_merchant.APIService.class);
        }
        return postService;
    }

    public interface APIService {

        @POST("merchant")
        Call<LoginResponse> getLoginResponse(@HeaderMap Map<String, String> headers,@Body MerchantDetail merchantDetail);

        @GET("merchant/profile")
        Call<MerchantInfo> getMerchantDetail(@HeaderMap Map<String, String> headers);

        @GET("feedback")
        Call<retrieveAllFeedback> getFeedback(@Query("merchantId") String categoryId, @HeaderMap Map<String, String> headers);

        @PUT("merchant/addImages")
        Call<LoginResponse> addImages(@Query("merchantId") String categoryId, @HeaderMap Map<String, String> headers, @Body AddImages addImages);
    }
}
