package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

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
        super(context, c);
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
        return inflater.inflate(R.layout.activity_top_list, parent, false);
    }

    /**
     * A method that binds the data from the cursor to the row view.
     * @param view
     * @param context
     * @param cursor
     * TODO vad gör denna?
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.beerName);
        textView.setText( cursor.getString(1) );
        TextView textView2 = view.findViewById(R.id.textView2);
        textView2.setText( Integer.toString(cursor.getInt(2)) );
    }
}
