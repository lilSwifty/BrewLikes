import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Milja on 2017-11-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    //Contructor method
    @Override
    public DBHelper(Context context) {
        //TODO Call superclass
    }

    //Create the database
    //Method called when database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Create the database
        //Här skapar vi tabeller!

        //Table for rating individual beer
        String sql = "CREATE TABLE BeerInfo ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
    }
}
