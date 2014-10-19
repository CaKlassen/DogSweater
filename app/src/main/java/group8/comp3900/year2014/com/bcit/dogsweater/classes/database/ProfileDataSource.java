package group8.comp3900.year2014.com.bcit.dogsweater.classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Style;

public class ProfileDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DogYarnItSQLHelper dbHelper;

    private static final String[] columns = {

            DogYarnItSQLHelper.PROFILE_ID,
            DogYarnItSQLHelper.PROFILE_NAME,
            DogYarnItSQLHelper.PROFILE_DIMENSIONS,
            DogYarnItSQLHelper.PROFILE_IMAGE
    };

    public ProfileDataSource(Context context) {

        dbHelper = new DogYarnItSQLHelper(context);
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

        Cursor cursor = database.query( DogYarnItSQLHelper.TABLE_PROFILES
                                      , columns
                                      , null
                                      , null
                                      , null
                                      , null
                                      , null );

        cursor.moveToFirst();
        while( !cursor.isAfterLast() ) {
            Profile profile = cursorToProfile( cursor );
            profiles.add(profile);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return profiles;
    }

    public Profile getProfile(long profileId) {

        Profile profile;

        Cursor cursor = database.query( DogYarnItSQLHelper.TABLE_PROFILES
                , columns                                   // columns
                , "ID = ?"                                  // where clause
                , new String[] {Long.toString(profileId)}   // where params
                , null
                , null
                , null );

        cursor.moveToFirst();
        profile = cursorToProfile(cursor);
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

        values.put( DogYarnItSQLHelper.PROJECT_NAME, project.getName() );
        values.put( DogYarnItSQLHelper.PROJECT_PERCENT, project.getPercentDone() );
        values.put( DogYarnItSQLHelper.PROJECT_ROWS, project.getRowCounter() );
        values.put( DogYarnItSQLHelper.PROJECT_PROFILE, project.getProfile().getId() );
        values.put( DogYarnItSQLHelper.PROJECT_STYLE, project.getStyle().getName() );

        long insertId = database.insert( DogYarnItSQLHelper.TABLE_PROJECTS
                , null
                , values );

        project.setId(insertId);
    }

    public void deleteProject(Project project) {

        long projectId = project.getId();

        database.delete( DogYarnItSQLHelper.TABLE_PROJECTS
                , DogYarnItSQLHelper.PROJECT_ID + " = " + projectId
                , null );
    }

    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<Project>();

        Cursor cursor = database.query( DogYarnItSQLHelper.TABLE_PROJECTS
                , columns
                , null
                , null
                , null
                , null
                , null );

        cursor.moveToFirst();
        while( !cursor.isAfterLast() ) {
            Project project = cursorToProject(cursor);
            projects.add(project);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return projects;
    }

    public Project getProject(long projectId) {

        Project project;

        Cursor cursor = database.query( DogYarnItSQLHelper.TABLE_PROJECTS
                , columns                                   // columns
                , "ID = ?"                                  // where clause
                , new String[] {Long.toString(projectId)}   // where params
                , null
                , null
                , null );

        cursor.moveToFirst();
        project = cursorToProject(cursor);
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
        // TODO: Get task completion state?
        Project project = new Project( cursor.getString(
                cursor.getColumnIndex(
                        DogYarnItSQLHelper.PROJECT_NAME
                )
        )
                , cursor.getFloat (
                cursor.getColumnIndex (
                        DogYarnItSQLHelper.PROJECT_PERCENT
                )
        )
                , cursor.getInt (
                cursor.getColumnIndex (
                        DogYarnItSQLHelper.PROJECT_ROWS
                )
        )
                , p
                , s
        );

        project.setId( cursor.getLong (
                        cursor.getColumnIndex (
                                DogYarnItSQLHelper.PROJECT_ID
                        )
                )
        );

        return project;
    }
}