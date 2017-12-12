package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.List;

/**
 * This activity implements ExpandableListAdapter.java with 10 built in methods that creates the mechanics for expandable listview.
 * The expandable listview referes 3xml files: activity_categories, list_categories and list_categoryitems.
 * Category and beer lists from db + HasMap is needed to form ExpandableList. The setItems() method fills data and itemizes the expandablelist with headers and childs.
 * HashMap creates doubble array with category and item relation, it refers index of header(category) with child (beername) and keeps them in order.
 * Created by patrikrikamahinnenberg on 22/11/17.
 */


public class CategoriesActivity extends BottomNavigationBaseActivity{
    private Context context = CategoriesActivity.this;
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private DBHelper mDBHelper;
    private HashMap<String, List<Beer>> hashMap;
    private List<Category> header;
    ImageView logo;
    private static final String TAG = "testing";
    //private List<Long> beersById = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setupBottomNavigation();
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logoImageView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

            //Object of listView from xml. Setting group indicator null for custom indicator
            expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
            expandableListView.setGroupIndicator(null);

            mDBHelper = new DBHelper(this);

            setItems();

            //passing the 3 things; object of context, header list, chliddren list
            adapter = new ExpandableListAdapter(CategoriesActivity.this, header, hashMap);
            // Setting adpater for expandablelistview, hard part start here:)
            expandableListView.setAdapter(adapter);


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent (CategoriesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                // Log.d(TAG, "onChildClick: "+id+"HERE ID");
                Intent i = new Intent(getApplicationContext(),InfoActivity.class);
                //long beerId = beersById.get(childPosition);
                i.putExtra("BeerID", id);
                //Log.d(TAG, "onChildClick: "+beerId+"beer ID");
                i.putExtra("info", 2);
                startActivity(i);

                return true;

            }
        });
        }


        void setItems() {

            header= mDBHelper.getAllCategories();
            hashMap = new HashMap<>();

            /**
             * Categories + beers to list from DB
             */

            for (int i = 0; i < header.size(); i++) {
                List<Beer> beersByCategory = mDBHelper.getBeersByCategory(header.get(i).getName());
                hashMap.put(header.get(i).getName(), beersByCategory);
            }
        }


    /**
     * Upper toolbar with icons
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.categories_activity_menu, menu);
        return true;
    }


    /**
     * Toolbar and its activity
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.aboutIcon){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if(id == R.id.cameraIcon){
            Intent cameraIntent = new Intent(this, RankingActivity.class);
            startActivity(cameraIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    }