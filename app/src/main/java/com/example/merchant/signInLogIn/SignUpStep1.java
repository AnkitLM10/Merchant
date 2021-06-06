package com.example.merchant.signInLogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    androidx.appcompat.widget.AppCompatSpinner spinner;

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
        spinner = (androidx.appcompat.widget.AppCompatSpinner) findViewById(R.id.spinner);


        String codes[] = {"+ 345", "+1 242", "+1 246", "+1 264", "+1 268", "+1 268", "+1 284", "+1 340", "+1 441", "+1 473", "+1 649", "+1 670", "+1 671", "+1 684", "+1 767", "+1 787", "+1 808", "+1 808", "+1 809", "+1 868", "+1 869", "+1 876", "+1", "+1", "+1664", "+20", "+212", "+213", "+216", "+218", "+220", "+221", "+222", "+223", "+224", "+225", "+226", "+227", "+228", "+229", "+230", "+231", "+232", "+233", "+234", "+235", "+236", "+237", "+238", "+240", "+241", "+242", "+243", "+244", "+245", "+246", "+246", "+247", "+248", "+249", "+250", "+251", "+253", "+254", "+255", "+256", "+257", "+261", "+262", "+262", "+264", "+265", "+266", "+267", "+268", "+269", "+27", "+291", "+297", "+298", "+299", "+30", "+31", "+32", "+33", "+34", "+350", "+351", "+352", "+353", "+354", "+355", "+356", "+358", "+359", "+36", "+370", "+371", "+372", "+373", "+374", "+375", "+376", "+377", "+378", "+380", "+381", "+382", "+385", "+386", "+387", "+389", "+39", "+40", "+41", "+420", "+421", "+423", "+43", "+44", "+45", "+46", "+47", "+48", "+49", "+500", "+500", "+501", "+502", "+503", "+504", "+505", "+506", "+507", "+509", "+51", "+52", "+53", "+537", "+54", "+55", "+56", "+56", "+57", "+58", "+590", "+591", "+593", "+594", "+595", "+595", "+596", "+596", "+597", "+598", "+599", "+599", "+60", "+61", "+61", "+61", "+62", "+63", "+64", "+65", "+66", "+670", "+670", "+672", "+672", "+673", "+674", "+675", "+676", "+677", "+678", "+679", "+680", "+681", "+682", "+683", "+685", "+686", "+687", "+688", "+689", "+690", "+691", "+692", "+7 7", "+7 840", "+7", "+81", "+82", "+84", "+850", "+852", "+853", "+855", "+856", "+86", "+880", "+886", "+90", "+91", "+92", "+93", "+94", "+95", "+960", "+961", "+962", "+963", "+964", "+965", "+966", "+967", "+968", "+970", "+971", "+972", "+973", "+974", "+975", "+976", "+977", "+98", "+992", "+993", "+994", "+995", "+996", "+998"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, codes);
//        spinner.getBackground().setColorFilter(Color.parseColor("#AA7744"), PorterDuff.Mode.SRC_ATOP);

        spinner.setAdapter(adapter);
        spinner.setSelection(204);
        step1ButtonContinueClickListener();


    }

    private void step1ButtonContinueClickListener() {
        signUpStep1Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Value",spinner.getSelectedItem().toString());

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


                if (signUpStep1Password.getText().toString().length() < 8) {
                    Toast.makeText(SignUpStep1.this, "Please Enter Password of atleast 8 Character!", Toast.LENGTH_SHORT).show();
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
        verifyUniqueDetail.phone = spinner.getSelectedItem().toString() + signUpStep1Phone.getText().toString().trim();
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
                intent.putExtra("Phone", spinner.getSelectedItem().toString().trim() + signUpStep1Phone.getText().toString().trim());
                intent.putExtra("Password", signUpStep1Password.getText().toString().trim());
                startActivity(intent);


            }

            @Override
            public void onFailure(Call<VerifyUniqueDetailCallback> call, Throwable t) {
                Toast.makeText(SignUpStep1.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());

            }
        });


    }
}