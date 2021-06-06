package com.example.merchant.signInLogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.Api_ImageKit;
import com.example.merchant.Api_Upload;
import com.example.merchant.Api_call_merchant;
import com.example.merchant.LoginActivity;
import com.example.merchant.LoginResponse;
import com.example.merchant.Merchant;
import com.example.merchant.MerchantDetail;
import com.example.merchant.MerchantHome;
import com.example.merchant.R;
import com.example.merchant.pojo.ImageKitResponse;
import com.example.merchant.pojo.Signature;
import com.example.merchant.pojo.ImageKitPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStep4 extends AppCompatActivity {
    String token, email, phone, password, longitude, latitude, name;
    TextView signUpstep4ClickFromCamera, signUpstep4SelectFromGallery, signUpstep4Finish;
    private int SELECT_PICTURE = 1;
    private int SELECT_PICTURE_GALLERY = 1;
    private StorageReference mStorageRef;
    int imageCountDialog = 0, currentImageCount = 0;
    List<String> listImages;
    String image = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step4);
        token = getIntent().getStringExtra("Token");
        email = getIntent().getStringExtra("Email");
        phone = getIntent().getStringExtra("Phone");
        password = getIntent().getStringExtra("Password");
        longitude = getIntent().getStringExtra("Longitude");
        name = getIntent().getStringExtra("Name");
        latitude = getIntent().getStringExtra("Latitude");
        Log.d("Info from Step 2", token + "\n" + email + "\n" + phone + "\n" + password + "\n" + longitude + "\n" + latitude);
        signUpstep4ClickFromCamera = (TextView) findViewById(R.id.signUpstep4ClickFromCamera);
        signUpstep4SelectFromGallery = (TextView) findViewById(R.id.signUpstep4SelectFromGallery);
        signUpstep4Finish = (TextView) findViewById(R.id.signUpstep4Finish);
        listImages = new ArrayList<>();
        clickListenerForCameraCapture();
        clickListenerForGallery();
        clickListenerForFinish();


    }

    private void clickListenerForGallery() {


        signUpstep4SelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_GALLERY);

            }
        });

    }

    private void clickListenerForFinish() {
        signUpstep4Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "inside " + latitude + " " + longitude);

                MerchantDetail merchantDetail = new MerchantDetail(name, "unisex"
                        , email, phone, latitude
                        , longitude, listImages, password
                );
                Map<String, String> headers = new HashMap<>();
                Log.d("header from Details", token);
                headers.put("token", token);
                Call<LoginResponse> loginResponse = Api_call_merchant.getService().getLoginResponse(headers, merchantDetail);
                loginResponse.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d("tag", response.message());
                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }
                        Log.d("tag", response.toString());
                        Log.d("tag", response.body().getMessage());
                        Log.d("tag", response.headers().toString());
                        Intent intent = new Intent(SignUpStep4.this, MerchantHome.class);
                        intent.putExtra("Token", token);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(SignUpStep4.this, "failure", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });


    }

    private void clickListenerForCameraCapture() {
        signUpstep4ClickFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imageCountDialog = 0;
            currentImageCount = 0;
            dialog = new ProgressDialog(this); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading");
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            System.out.println(data.getData() + ";;");


            Bitmap photo = (Bitmap) data.getExtras().get("data");


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);


            System.out.println(tempUri + ";;");


            if (tempUri != null) {
                System.out.println("From Camera");
                imageCountDialog = 1;
                System.out.println(imageCountDialog + "count");
                Uri imageUri = tempUri;

//                uploadToImageKit(imageUri);
                uploadImage(imageUri);
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }


        //*********************************This is for multiple Gallery Iamges Selected!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
        else if (requestCode == SELECT_PICTURE_GALLERY && resultCode == RESULT_OK) {
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
//                    uploadToImageKit(imageUri);
                    uploadImage(imageUri);
                }//do something with the image (save it to some directory or whatever you need to do with it here)

            } else if (data.getData() != null) {
                imageCountDialog = 1;
                Uri imageUri = data.getData();
                uploadImage(imageUri);
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }


    }

    private void uploadToImageKit(Uri imageUri) {

        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            final String encodedImage = encodeImage(selectedImage);


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
                    Map<String, String> headers = new HashMap<>();
                    Log.d("header from Details", token);
                    headers.put("Content-Type", "application/x-www-form-urlencoded");

                    ImageKitPost imageKitPost = new ImageKitPost(System.currentTimeMillis() + "", signature, getResources().getString(R.string.imagekitKey),
                            token, expiry, email,"data:image/jpeg;base64,"+encodedImage);

//                    Call<ImageKitResponse> loginResponse = Api_ImageKit.getService().getUploadedImage(headers, );
//                    loginResponse.enqueue(new Callback<ImageKitResponse>() {
//                        @Override
//                        public void onResponse(Call<ImageKitResponse> call, Response<ImageKitResponse> response) {
//                            Log.d("tag", response.message());
//                            if (!response.isSuccessful()) {
//                                Log.d("tag", response.toString());
//                                return;
//                            }
//
//
//                            System.out.println(response.body().url+";urll");
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ImageKitResponse> call, Throwable t) {
//                            Toast.makeText(SignUpStep4.this, "failure", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    });


                }

                @Override
                public void onFailure(Call<Signature> call, Throwable t) {
                    Toast.makeText(SignUpStep4.this, "failure", Toast.LENGTH_SHORT).show();


                }
            });


        } catch (Exception e) {

        }


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void uploadImage(Uri imageUri) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        System.out.println(imageUri + "uri;;");
        Uri file = imageUri;
        final StorageReference riversRef = mStorageRef.child("images/" + "email" + "/" + System.currentTimeMillis() + ".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(SignUpStep4.this, "File Uploaded", Toast.LENGTH_LONG).show();
                        String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        currentImageCount++;
                        System.out.println(currentImageCount + " " + imageCountDialog + " debug");
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

}