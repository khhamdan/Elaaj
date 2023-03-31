package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {

    IpAddress ipAddress;

    String url = "http://"+ipAddress.ip+":8000/api/checkEmail";
    AppCompatButton next_btn;
    EditText forget_password_email;
    String userEmailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        next_btn = findViewById(R.id.next_btn);
        forget_password_email = findViewById(R.id.forget_password_email);


        Toolbar toolbar = findViewById(R.id.toolbar_forget_password);
        toolbar.setTitle("Forget Password");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));


        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailInDatabase();
            }
        });
    }

    private void checkEmailInDatabase() {

        userEmailInput = forget_password_email.getText().toString().trim();

        if (!userEmailInput.equals(""))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray usersList = new JSONArray(response);
                        for (int i = 0; i < usersList.length(); i++) {
                            JSONObject object = usersList.getJSONObject(i);

                            String email = object.getString("email");

                            if(userEmailInput.equals(email))
                            {
                                Intent intent = new Intent(getApplicationContext(), UserVerifyCodeInForgetPassword.class);
                                intent.putExtra("email",userEmailInput);
                                startActivity(intent);
                            }
                            else
                            {
                                forget_password_email.setError("Email does not exist");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            forget_password_email.setError("Field can't be empty");

        }
    }
}

