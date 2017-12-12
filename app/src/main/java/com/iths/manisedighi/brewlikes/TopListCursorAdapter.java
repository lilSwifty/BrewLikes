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
     * @param context - the context we are using in this method
     * @param cursor - needs a cursor when set up
     */
    public TopListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Inflates a row of layout from top_list_listview-file.
     * @param context - the context we are using in this method
     * @param cursor - the cursor we use
     * @param parent - the parent to the layout being used here
     * @return the inflated layout.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.top_list_listview2, parent, false);
    }

    /**
     * A method that binds the data from the cursor to each row view. Also makes the picture round.
     * @param view - the view being used
     * @param context - the context we are using in this method
     * @param cursor - the cursor we use
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView beerName = view.findViewById(R.id.beerName);
        beerName.setText(cursor.getString(1));
        TextView score = view.findViewById(R.id.score);
        score.setText(cursor.getString(5));

        //Gets the path to the picture
        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(14));
        ImageView beerImage = view.findViewById(R.id.beerImage);

        //Makes the picture round.
        beerImage.setImageBitmap(bitmap);
        RoundedBitmapDrawable roundPic = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundPic.setCircular(true);
        beerImage.setImageDrawable(roundPic);
    }

}
