package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hamdan.elaajmedicalapp.models.IpAddress;

public class PatientReceiveDoctorVoiceNote extends AppCompatActivity {
    private ImageView imagePlayPause;
    private TextView textCurrentTime, textTotalDuration;
    private SeekBar playerSeekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    IpAddress ipAddress;
    String audioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_receive_doctor_voice_note);

        imagePlayPause = findViewById(R.id.imagePlayPause);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalDuration = findViewById(R.id.textTotalDuration);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        mediaPlayer = new MediaPlayer();
        playerSeekBar.setMax(100);

        Intent intent = getIntent();
        audioFile = intent.getStringExtra("recordedAudio");

        Toolbar toolbar = findViewById(R.id.toolbar_patientReceiveDoctorVoiceNote);
        toolbar.setTitle("Doctor's Voice Note");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

//        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);


        imagePlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    imagePlayPause.setImageResource(R.drawable.play_circle);
                } else {
                    mediaPlayer.start();
                    imagePlayPause.setImageResource(R.drawable.pause_circle);
                    updateSeekBar();
                }
            }
        });
        prepareMediaPlayer(audioFile);


        playerSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                textCurrentTime.setText(milliSecondToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                playerSeekBar.setSecondaryProgress(i);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playerSeekBar.setProgress(0);
                imagePlayPause.setImageResource(R.drawable.play_circle);
                textCurrentTime.setText("0:00");
                textTotalDuration.setText("0:00");
                mediaPlayer.reset();
                prepareMediaPlayer(audioFile);
            }
        });

    }
    private void prepareMediaPlayer(String audioFile) {
        try {
            mediaPlayer.setDataSource("http://" + ipAddress.ip + ":8000/voice_messages/" + audioFile);
            mediaPlayer.prepare();
            textTotalDuration.setText(milliSecondToTimer(mediaPlayer.getDuration()));
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondToTimer(currentDuration));
            handler.postDelayed(updater, 1000);
        }
    };

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
        }
    }

    private String milliSecondToTimer(long milliSeconds) {
        String timerString = "";
        String secondString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minute = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        timerString = timerString + minute + ":" + secondString;
        return timerString;

    }
}