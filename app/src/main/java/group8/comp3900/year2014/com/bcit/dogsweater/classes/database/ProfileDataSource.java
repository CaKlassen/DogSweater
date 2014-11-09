package group8.comp3900.year2014.com.bcit.dogsweater.classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Style;

public class ProfileDataSource {

    // Database fields
    private SQLiteDatabase database;
    private static DogYarnItSQLHelper dbHelper;


    private static final String[] columns = {

            DogYarnItSQLHelper.PROFILE_ID,
            DogYarnItSQLHelper.PROFILE_NAME,
            DogYarnItSQLHelper.PROFILE_DIMENSIONS,
            DogYarnItSQLHelper.PROFILE_IMAGE
    };

    private static final String[] columnsProject = {

            DogYarnItSQLHelper.PROJECT_ID,
            DogYarnItSQLHelper.PROJECT_NAME,
            DogYarnItSQLHelper.PROJECT_PERCENT,
            DogYarnItSQLHelper.PROJECT_ROWS,
            DogYarnItSQLHelper.PROJECT_PROFILE,
            DogYarnItSQLHelper.PROJECT_STYLE,
            DogYarnItSQLHelper.PROJECT_SECTION
    };

    private static final String[] columnsStep = {

            DogYarnItSQLHelper.STEP_ID,
            DogYarnItSQLHelper.STEP_PROJECT,
            DogYarnItSQLHelper.STEP_SECTION,
            DogYarnItSQLHelper.STEP_STEP,
            DogYarnItSQLHelper.STEP_STATE
    };

    public ProfileDataSource(Context context) {

        dbHelper = getInstance(context);
    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }


    /*******************************************
    * PROFILE METHODS
    *******************************************/

    public void insertProfile(Profile profile) {

        ContentValues values = new ContentValues();

        values.put( DogYarnItSQLHelper.PROFILE_NAME, profile.getName() );
        values.put( DogYarnItSQLHelper.PROFILE_IMAGE, profile.getImageURI().toString() );
        values.put( DogYarnItSQLHelper.PROFILE_DIMENSIONS, profile.getDimensions().stringify() );

        long insertId = database.insert( DogYarnItSQLHelper.TABLE_PROFILES
                                       , null
                                       , values );

        profile.setId( insertId );
    }

    public void deleteProfile(Profile profile) {

        long profileId = profile.getId();

        database.delete( DogYarnItSQLHelper.TABLE_PROFILES
                       , DogYarnItSQLHelper.PROFILE_ID + " = " + profileId
                       , null );
    }

