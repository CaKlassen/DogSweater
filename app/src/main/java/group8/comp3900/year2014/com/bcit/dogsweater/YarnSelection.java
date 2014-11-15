package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

    /**
     * EditText used to display the gauge of the selected yarn, or for te user
     *   to enter their own, custom gauge
     */
    private EditText gaugeTextInput;


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
        configureGUIReferences();
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
    /**
     * author: Eric Tsang
     * date: November 15 2014
     *
     * creates a project, and associates the selected yarn type and selected
     *   profile with it, then starts the next activity
     */
    public void next(View v) {

        // extract yarn gauge information from the GUI
        int gauge = getGauge((String) yarnTypesSpinner.getSelectedItem());

        // create the style object
        Style s = new Style(Style.getNameFromId(styleId), styleId);
        s.initializeSectionList(Style.makeStyle(styleId));

        // load the profile from the database
        getProfileDataSource().open();
        Profile p = getProfileDataSource().getProfile(profileId);

        // create the new project, and associate the profile, style and yarn
        // gauge with it
        Project newProject = new Project(p);
        newProject.setStyle(s);
        newProject.setName(p.getName() + " - " + s.getName());
        newProject.setGauge(gauge);
        getProfileDataSource().insertProject(newProject);
        getProfileDataSource().close();

        // go to the next activity
        Intent in = new Intent(this, Materials.class);
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
        gaugeTextInput = (EditText) findViewById(R.id.gaugeInput);

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

    /**
     * author: Eric Tsang
     * date: October 18 2014
     *
     * does one-time things to GUI references as necessary to make them behave
     * the way they need to behave for the Activity
     */
    private void configureGUIReferences() {
        yarnTypesSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * author: Eric Tsang
             * date: November 15 2014
             *
             * when a new yarn type is selected, we figure out what its gauge is
             *   and display it on our gaugeTextInput
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedYarnName =
                        (String) yarnTypesSpinner.getItemAtPosition(position);

                String selectedYarnGauge =
                        String.valueOf(getGauge(selectedYarnName));

                if (!gaugeTextInput.getText().toString().equals(selectedYarnGauge)) {
                    gaugeTextInput.setText(selectedYarnGauge);
                }

            }
        });

        gaugeTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            /**
             * author: Eric Tsang
             * date: November 15 2014
             *
             * when a new gauge is entered, find the nearest YarnType name, and
             * set our yarnTypesSpinner
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int selectedGauge = Integer.valueOf(s.toString());
                yarnTypesSpinner.setSelection(getYarnType(selectedGauge), true);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    /**
     * author: Eric Tsang
     * date: November 15 2014
     * revisions: none
     *
     * returns the gauge associated with the passed yarnName.
     */
    private int getGauge(String yarnName) {

        int gauge;

        // figure out what the gauge of the yarn is
        if (yarnName.equals("Fingering")) {
            gauge = 28;
        } else if (yarnName.equals("Spot")) {
            gauge = 24;
        } else if (yarnName.equals("Double Knitting")) {
            gauge = 22;
        } else if (yarnName.equals("Worsted")) {
            gauge = 20;
        } else if (yarnName.equals("Aran")) {
            gauge = 16;
        } else if (yarnName.equals("Bulky")) {
            gauge = 14;
        } else if (yarnName.equals("Super Bulky")) {
            gauge = 10;
        } else {

            // don't know what gauge is; throw exception with helpful message
            throw new RuntimeException(yarnName + " is an undefined kind of " +
                    "yarn; please define it in YarnSelection.getGauge()");

        }

        return gauge;

    }

    /**
     * author: Eric Tsang
     * date: November 15 2014
     * revisions: none
     *
     * returns the gauge associated with the passed yarnName.
     */
    private int getYarnType(int gauge) {

        int minGaugeDiff = Integer.MAX_VALUE;
        int minYarnType = 0;
        String[] yarnNames = getResources().getStringArray(R.array.types);

        // figure out which yarn type is nearest to the passed gauge
        for (int i = 0; i < yarnNames.length; i++) {
            int currGauge = getGauge(yarnNames[i]);
            int currGaugeDiff = Math.abs(currGauge - gauge);

            if (currGaugeDiff < minGaugeDiff) {
                minGaugeDiff = currGaugeDiff;
                minYarnType = i;

            }
        }

        return minYarnType;

    }
}
