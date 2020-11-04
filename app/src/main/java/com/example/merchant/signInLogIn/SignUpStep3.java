package com.example.merchant.signInLogIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;

import static com.sucho.placepicker.Constants.GOOGLE_API_KEY;

public class SignUpStep3 extends AppCompatActivity {
    String token, email, phone, password, name;
    TextView signUpStep3Continue, signUpStep3UseMyCurrentLocation, signUpStep3SelectOnMap;
    int PLACE_PICKER_REQUEST = 1;
    int step = 0;
    long systemTimeWhenPickerActivityStarted = 0;
    double latitude = -9999, longitude = -9999;
    private final int REQUEST_FINE_LOCATION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);
        token = getIntent().getStringExtra("Token");
        email = getIntent().getStringExtra("Email");
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
        password = getIntent().getStringExtra("Password");
        Log.d("Info from Step 2", token + "\n" + email + "\n" + phone + "\n" + password);

        signUpStep3Continue = (TextView) findViewById(R.id.signUpStep3Continue);
        signUpStep3UseMyCurrentLocation = (TextView) findViewById(R.id.signUpStep3UseMyCurrentLocation);
        signUpStep3SelectOnMap = (TextView) findViewById(R.id.signUpStep3SelectOnMap);

        setOnClickListenerForUseCurrentLocation();
        setOnClickListenerForSelectOnMap();
        setOnClickListenerForContinueButton();


    }

    private void setOnClickListenerForContinueButton() {
        signUpStep3Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude == -9999 || longitude == 9999) {
                    Toast.makeText(SignUpStep3.this, "Please Select the Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignUpStep3.this, SignUpStep4.class);
                intent.putExtra("Token", token);
                intent.putExtra("Email", email);
                intent.putExtra("Phone", phone);
                intent.putExtra("Name", name);
                intent.putExtra("Password", password);
                intent.putExtra("Longitude", longitude + "");
                intent.putExtra("Latitude", latitude + "");
                startActivity(intent);
            }
        });

    }

    static final int PICK_MAP_POINT_REQUEST = 999;

    private void setOnClickListenerForSelectOnMap() {
        signUpStep3SelectOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SignUpStep3.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignUpStep3.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    Toast.makeText(SignUpStep3.this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = null;
                try {
                    intent = new PlacePicker.IntentBuilder()// Initial Latitude and Longitude the Map will load into// Show Coordinates in the Activity
                            .build(SignUpStep3.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                systemTimeWhenPickerActivityStarted = System.currentTimeMillis();
                step++;
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });
    }

    private void setOnClickListenerForUseCurrentLocation() {
        signUpStep3UseMyCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SignUpStep3.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignUpStep3.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    Toast.makeText(SignUpStep3.this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
                    return;
                }

                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {


                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        System.out.println(latitude + " " + longitude + ";;");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("Status Changed", String.valueOf(status));
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d("Provider Enabled", provider);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d("Provider Disabled", provider);
                    }
                };

                // Now first make a criteria with your requirements
                // this is done to save the battery life of the device
                // there are various other other criteria you can search for..
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

                // Now create a location manager
                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                // This is the Best And IMPORTANT part
                final Looper looper = null;

                locationManager.requestSingleUpdate(criteria, locationListener, looper);
            }
        });


//        token = getIntent().getStringExtra("Token");
//        merchantEmail = getIntent().getStringExtra("Email");
//        Log.d("tagToken", token);

    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("tag", longitude + " " + latitude);
            System.out.println(latitude + " " + longitude + ";;");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Place place = PlacePicker.getPlace(data, this);
                System.out.println(place.getLatLng().latitude + "laa");
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                System.out.println(latitude + " " + longitude + ";;");

            } else if (data == null && System.currentTimeMillis() - systemTimeWhenPickerActivityStarted <= 2500) {
                Intent intent = null;
                try {
                    intent = new PlacePicker.IntentBuilder()// Initial Latitude and Longitude the Map will load into// Show Coordinates in the Activity
                            .build(SignUpStep3.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                step++;
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }

    }
}