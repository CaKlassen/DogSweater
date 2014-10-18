package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.InvalidParameterException;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Dimensions;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;

/**
 * intent signatures:
 *      creating a new profile from scratch:
 *          KEY_PROFILE_NAME - String           name of the new profile
 *          KEY_DIMENSION_KEYS - String[]       dimension keys needed to fill in
 *
 *      continuing the process of creating the previous new profile:
 *          KEY_DIMENSION_KEYS_INDEX - int
 *
 */
public class DogProfileCreation extends Activity {



    //////////////////////////
    // starting intent keys //
    //////////////////////////
    /** name of the class. (i.e.: group8.comp3900...DogProfileCreation) */
    public static final String CLASS_NAME =
            DogProfileCreation.class.getCanonicalName();

    /** starting intent key for String name of the profile */
    public static final String KEY_PROFILE_NAME =
            CLASS_NAME + "KEY_PROFILE_NAME";

    /** starting intent key for String array of dimensions keys */
    public static final String KEY_DIMENSION_KEYS =
            CLASS_NAME + "KEY_DIMENSION_KEYS";

    /**
     * starting intent key for integer index of the above arrays this instance
     * of the activity is to display
     */
    public static final String KEY_DIMENSION_KEYS_INDEX =
            CLASS_NAME + "KEY_DIMENSION_KEYS_INDEX";



    /////////////////////////////////
    // parsed starting intent data //
    /////////////////////////////////
    /** reference to a profile in that's currently being created */
    private static Profile newProfile = null;

    /** dimension keys to dimensions to be inputted by the user */
    private static String[] dimensionKeys = null;

    /**
     * index in the dimensionKeys and imageUris array that activity is
     * getting
     */
    private int arrayIndex = 0;



    ////////////////////////////
    // reference to GUI views //
    ////////////////////////////
    /** image view reference on activity */
    private ImageView image;

    /** text input reference on activity */
    private EditText dimensionInput;



    /////////////////////
    // database things //
    /////////////////////
    /** database interface object used to save profiles to the database */
    // TODO: discuss with group; should i make this into a static object?
    private ProfileDataSource profileDataSource = new ProfileDataSource(this);



    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile_creation);
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
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     * @param           v    used by the android system
     *
     * goes to the next activity if possible, and adds the dimensional
     * information to our newProfile if possible.
     *
     * EXTRA DETAILS:
     * extract user input, then validate user input. add dimension if possible;
     * re-prompt is necessary. if all dimensional information has been gathered,
     * go to the Yarn activity (the next stage). start another
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
            another DogProfileCreation activity; go to the Yarn activity
            otherwise.
             */

            // add the dimensional information that we just got to newProfile,
            // then to database
            newProfile.getDimensions().setDimension(
                    dimensionKeys[arrayIndex], dimensionValue);
            profileDataSource.open();
            profileDataSource.insertProfile(newProfile);
            profileDataSource.close();

            // do we need to continue gather dimensional information for
            // newProfile?
            Intent in;
            if (arrayIndex < dimensionKeys.length - 1) {
                /* yes; go get them then */
                in = new Intent(this, DogProfileCreation.class);
                in.putExtra(KEY_DIMENSION_KEYS_INDEX, arrayIndex + 1);

            } else {
                /* nope; move on to the next stage */
                in = new Intent(this, Yarn.class);

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
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     *
     * initializes activity instance's View references
     */
    private void initializeGUIReferences() {
        image = (ImageView) findViewById(R.id.imageView);
        dimensionInput = (EditText) findViewById(R.id.measureA);

    }

    /**
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     * @param startIntent   reference to intent used to start this activity
     *
     * parses the activity's starting intent, and assigns them to our starting
     * intent data fields as needed
     */
    private void parseStartingIntent(Intent startIntent) {
        // interpret starting intent; are we creating a profile from scratch or
        // continuing to make a previous one?
        if (startIntent.getIntExtra(KEY_DIMENSION_KEYS_INDEX, -1) == -1) {
            /*
            we are creating a new dog profile from scratch; create a new
            Profile object, and reassign our dimensionKeys and imageUris from
            the startIntent extras
             */
            newProfile = new Profile(
                    startIntent.getStringExtra(KEY_PROFILE_NAME));
            dimensionKeys =
                    startIntent.getStringArrayExtra(KEY_DIMENSION_KEYS);
            arrayIndex = 0;

        } else {
            /*
            we're continuing to gather dimensions for our newProfile object
             */
            arrayIndex = startIntent.getIntExtra(KEY_DIMENSION_KEYS_INDEX, -1);
            if (arrayIndex == -1)
                throw new InvalidParameterException("KEY_DIMENSION_KEYS_INDEX" +
                        " == -1. Start intent did not provide a value for the" +
                        " key: KEY_ARRAY_INDEX.");

        }

    }

    /**
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     *
     * updates the activity's GUI based on starting intent data
     */
    private void updateGUI() {
        // update the GUI
        image.setImageDrawable(
                Dimensions.getDrawable(this, dimensionKeys[arrayIndex]));
        dimensionInput.setHint(
                Dimensions.getFriendly(this, dimensionKeys[arrayIndex]));

    }

}
