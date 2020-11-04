package com.example.merchant;

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

public class Api_booking {
    public static String BASE_URL = "https://bmkbookings.herokuapp.com/";

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
        @Headers("Content-Type: application/json")
        @GET("booking/merchant")
        Call<BookingResponse> getAllBooking(@HeaderMap Map<String, String> headers);

        @PUT("booking/merchant/updateStatus")
        Call<UpdateBookingStatus> putBookingData(@HeaderMap Map<String, String> headers, @Body BookingDataAction BookingDataAction);

    }
}
