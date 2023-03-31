package com.hamdan.elaajmedicalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.hamdan.elaajmedicalapp.models.User_Details;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginTabFragment extends Fragment {

    ProgressBar progressBar;
    private List<User_Details> user_detailsList;
    EditText iusername, ipass;
    TextView forget_pass;
    AppCompatButton login;
    String username, password, role, email;
    String nameOfUser, user_id;

    IpAddress ipAddress;
    String url = "http://" + ipAddress.ip + ":8000/api/patientDoctorLogin";
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        iusername = root.findViewById(R.id.iusername);
        ipass = root.findViewById(R.id.ipass);
        forget_pass = root.findViewById(R.id.forget_pass_btn);
        login = root.findViewById(R.id.login);
        progressBar = root.findViewById(R.id.progress_bar_login);
        user_detailsList = new ArrayList<>();


        iusername.setTranslationX(800);
        ipass.setTranslationX(800);
        forget_pass.setTranslationX(800);
        login.setTranslationX(800);

        iusername.setAlpha(v);
        ipass.setAlpha(v);
        forget_pass.setAlpha(v);
        login.setAlpha(v);

        iusername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        ipass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forget_pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                loginUser();

            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgetPassword.class);
                startActivity(intent);
            }
        });


        return root;
    }

    public void loginUser() {
        username = iusername.getText().toString().trim();
        password = ipass.getText().toString().trim();

        if (!username.equals("") && !password.equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    user_detailsList.clear();
                    try {
                        Object json = new JSONTokener(response).nextValue();
                        if (json instanceof JSONObject) {
                            JSONObject obj = new JSONObject(response);
                            user_id = obj.optString("id");
                            System.out.println("patient id is :" + user_id);
                            String userProfilePic = obj.getString("profile_pic");
                            System.out.println(userProfilePic);
                            nameOfUser = obj.getString("name");//getString("name");
                            System.out.println("Displaying patient Name :" + nameOfUser);
                            role = obj.optString("role");
                            email = obj.optString("email");
                            System.out.println("Displaying email :" + email);
                            User_Details user_details = new User_Details(nameOfUser, userProfilePic);
                            user_detailsList.add(user_details);
                            sharedPreferences(user_id, username, nameOfUser,email);
                            if (role.equals("patient")) {
                                Intent i = new Intent(getActivity(), PatientHomePage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                startActivity(i);
                            } else if (role.equals("doctor")) {
                                Intent i = new Intent(getActivity(), DoctorHomePage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", username);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(getActivity(), "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sharedPreferences(String user_id, String username, String nameOfUser, String email) {
        SharedPreferences sp = getContext().getSharedPreferences("credentials", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", user_id);
        editor.putString("username", username);
        editor.putString("nameOfUser", nameOfUser);
        editor.putString("email", email);
        editor.apply();
    }
//        public void hashMapSharePreferences(String user_id,String username,String nameOfUser)
//        {
//            //create test hashmap
//            HashMap<String,String> testHashMap = new HashMap<String,String>();
//            testHashMap.put("userId",user_id);
//            testHashMap.put("username",username);
//            testHashMap.put("nameOfUser",nameOfUser);
//
//            //convert the string to gson
//            Gson gson = new Gson();
//            String hashMapString = gson.toJson(testHashMap);
//
//            //save in shared pref
//            SharedPreferences prefs = getContext().getSharedPreferences("credentials", getContext().MODE_PRIVATE);
//            prefs.edit().putString("hashString",hashMapString).apply();
//
//            //get from shared prefs
//
//
//
//        }
}

