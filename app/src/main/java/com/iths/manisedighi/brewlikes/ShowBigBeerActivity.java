package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by emmapersson on 2017-12-10.
 */

public class ShowBigBeerActivity extends AppCompatActivity {


    private static final String TAG = "BeerPhotoActivity";
    private BitmapHelper bitmapHelper;
    private String photoPath;
    private Bitmap image;
    private ImageView ivBigBeer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: beerphoto");
        setContentView(R.layout.activity_show_big_beer);
        showBigPicture();
    }

    private void showBigPicture() {
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("photoPath");
        ivBigBeer = findViewById(R.id.ivBigBeer);

        image = BitmapFactory.decodeFile(photoPath);
        ivBigBeer.setImageBitmap(image);

        //ivBigBeer.setAdjustViewBounds(true);
        //ivBigBeer.setImageBitmap(bitmapHelper.decodeSampledBitmapFromFile(photoPath, 960, 960));
    }
}
