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
    public static final String PROJECT_IMAGE      = "image";
    public static final String PROJECT_PROFILE    = "profile";
    public static final String PROJECT_STYLE      = "style";
    public static final String PROJECT_PERCENT    = "percent";
    public static final String PROJECT_SECTION    = "section";
    public static final String PROJECT_ROWS       = "rows";

    //table to hold step information
    public static final String TABLE_STEPS          = "steps";
    public static final String STEP_ID              = "_idStep";
    public static final String STEP_PROJECT         = "project";
    public static final String STEP_SECTION         = "section";
    public static final String STEP_STEP            = "step";
    public static final String STEP_STATE           = "state";

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
                    + "(" + PROJECT_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + "," + PROJECT_NAME    + " TEXT NOT NULL"
                    + "," + PROJECT_IMAGE   + " TEXT"
                    + "," + PROJECT_PERCENT + " REAL NOT NULL"
                    + "," + PROJECT_ROWS    + " INTEGER NOT NULL"
                    + "," + PROJECT_PROFILE + " INTEGER NOT NULL"
                    + "," + PROJECT_STYLE   + " INTEGER NOT NULL"
                    + "," + PROJECT_SECTION + " INTEGER NOT NULL"
                    + ");";
    private static final String DATABASE_CREATE_STEP =
            "CREATE TABLE " + TABLE_STEPS
                    + "(" + STEP_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + "," + STEP_PROJECT      + " INTEGER NOT NULL"
                    + "," + STEP_SECTION      + " INTEGER NOT NULL"
                    + "," + STEP_STEP         + " INTEGER NOT NULL"
                    + "," + STEP_STATE        + " INTEGER NOT NULL"
                    + ");";

    public DogYarnItSQLHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_PROFILE);
        database.execSQL(DATABASE_CREATE_PROJECT);
        database.execSQL(DATABASE_CREATE_STEP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DogYarnItSQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PROFILES + ";");
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PROJECTS + ";");
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_STEPS + ";");
        onCreate(db);
    }

}