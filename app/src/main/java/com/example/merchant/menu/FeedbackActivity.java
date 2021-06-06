package com.example.merchant.menu;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.merchant.MerchantHome;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.Api_call_merchant;
import com.example.merchant.Api_endPoint;
import com.example.merchant.MerchantHome;
import com.example.merchant.Profile;
import com.example.merchant.R;
import com.example.merchant.ServiceOffered;
import com.example.merchant.ViewCategory;
import com.example.merchant.ViewMerchantServices;
import com.example.merchant.bookingNewFragment;
import com.example.merchant.pojo.Review;
import com.example.merchant.pojo.retrieveAllFeedback;
import com.google.android.material.navigation.NavigationView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String MerchantId;
    private String MerchantName;
    private String token;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    TextView feedbackSalonTitle, feedbackAverageRating, feedbackNumberOfReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        MerchantName = getIntent().getStringExtra("MerchantName");
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
        feedbackSalonTitle = (TextView) findViewById(R.id.feedbackSalonTitle);
        feedbackAverageRating = (TextView) findViewById(R.id.feedbackAverageRating);
        feedbackNumberOfReviews = (TextView) findViewById(R.id.feedbackNumberOfReviews);
        setMenuDetails();
        getAllFeedback();

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
                Toast.makeText(FeedbackActivity.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(FeedbackActivity.this, Profile.class);
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
    private void getAllFeedback() {

        System.out.println("getting all the merchants!!");
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        Log.d("MErchantID", MerchantId);
        Call<retrieveAllFeedback> loginResponse = Api_call_merchant.getService().getFeedback(MerchantId, headers);
        loginResponse.enqueue(new Callback<retrieveAllFeedback>() {
            @Override
            public void onResponse(Call<retrieveAllFeedback> call, Response<retrieveAllFeedback> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }
                Log.d("ans", response.body().reviews.size() + ";;");
                listView = (ListView) findViewById(R.id.feedbackListView);
                CustomListView customListView = new CustomListView(response.body().reviews.size(), FeedbackActivity.this, response.body().reviews);
                listView.setAdapter(customListView);
                feedbackSalonTitle.setText("REVIEWS: " + MerchantName);
                feedbackNumberOfReviews.setText("Number of Reviews: " + response.body().reviews.size());
                double averageReview = 0;
                for (Review review : response.body().reviews) {
                    averageReview += review.rating;
                }
                DecimalFormat newFormat = new DecimalFormat("#.##");
                double twoDecimal = Double.valueOf(newFormat.format(averageReview / (response.body().reviews.size() * 1.0)));
                feedbackAverageRating.setText("Average rating: " + twoDecimal);

            }

            @Override
            public void onFailure(Call<retrieveAllFeedback> call, Throwable t) {
                System.out.println("getting all the merchants!!");
                System.out.println(t.getMessage() + ";;");

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(FeedbackActivity.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

                Toast.makeText(FeedbackActivity.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(FeedbackActivity.this, ViewMerchantServices.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);

                finish();

                break;

            case R.id.drawerEditUpdatePortfolio:

                Toast.makeText(FeedbackActivity.this, " Edit/Update Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(FeedbackActivity.this, ViewCategory.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);
                finish();
                break;

            case R.id.drawerCustomerFeedback:

//                Toast.makeText(FeedbackActivity.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
//                intent = new Intent(FeedbackActivity.this, FeedbackActivity.class);
//                intent.putExtra("Token", token);
//                intent.putExtra("MerchantId", MerchantId);
//                intent.putExtra("MerchantName", MerchantName);
//                startActivity(intent);
                break;

            case R.id.drawerContactUs:
                intent = new Intent(FeedbackActivity.this, ContactUs.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                finish();
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


    class CustomListView extends BaseAdapter {
        int size = 0;
        Context context;
        List<Review> reviews;
        boolean isExpanded[];
        boolean shouldBeExpanded[];
        private  SparseBooleanArray mCollapsedStatus;
        private  String[] sampleStrings;

        class MyViewHolder {
            TextView feedbackText;
//            TextView feedbackSeemore;
            RatingBar rating;
            int line = 0;
            boolean shouldSeeMoreBeShown;
            ExpandableTextView expandableTextView;
            MyViewHolder(View view) {
//                feedbackText = (TextView) view.findViewById(R.id.feedbackText);
//                feedbackSeemore = (TextView) view.findViewById(R.id.feedbackSeemore);
                expandableTextView = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                rating = (RatingBar) view.findViewById(R.id.rating);

            }

        }


        CustomListView(int size, Context context, List<Review> reviews) {
            this.size = size;
            this.context = context;
            this.reviews = reviews;
            isExpanded = new boolean[size];
            mCollapsedStatus = new SparseBooleanArray();
            Arrays.fill(isExpanded, true);
            shouldBeExpanded = new boolean[size];
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View row = view;
            CustomListView.MyViewHolder holder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.feedback_listview, null);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            } else
                holder = (CustomListView.MyViewHolder) row.getTag();


//            holder.feedbackText.setText(reviews.get(i).review);

            holder.expandableTextView.setText(reviews.get(i).review, mCollapsedStatus, i);

            holder.rating.setRating(reviews.get(i).rating);
            return row;
        }
    }


}
