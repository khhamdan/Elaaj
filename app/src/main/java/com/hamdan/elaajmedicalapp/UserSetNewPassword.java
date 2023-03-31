package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserSetNewPassword extends AppCompatActivity {


    EditText new_password, new_password_again;
    String stringNewPassword, stringNewPasswordAgain;
    AppCompatButton updatePassword_btn;
    String url,emailOfUser;
    IpAddress ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_password);

        url="http://"+ipAddress.ip+":8000/api/forgetPassword";
        new_password = findViewById(R.id.new_password);
        new_password_again = findViewById(R.id.new_password_again);
        updatePassword_btn = findViewById(R.id.updatePassword_btn);

        Bundle bundle = getIntent().getExtras();
        emailOfUser =  bundle.getString("email");


        Toolbar toolbar = findViewById(R.id.toolbar_new_credentials);
        toolbar.setTitle("New Password");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        updatePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringNewPassword = new_password.getText().toString().trim();
                stringNewPasswordAgain = new_password_again.getText().toString().trim();

//                Toast.makeText(SetNewPassword.this, emailOfUser, Toast.LENGTH_LONG).show();
                if (stringNewPassword.equals(stringNewPasswordAgain)) {
                    checkEmailInDatabase();
                } else {
                    new_password_again.setError("Password Don't match");
                }
            }
        });


    }
    private Boolean validatePassword() {
        String upassword = new_password.getText().toString().trim();
        String upasswordagain = new_password_again.getText().toString().trim();

        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (upassword.isEmpty()) {
            new_password.setError("Field cannot be empty");
            return false;
        }
        else if (upasswordagain.isEmpty()) {
            new_password_again.setError("Field cannot be empty");
            return false;
        }
        else if (!upassword.matches(passwordVal)) {
            new_password.setError("Password is too weak");
            return false;
        } else {
            new_password.setError(null);
            new_password_again.setError(null);
            return true;
        }
    }

    private void checkEmailInDatabase() {

        if (!validatePassword()) {
            return;
        }
        String upassword1 = new_password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginRegister.class);
                    startActivity(intent);

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", emailOfUser);
                    data.put("password", upassword1);
                    return data;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
