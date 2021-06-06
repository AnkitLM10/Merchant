package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
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

import com.example.merchant.menu.ContactUs;
import com.example.merchant.menu.FeedbackActivity;
import com.google.android.material.navigation.NavigationView;

public class ViewCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String token = "";
    String MerchantId = "";
    List<String> listDataHeader;
    HashMap<String, List<Data>> listDataChild;
    ExpandableListAdapter listAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ExpandableListView expListView;
    Set<Integer> servicesAlreadyOffered;
    HashMap<Integer, ServiceDataList> serviceOfferedToDataMappingHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Setting  Menu Item Click Listener.....................
        navigationView.setNavigationItemSelectedListener(this);
        
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        Log.d("tagToken", token);
        servicesAlreadyOffered = new TreeSet<>();

        setMenuDetails();
        
        getAllServiceAlreadyOffered();

        //getCategory();  // Calling this from ServiceAlreadyOffered.................
        // get the listview


    }

    private void setMenuDetails() {

        View menuView = navigationView.getHeaderView(0);
        TextView email = (TextView) menuView.findViewById(R.id.menuEmail);
        email.setText( MerchantHome.emailText);


        TextView name = (TextView) menuView.findViewById(R.id.menuName);
        name.setText(MerchantHome.mainName);

        TextView number = (TextView) menuView.findViewById(R.id.menuNumber);
        number.setText(MerchantHome.mainNumber);


        TextView image = (TextView) menuView.findViewById(R.id.menuImageSize);
        image.setText(MerchantHome.mainImage+" Images");



        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewCategory.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ViewCategory.this, Profile.class);
                intent.putExtra("Token", token);
                Bundle args = new Bundle();
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                args.putSerializable("Message", (Serializable) MerchantHome.mainMessage);
                args.putSerializable("Image", (Serializable)MerchantHome.imageArr);

                intent.putExtra("BUNDLE", args);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getAllServiceAlreadyOffered() {
        serviceOfferedToDataMappingHashMap = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Log.d("MErchantID", MerchantId);
        Call<ServiceOffered> loginResponse = Api_endPoint.getService().getServicesOffered( headers);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(ViewCategory.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

                Toast.makeText(ViewCategory.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ViewCategory.this, ViewMerchantServices.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);

                finish();

                break;

            case R.id.drawerEditUpdatePortfolio:

                break;

            case R.id.drawerCustomerFeedback:

                Toast.makeText(ViewCategory.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ViewCategory.this, FeedbackActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                finish();

                break;
//            case R.id.menuName:
//
//                Toast.makeText(ViewCategory.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();
////                intent = new Intent(ViewCategory.this, FeedbackActivity.class);
////                intent.putExtra("Token", token);
////                intent.putExtra("MerchantId", MerchantId);
////                intent.putExtra("MerchantName", MerchantName);
////                startActivity(intent);
//                break;
            case R.id.drawerContactUs:
                intent = new Intent(ViewCategory.this, ContactUs.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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