    public List<Profile> getAllProfiles() {

        List<Profile> profiles = new ArrayList<Profile>();

        Cursor cursor = database.query(DogYarnItSQLHelper.TABLE_PROFILES
                , columns
                , null
                , null
                , null
                , null
                , null);
        if (cursor != null && cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Profile profile = cursorToProfile(cursor);
                profiles.add(profile);
                cursor.moveToNext();
            }
            cursor.close();
        }
        else
        {
           //No profiles available
        }
        return profiles;
    }

    public Profile getProfile(long profileId) {

        Profile profile;

        String query = "SELECT * FROM "  + DogYarnItSQLHelper.TABLE_PROFILES+  " WHERE " + DogYarnItSQLHelper.PROFILE_ID + " =  " + profileId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0)
        {
            Log.d("Cursor", "rows");
            cursor.moveToFirst();
            profile = cursorToProfile(cursor);
        }
        else
        {
            Log.d("Cursor", "No rows");
            profile = null;
        }
        cursor.close();

        return profile;
    }

    private Profile cursorToProfile(Cursor cursor) {
        Profile profile = new Profile( cursor.getString (
                                         cursor.getColumnIndex (
                                           DogYarnItSQLHelper.PROFILE_NAME
                                         )
                                       )
                                     , cursor.getString (
                                         cursor.getColumnIndex (
                                          DogYarnItSQLHelper.PROFILE_DIMENSIONS
                                         )
                                       )
                                     , cursor.getString (
                                         cursor.getColumnIndex (
                                          DogYarnItSQLHelper.PROFILE_IMAGE
                                         )
                                       )
                                     );
        profile.setId( cursor.getLong (
                         cursor.getColumnIndex (
                           DogYarnItSQLHelper.PROFILE_ID
                         )
                       )
                     );
        return profile;
    }

    /*******************************************
     * PROJECT METHODS
     *******************************************/

    public void insertProject(Project project) {

        ContentValues values = new ContentValues();

        values.put( DogYarnItSQLHelper.PROJECT_NAME,       project.getName()                   );
        values.put( DogYarnItSQLHelper.PROJECT_IMAGE, "" + project.getImageURI()               );
        values.put( DogYarnItSQLHelper.PROJECT_PERCENT,    project.getPercentDone()            );
        values.put( DogYarnItSQLHelper.PROJECT_ROWS,       project.getRowCounter()             );
        values.put( DogYarnItSQLHelper.PROJECT_PROFILE,    project.getProfile().getId()        );
        values.put( DogYarnItSQLHelper.PROJECT_STYLE,      project.getStyle().getStyleNumber() );
        values.put( DogYarnItSQLHelper.PROJECT_SECTION,    project.getSection()                );
        values.put( DogYarnItSQLHelper.PROJECT_GAUGE,      project.getGauge()                  );

        long insertId = database.insert( DogYarnItSQLHelper.TABLE_PROJECTS
                , null
                , values );

        project.setId(insertId);
    }

    public void updateProject(Project project) {

        ContentValues values = new ContentValues();

        values.put( DogYarnItSQLHelper.PROJECT_NAME,       project.getName()            );
        values.put( DogYarnItSQLHelper.PROJECT_IMAGE, "" + project.getImageURI()        );
        values.put( DogYarnItSQLHelper.PROJECT_PERCENT,    project.getPercentDone()     );
        values.put( DogYarnItSQLHelper.PROJECT_ROWS,       project.getRowCounter()      );
        values.put( DogYarnItSQLHelper.PROJECT_PROFILE,    project.getProfile().getId() );
        values.put( DogYarnItSQLHelper.PROJECT_STYLE,      project.getStyle().getName() );
        values.put( DogYarnItSQLHelper.PROJECT_SECTION,    project.getSection()         );
        values.put( DogYarnItSQLHelper.PROJECT_GAUGE,      project.getGauge()                  );

        database.update(DogYarnItSQLHelper.TABLE_PROJECTS, values,  DogYarnItSQLHelper.PROJECT_ID + " = " +  project.getId(), null );


    }

    public void deleteProject(Project project) {

        long projectId = project.getId();

        database.delete( DogYarnItSQLHelper.TABLE_PROJECTS
                , DogYarnItSQLHelper.PROJECT_ID + " = " + projectId
                , null );
    }

    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<Project>();

        String query = "SELECT * FROM " + DogYarnItSQLHelper.TABLE_PROJECTS;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0 )
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Project project = cursorToProject(cursor);
                projects.add(project);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();

        }
        else
        {
            //No projects available
        }

        return projects;
    }

    public Project getProject(long projectId) {

        Project project;
        Log.d("Project ID: ", " " + projectId);

        String query = "SELECT * FROM "  + DogYarnItSQLHelper.TABLE_PROJECTS +  " WHERE " + DogYarnItSQLHelper.PROJECT_ID + " =  " + projectId;
        Cursor cursor = database.rawQuery(query, null);

        Log.d("count: ", " " + cursor.getCount());

        if ( cursor.getCount() > 0)
        {
            Log.d("Project", "not null");
            cursor.moveToFirst();
            project = cursorToProject(cursor);
        }
        else
        {
            Log.d("Project", "null");
            project = null;
        }
        cursor.close();

        return project;
    }

    private Project cursorToProject(Cursor cursor) {
        // Retrieve the linked profile from the database
        Profile p = getProfile(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DogYarnItSQLHelper.PROJECT_PROFILE
                        )
                )
        );

        // Retrieve the linked style via id
        int styleNumber = cursor.getInt(
                cursor.getColumnIndex(
                        DogYarnItSQLHelper.PROJECT_STYLE
                )
        );

        Style s = new Style(Style.getNameFromId(styleNumber), styleNumber);
        s.initializeSectionList(Style.makeStyle(styleNumber));

        // Initialize the project
        Project project = new Project(
                                cursor.getString(
                                  cursor.getColumnIndex(
                                    DogYarnItSQLHelper.PROJECT_NAME ) )
                              , cursor.getFloat (
                                  cursor.getColumnIndex (
                                    DogYarnItSQLHelper.PROJECT_PERCENT ) )
                              , cursor.getInt (
                                  cursor.getColumnIndex (
                                    DogYarnItSQLHelper.PROJECT_ROWS ) )
                              , p
                              , s
                              , cursor.getInt(
                                  cursor.getColumnIndex(
                                    DogYarnItSQLHelper.PROJECT_SECTION ) ) );

        project.setImageURI(
                  cursor.getString(
                    cursor.getColumnIndex(
                      DogYarnItSQLHelper.PROJECT_IMAGE ) ) );

        project.setGauge(
                  cursor.getDouble(
                    cursor.getColumnIndex(
                      DogYarnItSQLHelper.PROJECT_GAUGE ) ) );

        project.setId(
                  cursor.getLong (
                    cursor.getColumnIndex (
                      DogYarnItSQLHelper.PROJECT_ID ) ) );

        return project;
    }

    /*******************************************
     * STEP METHODS
     *******************************************/
    public void saveStepState( long projNum, int secNum, int stepNum, boolean state ) {

        ContentValues values = new ContentValues();
        Cursor cursor = database.rawQuery("SELECT * from " + dbHelper.TABLE_STEPS + " WHERE " +
                dbHelper.STEP_PROJECT + " = " + projNum + " AND " + dbHelper.STEP_SECTION +
                " = " + secNum + " AND " + dbHelper.STEP_STEP + " = " + stepNum, null);

        values.put( DogYarnItSQLHelper.STEP_PROJECT, projNum );
        values.put( DogYarnItSQLHelper.STEP_SECTION, secNum );
        values.put( DogYarnItSQLHelper.STEP_STEP, stepNum );
        values.put( DogYarnItSQLHelper.STEP_STATE, state );

        if ( cursor.getCount() > 0 ) {
            database.update( DogYarnItSQLHelper.TABLE_STEPS, values, dbHelper.STEP_PROJECT + " = " + projNum +
                    " AND " + dbHelper.STEP_SECTION + " = " + secNum + " AND " + dbHelper.STEP_STEP +
                    " = " + stepNum, null );
        } else {
            database.insert( DogYarnItSQLHelper.TABLE_STEPS
                    , null
                    , values );
        }

        cursor.close();
    }

    public int getStepState( long projNum, int secNum, int stepNum ) {
        Cursor cursor = database.rawQuery("SELECT " + dbHelper.STEP_STATE + " from " + dbHelper.TABLE_STEPS + " WHERE " +
                dbHelper.STEP_PROJECT + " = " + projNum + " AND " + dbHelper.STEP_SECTION +
                " = " + secNum + " AND " + dbHelper.STEP_STEP + " = " + stepNum, null);

        if ( cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            int state = cursor.getInt( 0 );
            cursor.close();
            return state;
        } else {
            // This does not exist yet
            cursor.close();
            return -1;
        }

    }



    public static DogYarnItSQLHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbHelper == null) {
            dbHelper = new DogYarnItSQLHelper(context.getApplicationContext());
        }
        return dbHelper;
    }
}