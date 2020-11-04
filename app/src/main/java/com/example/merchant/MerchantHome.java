package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.menu.FeedbackActivity;
import com.example.merchant.merchantInfo.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.collection.LLRBNode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static String token = "", emailText = "";
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    String MerchantId = null;
    String MerchantName = null;

    static List<Booking> upcomingBooking;
    static List<Booking> pastBooking;
    static List<Booking> newBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_home);
        token = getIntent().getStringExtra("Token");
        emailText = getIntent().getStringExtra("Email");
        Log.d("tagToken", token);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.merchantHomeNavbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Setting  Menu Item Click Listener.....................
        navigationView.setNavigationItemSelectedListener(this);

        //getting merchant Booking Details......................
        gettingMerchantBookingDetails();


        //getting merchant Details..............................
        gettingMerchantDetails();

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.merchantHomeNavbarContainer, new bookingFragment()).commit();


    }

    private void gettingMerchantBookingDetails() {

        upcomingBooking = new ArrayList<>();
        newBooking = new ArrayList<>();
        pastBooking = new ArrayList<>();


        System.out.println("inside booking merchant " + token);
        Map<String, String> headers = new HashMap<>();
        Log.d("header from Details", token);
        headers.put("token", token);
        Call<BookingResponse> loginResponse = Api_booking.getService().getAllBooking(headers);
        loginResponse.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }

                Log.d("tag", response.toString());
                Log.d("tagSize", response.body().bookings.size() + "");

                for (int i = 0; i < response.body().bookings.size(); i++) {
                    SimpleDateFormat
                            sdfo
                            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//2020-08-19T12:59:38.226+00:00

                    try {
                        String str1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(response.body().bookings.get(i).date);
                        Date d1 = sdfo.parse(str1);
                        Date date = new Date();
                        String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(date);
                        Date d2 = sdfo.parse(str);

                        System.out.println(i + " " + response.body().bookings.get(i).payableAmount);
                        if (d1.after(d2)) {
                            // means upcoming.....
                            System.out.println("New......");
                            if (response.body().bookings.get(i).status.equals("approve")) {
                                //means already approved......................
                                upcomingBooking.add(response.body().bookings.get(i));
                            } else {

                                newBooking.add(response.body().bookings.get(i));
                            }
                        } else if (d1.before(d2)) {
                            //means Past Booking.......
                            System.out.println("PAst......");
                            pastBooking.add(response.body().bookings.get(i));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("exxxxx");
                    }
                }
                System.out.println("coming outtttt.........");

//                Log.d("tag", response.body());


            }


            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getCause().getMessage());
                System.out.println("fail......... " + call.request().url() + "");
                System.out.println("fail......... " + call.toString() + "");

            }
        });


    }

    private void gettingMerchantDetails() {


        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Call<MerchantInfo> loginResponse = Api_call_merchant.getService().getMerchantDetail(headers);
        loginResponse.enqueue(new Callback<MerchantInfo>() {
            @Override
            public void onResponse(Call<MerchantInfo> call, final Response<MerchantInfo> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }

                Log.d("tag", response.toString());

                Log.d("tag", response.body().getMessage().merchantName);
                Log.d("tag", response.body().getMessage().merchantId + "");

                MerchantId = response.body().getMessage().merchantId + "";
                MerchantName = response.body().getMessage().merchantName + "";
//                Log.d("tag", response.body());


                //Setting menu items details...............

                View menuView = navigationView.getHeaderView(0);
                TextView email = (TextView) menuView.findViewById(R.id.menuEmail);
                email.setText(response.body().getMessage().email);

                TextView name = (TextView) menuView.findViewById(R.id.menuName);
                name.setText(response.body().getMessage().merchantName);

                TextView number = (TextView) menuView.findViewById(R.id.menuNumber);
                number.setText(response.body().getMessage().phoneNumber);

                TextView image = (TextView) menuView.findViewById(R.id.menuImageSize);
                image.setText(response.body().getMessage().imageArr.size() + " Image");



                menuView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MerchantHome.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MerchantHome.this, Profile.class);
                        intent.putExtra("Token", token);
                        Bundle args = new Bundle();
                        intent.putExtra("Token", token);
                        intent.putExtra("MerchantId", MerchantId);
                        args.putSerializable("Message", (Serializable) response.body().getMessage());
                        args.putSerializable("Image", (Serializable) response.body().getMessage().imageArr);
                        System.out.println(response.body().message.imageArr.size()+"sizeee");
                        intent.putExtra("BUNDLE", args);
                        startActivity(intent);
                    }
                });


