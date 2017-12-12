package com.iths.manisedighi.brewlikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is specifically for working with the database.
 *
 * @author Milja Virtanen
 * @since 2017-11-21
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BREWLIKES_DATABASE";
    private static final String BEER_TABLE = "BeerRanking";
    private static final String CATEGORY_TABLE = "BeerCategories";

    //Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates the database for the first time.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TABLE: Beer
        String sql_beer = "CREATE TABLE " + BEER_TABLE + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_BEER_NAME TEXT NOT NULL," +
                "COL_BEER_CATEGORY INTEGER," +
                "COL_BEER_PRICE INTEGER," +
                "COL_BEER_TASTE INTEGER," +
                "COL_BEER_AVERAGE INTEGER," +
                "COL_BEER_COMMENT TEXT," +
                "COL_BEER_IMAGE_PATH BLOB," +
                "COL_BEER_LOCATION TEXT," +
                "COL_BEER_LAT INTEGER," +
                "COL_BEER_LNG INTEGER," +
                "COL_BEER_ADD TEXT," +
                "COL_BEER_TEL TEXT," +
                "COL_BEER_WEB TEXT," +
                "COL_BEER_IMAGE_SMALL TEXT);";

        //TABLE: Categories
        String sql_categories = "CREATE TABLE " + CATEGORY_TABLE + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_CATEGORY_NAME TEXT NOT NULL);";

        db.execSQL(sql_beer);
        db.execSQL(sql_categories);

        //Insert initial values into CATEGORY_TABLE
        ContentValues values = new ContentValues();
        values.put("COL_CATEGORY_NAME", "Unknown");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Ale");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Fruit Beer");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Homebrewed");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "IPA");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Lager");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Lager Dark");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Low & non-alcoholic");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Porter");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Sour Beer");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Stout");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Wheat");
        db.insert(CATEGORY_TABLE, null, values);
    }

    /**
     * Recreate tables in database.
     * @param db Database
     * @param oldVersion the old version of the database
     * @param newVersion the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BEER_TABLE);
        onCreate(db);
    }

    /**
     * Add a beer to the database
     * @param beer
     */
    public void addBeer(Beer beer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_BEER_NAME", beer.getName());
        values.put("COL_BEER_CATEGORY", beer.getCategoryId());
        values.put("COL_BEER_PRICE", beer.getPrice());
        values.put("COL_BEER_TASTE", beer.getTaste());
        values.put("COL_BEER_AVERAGE", beer.getAverage());
        values.put("COL_BEER_COMMENT", beer.getComment());
        values.put("COL_BEER_IMAGE_PATH", beer.getPhotoPath());
        values.put("COL_BEER_LOCATION", beer.getLocation());
        values.put("COL_BEER_LAT", beer.getLat());
        values.put("COL_BEER_LNG", beer.getLng());
        values.put("COL_BEER_ADD", beer.getAddress());
        values.put("COL_BEER_TEL", beer.getTel());
        values.put("COL_BEER_WEB", beer.getWeb());
        values.put("COL_BEER_IMAGE_SMALL", beer.getPhotoPathSmall());

        //Insert() returns an id -> set this as the beer's id number
        long id = db.insert(BEER_TABLE,null, values);
        beer.setId(id);
        //Get the categoryName for the beer based on its categoryId
        beer.setCategoryName(getBeerCategoryName(beer));
    }

    /**
     * Adds a new category to the database
     * @param categoryName Name of the new category
     */
    public void addCategory(String categoryName) {
        Category category = new Category();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_CATEGORY_NAME", categoryName);
        long id = db.insert(CATEGORY_TABLE, null, values);
        category.setId(id);
        category.setName(categoryName);
    }

    /**
     * Returns the category name of the beer and sets the value of beer's category.
     * @param beer
     * @return String Name of the category
     */
    public String getBeerCategoryName(Beer beer) {
        int id = beer.getCategoryId();

        String selection = "_id=?";
        String[] selectionArgs = new String[] {Integer.toString(id)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CATEGORY_TABLE, null, selection, selectionArgs, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(1);
            beer.setCategoryName(name);
        }
        cursor.close();
        return beer.getCategoryName();
    }

    /**
     * Returns a beer from the database by its id.
     * @param id id of the beer
     * @return beer
     */
    public Beer getBeerById(long id) {
        String selection = "_id=?";
        String[] selectionArgs = new String[] {Long.toString(id)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(BEER_TABLE,null, selection, selectionArgs, null, null, null);

        boolean success = cursor.moveToFirst();

        Beer beer = new Beer();

        if (success) {
            do {
                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));
                beer.setCategoryId(cursor.getInt(2));
                beer.setPrice(cursor.getFloat(3));
                beer.setTaste(cursor.getFloat(4));
                beer.setAverage(cursor.getFloat(5));
                beer.setComment(cursor.getString(6));
                beer.setPhotoPath(cursor.getString(7));
                beer.setLocation(cursor.getString(8));
                beer.setLat(cursor.getDouble(9));
                beer.setLng(cursor.getDouble(10));
                beer.setAddress(cursor.getString(11));
                beer.setTel(cursor.getString(12));
                beer.setWeb(cursor.getString(13));
                beer.setPhotoPathSmall(cursor.getString(14));

                //See which category name the id stands for
                getBeerCategoryName(beer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beer;
    }

    /**
     * Returns a Cursor that points at a row in the database.
     * @return cursor pointing at top ranked beers
     */
    public Cursor getTopListCursor() {
        SQLiteDatabase db = getReadableDatabase();

        long rows = DatabaseUtils.queryNumEntries(db, BEER_TABLE);
        Log.d("MyLog", "Rows" + rows);

        if (rows >= 10) {
            return db.query(BEER_TABLE, null, null, null, null, null, "COL_BEER_AVERAGE DESC LIMIT 10");
        } else if (rows < 10){
            Log.d("MyLog", "We have less than 10");
            return db.query(BEER_TABLE, null, null, null, null, null, "COL_BEER_AVERAGE DESC LIMIT " + rows);
        } else {
            return null;
        }
    }

    /**
     * Returns a Cursor that points at individual beers within a certain beer category.
     * @param categoryName Name of the category that one wants to get beers from.
     * @return a Cursor with beers within the given beer category.
     */
    public Cursor getBeersByCategoryCursor(String categoryName) {
        SQLiteDatabase db = getReadableDatabase();

        //STEP 1. What id/row does the category name have?
        String selection = "COL_CATEGORY_NAME=?";
        String[] selectionArgs = new String[] {categoryName};

        Cursor cursor = db.query(CATEGORY_TABLE, null, selection, selectionArgs, null, null, null);

        int id = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }

        cursor.close();

        //STEP 2. Use the id from STEP 1 to return all beers within that category.
        String selectionTwo = "COL_BEER_CATEGORY=?";
        String[] selectionArgsTwo = new String[] {Integer.toString(id)};

        return db.query(BEER_TABLE, null, selectionTwo, selectionArgsTwo, null, null, "COL_BEER_AVERAGE DESC");
    }

    /**
     * Returns all beers within a certain category and ranks them according to their average score.
     * @param categoryName Name of the beer category
     * @return List of beers within category
     */
    public List<Beer> getBeersByCategory(String categoryName) {
        List<Beer> beerList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        //STEP 1. What id/row nr does the category name have?
        String selection = "COL_CATEGORY_NAME=?";
        String[] selectionArgs = new String[] {categoryName};

        Cursor cursor = db.query(CATEGORY_TABLE, null, selection, selectionArgs, null, null, null);

        int id = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }

        //STEP 2. Use id from STEP 1 to return all beers within said category.
        String selectionTwo = "COL_BEER_CATEGORY=?";
        String[] selectionArgsTwo = new String[] {Integer.toString(id)};

        cursor = db.query(BEER_TABLE, null, selectionTwo, selectionArgsTwo, null, null, "COL_BEER_AVERAGE DESC");

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Beer beer = new Beer();

                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));
                beer.setCategoryId(cursor.getInt(2));
                beer.setPrice(cursor.getFloat(3));
                beer.setTaste(cursor.getFloat(4));
                beer.setAverage(cursor.getFloat(5));
                beer.setComment(cursor.getString(6));
                beer.setPhotoPath(cursor.getString(7));
                beer.setLocation(cursor.getString(8));
                beer.setLat(cursor.getDouble(9));
                beer.setLng(cursor.getDouble(10));
                beer.setAddress(cursor.getString(11));
                beer.setTel(cursor.getString(12));
                beer.setWeb(cursor.getString(13));
                beer.setPhotoPathSmall(cursor.getString(14));

                beer.setCategoryName( getBeerCategoryName(beer) );

                beerList.add(beer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beerList;
    }

    /**
     * Get all beers from the database.
     * @return List with all beers
     */
    public List<Beer> getAllBeers() {
        List<Beer> beerList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(BEER_TABLE, null, null, null, null, null, null);

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Beer beer = new Beer();

                beer.setId(cursor.getLong(0));
                beer.setName(cursor.getString(1));
                beer.setCategoryId(cursor.getInt(2));
                beer.setPrice(cursor.getFloat(3));
                beer.setTaste(cursor.getFloat(4));
                beer.setAverage(cursor.getFloat(5));
                beer.setComment(cursor.getString(6));
                beer.setPhotoPath(cursor.getString(7));
                beer.setLocation(cursor.getString(8));
                beer.setLat(cursor.getDouble(9));
                beer.setLng(cursor.getDouble(10));
                beer.setAddress(cursor.getString(11));
                beer.setTel(cursor.getString(12));
                beer.setWeb(cursor.getString(13));
                beer.setPhotoPathSmall(cursor.getString(14));

                //Add category name of beer
                beer.setCategoryName( getBeerCategoryName(beer) );

                //Add beer to array
                beerList.add(beer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beerList;
    }

    /**
     * Returns a Cursor with category names in alphabetical order.
     * @return a Cursor with category names in alphabetical order.
     */
    public Cursor getAllCategoriesCursor() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(CATEGORY_TABLE, null, null, null, null, null, "COL_CATEGORY_NAME COLLATE NOCASE");
    }

    /**
     * Returns all category names in alphabetical order.
     * @return Arraylist with category names in alphabetical order
     */
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(CATEGORY_TABLE, null, null, null, null, null, null);

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    /**
     * Removes a beer from the database.
     * @param id id of the beer to be removed.
     * @return Returns true if a row was deleted. If nothing deleted, returns false.
     */
    public boolean removeBeer(long id) {
       SQLiteDatabase db = getWritableDatabase();

       String[] selectionArgs = new String[] {Long.toString(id)};
       int result = db.delete(BEER_TABLE, "_id=?", selectionArgs);

       if (result==1) {
           return true;
       } else {
           return false;
       }
    }

    /**
     * Edit the comment for a beer in the database.
     * @param id The id of the beer.
     * @param comment The new comment.
     * @return true if update successful, false if update failed.
     */
    public boolean editBeer(long id, String comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_BEER_COMMENT", comment);

        String selection = "_id=?";
        String[] selectionArgs = new String[] {Long.toString(id)};
        int result = db.update(BEER_TABLE, values, selection, selectionArgs);

        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Edit the name of a beer in the database.
     * @param id Id of the beer to be edited.
     * @param name the new name of the beer.
     * @return true if update successful, false if update failed.
     */
    public boolean editBeerName(long id, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_BEER_NAME", name);

        String selection = "_id=?";
        String[] selectionArgs = new String[] {Long.toString(id)};
        int result = db.update(BEER_TABLE, values, selection, selectionArgs);

        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Delete a beer category from the database.
     * @param name Name of the category to delete.
     * @return true if update successful, false if update failed.
     */
    public boolean deleteCategory(String name) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = "COL_CATEGORY_NAME=?";
        String[] selectionArgs = new String[] {name.trim()};
        int result = db.delete(CATEGORY_TABLE, selection, selectionArgs);

        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
}