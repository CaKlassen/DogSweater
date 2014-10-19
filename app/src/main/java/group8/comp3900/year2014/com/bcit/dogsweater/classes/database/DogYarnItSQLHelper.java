package group8.comp3900.year2014.com.bcit.dogsweater.classes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DogYarnItSQLHelper extends SQLiteOpenHelper {


    //table to hold profile information
    public static final String TABLE_PROFILES     = "profiles";
    public static final String PROFILE_ID         = "_idProfile";
    public static final String PROFILE_NAME       = "name";
    public static final String PROFILE_DIMENSIONS = "dimension";
    public static final String PROFILE_IMAGE      = "image";

    // table to hold project information
    public static final String TABLE_PROJECTS     = "projects";
    public static final String PROJECT_ID         = "_idProject";
    public static final String PROJECT_NAME       = "name";
    public static final String PROJECT_PROFILE    = "profile";
    public static final String PROJECT_STYLE      = "style";
    public static final String PROJECT_PERCENT    = "percent";
    public static final String PROJECT_ROWS       = "rows";

    private static final String DATABASE_NAME = "DogYarnIt.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_PROFILE =
              "CREATE TABLE " + TABLE_PROFILES
            + "(" + PROFILE_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + "," + PROFILE_NAME         + " TEXT NOT NULL"
            + "," + PROFILE_DIMENSIONS   + " TEXT"
            + "," + PROFILE_IMAGE        + " TEXT"
            + ");";
    private static final String DATABASE_CREATE_PROJECT =
            "CREATE TABLE " + TABLE_PROJECTS
                    + "(" + PROJECT_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + "," + PROJECT_NAME         + " TEXT NOT NULL"
                    + "," + PROJECT_PERCENT      + " REAL NOT NULL"
                    + "," + PROJECT_ROWS         + " INTEGER NOT NULL"
                    + "," + PROJECT_PROFILE      + " INTEGER NOT NULL"
                    + "," + PROJECT_STYLE        + " INTEGER NOT NULL"
                    + ");";

    public DogYarnItSQLHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_PROFILE);
        database.execSQL(DATABASE_CREATE_PROJECT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DogYarnItSQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PROFILES + ";");
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PROJECTS + ";");
        onCreate(db);
    }

}