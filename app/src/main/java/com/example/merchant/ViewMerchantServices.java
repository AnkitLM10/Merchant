package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMerchantServices extends AppCompatActivity {
    List<String> listDataHeader;
    String token = "";
    String MerchantId = "";
    HashMap<String, List<ServiceDataList>> listDataChild;
    ExpandableAdapterListServicesOfferedextends listAdapter;
    ExpandableListView expListView;
    Set<Integer> servicesAlreadyOffered;
    HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merchant_services);
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        Log.d("tagToken", token);
        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        // preparing list data
        getAllServiceAlreadyOffered();
// get the listview


    }

    private void getAllServiceAlreadyOffered() {
        serviceOfferedToDataMappingHashMap = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Log.d("MErchantID", MerchantId);
        Call<ServiceOffered> loginResponse = Api_endPoint.getService().getServicesOffered(headers);
        loginResponse.enqueue(new Callback<ServiceOffered>() {
            @Override
            public void onResponse(Call<ServiceOffered> call, Response<ServiceOffered> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }


                Log.d("AllServicesOffered", response.body().data.size() + "");

                for (int i = 0; i < response.body().data.size(); i++) {
//                    servicesAlreadyOffered.add(response.body().data.get(i).service.serviceId);
//                    serviceOfferedToDataMappingHashMap.put(response.body().data.get(i).service.serviceId, response.body().data.get(i));
//                    listDataChild.put(response.body().data.get(i).service.service,response.body().data.get(i));

                    if (!listDataChild.containsKey(response.body().data.get(i).category))
                        listDataChild.put(response.body().data.get(i).category, new ArrayList<ServiceDataList>());

                    listDataChild.get(response.body().data.get(i).category).add(response.body().data.get(i));
                }


                for (String key : listDataChild.keySet())
                    listDataHeader.add(key);


                expListView = (ExpandableListView) findViewById(R.id.lvExpServiceOffered);
                listAdapter = new ExpandableAdapterListServicesOfferedextends(ViewMerchantServices.this, listDataHeader, listDataChild,token,MerchantId);

                // setting list adapter
                expListView.setAdapter(listAdapter);

            }

            @Override
            public void onFailure(Call<ServiceOffered> call, Throwable t) {


            }
        });


    }
}