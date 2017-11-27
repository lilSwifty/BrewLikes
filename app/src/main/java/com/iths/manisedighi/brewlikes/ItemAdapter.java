package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by manisedighi on 27/11/2017.
 */

public class ItemAdapter extends BaseAdapter {

    public ItemAdapter(Context c, String[] i, String[] p, String[] d){
        items = i;
        //prices = p;
        descriptions = d;
        mInflator =(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    LayoutInflater mInflator;
    String[] items;
    String[] prices;
    String[] descriptions;


    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflator.inflate(R.layout.detail, null);

        return null;
    }
}
