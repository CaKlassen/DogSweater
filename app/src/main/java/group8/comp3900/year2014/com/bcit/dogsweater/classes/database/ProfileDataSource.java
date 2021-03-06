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
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Section;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Style;

public class ProfileDataSource {

    // Database fields
    private SQLiteDatabase database;
    private static DogYarnItSQLHelper dbHelper;
    private Context context;


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

        this.context = context;
        dbHelper = getInstance(context);
    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

        Log.d("Database Creation" , ""+database);
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
                                     , context
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

    public void updateProfile(Profile profile) {

        ContentValues values = new ContentValues();

        values.put( DogYarnItSQLHelper.PROFILE_NAME,       profile.getName() );
        values.put( DogYarnItSQLHelper.PROFILE_IMAGE,      profile.getImageURI().toString() );
        values.put( DogYarnItSQLHelper.PROFILE_DIMENSIONS, profile.getDimensions().stringify() );


        database.update(DogYarnItSQLHelper.TABLE_PROFILES, values,  DogYarnItSQLHelper.PROFILE_ID + " = " +  profile.getId(), null );


    }

    /*******************************************
     * PROJECT METHODS
     *******************************************/

    /**
     * author: Chris Klassen
     * @param project a passed in project
     *
     * Saves a project to the database for the first time.
     */
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

    /**
     * author: Chris Klassen
     * @param project a passed in project
     *
     * Updates the status of a project in the database.
     */
    public void updateProject(Project project) {

        ContentValues values = new ContentValues();

        values.put( DogYarnItSQLHelper.PROJECT_NAME,       project.getName()            );
        values.put( DogYarnItSQLHelper.PROJECT_IMAGE, "" + project.getImageURI()        );
        values.put( DogYarnItSQLHelper.PROJECT_PERCENT,    project.getPercentDone()     );
        values.put( DogYarnItSQLHelper.PROJECT_ROWS,       project.getRowCounter()      );
        values.put( DogYarnItSQLHelper.PROJECT_PROFILE,    project.getProfile().getId() );
        values.put( DogYarnItSQLHelper.PROJECT_STYLE,      project.getStyle().getStyleNumber() );
        values.put( DogYarnItSQLHelper.PROJECT_SECTION,    project.getSection()         );
        values.put( DogYarnItSQLHelper.PROJECT_GAUGE,      project.getGauge()           );

        database.update(DogYarnItSQLHelper.TABLE_PROJECTS, values,  DogYarnItSQLHelper.PROJECT_ID + " = " +  project.getId(), null );


    }

    /**
     * author: Eric Tsang
     * date: 23 November 2014
     *
     * returns list of projects for the passed profile
     *
     * @param profileId id of the profile that projects should contain
     * @return list of projects for the passed profile
     */
    public List<Project> getProjectsWithProfile(long profileId) {

        List<Project> projects = new ArrayList<Project>();

        Cursor cursor = database.query(DogYarnItSQLHelper.TABLE_PROJECTS
                , columnsProject
                , DogYarnItSQLHelper.PROJECT_PROFILE + "=?"
                , new String[] {String.valueOf(profileId)}
                , null
                , null
                , null);
        if (cursor != null && cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Project project = cursorToProject(cursor);
                projects.add(project);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return projects;
    }

    public void deleteProjectsWithProfile(long profileId) {

        database.delete( DogYarnItSQLHelper.TABLE_PROJECTS
                , DogYarnItSQLHelper.PROJECT_PROFILE + "=?"
                , new String[] {String.valueOf(profileId)} );
    }

    /**
     * author: Chris Klassen
     * @param project a passed in project
     *
     * Removes a project from the database.
     */
    public void deleteProject(Project project) {

        long projectId = project.getId();

        database.delete( DogYarnItSQLHelper.TABLE_PROJECTS
                , DogYarnItSQLHelper.PROJECT_ID + " = " + projectId
                , null );
    }

    /**
     * author: Chris Klassen
     * @return a list of projects created
     *
     * Returns a List of all projects in the database.
     */
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

        return projects;
    }

    /**
     * author: Chris Klassen
     * @param projectId the ID number of the requested project
     * @return the project returned
     *
     * Retrieves a project from the database and returns it, or a null reference
     * if no project with that ID exists.
     */
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

    /**
     * author: Chris Klassen
     * @param cursor the database row containing the project information
     * @return the created project object
     *
     * Constructs a project from database values.
     */
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
        Project project = new Project( context
                              , cursor.getString(
                                  cursor.getColumnIndex(
                                    DogYarnItSQLHelper.PROJECT_NAME ) )
                              , cursor.getFloat(
                cursor.getColumnIndex(
                        DogYarnItSQLHelper.PROJECT_PERCENT))
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

    /**
     * author: Chris Klassen
     * @param projNum the desired project
     * @param secNum the desired section
     * @param stepNum the desired step
     * @param state the state of the step to save
     *
     * Saves the state of a specific project step.
     */
    public void saveStepState( long projNum, int secNum, int stepNum, boolean state ) {

        ContentValues values = new ContentValues();
        Cursor cursor = database.rawQuery("SELECT * from " + dbHelper.TABLE_STEPS + " WHERE " +
                dbHelper.STEP_PROJECT + " = " + projNum + " AND " + dbHelper.STEP_SECTION +
                " = " + secNum + " AND " + dbHelper.STEP_STEP + " = " + stepNum, null);

        values.put( DogYarnItSQLHelper.STEP_PROJECT, projNum );
        values.put( DogYarnItSQLHelper.STEP_SECTION, secNum );
        values.put( DogYarnItSQLHelper.STEP_STEP, stepNum );
        values.put( DogYarnItSQLHelper.STEP_STATE, state ? 1 : 0 );

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

    /**
     * author: Chris Klassen
     * @param projNum the desired project
     * @param secNum the desired section
     * @param stepNum the desired step
     * @return the state of the step
     *
     * Returns the state of a specific project step.
     */
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

    /**
     * author: Chris Klassen
     * @param projNum the desired project
     * @return the percent completion of a specific project
     *
     * Returns the percent completion of a project.
     */
    public float getPercentDone( long projNum ) {
        Log.d("Database", ""+database);
        Cursor cursor = database.rawQuery("SELECT " +
                dbHelper.STEP_STATE + " from " +
                dbHelper.TABLE_STEPS + " WHERE " +
                dbHelper.STEP_PROJECT + " = " + projNum, null);
        int doneCount = 0;
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(dbHelper.STEP_STATE)) == 1)
                    ++doneCount;
            }
        }

        // Get total step number
        cursor.close();

        Project p = getProject( projNum );
        int totalCount = 0;

        for ( Section s : p.getStyle().getSectionList() ) {
            totalCount += s.getStepList().size();
        }

        try {
            return 100.0f * doneCount / totalCount;
        }
        finally {
            cursor.close();
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