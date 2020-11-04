package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Merchant extends AppCompatActivity {
    private int SELECT_PICTURE = 1;
    private StorageReference mStorageRef;
    ProgressDialog dialog;
    private final int REQUEST_FINE_LOCATION = 1234;
    EditText merchantName, merchantType, email, phoneNumber, password;
    String image = "";
    String token = "";
    String merchantEmail;
    List<String> listImages;
    int imageCountDialog = 0, currentImageCount = 0;
    Location location;
    LocationManager lm;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        merchantName = (EditText) findViewById(R.id.merchantDetailName);
        merchantType = (EditText) findViewById(R.id.merchantDetailType);
        email = (EditText) findViewById(R.id.merchantDetailEmail);
        listImages = new ArrayList<>();
        password = (EditText) findViewById(R.id.merchantDetailPassword);
        phoneNumber = (EditText) findViewById(R.id.merchantDetailPhoneNumber);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                latitude = String.valueOf(location.getLatitude());
                longitude = (String.valueOf(location.getLongitude()));
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

//        token = getIntent().getStringExtra("Token");
//        merchantEmail = getIntent().getStringExtra("Email");
//        Log.d("tagToken", token);

    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude() + "";
            latitude = location.getLatitude() + "";
            Log.d("tag", longitude + " " + latitude);
        }
    };

    public void SetImage(View view) {

        if (email.getText().toString().equals("") || email == null) {
            Toast.makeText(Merchant.this, "Please Enter the Email First", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            imageCountDialog = 0;
            currentImageCount = 0;
            dialog = new ProgressDialog(this); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading");
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                Log.d("count of images is", count + "");
                imageCountDialog = count;
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uploadImage(imageUri);
                }//do something with the image (save it to some directory or whatever you need to do with it here)

            } else if (data.getData() != null) {
                imageCountDialog = 1;
                Uri imageUri = data.getData();
                uploadImage(imageUri);
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
//            mStorageRef = FirebaseStorage.getInstance().getReference();
//            dialog = new ProgressDialog(this); // this = YourActivity
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.setTitle("Loading");
//            dialog.setMessage("Loading. Please wait...");
//            dialog.setIndeterminate(true);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//            Uri file = (data.getData());
//            final StorageReference riversRef = mStorageRef.child("images/"+ merchantEmail +".jpg");
//
//            riversRef.putFile(file)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
//                            Toast.makeText(Merchant.this, "File Uploaded", Toast.LENGTH_LONG).show();
//                            String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            dialog.dismiss();
//                            Log.d("imagePath", downloadUrl);
//
//                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Log.d("imagePath", uri.toString());
//                                    image = uri.toString();
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            // ...
//                            dialog.dismiss();
//                        }
//                    });
        }
    }


    public void merchantDetailSubmit(View view) {


        Log.d("tag", "inside " + latitude + " " + longitude);

        MerchantDetail merchantDetail = new MerchantDetail(merchantName.getText().toString(), merchantType.getText().toString()
                , email.getText().toString(), phoneNumber.getText().toString(), latitude
                , longitude, listImages, password.getText().toString()
        );
        Map<String, String> headers = new HashMap<>();
        Log.d("header from Details", token);
        headers.put("token", token);
        Call<LoginResponse> loginResponse = Api_call_merchant.getService().getLoginResponse(headers,merchantDetail);
        loginResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("tag", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }
                Log.d("tag", response.toString());
                Log.d("tag", response.body().getMessage());
                Log.d("tag", response.headers().toString());
                Intent intent = new Intent(Merchant.this, LoginActivity.class);
                intent.putExtra("Token", token);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Merchant.this, "failure", Toast.LENGTH_SHORT).show();


            }
        });


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


    public void loginClicking(View view) {
        Intent intent = new Intent(Merchant.this, LoginActivity.class);
        startActivity(intent);
    }


    public void uploadImage(Uri imageUri) {

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Uri file = imageUri;
        final StorageReference riversRef = mStorageRef.child("images/" + email.getText().toString() + "/" + System.currentTimeMillis() + ".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(Merchant.this, "File Uploaded", Toast.LENGTH_LONG).show();
                        String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        currentImageCount++;
                        if (currentImageCount == imageCountDialog)
                            dialog.dismiss();
                        Log.d("imagePath", downloadUrl);

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("imagePath", uri.toString());
                                image = uri.toString();
                                listImages.add(image);
                                if (currentImageCount == imageCountDialog)
                                    dialog.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        currentImageCount++;
                        if (currentImageCount == imageCountDialog)
                            dialog.dismiss();

                    }
                });
    }


}