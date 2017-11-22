package com.iths.manisedighi.brewlikes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

public class RankingActivity extends CameraActivity {
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
    private ScrollView categoryScroll;
    private EditText beerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        CameraLauncher();

        try {
            createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addPictureToGallery();

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

    /**
     * The method that does all the work with saving the rankings and put them into the database/infoviews.
     * @param view
     */
    private void onRankingButtonClick(View view){

        float taste = saveTasteRate(view);
        float price = savePriceRate(view);
        String name = saveBeerName(view);
        String comment = saveBeerComment(view);

    }

    /**
     * Saving the ranking-number of the taste.
     * @param view
     * @return a float for the number of stars filled in.
     */
    private float saveTasteRate(View view){
        return tasteRate.getRating();
    }

    /**
     * Saving the ranking-number of the price.
     * @param view
     * @return a float for the number of stars filled in.
     */
    private float savePriceRate(View view){
        return priceRate.getRating();
    }

    /**
     * Saving the name of the beer.
     * @param view
     * @return a String for the name of the beer.
     */
    private String saveBeerName(View view){
        return beerName.getText().toString();
    }
    /**
     * Saving the comment for the beer.
     * @param view
     * @return a String for the comment to the beer.
     */
    private String saveBeerComment(View view){
        return beerComment.getText().toString();
    }
}
