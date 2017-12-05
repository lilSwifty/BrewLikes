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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity implements ExpandableListAdapter.java with 10 built in methods that create the mechanics for expandable listview.
 * The expandable listview referes 3xml files: activity_categories, list_categories and list_categoryitems.
 * CategoryArray BeerArrays from db + HasMap is needed to form ExpandableList. The setItems() method fills data and itemizes the expandablelist.
 * HashMap creates doubble array with category and item relation, it refers index of header(category to String from beername and keeps them in order.
 * Created by patrikrikamahinnenberg on 22/11/17.
 */


public class CategoriesActivity extends BottomNavigationBaseActivity{
    private Context context = CategoriesActivity.this;
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private DBHelper mDBHelper;
    HashMap<String, List<String>> hashMap;
    ArrayList<String> header;
    ImageView logo;
    private static final String TAG = "CategoriesActivity";
    private List<Long> beersById = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setupBottomNavigation();
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logoImageView);
        //Hides the BrewLikes text from the upper toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);


            //Object of listView from xml. Setting group indicator null for custom indicator
            expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
            // Setting group indicator null for custom indicator
            expandableListView.setGroupIndicator(null);

            mDBHelper = new DBHelper(this);

            setItems();

            //passing the 3 things; object of context, header array, chliddren
            adapter = new ExpandableListAdapter(CategoriesActivity.this, header, hashMap);
            // Setting adpater for expandablelistview, the hard part start here:)
            expandableListView.setAdapter(adapter);



        /**
         *Test listener for item clicks
         */
          /*  expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return false;
                }
            });*/


        /**
         * Outcommentted toast log that informs list expand and collapse for each category
         */
           /* // Listview Group expanded listener
            expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            header.get(groupPosition) + " Expanded",
                            Toast.LENGTH_SHORT).show();
                }
            });*/

            /*// Listview Group collasped listener
            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            header.get(groupPosition) + " Collapsed",
                            Toast.LENGTH_SHORT).show();

                }
            });*/

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                // Log.d(TAG, "onChildClick: "+id+"HERE ID");
                Intent i = new Intent(getApplicationContext(),InfoActivity.class);
                long beerId = beersById.get(childPosition);
                i.putExtra("BeerID", beerId);
                //Log.d(TAG, "onChildClick: "+beerId+"beer ID");
                startActivity(i);

                return true;

            }
        });

        /*expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent i = new Intent();
                i.putExtra("","");
                startActivity(i);

            }
        });*/

        /**
         * Outcommentted toast log that informs item selection inside expanded listview
         */
            /*// Listview on child click listener
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    Toast.makeText(
                            getApplicationContext(),
                            header.get(groupPosition)
                                    + " : "
                                    + hashMap.get(
                                    header.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            });*/
        }


        void setItems() {

            header = new ArrayList<String>();

            //mDBHelper.addCategory("A"); // <- Who added this testcode, can it be deleted? BR Patrik

            List<Category> allCategories = mDBHelper.getAllCategories();

            hashMap = new HashMap<String, List<String>>();

            // Adding headers + beers to list from DB
            for (int i = 0; i < allCategories.size(); i++) {

                header.add(allCategories.get(i).getName());
                List<Beer> beersByCategory = mDBHelper.getBeersByCategory(allCategories.get(i).getName());
                //TODO: Convert from List<Beer> to List<String> Advice from Martin.
                //List<Beer> beersByCategory = mDBHelper.getBeersByCategory(String categoryName); not working
                List<String> newbeerList = new ArrayList<String>(beersByCategory.size());
                for (Beer beer : beersByCategory) {
                    newbeerList.add((beer.getName()));

                }

                for (Beer beer :  beersByCategory) {
                    beersById.add((beer.getId()));
                }

                hashMap.put(allCategories.get(i).getName(), newbeerList);

            }

        }

    /**
     * Upper toolbar with icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.about_and_camera_icons, menu);
        return true;
    }

    /**
     * Handles what happens when the icons in the toolbar are clicked
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