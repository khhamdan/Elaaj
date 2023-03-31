package com.hamdan.elaajmedicalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.hamdan.elaajmedicalapp.models.IpAddress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DoctorGivingPrescription extends AppCompatActivity {
    IpAddress ipAddress;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    //new Record
    private static int Request_AUDIO_PERMISSION_CODE = 101;
    MediaRecorder mediaRecorder;  //To record a audio
    MediaPlayer mediaPlayer;      //To play a audio
    TextView tv_time;
    ImageView ibRecord,ibPlay;
    boolean isRecording = false;
    boolean isPlaying = false;
    int seconds = 0;
    String path = null;
    int dummySeconds = 0; //play no need to start from 0, we will save the instance of the recording time
    int playableSeconds=0;  //for how much time we need to play the recording, we need to autoStop the playing
    ExecutorService executorService = Executors.newSingleThreadExecutor(); //can be used to perform background task
    Handler handler;
    //old Record
    ImageButton button_record, button_play, button_stop, choose_picture;
    ImageView default_page;

    //Image
    String displayName = null;
    String image,appointment_id;
    Uri filePath;
    URI path2;
    private RequestQueue rQueue;
    String url = "http://"+ipAddress.ip+":8000/api/doctorGivingPrescription";
    AppCompatButton sendPrescriptionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_giving_prescription);

        Toolbar toolbar = findViewById(R.id.doctor_prescription_toolbar);
        toolbar.setTitle("Doctor Prescription");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));

        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);



//        button_play = findViewById(R.id.button_play);
        ibRecord = findViewById(R.id.ibRecord);
        ibPlay = findViewById(R.id.ibPlay);
        tv_time = findViewById(R.id.tv_time);
        mediaPlayer = new MediaPlayer();

        choose_picture = findViewById(R.id.choose_picture);
        default_page = findViewById(R.id.default_page);
        sendPrescriptionButton = findViewById(R.id.sendPrescriptionButton);
        tv_time = findViewById(R.id.tv_time);

        Intent intent = getIntent();
        appointment_id = intent.getStringExtra("appointment_id");


        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying)
                {
                    if (path!=null) {
                        try {
                            mediaPlayer.setDataSource(getRecordingFilePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(DoctorGivingPrescription.this, "No Recording Present", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying=true;
                    ibPlay.setImageDrawable(ContextCompat.getDrawable(DoctorGivingPrescription.this,R.drawable.player_pause_btn));
                    runTimer();
                }
                else
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    isPlaying= false;
                    seconds=0;
                    isRecording=false;
                    handler.removeCallbacksAndMessages(null);
                    ibPlay.setImageDrawable(ContextCompat.getDrawable(DoctorGivingPrescription.this,R.drawable.player_play_btn));

                }
            }
        });

        ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRecordingPermission())
                {
                    if (!isRecording)
                    {
                        isRecording = true;
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                mediaRecorder = new MediaRecorder();
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                mediaRecorder.setOutputFile(getRecordingFilePath());
                                path = getRecordingFilePath();
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                try {
                                    mediaRecorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mediaRecorder.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playableSeconds = 0;
                                        seconds = 0;
                                        dummySeconds = 0;
                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(DoctorGivingPrescription.this,R.drawable.record_btn_recording));
                                        runTimer();
                                    }
                                });
                            }

                        });
                    }
                    else
                    {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                mediaRecorder=null;
                                playableSeconds = seconds;
                                dummySeconds = seconds;
                                seconds = 0;
                                isRecording = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handler.removeCallbacksAndMessages(null);
                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(DoctorGivingPrescription.this,R.drawable.record_btn_stopped));

                                    }
                                });
                            }
                        });
                    }
                }
                else
                {
                    requestRecordingPermission();
                }
            }
        });



//                                              OLD STUFF
//        if (isMicrophonePresent()) {
//            getMicrophonePermission();
//        }
//
//        starRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    mediaRecorder = new MediaRecorder();
//                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                    mediaRecorder.setOutputFile(getRecordingFilePath());
//                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                    mediaRecorder.prepare();
//                    mediaRecorder.start();
//
//                    Toast.makeText(DoctorGivingPrescription.this, "Recording is started", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//
//                }
//            }
//        });

