package com.example.merchant.signInLogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.Api_call;
import com.example.merchant.LoginResponse;
import com.example.merchant.LoginUser;
import com.example.merchant.MerchantHome;
import com.example.merchant.R;
import com.example.merchant.pojo.VerifyUniqueDetail;
import com.example.merchant.pojo.VerifyUniqueDetailCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStep1 extends AppCompatActivity {


    TextView signUpStep1Continue;
    EditText signUpStep1NameOfKainchi, signUpStep1Phone, signUpStep1Email, signUpStep1Password, signUpStep1ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step1);
        signUpStep1Continue = (TextView) findViewById(R.id.signUpStep1Continue);
        signUpStep1NameOfKainchi = (EditText) findViewById(R.id.signUpStep1NameOfKainchi);
        signUpStep1Phone = (EditText) findViewById(R.id.signUpStep1Phone);
        signUpStep1Email = (EditText) findViewById(R.id.signUpStep1Email);
        signUpStep1Password = (EditText) findViewById(R.id.signUpStep1Password);
        signUpStep1ConfirmPassword = (EditText) findViewById(R.id.signUpStep1ConfirmPassword);

        step1ButtonContinueClickListener();


    }

    private void step1ButtonContinueClickListener() {
        signUpStep1Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (signUpStep1NameOfKainchi.getText().toString().equals("")) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Name of Kainchi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (signUpStep1Phone.getText().toString().equals("")) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Phone!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (signUpStep1Email.getText().toString().equals("")) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (signUpStep1Password.getText().toString().equals("")) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signUpStep1ConfirmPassword.getText().toString().equals("")) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Confirm Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!signUpStep1Password.getText().toString().equals(signUpStep1ConfirmPassword.getText().toString())) {
                    Toast.makeText(SignUpStep1.this, "Password did not matched!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calling the api to complete Step1 for Sign Up
                signUpStep1();

            }
        });


    }

    private void signUpStep1() {

        VerifyUniqueDetail verifyUniqueDetail = new VerifyUniqueDetail();
        verifyUniqueDetail.email = signUpStep1Email.getText().toString();
        verifyUniqueDetail.phone = "+91" + signUpStep1Phone.getText().toString().trim();
        Call<VerifyUniqueDetailCallback> loginResponse = Api_call.getService().verifyUniqueDetails(verifyUniqueDetail);
        loginResponse.enqueue(new Callback<VerifyUniqueDetailCallback>() {
            @Override
            public void onResponse(Call<VerifyUniqueDetailCallback> call, Response<VerifyUniqueDetailCallback> response) {

                if (!response.isSuccessful()) {
                    Log.d("tag", response.toString());
                    return;
                }

                Log.d("tag", response.toString());
                Log.d("tag", response.body().getMessage());
                Log.d("tag", response.headers().toString());
                Intent intent = new Intent(SignUpStep1.this, SignUpStep2.class);
                intent.putExtra("Token", response.body().token);
                intent.putExtra("Email", signUpStep1Email.getText().toString().trim());
                intent.putExtra("Name", signUpStep1NameOfKainchi.getText().toString().trim());
                intent.putExtra("Phone","+91"+ signUpStep1Phone.getText().toString().trim());
                intent.putExtra("Password", signUpStep1Password.getText().toString().trim());
                startActivity(intent);


            }

            @Override
            public void onFailure(Call<VerifyUniqueDetailCallback> call, Throwable t) {
                Toast.makeText(SignUpStep1.this, "failure", Toast.LENGTH_SHORT).show();


            }
        });


    }
}