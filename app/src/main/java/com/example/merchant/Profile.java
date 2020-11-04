package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.merchantInfo.ImageArr;
import com.example.merchant.merchantInfo.Message;
import com.example.merchant.pojo.AddImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    LinearLayout mainLinearLayout;
    Message listData;
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
                    uploadImage(imageUri);
                }//do something with the image (save it to some directory or whatever you need to do with it here)

            } else if (data.getData() != null) {
                Log.d("count of images is", 1 + "");
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


}