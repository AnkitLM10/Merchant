package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.menu.ContactUs;
import com.example.merchant.menu.FeedbackActivity;
import com.example.merchant.merchantInfo.ImageArr;
import com.example.merchant.merchantInfo.Message;
import com.example.merchant.pojo.AddImages;
import com.example.merchant.pojo.ImageKitResponse;
import com.example.merchant.pojo.Signature;
import com.example.merchant.pojo.ImageKitPost;
import com.example.merchant.signInLogIn.SignUpStep4;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{

    private static final int SELECT_PICTURE = 1;
    LinearLayout mainLinearLayout;
    Message listData;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    List<ImageArr> listImageArr;
    TextView profileBooking;
    TextView profileAverageRating;
    TextView profileServiceOffered;
    TextView profileEmail;
    TextView profilePhone;
    ImageView profileAddImage;
    ProgressDialog dialog;
    String MerchantId = null;
    String token = null;
    List<String> imageUrlList;
    int imageCountDialog = 0, currentImageCount = 0;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listData = (Message) args.getSerializable("Message");
        token = getIntent().getStringExtra("Token");
        MerchantId = getIntent().getStringExtra("MerchantId");
        listImageArr = (List<ImageArr>) args.getSerializable("Image");
        System.out.println(listImageArr.size() + "osize");
        mainLinearLayout = (LinearLayout) findViewById(R.id.imageViewLinearLayout);
        profileBooking = (TextView) findViewById(R.id.profileBooking);
        profileAverageRating = (TextView) findViewById(R.id.profileAverageRating);
        profileServiceOffered = (TextView) findViewById(R.id.profileServiceOffered);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profilePhone = (TextView) findViewById(R.id.profilePhone);
        profileAddImage = (ImageView) findViewById(R.id.profileAddImage);

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

        //Adding Image Adding Button
        addImageClick();

        // not written   -->  profileBooking.setText(listData.);
        // not written   -->  profileServiceOffered.setText(listData.);

        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal = Double.valueOf(newFormat.format(listData.averageRating));
        profileAverageRating.setText("AVERAGE RATING: " + twoDecimal);

        profileEmail.setText("Email:\t\t" + listData.email);
        profilePhone.setText("Phone:\t\t" + listData.phoneNumber);

        //To populate ImageView Accordingly.......................................
        populateLinearLayoutWithImages();


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





    }
    private void populateLinearLayoutWithImages() {
        int size = listImageArr.size();
        int index = 0;
        for (int i = 1; i <= (int) Math.ceil(size / 3.0); i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 500);
            linearLayout.setWeightSum(3.0f);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutParams);

            for (int j = 1; j <= 3; j++) {
                if (index == size)
                    break;
                ImageView imageView = new ImageView(this);
                layoutParams = new LinearLayout.LayoutParams(0, 500);
                layoutParams.weight = 1.0f;
                imageView.setLayoutParams(layoutParams);
                System.out.println("image address " + listImageArr.get(index).imageAddress);
                Picasso.with(Profile.this).load(listImageArr.get(index).imageAddress).fit().into(imageView);
                linearLayout.addView(imageView);
                index++;
            }
            mainLinearLayout.addView(linearLayout);
        }
    }

    private void addImageClick() {
        profileAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            imageCountDialog = 0;
            imageUrlList = new ArrayList<>();
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
                    uploadToImageKit(imageUri);
//                    uploadImage(imageUri);
                }//do something with the image (save it to some directory or whatever you need to do with it here)

            } else if (data.getData() != null) {
                Log.d("count of images is", 1 + "");
                imageCountDialog = 1;
                Uri imageUri = data.getData();
                uploadToImageKit(imageUri);
//                uploadImage(imageUri);
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

    public void uploadImage(Uri imageUri) {

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Uri file = imageUri;
        final StorageReference riversRef = mStorageRef.child("images/" + listData.email + "/" + System.currentTimeMillis() + ".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(Profile.this, "File Uploaded", Toast.LENGTH_LONG).show();
                        String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        currentImageCount++;
                        if (currentImageCount == imageCountDialog)
                            dialog.dismiss();
                        Log.d("imagePath", downloadUrl);

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("imagePath", uri.toString());
                                imageUrlList.add(uri.toString());
                                if (currentImageCount == imageCountDialog) {


                                    // Till here every Image is uploaded hence lets upload it To our Database..........

                                    uploadImageToDB();
                                    dialog.dismiss();
                                }
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
                        if (currentImageCount == imageCountDialog) {
                            dialog.dismiss();
                        }

                    }
                });
    }

    private void uploadImageToDB() {

        Map<String, String> headers = new HashMap<>();
        Log.d("header from Details", token);
        headers.put("token", token);
        AddImages addImages = new AddImages();
        addImages.imageUrl = imageUrlList;
        Log.d("Image Object Sending", addImages.toString());
        Call<LoginResponse> loginResponse = Api_call_merchant.getService().addImages(MerchantId, headers, addImages);
        loginResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("tagImg", call.toString());
                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }


                // Here we are Adding List Image Array to the main Array..........
                // Need to discuss With AG bhai about Return Type of the Whole Document till then dummy addition

                List<ImageArr> dummy = new ArrayList<>();
                for (int i = 0; i < imageUrlList.size(); i++) {
                    ImageArr imageArr = new ImageArr();
                    imageArr.imageAddress = imageUrlList.get(i);
                    imageArr.imageId = 1;
                    dummy.add(imageArr);
                }
                listImageArr.addAll(dummy);
                mainLinearLayout.removeAllViews();
                populateLinearLayoutWithImages();


                Log.d("tagImages", response.toString());


            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getCause().getMessage());
                System.out.println("fail......... " + call.request().url() + "");
                System.out.println("fail......... " + call.toString() + "");

            }
        });

    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void uploadToImageKit(Uri imageUri) {





        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            final String encodedImage = encodeImage(selectedImage);
            System.out.println(encodedImage+"encodeeed");

            Map<String, String> headers = new HashMap<>();
            Log.d("header from Details", token);
            headers.put("token", token);
            Call<Signature> loginResponse = Api_Upload.getService().getSignature(headers);
            loginResponse.enqueue(new Callback<Signature>() {
                @Override
                public void onResponse(Call<Signature> call, Response<Signature> response) {
                    Log.d("tag", response.message());
                    if (!response.isSuccessful()) {
                        Log.d("tag", response.toString());
                        return;
                    }

                    String token = response.body().token;
                    String signature = response.body().signature;
                    long expiry = response.body().expiry;

                    System.out.println("tokenn " + token + " " + signature + " " + expiry);


                    // Now hitting the ImageKit URI
//                    Map<String, String> headers = new HashMap<>();
//                    Log.d("header from Details", token);
//                    headers.put("Content-Type", "application/x-www-form-urlencoded");

//
//                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                    builder.setType(MultipartBody.FORM)
//                            .addFormDataPart("file", "data:image/jpeg;base64," + encodedImage)
//                            .addFormDataPart("fileName", System.currentTimeMillis() + ".png")
//                            .addFormDataPart("tags", "Simple")
//                            .addFormDataPart("signature", signature)
//                            .addFormDataPart("publicKey", getResources().getString(R.string.imagekitKey))
//                            .addFormDataPart("token", token)
//                            .addFormDataPart("expire", expiry + "")
//                            .addFormDataPart("folder", listData.email+"/")
//                            .build();


                    //RequestBody requestBody = builder.build();
//                    RequestBody req = builder.build();

//                    ImageKitResponse imageKitPost = new ImageKitResponse(System.currentTimeMillis() + "", signature, getResources().getString(R.string.imagekitKey),
//                            token, expiry, listData.email, "data:image/jpeg;base64," + encodedImage);



                    Call<ImageKitResponse> loginResponse = Api_ImageKit.getService().getUploadedImage("data:image/jpeg;base64," +encodedImage,System.currentTimeMillis() + ".jpg",
                            "Simple",
                            signature,
                            "public_ks+Di7EZRiH4Yd8qP0b9UPnEIOs=",
                            token,
                            expiry+"",
                            listData.email+"/");



//                    Call<ImageKitResponse> loginResponse = Api_ImageKit.getService().getUploadedImage("data:image/jpeg;base64," + encodedImage,
//                            System.currentTimeMillis() + ".jpg",
//                            "Simple",
//                            signature,
//                            "public_ks+Di7EZRiH4Yd8qP0b9UPnEIOs=",
//                            token,
//                            expiry,
//                            listData.email+"/"
//                    );

//                    Call<ImageKitResponse> loginResponse = Api_ImageKit.getService().getUploadedImage(requestBody);

                    loginResponse.enqueue(new Callback<ImageKitResponse>() {
                        @Override
                        public void onResponse(Call<ImageKitResponse> call, Response<ImageKitResponse> response) {
                            Log.d("tag", response.message());
                            if (!response.isSuccessful()) {
                                Log.d("tag", response.toString());
                                return;
                            }


                            System.out.println(response.body().url + ";urll");

                        }

                        @Override
                        public void onFailure(Call<ImageKitResponse> call, Throwable t) {
                            Toast.makeText(Profile.this, "failure", Toast.LENGTH_SHORT).show();


                        }
                    });


                }

                @Override
                public void onFailure(Call<Signature> call, Throwable t) {
                    Toast.makeText(Profile.this, "failure", Toast.LENGTH_SHORT).show();


                }
            });


        } catch (Exception e) {

        }


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.drawerViewPortfolio:
                if (MerchantId == null) {
                    Toast.makeText(Profile.this, "Please Try again Later!!", Toast.LENGTH_LONG).show();
                    return false;
                }

                Toast.makeText(Profile.this, " View Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(Profile.this, ViewMerchantServices.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                finish();
                startActivity(intent);


                break;

            case R.id.drawerEditUpdatePortfolio:

                Toast.makeText(Profile.this, " Edit/Update Portfolio Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(Profile.this, ViewCategory.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                finish();
                startActivity(intent);
                break;

            case R.id.drawerCustomerFeedback:

                Toast.makeText(Profile.this, "Feedback Clicked!", Toast.LENGTH_LONG).show();
                intent = new Intent(Profile.this, FeedbackActivity.class);
                intent.putExtra("Token", token);
                intent.putExtra("MerchantId", MerchantId);
                intent.putExtra("MerchantName", MerchantHome.mainName);
                startActivity(intent);
                finish();
                break;

            case R.id.drawerContactUs:
                intent = new Intent(Profile.this, ContactUs.class);
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
}