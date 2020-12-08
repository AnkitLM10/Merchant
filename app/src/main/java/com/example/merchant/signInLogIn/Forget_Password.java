package com.example.merchant.signInLogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchant.Api_call;
import com.example.merchant.R;
import com.example.merchant.pojo.ValidateOtpCallback;
import com.example.merchant.pojo.VerifyUniqueDetailCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forget_Password extends AppCompatActivity {


    EditText forgetPasswordEditText;
    TextView forgetPasswordContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        forgetPasswordEditText = (EditText) findViewById(R.id.forgetPasswordEditText);
        forgetPasswordContinue = (TextView) findViewById(R.id.forgetPasswordContinue);

        setOnClickListenerForContinueButton();


    }

    private void setOnClickListenerForContinueButton() {

        forgetPasswordContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailOrPhone = forgetPasswordEditText.getText().toString();
                if (emailOrPhone.equals("")) {
                    Toast.makeText(Forget_Password.this, "Please Enter either Email or Password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (emailOrPhone.contains("@")) {
                    callBackForEmail(emailOrPhone);
                } else {
                    callBackForPassword(emailOrPhone);
                }


            }

            private void callBackForEmail(final String email) {


                Call<ValidateOtpCallback> loginResponse = Api_call.getService().forgetPasswordEmail(email.trim());
                loginResponse.enqueue(new Callback<ValidateOtpCallback>() {
                    @Override
                    public void onResponse(Call<ValidateOtpCallback> call, Response<ValidateOtpCallback> response) {

                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }
                        System.out.println(response.body().token + "token");
                        System.out.println(response.body().message + "message");

                        Intent intent = new Intent(Forget_Password.this, SignUpStep2.class);
                        intent.putExtra("Token", response.body().token);
                        intent.putExtra("Email", email);
                        intent.putExtra("Name", "");
                        intent.putExtra("Phone", "");
                        intent.putExtra("Password", "");
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure(Call<ValidateOtpCallback> call, Throwable t) {
                        Toast.makeText(Forget_Password.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());

                    }
                });


            }


            private void callBackForPassword(String phone) {


                phone = phone.trim();
                if (!phone.startsWith("+91"))
                    phone = "+91" + phone;

                Call<ValidateOtpCallback> loginResponse = Api_call.getService().forgetPasswordEmail(phone);
                final String finalPhone = phone;
                loginResponse.enqueue(new Callback<ValidateOtpCallback>() {
                    @Override
                    public void onResponse(Call<ValidateOtpCallback> call, Response<ValidateOtpCallback> response) {

                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }
                        Intent intent = new Intent(Forget_Password.this, SignUpStep2.class);
                        intent.putExtra("Token", response.body().token);
                        intent.putExtra("Email", "");
                        intent.putExtra("Name", "");
                        intent.putExtra("Phone", finalPhone);
                        intent.putExtra("Password", "");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ValidateOtpCallback> call, Throwable t) {
                        Toast.makeText(Forget_Password.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());

                    }
                });


            }
        });


    }
}