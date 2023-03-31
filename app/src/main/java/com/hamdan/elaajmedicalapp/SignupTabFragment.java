package com.hamdan.elaajmedicalapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupTabFragment extends Fragment {



    EditText name, email, username, mobileNumber, pass, dateOfBirth, occupation, emergencyContact, referredBy;
    AppCompatButton signup;
    Spinner genderSpinner, martialStatusSpinner;
    List<String> genders, maritalStatuses;
    String selectedGender, selectedDate, selectedMaritalStatus;
    AppCompatButton browse;
    CircleImageView img;
    String encodeImageString;
    Bitmap bitmap;
    String image;
    String displayName = null;
    Uri filePath;
    private RequestQueue rQueue;
    float v = 0;
    IpAddress ipAddress;

    String url = "http://"+ipAddress.ip+":8000/api/userSignUp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragments, container, false);

        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        username = root.findViewById(R.id.username);
        mobileNumber = root.findViewById(R.id.mobileNumber);
        pass = root.findViewById(R.id.pass);
        signup = root.findViewById(R.id.signUp);
        genderSpinner = root.findViewById(R.id.gender);

        dateOfBirth = root.findViewById(R.id.dateOfBirth);
        browse = root.findViewById(R.id.browse);
        img = root.findViewById(R.id.circularImg);

        occupation = root.findViewById(R.id.occupation);
        emergencyContact = root.findViewById(R.id.emergencyContact);
        referredBy = root.findViewById(R.id.referredBy);
        martialStatusSpinner = root.findViewById(R.id.maritalStatus);

        //image browse
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
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
                                startActivityForResult(intent, 1);
//                                startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        //Date Picker
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar() {
                String format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

                dateOfBirth.setText(sdf.format(calendar.getTime()));
            }
        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        genders = new ArrayList<String>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, genders);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender = (String) genderSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        maritalStatuses = new ArrayList<String>();
        maritalStatuses.add("Single");
        maritalStatuses.add("Married");
        maritalStatuses.add("Other");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity().getBaseContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, maritalStatuses);
        martialStatusSpinner.setAdapter(adapter1);
        martialStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMaritalStatus = (String) martialStatusSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        name.setTranslationX(800);
        email.setTranslationX(800);
        username.setTranslationX(800);
        mobileNumber.setTranslationX(800);
        pass.setTranslationX(800);
        signup.setTranslationX(800);

        name.setAlpha(v);
        email.setAlpha(v);
        username.setAlpha(v);
        mobileNumber.setAlpha(v);
        pass.setAlpha(v);
        signup.setAlpha(v);

        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mobileNumber.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            filePath = data.getData();
            String uriString = filePath.toString();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                image = getStringImage(lastBitmap);
                Log.d("image", image);
                //passing the image to volley
                img.setImageBitmap(bitmap);

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
                    cursor = getActivity().getContentResolver().query(filePath, null, null, null, null);
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

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);


    }


    private Boolean validateName() {
        String namePattern = "^[A-Za-z ]+$";
        String ufullname = name.getText().toString().trim();
        if (ufullname.isEmpty()) {
            name.setError("Filed cannot be empty");
            return false;
        } else if (!ufullname.matches(namePattern)) {
            name.setError("Invalid name");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private Boolean validateDateOfBirth() {
        String date = dateOfBirth.getText().toString().trim();

        if (date.isEmpty()) {
            dateOfBirth.setError("Empty Field! Please use Date picker");
            return false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                format.parse(date);
                return true;
            } catch (ParseException e) {
                dateOfBirth.setError("Invalid date Format");
                return false;
            }
        }
    }


    private Boolean validateReferredBy() {
        String referredByPattern = "^[A-Za-z ]+$";
        String ureferredBy = referredBy.getText().toString().trim();
        if (ureferredBy.isEmpty()) {
            referredBy.setError("Filed cannot be empty");
            return false;
        } else if (!ureferredBy.matches(referredByPattern)) {
            referredBy.setError("Invalid name");
            return false;
        } else {
            referredBy.setError(null);
            return true;
        }
    }

    private Boolean validateOccupation() {
        String occupationPattern = "^[A-Za-z ]+$";
        String uoccupation = occupation.getText().toString().trim();
        if (uoccupation.isEmpty()) {
            occupation.setError("Filed cannot be empty");
            return false;
        } else if (!uoccupation.matches(occupationPattern)) {
            occupation.setError("Invalid name");
            return false;
        } else {
            occupation.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String usernamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        String uusername = username.getText().toString().trim();
        if (uusername.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (username.length() < 5 || username.length() > 15) {
            username.setError("Invalid length");
            return false;
        } else if (!uusername.matches(usernamePattern)) {
            username.setError("Invalid Username");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String uemail = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (uemail.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String phonematcher = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$";
        String umobileNumber = mobileNumber.getText().toString().trim();
        if (umobileNumber.isEmpty()) {
            mobileNumber.setError("Field cannot be empty");
            return false;
        } else if (!umobileNumber.matches(phonematcher)) {
            mobileNumber.setError("Invalid number! Use 03008612689 format");
            return false;
        } else {
            mobileNumber.setError(null);
            return true;
        }
    }

    private Boolean validateEmergencyContact() {
        String phonematcher = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$";
        String uemergencyContact = emergencyContact.getText().toString().trim();
        if (uemergencyContact.isEmpty()) {
            emergencyContact.setError("Field cannot be empty");
            return false;
        } else if (!uemergencyContact.matches(phonematcher)) {
            emergencyContact.setError("Invalid number");
            return false;
        } else {
            emergencyContact.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String upassword = pass.getText().toString().trim();
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
            pass.setError("Field cannot be empty");
            return false;
        } else if (!upassword.matches(passwordVal)) {
            pass.setError("Password is too weak");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    private void insertData() {

        if (!validateName() | !validatePassword() | !validateEmail() | !validatePhoneNo() | !validateUsername() | !validateEmergencyContact() | !validateOccupation() | !validateReferredBy() | !validateDateOfBirth()) {
            return;
        }

        String ufullname = name.getText().toString().trim();
        String uemail = email.getText().toString().trim();
        String uusername = username.getText().toString().trim();
        String umobileNumber = mobileNumber.getText().toString().trim();
        String upassword = pass.getText().toString().trim();
        String uoccupation = occupation.getText().toString().trim();
        String ureferredBy = referredBy.getText().toString().trim();
        String uemergencyContact = emergencyContact.getText().toString().trim();
        String uselectedDate = String.valueOf(dateOfBirth.getText());


        InputStream iStream = null;
        try {

            iStream = getActivity().getContentResolver().openInputStream(filePath);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {

                @Override
                public void onResponse(NetworkResponse response) {
                    name.setText("");
                    email.setText("");
                    username.setText("");
                    mobileNumber.setText("");
                    pass.setText("");
                    dateOfBirth.setText("");
                    emergencyContact.setText("");
                    referredBy.setText("");
                    occupation.setText("");
                    img.setImageResource(R.drawable.defaultdp);
                    Toast.makeText(getActivity(), "Successfully signed up", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Nullable
                @Override
                //map is implemented which accepts the variable name and its value
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("name", ufullname);
                    param.put("username", uusername);
                    param.put("password", upassword);
                    param.put("email", uemail);
                    param.put("contact_no", umobileNumber);
                    param.put("gender", selectedGender);
                    param.put("DOB", uselectedDate);
                    param.put("emergency_contact", uemergencyContact);
                    param.put("marital_status", selectedMaritalStatus);
                    param.put("referred_by", ureferredBy);
                    param.put("occupation", uoccupation);

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
                rQueue = Volley.newRequestQueue(getActivity());
                rQueue.add(volleyMultipartRequest);


//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            requestQueue.add(request);
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
}
