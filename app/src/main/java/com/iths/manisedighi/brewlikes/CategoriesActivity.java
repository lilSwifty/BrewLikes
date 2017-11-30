package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The expandable listview uses 3 xml files: activity_categories, list_categories and list_categoryitems.
 * This Activity implements ExpandableListAdapter.java with 10 methods, creates the mechanics.
 * Array and HasMap is needed to form ExpandableList. And setItems() fills data and itemizes the list.
 * HashMap is used to create array list with category and item relation with index, String parameters for category vs beer.
 */

public class CategoriesActivity extends BottomNavigationBaseActivity{
    private Context context = CategoriesActivity.this;
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private DBHelper mDBHelper;
    HashMap<String, List<String>> hashMap;
    ArrayList<String> header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setupBottomNavigation();

            //Object of listView from xml. Setting group indicator null for custom indicator
            expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
            expandableListView.setGroupIndicator(null);

            mDBHelper = new DBHelper(this);


            setItems();

            //passing the 3 things; object of context, header array, chliddren
            adapter = new ExpandableListAdapter(CategoriesActivity.this, header, hashMap);
            // Setting adpater for expandablelistview, the hard part start here:)
            expandableListView.setAdapter(adapter);

        /*
        You can add listeners for the item clicks
         */
            expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return false;
                }
            });


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


        // Setting headers and childs to expandable listview
        void setItems() {

            // Array list for header
            header = new ArrayList<String>();

            /*mDBHelper.addCategory("A");
            */

            List<Category> allCategories = mDBHelper.getAllCategories();


            // Hash map for both header and child
            hashMap = new HashMap<String, List<String>>();

            // Adding headers to list
            for (int i = 0; i < allCategories.size(); i++) {

                header.add(allCategories.get(i).getName());
                List<Beer> beersByCategory = mDBHelper.getBeersByCategory(allCategories.get(i).getName());
                //TODO: Convert from List<Beer> to List<String>
                //List<Beer> beersByCategory = mDBHelper.getBeersByCategory(String categoryName);
                List<String> newbeerList = new ArrayList<String>(beersByCategory.size());
                for (Beer beer : beersByCategory) {
                    newbeerList.add((beer.getName()));
                }

                hashMap.put(allCategories.get(i).getName(), newbeerList);

            }

        }

    }