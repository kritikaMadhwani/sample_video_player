package com.example.samplevideoplayer;

import android.annotation.SuppressLint;
import android.app.AppComponentFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.samplevideoplayer.Utils.getListOfVideos;
import static com.example.samplevideoplayer.Utils.getListofVideoURL;

public class ExoPlayerActivity extends AppCompatActivity  {

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_exoplayer);
        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);

     //   initNextButton();

        recyclerView = (RecyclerView) findViewById(R.id.player_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(getBaseContext(),getListOfVideos(),getListofVideoURL());
        recyclerView.setAdapter(mAdapter);

        videoURL = getIntent().getStringExtra("VideoUrl");

        intializeExoplayer();

    }

    private void intializeExoplayer() {

        try {

            //for monitoring bandwidth of internet connection
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            //to buffer and keep player current status.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            //media file URL path
            MediaSource mediaSource = buildMediaSource(videoURI);


            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);




        }catch (Exception e){
            Log.e("VideoPlayerActivity"," exoplayer error "+ e.toString());
        }

    }

    private void initNextButton() {
        final View nextButton = exoPlayerView.findViewById(R.id.exo_next);
        nextButton.setVisibility(View.VISIBLE);
        nextButton.setFocusable(true);
        nextButton.setClickable(true);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("VideoActivity","next Button clicked" );
            }
        });
       // nextButton.getViewTreeObserver().addOnGlobalLayoutListener(obtainSetButtonEnabledListener(nextButton));
    }

    private MediaSource buildMediaSource(Uri uri) {
        
        String[] videoUrls = Utils.getListofVideoURL();
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("user-agent");

        ConcatenatingMediaSource concatenatingMediaSource = null;
        
        for(int i =0; i<videoUrls.length; i++) {
            // these are reused for both media sources we create below
          
            ExtractorMediaSource videoSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                    createMediaSource(uri);
            concatenatingMediaSource = new ConcatenatingMediaSource(videoSource);
            
        }
        
        return concatenatingMediaSource;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            intializeExoplayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            intializeExoplayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        exoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }


}
