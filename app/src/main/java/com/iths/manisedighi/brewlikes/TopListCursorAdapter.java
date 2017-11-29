package com.iths.manisedighi.brewlikes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /**
     * Constructor for the adapter
     * @param context
     * @param c - a cursor
     */
    public TopListCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Inflates a row of layout
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.top_list_listview, parent, false);
    }

    /**
     * A method that binds the data from the cursor to the row view.
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*TextView textView = view.findViewById(R.id.beerName);
        textView.setText( cursor.getString(1) );
        TextView textView2 = view.findViewById(R.id.textView2);
        textView2.setText( Integer.toString(cursor.getInt(2)) );
        */

        TextView beerName = view.findViewById(R.id.beerName);
        beerName.setText(cursor.getString(1));
        TextView score = view.findViewById(R.id.score);
        score.setText(cursor.getString(5));

        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(7));
        ImageView beerImage = view.findViewById(R.id.beerImage);
        beerImage.setImageBitmap(bitmap);
        //beerImage.image;
                //"COL_BEER_IMAGE_PATH"

        //"COL_BEER_NAME"


    }
/*
    @Override
    public boolean setViewValue (View view, Cursor cursor, int columnIndex){
        if (view.getId() == R.id.imageView1) {
            ImageView IV=(ImageView) view;
            int resID = Activity.getApplicationContext().getResources().getIdentifier(cursor.getString(columnIndex),
                    "drawable",  getApplicationContext().getPackageName());
            IV.setImageDrawable(getApplicationContext().getResources().getDrawable(resID));
            return true;
        }
        return false;
        */


}
