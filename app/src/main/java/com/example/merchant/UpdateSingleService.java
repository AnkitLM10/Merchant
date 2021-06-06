package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSingleService extends AppCompatActivity {

    String token = "", MerchantId = "", itemId = "", price = "", discountPercentage = "", serviceTitle = "";
    TextView updateServiceTitle;
    EditText updateServicePrice;
    EditText updateServiceDiscount;
    Button updateServiceDetail , deleteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_single_service);


        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        itemId = getIntent().getStringExtra("itemId");
        price = getIntent().getStringExtra("price");
        discountPercentage = getIntent().getStringExtra("discountPercentage");
        serviceTitle = getIntent().getStringExtra("serviceTitle");


        updateServiceTitle = (TextView) findViewById(R.id.updateServiceTitle);
        updateServicePrice = (EditText) findViewById(R.id.updateServicePrice);
        updateServiceDiscount = (EditText) findViewById(R.id.updateServiceDiscount);

        updateServiceDetail = (Button) findViewById(R.id.updateServiceDetail);
        deleteService = (Button) findViewById(R.id.deleteService);


        setClickListenerForDeleteButton();

        String rupee = "\u20B9";
        updateServiceTitle.setText(serviceTitle);
        updateServicePrice.setHint(rupee + " " + price);
        updateServiceDiscount.setHint(discountPercentage + " %");

        updateServiceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// updating the portfolio..........................................................................

                if (updateServicePrice.getText().toString().equals("")) {
                    Toast.makeText(UpdateSingleService.this, "Please Enter the New Price", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (updateServiceDiscount.getText().toString().equals("")) {
                    Toast.makeText(UpdateSingleService.this, "Please Enter the New Discount", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("token", token);
                Portfolio portfolio = new Portfolio();
                PortfolioItem portfolioItem = new PortfolioItem();
                portfolioItem.itemId = Integer.parseInt(itemId);
                portfolioItem.price = Integer.parseInt(updateServicePrice.getText().toString());
                portfolioItem.discountPercentage = Integer.parseInt(updateServiceDiscount.getText().toString());
                portfolio.portfolioItems.add(portfolioItem);
                Call<LoginResponse> loginResponse = Api_endPoint.getService().putPortfolio(headers, portfolio);
                loginResponse.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d("tag", call.toString());
                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }

                        Log.d("tag", response.body().getMessage());
                        Log.d("tag", response.body().getResponseCode());

                        Intent intent;
                        intent = new Intent(UpdateSingleService.this, ViewMerchantServices.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Token", token);
                        intent.putExtra("MerchantId", MerchantId);
                        Toast.makeText(UpdateSingleService.this, "Update Saved!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

//                startActivity(new Intent(MerchantHome.this, MerchantHome.class));

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {


                    }
                });

            }
        });


    }

    private void setClickListenerForDeleteButton() {
    deleteService.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Map<String, String> headers = new HashMap<>();
            headers.put("token", token);
            Portfolio portfolio = new Portfolio();
            PortfolioItem portfolioItem = new PortfolioItem();
            portfolioItem.itemId = Integer.parseInt(itemId);
            portfolioItem.price = -1;
            portfolioItem.discountPercentage = 0;
            portfolio.portfolioItems.add(portfolioItem);
            Call<LoginResponse> loginResponse = Api_endPoint.getService().putPortfolio(headers, portfolio);
            loginResponse.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d("tag", call.toString());
                    if (!response.isSuccessful()) {
                        Log.d("tag", response.toString());
                        return;
                    }

                    Log.d("tag", response.body().getMessage());
                    Log.d("tag", response.body().getResponseCode());

                    Intent intent;
                    intent = new Intent(UpdateSingleService.this, ViewMerchantServices.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Token", token);
                    intent.putExtra("MerchantId", MerchantId);
//                    Toast.makeText(UpdateSingleService.this, "Update Saved!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

//                startActivity(new Intent(MerchantHome.this, MerchantHome.class));

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {


                }
            });

        }
    });


    }
}