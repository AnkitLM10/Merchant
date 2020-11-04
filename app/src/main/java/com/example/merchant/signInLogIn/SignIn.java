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
import com.example.merchant.LoginActivity;
import com.example.merchant.LoginResponse;
import com.example.merchant.LoginUser;
import com.example.merchant.MerchantHome;
import com.example.merchant.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    EditText signInPassword, signInEmail;
    TextView signInCreateNewAccount, signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInPassword = (EditText) findViewById(R.id.signInPassword);
        signInEmail = (EditText) findViewById(R.id.signInEmail);
        signInCreateNewAccount = (TextView) findViewById(R.id.signInCreateNewAccount);
        signInButton = (TextView) findViewById(R.id.signInButton);
        setOnCliclListenerForSignInButton();
        setOnCliclListenerForCreateNewAccount();


    }

    private void setOnCliclListenerForCreateNewAccount() {
        signInCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUpStep1.class);
                startActivity(intent);
            }
        });


    }

    private void setOnCliclListenerForSignInButton() {

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("tag", "inside");
                final String emailText = signInEmail.getText().toString();
                final String password = signInPassword.getText().toString();
                if (emailText.equals("")) {
                    Toast.makeText(SignIn.this, "Please Enter email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(SignIn.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                LoginUser loginUser = new LoginUser(emailText, password, "abcd");
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
                        Intent intent = new Intent(SignIn.this, MerchantHome.class);
                        intent.putExtra("Token", response.headers().get("Token"));
                        intent.putExtra("Email", emailText);
                        startActivity(intent);


                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(SignIn.this, "failure", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });
    }
}