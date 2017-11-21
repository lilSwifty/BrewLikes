package com.iths.manisedighi.brewlikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milja on 2017-11-21.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BREWLIKES_DATABASE";
    private static final String BEER_TABLE = "BeerRanking";
    private static final String BEER_CATEGORY_TABLE = "BeerCategories";

    //Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Creates database for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TABLE: Beer
        String sql_beer = "CREATE TABLE " + BEER_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_BEER_NAME TEXT NOT NULL," +
                "COL_BEER_CATEGORY TEXT," +
                "COL_BEER_PRICE INTEGER," +
                "COL_BEER_TASTE INTEGER," +
                "COL_BEER_COMMENT TEXT," +
                "COL_BEER_IMAGE_PATH BLOB," +
                "COL_BEER_LOCATION TEXT);";

        db.execSQL(sql_beer);
    }

    //Bygger om databasens tabeller
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Drop all tables and recreate
        db.execSQL("DROP TABLE IF EXISTS " + BEER_TABLE);
        onCreate(db);

        //TODO Update tables - ALTER TABLE
    }

    //Add beer to database (Beer Table)
    //Tabellens upplägg:
    //Id - Name - Category - Price - Taste - Comment - Image - Location
    public void addBeer(Beer beer) {
        //TODO Method for adding a beer into BeerRanking table

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_BEER_NAME", beer.getName());
        values.put("COL_BEER_CATEGORY", beer.getCategory());
        values.put("COL_BEER_PRICE", beer.getPrice());
        values.put("COL_BEER_TASTE", beer.getTaste());
        values.put("COL_BEER_COMMENT", beer.getComment());
        values.put("COL_BEER_IMAGE_PATH", beer.getPhotoPath());
        values.put("COL_BEER_LOCATION", beer.getLocation());

        //insert() returnerar en id -> sätter den till beer-objektets id.
        long id = db.insert(BEER_TABLE,null, values);
        beer.setId(id);

        db.close();

        //Adding image using its path: image.imagePath()
    }

    public List<Beer> getAllBeers() {
        List<Beer> beerList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("BEER_TABLE", null, null, null, null, null, null);

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                //TODO Add all info
                Beer beer = new Beer();
                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));

                beerList.add(beer);

                Log.d("MyLog","Print all beers:");
                Log.d("MyLog", beer.getId() + "," + beer.getName());
            } while (cursor.moveToNext());
        }
        db.close();
        return beerList;
    }

    public void removeBeer() {
        //TODO Method for removing beer from database
    }

    public void editBeer() {
        //TODO Method for editing a beer entry
    }
}
