package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceDetail extends AppCompatActivity {
    int pos = 0;

    Button addServiceTitleNumber;
    Button addServiceNext;
    TextView addServiceTitle;
    EditText addServicePrice;
    EditText addServiceDiscount;
    ArrayList<Data> listData;
    HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap;
    Portfolio portfolio;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portfolio = new Portfolio();
        token = getIntent().getStringExtra("Token");
        setContentView(R.layout.activity_add_service_detail);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listData = (ArrayList<Data>) args.getSerializable("ListData");
        serviceOfferedToDataMappingHashMap = (HashMap<Integer, ServiceDataList>) args.getSerializable("serviceOfferedToDataMappingHashMap");
        Log.d("ServiceMapping", serviceOfferedToDataMappingHashMap + "");
        for (Data d : listData)
            Log.d("from Add", d.service + " " + d.serviceCategoryId);

        pos = 0;

        addServiceTitleNumber = (Button) findViewById(R.id.addServiceTitleNumber);
        addServiceNext = (Button) findViewById(R.id.addServiceNext);
        addServiceTitle = (TextView) findViewById(R.id.addServiceTitle);
        addServicePrice = (EditText) findViewById(R.id.addServicePrice);
        addServiceDiscount = (EditText) findViewById(R.id.addServiceDiscount);
        addServiceTitleNumber.setText("Add SERVICE DETAILS " + (pos + 1) + "/" + listData.size());


        addServiceTitle.setText(listData.get(pos).service);

        //checking if This Service is already offered So that We can overwrite the value....................
        if (serviceOfferedToDataMappingHashMap.containsKey(listData.get(pos).serviceId)) {
            addServicePrice.setHint("Rs " + serviceOfferedToDataMappingHashMap.get(listData.get(pos).serviceId).price);
            addServiceDiscount.setHint(serviceOfferedToDataMappingHashMap.get(listData.get(pos).serviceId).discountPercentage + "%");
        }


        if (listData.size() - pos == 1)
            addServiceNext.setText("SAVE");
        else
            addServiceNext.setText("NEXT");

        addServiceNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos > listData.size()) {
                    //Out of Bound
                    return;
                }


                if (addServicePrice.getText().toString().equals("")) {
                    Toast.makeText(AddServiceDetail.this, "Please Enter Price...", Toast.LENGTH_SHORT).show();
                    return;
                }

                PortfolioItem portfolioItem = new PortfolioItem();
                portfolioItem.price = Integer.parseInt(addServicePrice.getText().toString());
                if (addServiceDiscount.getText().toString().equals(""))
                    portfolioItem.discountPercentage = 0;
                else
                    portfolioItem.discountPercentage = Integer.parseInt(addServiceDiscount.getText().toString());
                portfolioItem.itemId = listData.get(pos).serviceId;

                //Adding the Data To mainList
                portfolio.portfolioItems.add(portfolioItem);

                pos++;
                if (pos == listData.size()) {
                    //Means Sending data.......................
                    sendData();
                    return;

                }


                addServicePrice.setText("");
                addServiceDiscount.setText("");
                if (serviceOfferedToDataMappingHashMap.containsKey(listData.get(pos).serviceId)) {
                    addServicePrice.setHint("Rs " + serviceOfferedToDataMappingHashMap.get(listData.get(pos).serviceId).price);
                    addServiceDiscount.setHint(serviceOfferedToDataMappingHashMap.get(listData.get(pos).serviceId).discountPercentage + "%");
                } else {
                    addServicePrice.setHint("Rs");
                    addServiceDiscount.setHint("%");
                }

                addServiceTitleNumber.setText("Add SERVICE DETAILS " + (pos + 1) + "/" + listData.size());
                addServiceTitle.setText(listData.get(pos).service);
                if (listData.size() - pos == 1)
                    addServiceNext.setText("SAVE");
                else
                    addServiceNext.setText("NEXT");
            }
        });


//        addServicePrice.setText("Rs ");
//        Selection.setSelection(addServicePrice.getText(), addServicePrice.getText().length());
//
//
//        addServicePrice.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().startsWith("Rs ")) {
//                    addServicePrice.setText("Rs ");
//                    Selection.setSelection(addServicePrice.getText(), addServicePrice.getText().length());
//
//                }
//
//            }
//        });
//
//        addServiceDiscount.setText(" %");
//        addServiceDiscount.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                System.out.println(s.toString() + ";;");
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//
//            }
//        });


    }

    private void sendData() {


        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
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
                intent = new Intent(AddServiceDetail.this, MerchantHome.class);
                intent.putExtra("Token", token);
                startActivity(intent);
                finish();

//                startActivity(new Intent(MerchantHome.this, MerchantHome.class));

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {


            }
        });


    }
}