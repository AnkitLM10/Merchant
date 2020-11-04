package com.example.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.signInLogIn.SignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText signUpEmail, signUpPassword, signUpPhoneNumber, signUpName;
    RadioButton signUpMale, signUpFemale;
    TextView signUpDOB;
    Button signUpSubmit;
    RadioGroup signUpRadioGroup;
    DatePickerDialog picker;
    String gender;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, SignIn.class));


        signUpEmail = (EditText) findViewById(R.id.signUpEmail);
        signUpPassword = (EditText) findViewById(R.id.signUpPassword);
        signUpName = (EditText) findViewById(R.id.signUpName);
        signUpPhoneNumber = (EditText) findViewById(R.id.signUpPhoneNumber);

        signUpMale = (RadioButton) findViewById(R.id.signUpMale);
        signUpFemale = (RadioButton) findViewById(R.id.signUpFemale);
        signUpRadioGroup = (RadioGroup) findViewById(R.id.signUpRadioGroup);

        signUpDOB = (TextView) findViewById(R.id.signUpDob);
        signUpSubmit = (Button) findViewById(R.id.signUpSubmit);

        signUpRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                gender = rb.getText().toString();
                Log.d("button", i + " " + rb.getText());
            }
        });


        signUpDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                signUpDOB
                                        .setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


    }

    public void click(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        Toast.makeText(MainActivity.this, "called", Toast.LENGTH_SHORT).show();
        RegisterUser usr = new RegisterUser(signUpName.getText().toString(), gender, signUpEmail.getText().toString(), signUpPassword.getText().toString(), signUpPhoneNumber.getText().toString(), signUpDOB.getText().toString());
        post_details(usr);
    }

    private void post_details(RegisterUser usr) {

        //api call
        Call<LoginResponse> loginResponse = Api_call.getService().getSignupResponse(usr);
        loginResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    Log.d("tag", response.body().getMessage());
                } catch (Exception e) {
                    Log.d("tag", response.message());
                }
//                Intent intent = new Intent(Register.this, HomeScreen.class);
//                startActivity(intent);

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //TODO: action

            mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri file = (data.getData());
            final StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Toast.makeText(MainActivity.this, "File Uploaded", Toast.LENGTH_LONG).show();
                            String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            Log.d("imagePath", downloadUrl);

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("imagePath", uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }


    public void loginClick(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}