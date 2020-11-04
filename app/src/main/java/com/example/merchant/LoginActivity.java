package com.example.merchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button submit;
    String emailText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        submit = (Button) findViewById(R.id.loginSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "inside");
                emailText = loginEmail.getText().toString();
                LoginUser loginUser = new LoginUser(loginEmail.getText().toString(), loginPassword.getText().toString(), "abcd");
                Call<LoginResponse> loginResponse = Api_call.getService().getLoginResponse(loginUser);
                loginResponse.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (!response.isSuccessful()) {
                            Log.d("tag", response.toString());
                            return;
                        }

                        Log.d("tag", response.toString());
                        Log.d("tag", response.body().getMessage());
                        Log.d("tag", response.headers().toString());
                        Intent intent = new Intent(LoginActivity.this, MerchantHome.class);
                        intent.putExtra("Token", response.headers().get("Token"));
                        intent.putExtra("Email", emailText);
                        startActivity(intent);


                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "failure", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });


    }
}