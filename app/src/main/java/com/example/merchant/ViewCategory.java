package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ViewCategory extends AppCompatActivity {
    String token = "";
    String MerchantId = "";
    List<String> listDataHeader;
    HashMap<String, List<Data>> listDataChild;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Set<Integer> servicesAlreadyOffered;
    HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        Log.d("tagToken", token);
        servicesAlreadyOffered = new TreeSet<>();
        getAllServiceAlreadyOffered();

        //getCategory();  // Calling this from ServiceAlreadyOffered.................
        // get the listview


    }

    private void getAllServiceAlreadyOffered() {
        serviceOfferedToDataMappingHashMap = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Log.d("MErchantID", MerchantId);
        Call<ServiceOffered> loginResponse = Api_endPoint.getService().getServicesOffered(MerchantId, headers);
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
                    servicesAlreadyOffered.add(response.body().data.get(i).service.serviceId);
                    serviceOfferedToDataMappingHashMap.put(response.body().data.get(i).service.serviceId, response.body().data.get(i));
                }


                //Calling Get Category From Here so That After we get All the Services Offered Then only we populate the ListView
                getCategory();

            }

            @Override
            public void onFailure(Call<ServiceOffered> call, Throwable t) {


            }
        });


    }

    public void getCategory() {
        System.out.println("going inside categoriessssss");
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Call<Category> loginResponse = Api_endPoint.getService().getAllCategory(headers);
        loginResponse.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }

                //Now get All Categories..........................

                HashMap<String, List<Data>> hashMap = new HashMap<>();
                HashMap<Integer, Data> serviceIdToData = new HashMap<>();

                for (int i = 0; i < response.body().data.size(); i++) {


                    String category = response.body().data.get(i).category;
                    Log.d("Category", category);

                    if (!hashMap.containsKey(category)) {
                        hashMap.put(category, new ArrayList<Data>());
                    }
                    hashMap.get(category).add(response.body().data.get(i));
                    serviceIdToData.put(response.body().data.get(i).serviceId, response.body().data.get(i));
                }

                listDataHeader = new ArrayList<>();
                for (String header : hashMap.keySet()) {
                    listDataHeader.add(header);
                    Log.d("ListDataHeader", header);
                }


                listDataChild = hashMap;
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                Button categorySelected = (Button) findViewById(R.id.categorySelected);

                listAdapter = new ExpandableListAdapter(ViewCategory.this, listDataHeader, listDataChild, categorySelected, serviceIdToData, token, servicesAlreadyOffered,serviceOfferedToDataMappingHashMap);

                // setting list adapter
                expListView.setAdapter(listAdapter);


            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {


            }
        });


    }

//
//    class CustomListView extends BaseAdapter {
//        int size = 0;
//        Context context;
//        List<Data> dataList;
//
//        class MyViewHolder {
//            TextView category;
//            TextView categoryDescription;
//
//            MyViewHolder(View view) {
//                category = (TextView) view.findViewById(R.id.category);
//                categoryDescription = (TextView) view.findViewById(R.id.categoryDescription);
//            }
//
//        }
//
//
//        CustomListView(int size, Context context, List<Data> dataList) {
//            this.size = size;
//            this.context = context;
//            this.dataList = dataList;
//        }
//
//        @Override
//        public int getCount() {
//            return size;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            View row = view;
//            CustomListView.MyViewHolder holder = null;
//            if (view == null) {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.category_main, null);
//                holder = new MyViewHolder(row);
//                row.setTag(holder);
//            } else
//                holder = (CustomListView.MyViewHolder) row.getTag();
//            holder.category.setText(dataList.get(i).category);
//            holder.categoryDescription.setText(dataList.get(i).categoryDescription);
//            return row;
//        }
//    }
}