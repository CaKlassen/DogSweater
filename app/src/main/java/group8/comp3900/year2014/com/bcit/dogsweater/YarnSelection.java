package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Style;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class YarnSelection extends Activity {


    //////////////////////////
    // starting intent keys //
    //////////////////////////
    /** name of the class. (i.e.: group8.comp3900...DogProfileCreation) */
    public static final String CLASS_NAME =
            YarnSelection.class.getCanonicalName() + ".";

    /**
     * starting intent key to the id of the profile that will be associated with
     * the project to be created
     */
    public static final String KEY_PROFILE_ID = CLASS_NAME + "KEY_PROFILE_ID";

    /**
     * starting intent key to the id of the style that will be associated with
     * the project to be created
     */
    public static final String KEY_STYLE_ID = CLASS_NAME + "KEY_STYLE_ID";


    //////////////////////////////
    // parsed start intent data //
    //////////////////////////////
    /**
     * id of the profile that will be added to the new knitting project that
     *   will be created when enough information has been gathered.
     */
    private long profileId;

    /**
     * id of the style that will be added to the new knitting project that will
     *   be created when enough information has been gathered.
     */
    private int styleId;


    ////////////////////
    // GUI references //
    ////////////////////
    /** spinner used to select the type of yarn that's being used */
    private Spinner yarnTypesSpinner;


    /////////////////////
    // database things //
    /////////////////////
    /**
     * database interface object used to save profiles to the database
     *
     * IMPORTANT: from within this class, do not reference this directly;
     * instead, use the private getter getProfileDataSource().
     */
    private static ProfileDataSource profileDataSource = null;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarn_selection);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        parseStartingIntent(getIntent());
        initializeGUIReferences();
    }


    //////////////////////
    // system callbacks //
    //////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.yarn_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    ///////////////
    // interface //
    ///////////////
    public void next(View v) {
        // extract information from the GUI
        // TODO: convert to a real gauge - Eric
        String selectedItem = (String) yarnTypesSpinner.getSelectedItem();
        Log.d("selectedItem: ", selectedItem);

        // create a style object
        Style s = new Style(Style.getNameFromId(styleId), styleId);
        s.initializeSectionList(Style.makeStyle(styleId));

        // load the profile from the database
        getProfileDataSource().open();
        Profile p = getProfileDataSource().getProfile(profileId);

        // create the new project
        Project newProject = new Project(p);
        newProject.setStyle(s);
        newProject.setName(p.getName() + " - " + s.getName());
        newProject.setGauge(10.0);  // TODO: use a real gauge - Eric
        getProfileDataSource().insertProject(newProject);
        getProfileDataSource().close();

        // go to the next activity
        Intent in = new Intent(this, ProjectPattern.class);
        in.putExtra(ProjectPattern.KEY_PROJECT_ID, newProject.getId());
        startActivity(in);
    }


    /////////////////////
    // support methods //
    /////////////////////
    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * parses the starting intent into the activity's instance variables.
     */
    private void parseStartingIntent(Intent startIntent) {
        profileId = startIntent.getLongExtra(KEY_PROFILE_ID, -1);
        styleId = startIntent.getIntExtra(KEY_STYLE_ID, -1);

        if (profileId == -1 || styleId == -1) {
            throw new RuntimeException("startIntent was missing the long " +
                    "extras KEY_PROFILE_ID or KEY_STYLE_ID");
        }
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * initializes activity instance's View references
     */
    private void initializeGUIReferences() {
        yarnTypesSpinner = (Spinner) findViewById(R.id.types);
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * returns the ProfileDataSource instance associated with this class that's
     * used to interface with the database & instantiates it if necessary.
     */
    private ProfileDataSource getProfileDataSource() {
        if (profileDataSource == null) {
            profileDataSource = new ProfileDataSource(this);
        }

        return profileDataSource;

    }
}
