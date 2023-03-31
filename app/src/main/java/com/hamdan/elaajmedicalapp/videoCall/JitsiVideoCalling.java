package com.hamdan.elaajmedicalapp.videoCall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hamdan.elaajmedicalapp.R;

//import org.jitsi.meet.sdk.JitsiMeetActivity;
//import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class JitsiVideoCalling extends AppCompatActivity {
//
//    EditText ed_room;
//    Button btn_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jitsi_video_calling);

//
//        ed_room = findViewById(R.id.ed_room);
//        btn_room = findViewById(R.id.join);
//
//
//        btn_room.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ed_room.getText().toString().isEmpty()) {
//                    Toast.makeText(JitsiVideoCalling.this, "It is empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//
//                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                                .setServerURL(new URL("https://meet.jit.si"))
//                                .setRoom(ed_room.getText().toString())
////                                .setAudioMuted(false)
////                                .setVideoMuted(false)
//                                .setAudioOnly(true)
//                                .build();
//
//                        JitsiMeetActivity.launch(getApplicationContext(),options);
//
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

    }
}