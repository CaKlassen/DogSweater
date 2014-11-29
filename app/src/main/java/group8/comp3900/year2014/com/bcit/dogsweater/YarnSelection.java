package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


    ///////////////////
    // instance data //
    ///////////////////
    /**
     * database interface object used to save profiles to the database
     *
     * IMPORTANT: from within this class, do not reference this directly;
     * instead, use the private getter getProfileDataSource().
     */
    private static ProfileDataSource profileDataSource = null;

    /**
     * regarding having the gaugeTextInput change what the yarnTypesSpinner
     *   displays when it's modified by the user, and vice versa, this boolean
     *   is needed to determine if a modification is done by the user, or if
     *   it's done as the result the other input being changed.
     *
     * true if the input is being modified by the application; false otherwise.
     */
    private boolean programmaticChange = false;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarn_selection);

        parseStartingIntent(getIntent());
        initializeGUIReferences();
        configureGUIReferences();


        //change font of the headers
        Typeface titles = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );

        TextView titleText = (TextView) findViewById(R.id.yarnText);
        TextView gaugeText = (TextView) findViewById(R.id.gaugeTitle);
        TextView yarnText = (TextView) findViewById(R.id.yarnType);

        titleText.setTypeface( titles );
        gaugeText.setTypeface( titles );
        yarnText.setTypeface( titles );



    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

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
        int gauge = (gaugeTextInput.getText().toString().isEmpty()) ?
                0 : Integer.valueOf(gaugeTextInput.getText().toString());

        // validate input & bail out if invalid
        if (gauge == 0) {
            Toast.makeText(this, "Please enter a valid gauge",
                    Toast.LENGTH_LONG).show();
            return;
        }

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

        yarnTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * author: Eric Tsang
             * date: November 15 2014
             *
             * when a new yarn type is selected, we figure out what its gauge is
             *   and display it on our gaugeTextInput
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!programmaticChange) {

                    String selectedYarnName =
                            (String) yarnTypesSpinner.getItemAtPosition(i);
                    String selectedYarnGauge =
                            String.valueOf(getGauge(selectedYarnName));

                    if (!gaugeTextInput.getText().toString().equals(
                            selectedYarnGauge)) {
                        gaugeTextInput.setText(selectedYarnGauge);
                    }
                }

                programmaticChange = !programmaticChange;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
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
                if (!programmaticChange) {
                    int selectedGauge = (s.toString().isEmpty())?
                            0 : Integer.valueOf(s.toString());
                    yarnTypesSpinner.setSelection(getYarnType(selectedGauge));
                }

                programmaticChange = !programmaticChange;

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
