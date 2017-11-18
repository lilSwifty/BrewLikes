import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Milja on 2017-11-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "BeerRanking";

    //Contructor method
    public DBHelper(Context context) {
        //TODO Call superclass
        super(context, "beerRanking", null, DB_VERSION);
        Log.d("MyLog", "Database created");
    }

    //Create the database
    //Method called when database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Create the database
        //Här skapar vi tabeller!

        //Table for rating individual beer
        String sql = "CREATE TABLE " + TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "beerName TEXT NOT NULL," +
                "beerCategory INTEGER," +
                "price INTEGER," +
                "taste INTEGER," +
                "userComment TEXT," +
                "latitude INTEGER," +
                "longitude INTEGER);";

        db.execSQL(sql);

        //Table for beer categories that new categories can be added to
    }

    //Upgrade version if database structure has changed
    //i.e., columns added/removed, new tables added
    //NOT when we simply add data.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO When upgrading the database
        //För VG uppgiften?

        //Alter table without losing the table's data
        //TODO Code for ALTER TABLE
        //Create table
        onCreate(db);
    }

    public void addNewBeer(String beerName, String beerCategory, int price, int taste, String userComment, int latitude, int longitude) {
        SQLiteDatabase db = getWritableDatabase();

        //TODO Insert method for entering new beer entry into DB
        //addNewBeer måste skriva (dvs CREATE) i databasen

        db.close();
    }

    public list<IndividualBeer> getAllBeers() {
        SQLiteDatabase db = getReadableDatabase();
        //TODO Get all beer entries entered by user
        //getAllBeers måste läsa (READ) ur databasen
        db.close();
    }
}
