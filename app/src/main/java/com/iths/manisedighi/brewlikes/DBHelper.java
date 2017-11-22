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
 * This class is specifically for working with the database.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BREWLIKES_DATABASE";
    private static final String BEER_TABLE = "BeerRanking";

    //Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Creates database for the first time
    //Tabellens upplägg:
    //Id - Name - Category - Price - Taste - Average(Medeltal) - Comment - Image - Location
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TABLE: Beer
        String sql_beer = "CREATE TABLE " + BEER_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_BEER_NAME TEXT NOT NULL," +
                "COL_BEER_CATEGORY TEXT," +
                "COL_BEER_PRICE INTEGER," +
                "COL_BEER_TASTE INTEGER," +
                "COL_BEER_AVERAGE INTEGER," +
                "COL_BEER_COMMENT TEXT," +
                "COL_BEER_IMAGE_PATH BLOB," +
                "COL_BEER_LOCATION TEXT);";

        db.execSQL(sql_beer);
    }

    //Bygger om databasens tabeller
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Drop all tables and recreate (obs - deletes all data)
        db.execSQL("DROP TABLE IF EXISTS " + BEER_TABLE);
        onCreate(db);

        //TODO Update tables - ALTER TABLE
    }

    //Add beer to database (Beer Table)
    public void addBeer(Beer beer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_BEER_NAME", beer.getName());
        values.put("COL_BEER_CATEGORY", beer.getCategory());
        values.put("COL_BEER_PRICE", beer.getPrice());
        values.put("COL_BEER_TASTE", beer.getTaste());
        values.put("COL_BEER_AVERAGE", beer.getAverage());
        values.put("COL_BEER_COMMENT", beer.getComment());
        values.put("COL_BEER_IMAGE_PATH", beer.getPhotoPath());
        values.put("COL_BEER_LOCATION", beer.getLocation());

        //insert() returnerar en id -> sätter den till beer-objektets id.
        long id = db.insert(BEER_TABLE,null, values);
        beer.setId(id);

        db.close();

        //Adding image using its path: image.imagePath()
    }

    //Get one beer from database
    public Beer getBeerById(int id) {
        String selection = "_ID=?";
        String[] selectionArgs = new String[] { Integer.toString(id) };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(BEER_TABLE,null, selection, selectionArgs, null, null, null);
        Beer beer = new Beer();

        boolean success = cursor.moveToFirst();

        if (success) {
            beer.setId(cursor.getLong(0));
            beer.setName(cursor.getString(1));
            beer.setCategory(cursor.getString(2));
            beer.setPrice(cursor.getFloat(3));
            beer.setTaste(cursor.getFloat(4));
            beer.setAverage(cursor.getFloat(5));
            beer.setComment(cursor.getString(6));
            beer.setPhotoPath(cursor.getString(7));
            beer.setLocation(cursor.getString(8));
        }

        db.close();
        return beer;
    }

    public void getTopList() {
        //TODO Return descending list of beers with 10 highest ratings
        //ORDER BY ... DESC LIMIT 10
        //SELECT all FROM BEER_TABLE WHERE average =
    }

    //Get all beers in certain category, descending order according to average points
    public List<Beer> getBeersByCategory(String category) {
        List<Beer> beerCategoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = "COL_BEER_CATEGORY=?";
        String[] selectionArgs = new String[] { category };

        Cursor cursor = db.query(BEER_TABLE, null, selection, selectionArgs, null, null, "COL_BEER_AVERAGE DESC");

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Beer beer = new Beer();
                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));
                beer.setCategory(cursor.getString(2));
                beer.setPrice(cursor.getFloat(3));
                beer.setTaste(cursor.getFloat(4));
                beer.setAverage(cursor.getFloat(5));
                beer.setComment(cursor.getString(6));
                beer.setPhotoPath(cursor.getString(7));
                beer.setLocation(cursor.getString(8));
            } while (cursor.moveToNext());
        }
        db.close();
        return beerCategoryList;
    }

    public List<Beer> getAllBeers() {
        List<Beer> beerList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("BEER_TABLE", null, null, null, null, null, null);

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Beer beer = new Beer();

                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));
                beer.setCategory(cursor.getString(2));
                beer.setPrice(cursor.getFloat(3));
                beer.setTaste(cursor.getFloat(4));
                beer.setAverage(cursor.getFloat(5));
                beer.setComment(cursor.getString(6));
                beer.setPhotoPath(cursor.getString(7));
                beer.setLocation(cursor.getString(8));

                beerList.add(beer);
            } while (cursor.moveToNext());
        }
        db.close();
        return beerList;
    }

    public boolean removeBeer(Beer beer) {
        return removeBeer(beer.getId());
    }

    //Returns the nr of affected rows/deleted rows. If nothing deleted, returns 0.
    public boolean removeBeer(long id) {
       SQLiteDatabase db = getWritableDatabase();

       String[] selectionArgs = new String[] {Long.toString(id)};
       //whereClaus = on what basis do we want to delete data.
       //? replaced with third argument value, ie. selectionArgs
       int result = db.delete(BEER_TABLE, "_ID=?", selectionArgs);
       db.close();

       return result == 1;
    }

    public void editBeer() {
        //TODO Method for editing a beer entry


    }
}
