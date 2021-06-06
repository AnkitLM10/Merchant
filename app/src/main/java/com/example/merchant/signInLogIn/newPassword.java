package com.example.merchant.signInLogIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.merchant.Api_call;
import com.example.merchant.R;
import com.example.merchant.pojo.NewPassword;
import com.example.merchant.pojo.ValidateOtpCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class newPassword extends AppCompatActivity {


    EditText newPasswordEditText;
    EditText confirmPasswordEditText;
    TextView newPasswordContinue;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        newPasswordContinue = (TextView) findViewById(R.id.newPasswordContinue);
        token = getIntent().getStringExtra("Token");
        setOnClickListenerForContinueButton();


    }

    private void setOnClickListenerForContinueButton() {

        newPasswordContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (newPassword.equals("")) {
                    Toast.makeText(newPassword.this, "Please Enter The New Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length()<8) {
                    Toast.makeText(newPassword.this, "Please Enter atleast 8 Character!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!confirmPassword.equals(newPassword))
                {
                    Toast.makeText(newPassword.this, "Password did not Match!", Toast.LENGTH_SHORT).show();
                    return;
                }



                NewPassword newPassword1 = new NewPassword();
                newPassword1.password = newPassword;
                Map<String, String> headers = new HashMap<>();
                Log.d("header from Details", token);
                headers.put("token", token);


                Call<ValidateOtpCallback> loginResponse = Api_call.getService().newPassword(headers, newPassword1);
                loginResponse.enqueue(new Callback<ValidateOtpCallback>() {
                    @Override
                    public void onResponse(Call<ValidateOtpCallback> call, Response<ValidateOtpCallback> response) {

                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }
                        Toast.makeText(newPassword.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    @Override
                    public void onFailure(Call<ValidateOtpCallback> call, Throwable t) {
                        Toast.makeText(newPassword.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());

                    }
                });


            }
        });
    }
}