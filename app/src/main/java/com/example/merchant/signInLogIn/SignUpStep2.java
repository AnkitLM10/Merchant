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
import com.example.merchant.R;
import com.example.merchant.pojo.ValidateOtp;
import com.example.merchant.pojo.ValidateOtpCallback;
import com.example.merchant.pojo.VerifyUniqueDetailCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStep2 extends AppCompatActivity {

    String token, email, phone, password , name;
    TextView signUpStep2OtpText, signUpStep2Continue;
    EditText signUpStep2Otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
        token = getIntent().getStringExtra("Token");
        email = getIntent().getStringExtra("Email");
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
        password = getIntent().getStringExtra("Password");
        Log.d("Info from Step 1", token + "\n" + email + "\n" + phone + "\n" + password);

        signUpStep2OtpText = (TextView) findViewById(R.id.signUpStep2OtpText);
        signUpStep2Continue = (TextView) findViewById(R.id.signUpStep2Continue);
        signUpStep2Otp = (EditText) findViewById(R.id.signUpStep2Otp);
        signUpStep2OtpText.setText("An OTP has been sent to your number " + phone + ", please enter the OTP to continue");

        onClickListenerForContinueButton();
    }

    private void onClickListenerForContinueButton() {

        signUpStep2Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = signUpStep2Otp.getText().toString();
                if (otp.equals("")) {
                    Toast.makeText(SignUpStep2.this, "Please Enter Otp", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> headers = new HashMap<>();
                Log.d("header from Details", token);
                headers.put("token", token);

                ValidateOtp validateOtp = new ValidateOtp();
                validateOtp.otp = otp;

                Call<ValidateOtpCallback> loginResponse = Api_call.getService().validateOtp(headers, validateOtp);
                loginResponse.enqueue(new Callback<ValidateOtpCallback>() {
                    @Override
                    public void onResponse(Call<ValidateOtpCallback> call, Response<ValidateOtpCallback> response) {

                        if (!response.isSuccessful()) {
                            Toast.makeText(SignUpStep2.this, "Wrong Otp!", Toast.LENGTH_SHORT).show();
                            Log.d("tag", response.toString());
                            return;
                        }

                        Log.d("tag", response.toString());
                        Log.d("tag", response.body().getMessage());
                        Log.d("tag", response.headers().toString());
                        Intent intent = new Intent(SignUpStep2.this, SignUpStep3.class);
                        intent.putExtra("Token", response.body().token);
                        intent.putExtra("Email", email);
                        intent.putExtra("Name", name);
                        intent.putExtra("Phone", phone);
                        intent.putExtra("Password", password);
                        startActivity(intent);


                    }

                    @Override
                    public void onFailure(Call<ValidateOtpCallback> call, Throwable t) {
                        Toast.makeText(SignUpStep2.this, "failure", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });


    }
}