package com.example.merchant;

import com.example.merchant.pojo.ValidateOtp;
import com.example.merchant.pojo.ValidateOtpCallback;
import com.example.merchant.pojo.VerifyUniqueDetail;
import com.example.merchant.pojo.VerifyUniqueDetailCallback;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class Api_call {
    public static String BASE_URL = "https://bmkauth.herokuapp.com/";

    public static APIService postService = null;

    public static APIService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(APIService.class);
        }
        return postService;
    }

    public interface APIService {
        //       @POST("user/singin/")
//       Call<LoginResponse> getLoginResponse(@Body UserLogin loginResponse);
        @Headers("token: ")
        @POST("api/v1/user/signup")
        Call<LoginResponse> getSignupResponse(@Body RegisterUser userRegister);

        @Headers("token: ")
        @POST("api/v1/user/singin")
        Call<LoginResponse> getLoginResponse(@Body LoginUser loginUser);

        @POST("api/v1/user/verifyUniqueDetails")
        Call<VerifyUniqueDetailCallback> verifyUniqueDetails(@Body VerifyUniqueDetail verifyUniqueDetail);

        @PUT("api/v1/user/validateOtp")
        Call<ValidateOtpCallback> validateOtp(@HeaderMap Map<String, String> headers,@Body ValidateOtp validateOtp);


//        @POST("merchant")
//        Call<LoginResponse> getLoginResponse(@HeaderMap Map<String,String> headers , @Body MerchantDetail merchantDetail);
    }
}