//        stopRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mediaRecorder.stop();
//                mediaRecorder.release();
//                mediaRecorder = null;
//
//                Toast.makeText(DoctorGivingPrescription.this, "Recording is Stopped", Toast.LENGTH_SHORT).show();
//
//            }
//        });

//        button_play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                try {
//
//                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setDataSource(getRecordingFilePath());
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
        choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(DoctorGivingPrescription.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        sendPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPrescription();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        String uriString = filePath.toString();

//        default_page.setImageURI(uri);
        System.out.println(uriString);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            Bitmap lastBitmap = null;
            lastBitmap = bitmap;
            //encoding image to string
            image = getStringImage(lastBitmap);
            Log.d("image", image);
            //passing the image to volley
            default_page.setImageBitmap(bitmap);

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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void requestRecordingPermission()
    {
        ActivityCompat.requestPermissions(DoctorGivingPrescription.this,new String[]{Manifest.permission.RECORD_AUDIO},Request_AUDIO_PERMISSION_CODE);
    }
    public boolean checkRecordingPermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED)
        {
            requestRecordingPermission();
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Request_AUDIO_PERMISSION_CODE)
        {
            if(grantResults.length > 0)
            {
                boolean permissionToRecord=grantResults[0]==PackageManager.PERMISSION_GRANTED ;
                if (permissionToRecord)
                {
                    Toast.makeText(this, "Permission Given", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }

    }
    //    private boolean isMicrophonePresent() {
//        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    private void getMicrophonePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                    {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
//        }
//    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext()); //store the audio with the help of external storage or Environment
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile" + ".mp3");  //I can use randomFunction here to save the recordings
//        path2 = file.toURI();
        return file.getPath();
    }

    private String getRecordingFileName() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext()); //store the audio with the help of external storage or Environment
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile" + ".mp3");  //I can use randomFunction here to save the recordings
        return file.getName();
    }

    private void runTimer()
    {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minute = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),"%02d:%02d",minute,secs);
                tv_time.setText(time);

                if (isRecording || (isPlaying &&playableSeconds!=-1))
                {
                    seconds++;
                    playableSeconds--;

                    if (playableSeconds==-1 && isPlaying)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        isPlaying = false;
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        ibPlay.setImageDrawable(ContextCompat.getDrawable(DoctorGivingPrescription.this,R.drawable.player_play_btn));
                        return;
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }


    private void insertPrescription() {

        InputStream iStream = null;
        InputStream iStream2 = null;
        try {
            System.out.println("Appointment_id is:"+appointment_id);
            System.out.println("filepath:"+filePath);
            System.out.println("filepath2:"+Uri.parse("file://"+getRecordingFilePath()));
            iStream = getContentResolver().openInputStream(filePath);
            final byte[] inputData = getBytes(iStream);

//            Uri newUri = Uri.parse(path2.toString());
//            System.out.println("whats inside newUri:"+newUri);
            iStream2 = getContentResolver().openInputStream( Uri.parse("file://"+getRecordingFilePath()));
            final byte[] inputData2 = getBytes(iStream2);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {

                @Override
                public void onResponse(NetworkResponse response) {
                    default_page.setImageResource(R.drawable.default_page);
                    Toast.makeText(getApplicationContext(), "Prescription Successfully sent", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DoctorGivingPrescription.this, DoctorsTodayOnlineAppointmentsFragments.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Nullable
                @Override
                //map is implemented which accepts the variable name and its value
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("appointment_id", appointment_id);
                    return param;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("prescriptions", new DataPart(displayName, inputData));
                    params.put("recording", new DataPart("testRecordingFile.mp3", inputData2));
                    return params;
                }
            };
            {
//
                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(getApplicationContext());
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
        //Maybe I can use if for controlling null in her todo
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}