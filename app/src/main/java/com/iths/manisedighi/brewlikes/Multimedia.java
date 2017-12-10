package com.iths.manisedighi.brewlikes;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;



public class Multimedia {

    private static final String TAG = "MultimediaMethod";
    private Activity activity;

    public Multimedia(Activity activity) {
        this.activity = activity;
    }


    protected void setMultimedia(Context context){

        //Log.v(TAG, "starts");
        videoBackground();
        beerSoundClick(context);
        rotateLogo();
    }

    public void videoBackground() {

        VideoView videoview = (VideoView) this.activity.findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+this.activity.getPackageName()+"/"+R.raw.beer_bubbles);
        videoview.setVideoURI(uri);
        videoview.start();
    }

    public void rotateLogo() {
        //Rotate
        ImageView logoRotate = this.activity.findViewById(R.id.brewlikes_main_image);
        final Animation animRotate = AnimationUtils.loadAnimation(this.activity, R.anim.animation);
        logoRotate.startAnimation(animRotate);
        //Sound
        MediaPlayer mp = MediaPlayer.create(this.activity, R.raw.capopen);
        mp.start();
    }

    public void beerSoundClick(Context context) {

        final Context _context = context;
        final ImageView beerSound = this.activity.findViewById(R.id.brewlikes_main_image);
        final MediaPlayer mp = MediaPlayer.create(this.activity, R.raw.open_bottle_sound);
        beerSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.setVolume(1.0f, 1.0f);
                mp.start();

                final Animation animScale = AnimationUtils.loadAnimation(_context, R.anim.scale_up);
                beerSound.startAnimation(animScale);


                Intent cameraIntent = new Intent(_context.getApplicationContext(), RankingActivity.class);
                _context.startActivity(cameraIntent);
            }
        });

        final Animation animScale = AnimationUtils.loadAnimation(_context, R.anim.scale_up);

    }


}