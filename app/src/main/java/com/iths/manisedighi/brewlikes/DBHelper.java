package com.iths.manisedighi.brewlikes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is specifically for working with the database.
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
        //Id (column 0) - Name - Category - Price - Taste - Average(Medeltal) - Comment - Image - Location
        String sql_beer = "CREATE TABLE " + BEER_TABLE + " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_BEER_NAME TEXT NOT NULL," +
                "COL_BEER_CATEGORY INTEGER," +
                "COL_BEER_PRICE INTEGER," +
                "COL_BEER_TASTE INTEGER," +
                "COL_BEER_AVERAGE INTEGER," +
                "COL_BEER_COMMENT TEXT," +
                "COL_BEER_IMAGE_PATH BLOB," +
                "COL_BEER_LOCATION TEXT);";

        //TABLE: Categories
        String sql_categories = "CREATE TABLE " + CATEGORY_TABLE + " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COL_CATEGORY_NAME TEXT NOT NULL);";

        db.execSQL(sql_beer);
        db.execSQL(sql_categories);

        //Insert initial values into CATEGORY_TABLE
        ContentValues values = new ContentValues();
        values.put("COL_CATEGORY_NAME", "Unknown");
        db.insert(CATEGORY_TABLE, null, values);
        //values = new ContentValues();
        values.put("COL_CATEGORY_NAME", "Lager");
        db.insert(CATEGORY_TABLE, null, values);
        //values = new ContentValues();
        values.put("COL_CATEGORY_NAME", "Dark Lager");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Ale");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "IPA");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Wheat");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Fruit Beer");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Porter");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Stout");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Sour Beer");
        db.insert(CATEGORY_TABLE, null, values);
        values.put("COL_CATEGORY_NAME", "Low and non-alcoholic");
        db.insert(CATEGORY_TABLE, null, values);
        //db.close();
    }

    /**
     * Recreate tables in database.
     * @param db Database
     * @param oldVersion the old version of the database
     * @param newVersion the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Drop all tables and recreate (obs - deletes all data)
        db.execSQL("DROP TABLE IF EXISTS " + BEER_TABLE);
        onCreate(db);

        //TODO Update tables - ALTER TABLE
    }

    /**
     * Add a beer to the database
     * @param beer
     */
    public void addBeer(Beer beer) {
        Log.d("MyLog", "In addBeer method");

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

        Log.d("MyLog", "Added values");

        //Insert() returns an id -> set this as the beer's id number
        long id = db.insert(BEER_TABLE,null, values);
        beer.setId(id);
        //Get the categoryName for the beer based on its categoryId
        beer.setCategoryName( getBeerCategoryName(beer) );
        //db.close();
    }

    //ADD CATEGORY
    public void addCategory(String categoryName) {
        Category category = new Category();
        Log.d("MyLog", "In addCategory method");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("COL_CATEGORY_NAME", categoryName);
        long id = db.insert(CATEGORY_TABLE, null, values);
        category.setId(id);
        category.setName(categoryName);

        //db.close();
    }

    /**
     * Get the category name of the beer from the CATEGORY_TABLE
     * @param beer
     * @return String
     */
    public String getBeerCategoryName(Beer beer) {
        Log.d("MyLog", "In getBeerCategoryName method");
        int id = beer.getCategoryId();
        Log.d("MyLog", "Beer's category id is " + id);

        String selection = "_ID=?";
        String[] selectionArgs = new String[] { Integer.toString(id) };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CATEGORY_TABLE, null, selection, selectionArgs, null, null, null, null);

        Log.d("MyLog", "Done with cursor.");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(1);
            beer.setCategoryName(name);
        }
        return beer.getCategoryName();
    }

    /**
     * Get a beer from the database by its id
     * @param id id of the beer
     * @return beer
     */
    public Beer getBeerById(long id) {
        String selection = "_ID=?";
        String[] selectionArgs = new String[] { Long.toString(id) };
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

                //Se vilket Category Name CategoryId motsvara
                getBeerCategoryName(beer);
            } while (cursor.moveToNext());
        }
        //db.close();
        return beer;
    }

    public void getTopList() {
        //TODO Return descending list of beers with 10 highest ratings
        //ORDER BY ... DESC LIMIT 10
        //SELECT all FROM BEER_TABLE WHERE average =
    }

    public List<Beer> getBeersByCategory(long categoryId) {
        //TODO Get all beers with certain category, sorted in descending order according to average points
        //Returns list

        List<Beer> beerList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = "COL_BEER_CATEGORY=?";
        String[] selectionArgs = new String[] { Long.toString(categoryId) };

        Cursor cursor = db.query(CATEGORY_TABLE, null, null, null, null, null, "COL_BEER_AVERAGE DESC");

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

                //Add category name of beer
                beer.setCategoryName( getBeerCategoryName(beer) );

                //Add beer to array
                beerList.add(beer);
            } while (cursor.moveToNext());
        }
        //TODO GÖR KLART
        //db.close();
        return beerList;
    }

    /**
     * Get all beers
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

                //Add category name of beer
                beer.setCategoryName( getBeerCategoryName(beer) );

                //Add beer to array
                beerList.add(beer);
            } while (cursor.moveToNext());
        }
        //db.close();
        return beerList;
    }

    /**
     * Returns all categories in CATEGORY_TABLE
     * @return Arraylist
     */
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(CATEGORY_TABLE, null, null, null, null, null, "COL_CATEGORY_NAME ASC");

        boolean success = cursor.moveToFirst();

        if (success) {
            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setName(cursor.getString(1));

                //Add beer to array
                categoryList.add(category);

            } while (cursor.moveToNext());
        }
        return categoryList;
    }

    //Remove a beer from the database
    //Returns the nr of affected rows/deleted rows. If nothing deleted, returns 0.
    public boolean removeBeer(Beer beer) {
        return removeBeer(beer.getId());
    }

    public boolean removeBeer(long id) {
       SQLiteDatabase db = getWritableDatabase();

       String[] selectionArgs = new String[] {Long.toString(id)};
       //whereClaus = on what basis do we want to delete data.
       //? replaced with third argument value, ie. selectionArgs
       int result = db.delete(BEER_TABLE, "_ID=?", selectionArgs);
       //db.close();

       return result == 1;
    }

    public void editBeer() {
        //TODO Method for editing a beer entry
    }
}
