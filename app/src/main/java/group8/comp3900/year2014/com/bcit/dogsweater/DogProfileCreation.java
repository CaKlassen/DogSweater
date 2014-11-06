package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Dimensions;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;

/**
 * intent signatures:
 *   creating a new profile from scratch:
 *     KEY_PROFILE_NAME - String name of the new profile
 *     KEY_DIMENSION_KEYS - String[] dimension keys needed to fill in
 *     KEY_DEFAULT_VALUE_EXPRESSIONS - String[] array of expressions in string
 *         form (i.e.: "A + B - 12") used to compute the default value of a
 *         Dimension if needed
 *     KEY_PROFILE_IMAGE_URI - String string URI to image for profile
 *
 *   continuing the process of creating the previous new profile:
 *     KEY_PROFILE_NAME - String name of the new profile
 *     KEY_DIMENSION_KEYS - String[] dimension keys needed to fill in
 *     KEY_DEFAULT_VALUE_EXPRESSIONS - String[] array of expressions in string
 *         form (i.e.: "A + B - 12") used to compute the default value of a
 *         Dimension if needed
 *     KEY_DIMENSION_VALUES - double[] values of each dimension gathered and
 *         added by previous instances of this activity
 *     KEY_PROFILE_IMAGE_URI - String string URI to image for profile
 *     KEY_DIMENSION_KEYS_INDEX - int
 */
public class DogProfileCreation extends Activity {



    //////////////////////////
    // starting intent keys //
    //////////////////////////
    /** name of the class. (i.e.: group8.comp3900...DogProfileCreation) */
    public static final String CLASS_NAME =
            DogProfileCreation.class.getCanonicalName() + ".";

    /** starting intent key for String name of the profile */
    public static final String KEY_PROFILE_NAME =
            CLASS_NAME + "KEY_PROFILE_NAME";

    /** starting intent key for String array of dimensions keys */
    public static final String KEY_DIMENSION_KEYS =
            CLASS_NAME + "KEY_DIMENSION_KEYS";

    /**
     * starting intent key for String array of expressions used to calculate the
     * default values of a dimension
     */
    public static final String KEY_DEFAULT_VALUE_EXPRESSIONS =
            CLASS_NAME + "KEY_DEFAULT_VALUE_EXPRESSIONS";

    /**
     * starting intent key to a double[] that stores the values gathered for
     * each key from previous instances of this activity
     */
    public static final String KEY_DIMENSION_VALUES =
            CLASS_NAME + "KEY_DIMENSION_VALUES";

    /** starting intent key for String URI of image to use for the profile */
    public static final String KEY_PROFILE_IMAGE_URI =
            CLASS_NAME + "KEY_PROFILE_IMAGE_URI";

    /**
     * starting intent key for integer index of the above arrays this instance
     * of the activity is to display
     */
    public static final String KEY_DIMENSION_KEYS_INDEX =
            CLASS_NAME + "KEY_DIMENSION_KEYS_INDEX";



    /////////////////////////////////
    // parsed starting intent data //
    /////////////////////////////////
    /** list of dimension keys of dimensions to be inputted by the user */
    private String[] dimensionKeys = null;

    /** dimension values gathered by previous instances of this class */
    private double[] dimensionValues = null;

    /**
     * list of expressions used to calculate the default value of a dimension
     */
    private String[] defaultValueExpressions = null;

    /**
     * index in the dimensionKeys and imageUris array that this instance of this
     * activity is to display & gather information about (from user).
     */
    private int arrayIndex = 0;



    ////////////////////////////
    // reference to GUI views //
    ////////////////////////////
    /** reference to title text above the image view on the activity */
    private TextView titleText;

    /** image view reference on activity */
    private ImageView image;

