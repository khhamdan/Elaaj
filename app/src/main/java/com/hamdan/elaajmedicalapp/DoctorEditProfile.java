package com.hamdan.elaajmedicalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamdan.elaajmedicalapp.models.IpAddress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorEditProfile extends AppCompatActivity implements View.OnTouchListener{

    IpAddress ipAddress;
    CircleImageView profilePic;
    TextView tv_name, tName, email, phoneNo, dob, expertise, experience;
    ImageView changePicture;
    private RequestQueue rQueue;
    Uri filePath;
    String url, url2;
    String displayName = null;
    String image;
    String uName, uEmail, uDob,uPhoneNo, uExpertise, uExperience;
    String profile_pic;
    AppCompatButton editProfile;
    SharedPreferences sharedPreferences;
    String userDoctorId;
    RelativeLayout relativeLayout;
    RelativeLayoutTouchListener relativeLayoutTouchListener;
    static final String logTag = "ActivitySwipeDetector";
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_edit_profile);

        relativeLayout = findViewById(R.id.relativeLayout);

        tName = findViewById(R.id.tName);
        tv_name = findViewById(R.id.tv_name);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNo);
        dob = findViewById(R.id.dob);
        expertise = findViewById(R.id.expertise);
        experience = findViewById(R.id.experience);
        profilePic = findViewById(R.id.profilePic);
        changePicture = findViewById(R.id.changePicture);
        editProfile = findViewById(R.id.editProfile);
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        userDoctorId = sharedPreferences.getString("userId", "");

        url = "http://" + ipAddress.ip + ":8000/api/doctorUpdateProfile";
        url2 = "http://" + ipAddress.ip + ":8000/api/retrieveDoctorData";

        Toolbar toolbar = findViewById(R.id.editDoctorProfilePage);
        toolbar.setTitle("Edit Profile");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        Bundle bundle = getIntent().getExtras();
        uName = bundle.getString("name");
        uEmail = bundle.getString("email");
        uDob = bundle.getString("dob");
        profile_pic = bundle.getString("profile-pic");
        uPhoneNo = bundle.getString("phone");
        uExpertise = bundle.getString("expertise");
        uExperience = bundle.getString("experience");

        tName.setText(uName);
        tv_name.setText(uName);
        email.setText(uEmail);
        phoneNo.setText(uPhoneNo);
        dob.setText(uDob);
        expertise.setText(uExpertise);
        experience.setText(uExperience);
        Picasso.get().load("http://"+ipAddress.ip+":8000/profiles/users/" + profile_pic).into(profilePic);



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                getMyProfile();
                getMyProfile();
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(DoctorEditProfile.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.putExtra("aspectX", 1);
                                intent.putExtra("aspectY", 1);
                                intent.putExtra("scale", true);
                                intent.putExtra("outputFormat",
                                        Bitmap.CompressFormat.JPEG.toString());
                                startActivityForResult(intent, 10);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                   permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            filePath = data.getData();
            String uriString = filePath.toString();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                image = getStringImage(lastBitmap);
                Log.d("image", image);
                //passing the image to volley
                profilePic.setImageBitmap(bitmap);

//                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                img.setImageBitmap(bitmap);
//                encodeBitmapImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            Uri path2 = Uri.fromFile(myFile);

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(filePath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName);

                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateProfile()
    {
        String uName       = tName.getText().toString().trim();
        String uEmail      = email.getText().toString().trim();
        String uPhoneNo    = phoneNo.getText().toString().trim();
        String uDob        = dob.getText().toString().trim();
        String uExpertise  = expertise.getText().toString().trim();
        String uExperience = experience.getText().toString().trim();

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(filePath);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {

                @Override
                public void onResponse(NetworkResponse response) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
//                    param.put("DOB", "1999-12-12");
                    param.put("id", userDoctorId);
                    return param;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("profile-pic", new DataPart(displayName, inputData));
                    return params;
                }
            };
            {
                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(getApplicationContext());
                rQueue.add(volleyMultipartRequest);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void getMyProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray patientDetail = new JSONArray(response);
                    for (int i = 0; i < patientDetail.length(); i++) {
                        JSONObject object = patientDetail.getJSONObject(i);

                        String name = object.getString("name");
                        tv_name.setText(name);
                        Toast.makeText(DoctorEditProfile.this, tv_name.getText(), Toast.LENGTH_SHORT).show();
                        tName.setText(name);
                        profile_pic = object.getString("profile_pic");
                        Picasso.get().load("http://" + ipAddress.ip + ":8000/profiles/users/" + profile_pic).into(profilePic);
                        String tEmail = object.getString("email");
                        email.setText(tEmail);
                        String contact_no = object.getString("contact_no");
                        phoneNo.setText(contact_no);
                        String DOB = object.getString("DOB");
                        dob.setText(DOB);
                        String tExpertise = object.getString("expertise");
                        expertise.setText(tExpertise);
                        String tExperience = object.getString("experience");
                        experience.setText(tExperience);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoctorEditProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("doctor_id", userDoctorId);
                return data;
            }
            };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void onTopToBottomSwipe() {
        getMyProfile();
        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();


    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}