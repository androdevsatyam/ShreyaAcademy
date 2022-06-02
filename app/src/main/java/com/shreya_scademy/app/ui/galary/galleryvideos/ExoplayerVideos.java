package com.shreya_scademy.app.ui.galary.galleryvideos;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.shreya_scademy.app.App;
import com.shreya_scademy.app.R;
import com.shreya_scademy.app.data.ContentModel;
import com.shreya_scademy.app.data.DaoHelper;
import com.shreya_scademy.app.model.modellogin.ModelLogin;
import com.shreya_scademy.app.ui.base.BaseActivity;
import com.shreya_scademy.app.ui.home.ActivityHome;
import com.shreya_scademy.app.utils.AppConsts;
import com.shreya_scademy.app.utils.sharedpref.SharedPref;

import java.util.List;

public class ExoplayerVideos extends BaseActivity {
    PlayerView playerView;
    SimpleExoPlayer player;
    Context mContext;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    static long playbackPosition = 0;
    private long lastPosition;
    String url;
    ImageButton rotateBtn;
    private String videoId;
    private String courseId;
    private ModelLogin modelLogin;
    private SharedPref sharedPref;
    private DaoHelper daoHelper;
    private ContentModel contentModel;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;
    private static final String SELECTED_ITEM_POSITION = "ItemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exo_player);
        playerView = findViewById(R.id.playerView);
        //  playbackPosition = 0;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mContext = ExoplayerVideos.this;
        daoHelper = new DaoHelper(App.getInstance().getAppDatabase());
        sharedPref = SharedPref.getInstance(mContext);
        modelLogin = sharedPref.getUser(AppConsts.STUDENT_DATA);
        rotateBtn = findViewById(R.id.rotateBtn);
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//
//                } else {
//
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//                }

//

                // Initialize the variable sensorManager
//                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//                // getSensorList(Sensor.TYPE_ALL) lists all the sensors present in the device
//                deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the state of item position
        outState.putLong(SELECTED_ITEM_POSITION, playbackPosition);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read the state of item position
        lastPosition = savedInstanceState.getLong(String.valueOf(SELECTED_ITEM_POSITION));
        playbackPosition = lastPosition;
        player.prepare();

        //initializePlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent.hasExtra("WEB_URL")) {
            url = getIntent().getStringExtra("WEB_URL");
            initializePlayer();
        }
        if (intent.hasExtra("VIDEO_ID")) {
            videoId = getIntent().getStringExtra("VIDEO_ID");
        }
        if (intent.hasExtra("COURSE_ID")) {
            courseId = getIntent().getStringExtra("COURSE_ID");
        }


    }

    private void getVideoViewCount() {
        AndroidNetworking.get(AppConsts.BASE_URL + AppConsts.GET_VIDEO_VIEW_COUNT)
                .addPathParameter(AppConsts.VIDEO_ID, videoId)
                .addPathParameter(AppConsts.USER_ID, "" + modelLogin.getStudentData().getStudentId())
                .build().getAsObject(ContentViewCountModel.class, new ParsedRequestListener<ContentViewCountModel>() {
            @Override
            public void onResponse(ContentViewCountModel response) {
                int viewLimit = 0;
                int viewCount = 0;
                if (response != null && response.getData() != null && response.getStatus().equalsIgnoreCase("true")) {
                    viewLimit = response.getData().getViewLimit();
                    if (response.getData().getData() != null
                            && !response.getData().getData().isEmpty()) {
                        viewCount = Integer.parseInt(response.getData().getData().get(0).getCount());
                    }
                }
                contentModel = daoHelper.getContentModel(playbackPosition,
                        viewLimit,
                        viewCount,
                        modelLogin.getStudentData().getStudentId(),
                        courseId,
                        videoId);
                daoHelper.insertOrUpdate(contentModel);
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    private void initializePlayer() {
        if (player != null) {
            playWhenReady = false;
            player.stop();
        } else {
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            MediaItem mediaItem = MediaItem.fromUri(url);
            player.setMediaItem(mediaItem);
            playerView.setControllerShowTimeoutMs(0);
            playerView.setControllerHideOnTouch(false);
            player.prepare();
        }
/*
        new CountDownTimer(player.getDuration(), 1000) {

            public void onTick(long millisUntilFinished) {
                Log.v("saloni1234","seconds remaining: "+millisUntilFinished+"\n"+player.getDuration() +"\n"+player.getCurrentPosition());
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
              Log.v("saloni1234","  Done!!");
            }

        }.start();*/
    }


    void getPlayerPosition() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            getPlayerPosition();
        }
        if (contentModel != null) {
            final long lastPosition = playbackPosition;
            contentModel.setContentProgress(lastPosition);
            daoHelper.insertOrUpdate(contentModel);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        player.stop();
        if (Util.SDK_INT >= 24) {
            getPlayerPosition();
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("firebase/notice")) {
            player.stop();
            playWhenReady = false;
            startActivity(new Intent(getApplicationContext(), ActivityHome.class));
            finish();
        } else {
            player.stop();
            playWhenReady = false;
            super.onBackPressed();
        }
    }
}