    /** text input reference on activity */
    private EditText dimensionInput;



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



    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile_creation);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        parseStartingIntent(getIntent());
        initializeGUIReferences();
        updateGUI();

    }



    // -------------------------------------------------------------------------
    // android callbacks
    // -------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dog_profile_creation, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }



    // -------------------------------------------------------------------------
    // interface methods
    // -------------------------------------------------------------------------
    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param v used by the android system
     *
     * goes to the next activity if possible, and adds the dimensional
     * information to our newProfile if possible.
     *
     * EXTRA DETAILS:
     * extract user input, then validate user input. add dimension if possible;
     * re-prompt if necessary. if all dimensional information has been gathered,
     * go to the Yarn activity (the next stage); start another
     * DogProfileCreation activity to continue gathering dimensional information
     * otherwise.
     */
    public void next(View v) {
        // extract information from GUI
        String dimensionString = dimensionInput.getText().toString();
        Double dimensionValue;
        try {
            dimensionValue = Double.valueOf(dimensionString);

        } catch(Exception e) {
            dimensionValue = -1D;

        }

        // validate extracted information; take action depending on result
        if (!dimensionString.isEmpty() && dimensionValue >= 0) {
            /*
            we have enough information to go to the next activity; do we need to
            gather more dimension information about this profile first before we
            move to the next stage? if we do, get the next dimension by starting
            another DogProfileCreation activity; go to the Style choice activity
            otherwise.
             */

            // record the dimensional information that we just got
            dimensionValues[arrayIndex] = dimensionValue;

            // do we need to continue gather dimensional information for
            // newProfile?
            Intent in;
            if (arrayIndex < dimensionKeys.length - 1) {
                /* yes; go get them then */
                in = getIntent();
                in.putExtra(KEY_DIMENSION_KEYS_INDEX, arrayIndex + 1);
                in.putExtra(KEY_DIMENSION_VALUES, dimensionValues);

            } else {
                /*
                nope; save the newProfile into our database & move on to the
                next stage
                 */
                in = new Intent(this, StyleSelection.class);

                try {
                    Profile newProfile = new Profile(
                            getIntent().getStringExtra(KEY_PROFILE_NAME),
                            getDimensions(),
                            getIntent().getStringExtra(KEY_PROFILE_IMAGE_URI));
                    getProfileDataSource().open();
                    getProfileDataSource().insertProfile(newProfile);

                    //Create a new project
                    //TODO: WHAT IF THEY BAIL FROM HERE AND THERE IS NO STYLE?
                    // TODO: create the project at the very end, after the style has been selected and we have a reference to both the profile, and style... since the profile s in the DB, it has an ID, we can remember the ID to extract our profile later.
                    Project newProject = new Project(newProfile);
                    getProfileDataSource().insertProject(newProject);

                    long projectId = newProject.getId();
                    in.putExtra("projId", projectId );


                } catch(Exception e) {
                    Log.d(this.toString(), e.toString());
                } finally {
                    getProfileDataSource().close();
                }

            }
            startActivity(in);

        } else {
            /* not enough information to add current dimension; inform user */
            Toast.makeText(this, "please enter a valid dimension",
                    Toast.LENGTH_SHORT).show();

        }

    }



    // -------------------------------------------------------------------------
    // support methods
    // -------------------------------------------------------------------------
    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     *
     * initializes activity instance's View references
     */
    private void initializeGUIReferences() {
        titleText = (TextView) findViewById(R.id.profileText);
        image = (ImageView) findViewById(R.id.imageView);
        dimensionInput = (EditText) findViewById(R.id.measureA);

    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param startIntent reference to intent used to start this activity
     *
     * parses the activity's starting intent's extras, and assigns them to our
     * starting intent instance data fields as needed
     */
    private void parseStartingIntent(Intent startIntent) {

        defaultValueExpressions = startIntent.getStringArrayExtra(
                KEY_DEFAULT_VALUE_EXPRESSIONS);
        dimensionKeys = startIntent.getStringArrayExtra(
                KEY_DIMENSION_KEYS);
        dimensionValues = startIntent.hasExtra(KEY_DIMENSION_VALUES) ?
                startIntent.getDoubleArrayExtra(KEY_DIMENSION_VALUES) :
                new double[dimensionKeys.length];
        arrayIndex = startIntent.getIntExtra(KEY_DIMENSION_KEYS_INDEX, 0);

    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     *
     * updates the activity's GUI based on starting intent data
     */
    private void updateGUI() {
        // update the GUI
        image.setImageDrawable(
                Dimensions.getDrawable(this, dimensionKeys[arrayIndex]));
        dimensionInput.setHint(
                Dimensions.getFriendly(this, dimensionKeys[arrayIndex]));
        titleText.setText(
                Dimensions.getFriendly(this, dimensionKeys[arrayIndex]) + ":");

        // if there is a default or entered value for this dimension, prefill
        // the input
        if (!defaultValueExpressions[arrayIndex].isEmpty()
                || dimensionValues[arrayIndex] != 0) {
            dimensionInput.setText((dimensionValues[arrayIndex] == 0) ?
                    getDimensions().parseExpression(
                            defaultValueExpressions[arrayIndex]) :
                    String.valueOf(dimensionValues[arrayIndex]));

        }

    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * instantiates & returns the Dimensions instance that's currently being
     * build by this & passed instances of DogProfileCreation.
     */
    private Dimensions getDimensions() {

        // creating the dimension object to return & return it...
        Dimensions profileDimensions = new Dimensions();
        for (int i = 0; i < dimensionKeys.length; i++) {
            profileDimensions.setDimension(dimensionKeys[i], dimensionValues[i],
                    defaultValueExpressions[i]);
        }
        return profileDimensions;
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
