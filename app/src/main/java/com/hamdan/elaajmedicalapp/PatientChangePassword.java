package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.hamdan.elaajmedicalapp.LoginRegister;
import com.hamdan.elaajmedicalapp.R;
import com.hamdan.elaajmedicalapp.models.IpAddress;

import java.util.HashMap;
import java.util.Map;

public class PatientChangePassword extends AppCompatActivity {

    IpAddress ipAddress;
    EditText current_password,new_password,new_password_again;
    AppCompatButton changePassword_btn;
    String url="http://"+ipAddress.ip+":8000/api/changePassword";
    String currentPassword,newPassword,newPasswordAgain;
    SharedPreferences sharedPreferences;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_change_password);

        current_password = findViewById(R.id.current_password);
        new_password = findViewById(R.id.new_password);
        new_password_again = findViewById(R.id.new_password_again);
        changePassword_btn = findViewById(R.id.changePassword_btn);

        Toolbar toolbar = findViewById(R.id.toolbar_change_password);
        toolbar.setTitle("Change Password");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        currentPassword = current_password.getText().toString().trim();
        newPassword = new_password.getText().toString().trim();
        newPasswordAgain =new_password_again.getText().toString().trim();

        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassword.equals(newPasswordAgain))
                {
                    changePassword();
                }
                else
                {
                    Toast.makeText(PatientChangePassword.this, "Password don't match" , Toast.LENGTH_SHORT).show();;
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

    public void changePassword()
    {
        if (!validatePassword())
        {
            return;
        }
        String uCurrentPassword = current_password.getText().toString().trim();
        String upassword = new_password.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientChangePassword.this,PatientProfile.class);
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
                data.put("id", userId);
                data.put("current_password", uCurrentPassword);
                data.put("new_password", upassword);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}