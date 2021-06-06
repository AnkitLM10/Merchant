package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.menu.ContactUs;
import com.example.merchant.menu.FeedbackActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMerchantServices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    List<String> listDataHeader;
    String token = "";
    String MerchantId = "";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    HashMap<String, List<ServiceDataList>> listDataChild;
    ExpandableAdapterListServicesOffered listAdapter;
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

        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Setting  Menu Item Click Listener.....................
        navigationView.setNavigationItemSelectedListener(this);

        setMenuDetails();
        
        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        // preparing list data
        getAllServiceAlreadyOffered();
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
                Toast.makeText(ViewMerchantServices.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ViewMerchantServices.this, Profile.class);
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
                listAdapter = new ExpandableAdapterListServicesOffered(ViewMerchantServices.this, listDataHeader, listDataChild,token,MerchantId);

                // setting list adapter
                expListView.setAdapter(listAdapter);

            }

            @Override
            public void onFailure(Call<ServiceOffered> call, Throwable t) {


            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(ViewMerchantServices.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

//                Toast.makeText(ViewMerchantServices.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
//                intent = new Intent(ViewMerchantServices.this, ViewMerchantServices.class);
//                intent.putExtra("Token", token);
//                intent.putExtra("MerchantId", MerchantId);
//                startActivity(intent);
//

                break;

            case R.id.drawerEditUpdatePortfolio:

                Toast.makeText(ViewMerchantServices.this, " Edit/Update Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ViewMerchantServices.this, ViewCategory.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);
                finish();

                break;

            case R.id.drawerCustomerFeedback:

                Toast.makeText(ViewMerchantServices.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ViewMerchantServices.this, FeedbackActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                finish();

                break;

            case R.id.drawerContactUs:
                intent = new Intent(ViewMerchantServices.this, ContactUs.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

//            case R.id.menuName:
//
//                Toast.makeText(ViewMerchantServices.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();
////                intent = new Intent(ViewMerchantServices.this, FeedbackActivity.class);
////                intent.putExtra("Token", token);
////                intent.putExtra("MerchantId", MerchantId);
////                intent.putExtra("MerchantName", MerchantName);
////                startActivity(intent);
//                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}