//                startActivity(new Intent(MerchantHome.this, MerchantHome.class));

            }

            @Override
            public void onFailure(Call<MerchantInfo> call, Throwable t) {


            }
        });


    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.merchantHomeBookingsMenu:
                    fragment = new bookingFragment();
                    break;
                case R.id.merchantHomeProfileMenu:
                    fragment = new profileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.merchantHomeNavbarContainer, fragment).commit();
            return true;
        }
    };


    // This method to handle all the clicks on the Navigation Drawer menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(MerchantHome.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

                Toast.makeText(MerchantHome.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(MerchantHome.this, ViewMerchantServices.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);


                break;

            case R.id.drawerEditUpdatePortfolio:

                Toast.makeText(MerchantHome.this, " Edit/Update Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(MerchantHome.this, ViewCategory.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);
                break;

            case R.id.drawerCustomerFeedback:

                Toast.makeText(MerchantHome.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(MerchantHome.this, FeedbackActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantName);
                startActivity(intent);
                break;
//            case R.id.menuName:
//
//                Toast.makeText(MerchantHome.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();
////                intent = new Intent(MerchantHome.this, FeedbackActivity.class);
////                intent.putExtra("Token", token);
////                intent.putExtra("MerchantId", MerchantId);
////                intent.putExtra("MerchantName", MerchantName);
////                startActivity(intent);
//                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


//    public void getCategory(View view) {
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("token", token);
//        Call<Category> loginResponse = Api_endPoint.getService().getLoginResponse(headers);
//        loginResponse.enqueue(new Callback<Category>() {
//            @Override
//            public void onResponse(Call<Category> call, Response<Category> response) {
//                Log.d("tag", call.toString());
//                if (!response.isSuccessful()) {
//                    Log.d("tag", response.toString());
//                    return;
//                }
//                Log.d("tag", response.toString());
//                int size = response.body().data.size();
//                final List<Data> dataList = response.body().data;
//
//                for (int i = 0; i < size; i++) {
//                    Log.d("tagBody", response.body().data.get(i).categoryDescription);
//                }
//
//                ListView listView = (ListView) findViewById(R.id.listViewCategory);
//                CustomListView customListView = new CustomListView(dataList.size(), MerchantHome.this, dataList);
//                listView.setAdapter(customListView);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Log.d("tag","Clicked "+dataList.get(i).category);
//                        Intent intent = new Intent(MerchantHome.this, Merchant_Category_id.class);
//                        intent.putExtra("Token",token);
//                        intent.putExtra("Id",dataList.get(i).categoryId+"");
//                        startActivity(intent);
//
//
//
//                    }
//                });
//
////                startActivity(new Intent(MerchantHome.this, MerchantHome.class));
//
//            }
//
//            @Override
//            public void onFailure(Call<Category> call, Throwable t) {
//
//
//            }
//        });
//
//
//    }


    class CustomListView extends BaseAdapter {
        int size = 0;
        Context context;
        List<Data> dataList;

        class MyViewHolder {
            TextView category;
            TextView categoryDescription;

            MyViewHolder(View view) {
                category = (TextView) view.findViewById(R.id.category);
                categoryDescription = (TextView) view.findViewById(R.id.categoryDescription);
            }

        }


        CustomListView(int size, Context context, List<Data> dataList) {
            this.size = size;
            this.context = context;
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            MyViewHolder holder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.category_main, null);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            } else
                holder = (MyViewHolder) row.getTag();
            holder.category.setText(dataList.get(i).category);
//            holder.categoryDescription.setText(dataList.get(i).categoryDescription);
            return row;
        }
    }


}