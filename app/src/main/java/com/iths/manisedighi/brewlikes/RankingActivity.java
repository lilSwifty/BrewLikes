package com.iths.manisedighi.brewlikes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity {
    private ImageView beerImage;
    private TextView tasteText;
    private RatingBar tasteRate;
    private TextView priceText;
    private RatingBar priceRate;
    private EditText beerComment;
    private Button rankingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
    }
}
