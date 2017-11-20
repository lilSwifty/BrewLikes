package com.iths.manisedighi.brewlikes;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private TextView awfulText;
    private TextView perfectText;
    private TextView expensiveText;
    private TextView cheapText;
    private NestedScrollView categoryScroll;
    private EditText beerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        findViews();
    }

    /**
     * A method to find the views.
     */
    private void findViews(){
        beerImage = findViewById(R.id.beerImage);
        tasteText = findViewById(R.id.tasteText);
        tasteRate = findViewById(R.id.tasteRate);
        priceText = findViewById(R.id.priceText);
        priceRate = findViewById(R.id.priceRate);
        beerComment = findViewById(R.id.beerComment);
        rankingButton = findViewById(R.id.rankingButton);
        awfulText = findViewById(R.id.awfulText);
        perfectText = findViewById(R.id.perfectText);
        expensiveText = findViewById(R.id.expensiveText);
        cheapText = findViewById(R.id.cheapText);
        categoryScroll = findViewById(R.id.categoryScroll);
        beerName = findViewById(R.id.beerName);

    }
    private void onRankingButtonClick(View v){


    }

}
