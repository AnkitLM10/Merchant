package com.example.merchant.menu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.merchant.MerchantHome;
import com.example.merchant.Profile;
import com.example.merchant.R;
import com.example.merchant.ViewCategory;
import com.example.merchant.ViewMerchantServices;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

public class ContactUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String token = "";
    String MerchantId = "";
    androidx.appcompat.widget.AppCompatImageView twitter, instagram;
    TextView feedback, rateUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");

        feedback = (TextView) findViewById(R.id.feedback);
        rateUs = (TextView) findViewById(R.id.rateUs);
        twitter = (androidx.appcompat.widget.AppCompatImageView) findViewById(R.id.twitter);
        instagram = (androidx.appcompat.widget.AppCompatImageView) findViewById(R.id.instagram);


        setOnClickListenerForFeedbackButtonClick();
        setOnClickListenerForRateUsButtonClick();
        setOnClickListenerForInstagramButtonClick();
        setOnClickListenerForTwitterButtonClick();


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
    }

    private void setOnClickListenerForTwitterButtonClick() {
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String twitter_user_name = "BookMyKainchi";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
                }
            }
        });


    }

    private void setOnClickListenerForInstagramButtonClick() {
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/BookMyKainchi");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/BookMyKainchi")));
                }
            }
        });


    }

    private void setOnClickListenerForRateUsButtonClick() {

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To be implemented after app is deployed on Android app play Store
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "APP_PNAME")));
            }
        });

    }

    private void setOnClickListenerForFeedbackButtonClick() {
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contact@bookmykainchi.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });


    }

    private void setMenuDetails() {

        View menuView = navigationView.getHeaderView(0);
        TextView email = (TextView) menuView.findViewById(R.id.menuEmail);
        email.setText(MerchantHome.emailText);


        TextView name = (TextView) menuView.findViewById(R.id.menuName);
        name.setText(MerchantHome.mainName);

        TextView number = (TextView) menuView.findViewById(R.id.menuNumber);
        number.setText(MerchantHome.mainNumber);


        TextView image = (TextView) menuView.findViewById(R.id.menuImageSize);
        image.setText(MerchantHome.mainImage + " Images");


        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactUs.this, "Menu Name Clicked Clicked!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ContactUs.this, Profile.class);
                intent.putExtra("Token", token);
                Bundle args = new Bundle();
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                args.putSerializable("Message", (Serializable) MerchantHome.mainMessage);
                args.putSerializable("Image", (Serializable) MerchantHome.imageArr);

                intent.putExtra("BUNDLE", args);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(ContactUs.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

                Toast.makeText(ContactUs.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ContactUs.this, ViewMerchantServices.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);


                break;

            case R.id.drawerEditUpdatePortfolio:

                Toast.makeText(ContactUs.this, " Edit/Update Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ContactUs.this, ViewCategory.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                startActivity(intent);
                finish();

                break;

            case R.id.drawerCustomerFeedback:

                Toast.makeText(ContactUs.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(ContactUs.this, FeedbackActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                finish();

                break;

            case R.id.drawerContactUs:
//                intent = new Intent(ContactUs.this, ContactUs.class);
//                intent.putExtra("Token", token);
//                intent.putExtra("MerchantId", MerchantId);
//                intent.putExtra("MerchantName", MerchantHome.mainName);
//                startActivity(intent);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                finish();
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