package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by moalenngren on 2017-11-29.
 */

public class CustomCheckinWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    /**
     * Constructor for the customized check in window
     * @param context
     */
    public CustomCheckinWindowAdapter(Context context) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_checkin_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title);

        //Sets the markers title to the title of the window
        if(!title.equals("")){
            tvTitle.setText(title);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
