package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by victoriagronqvist on 2017-11-24.
 */

public class TopListCursorAdapter extends CursorAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private BitmapHelper bitmapHelper;

    /**
     * Constructor for the adapter
     * @param context -
     * @param c - needs a cursor when set up
     */
    public TopListCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Inflates a row of layout from top_list_listview-file.
     * @param context
     * @param cursor
     * @param parent
     * @return the inflated layout.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.top_list_listview2, parent, false);
    }

    /*
    public RoundedBitmapDrawable createRoundPicture() {

        //ImageView beerImage = findViewById(R.id.beerImage);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beer);
        RoundedBitmapDrawable roundPic = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundPic.setCircular(true);

        return roundPic;
    }
    */

    /**
     * A method that binds the data from the cursor to each row view. Also makes the picture round.
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView beerName = view.findViewById(R.id.beerName);
        beerName.setText(cursor.getString(1));
        TextView score = view.findViewById(R.id.score);
        score.setText(cursor.getString(5));

        //String string = cursor.getString(14);

        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(14));
        ImageView beerImage = view.findViewById(R.id.beerImage);

        //beerImage.setImageBitmap(bitmapHelper.decodeSampledBitmapFromFile(cursor.getString(14), 100, 100));

        //Makes the picture round.
        beerImage.setImageBitmap(bitmap);
        RoundedBitmapDrawable roundPic = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundPic.setCircular(true);
        beerImage.setImageDrawable(roundPic);
    }

}
