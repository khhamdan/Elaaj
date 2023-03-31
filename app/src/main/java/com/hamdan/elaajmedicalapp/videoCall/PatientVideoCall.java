package com.hamdan.elaajmedicalapp.videoCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hamdan.elaajmedicalapp.PatientGiveReviewsToDoctor;
import com.hamdan.elaajmedicalapp.R;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PatientVideoCall extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 124;
    private static final String TAG = PatientVideoCall.class.getSimpleName();
    private Session session;
    private FrameLayout publisherViewContainer;
    private FrameLayout subscriberViewContainer;
    private Publisher publisher;
    private Subscriber subscriber;
    ImageView imageView;
    String appointment_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_call);

        publisherViewContainer = findViewById(R.id.publisher_container);
        subscriberViewContainer = findViewById(R.id.subscriber_container);


        //1- Toolbar:

        Intent intent = getIntent();
        appointment_id = intent.getStringExtra("appointment_id");

        imageView = findViewById(R.id.btn_call);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientVideoCall.this, PatientGiveReviewsToDoctor.class);
                intent.putExtra("appointment_id",appointment_id);
                startActivity(intent);
            }
        });



        requestPermissions();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize and connect to the session
            initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", PERMISSIONS_REQUEST_CODE, perms);
        }
    }
    private void initializeSession(String apiKey, String sessionId, String token) {
        Log.i(TAG, "apiKey: " + apiKey);
        Log.i(TAG, "sessionId: " + sessionId);
        Log.i(TAG, "token: " + token);

        session = new Session.Builder(this, apiKey, sessionId).build();
        session.setSessionListener(sessionListener);
        session.connect(token);
    }

    private Session.SessionListener sessionListener = new Session.SessionListener() {
        @Override
        public void onConnected(Session session) {
            Log.d(TAG, "onConnected: Connected to session: " + session.getSessionId());
            publisher = new Publisher.Builder(PatientVideoCall.this).build();
            publisher.setPublisherListener(publisherListener);
            publisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);

            publisherViewContainer.addView(publisher.getView());

            if (publisher.getView() instanceof GLSurfaceView) {
                ((GLSurfaceView) publisher.getView()).setZOrderOnTop(true);
            }

            session.publish(publisher);
        }

        @Override
        public void onDisconnected(Session session) {
            Log.d(TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
        }

        @Override
        public void onStreamReceived(Session session, Stream stream) {
            Log.d(TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        }

        @Override
        public void onStreamDropped(Session session, Stream stream) {
            Log.d(TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
            if (subscriber != null) {
                subscriber = null;
                subscriberViewContainer.removeAllViews();
            }
        }

        @Override
        public void onError(Session session, OpentokError opentokError) {
            Log.e(TAG, "Session error: " + opentokError.getMessage());
        }
    };
    @Override
    protected void onPause() {
        super.onPause();

        if (session != null) {
            session.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session != null) {
            session.onResume();
        }
    }
    private PublisherKit.PublisherListener publisherListener = new PublisherKit.PublisherListener() {
        @Override
        public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
            Log.d(TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());
            if (subscriber == null) {
                subscriber = new Subscriber.Builder(PatientVideoCall
                        .this, stream).build();
                subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                subscriber.setSubscriberListener(subscriberListener);
                session.subscribe(subscriber);
                subscriberViewContainer.addView(subscriber.getView());
            }
        }

        @Override
        public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
            Log.d(TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
        }

        @Override
        public void onError(PublisherKit publisherKit, OpentokError opentokError) {
            Log.e(TAG, "PublisherKit onError: " + opentokError.getMessage());
        }
    };

    SubscriberKit.SubscriberListener subscriberListener = new SubscriberKit.SubscriberListener() {
        @Override
        public void onConnected(SubscriberKit subscriberKit) {
            Log.d(TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
        }

        @Override
        public void onDisconnected(SubscriberKit subscriberKit) {
            Log.d(TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
        }

        @Override
        public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
            Log.e(TAG, "SubscriberKit onError: " + opentokError.getMessage());
        }
    };

}