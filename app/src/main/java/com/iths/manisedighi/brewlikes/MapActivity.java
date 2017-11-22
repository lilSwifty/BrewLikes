package com.iths.manisedighi.brewlikes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by moalenngren on 2017-11-22.
 */

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }
}
