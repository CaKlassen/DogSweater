package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;

/**
 * intent signatures:
 *      creating a new profile from scratch:
 *          KEY_PROFILE_NAME - String
 *          KEY_DIMENSION_KEYS - String[]
 *          KEY_DIMENSION_IMAGE_URIS - String[]
 *
 *      continuing the process of creating a new profile:
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

    /** starting intent key for String array of image urls */
    public static final String KEY_DIMENSION_IMAGE_URIS =
            CLASS_NAME + "KEY_DIMENSION_IMAGE_URIS";

    /**
     * starting intent key for integer index of the above arrays this instance
     * of the activity is to display
     */
    public static final String KEY_ARRAY_INDEX =
            CLASS_NAME + "KEY_ARRAY_INDEX";



    /////////////////////////////////
    // parsed starting intent data //
    /////////////////////////////////
    /** reference to a profile in that's currently being created */
    private static Profile newProfile = null;

    /** dimension keys to dimensions to be inputted by the user */
    private static String[] dimensionKeys = null;

    /** images to display when getting value for dimension */
    private static String[] imageUris = null;

    /**
     * index in the dimensionKeys and imageUris array that activity is
     * getting
     */
    private static int arrayIndex = 0;


    ////////////////////////////
    // reference to GUI views //
    ////////////////////////////
    /** image view reference on activity */
    private ImageView image;

    /** title text reference on activity */
    private TextView titleText;

    /** text input reference on activity */
    private EditText dimensionInput;



    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile_creation);
        initializeGUIReferences();
        parseStartingIntent(getIntent());

    }


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
    public void next(View view) {
        Intent intent = new Intent(this, Yarn.class);
        startActivity(intent);

    }



    // -------------------------------------------------------------------------
    // support methods
    // -------------------------------------------------------------------------
    private void initializeGUIReferences() {
        image = (ImageView) findViewById(R.id.imageView);
        titleText = (TextView) findViewById(R.id.profileText);
        dimensionInput = (EditText) findViewById(R.id.measureA);

    }

    private void parseStartingIntent(Intent startIntent) {

        // interpret starting intent; are wee creating a profile from scratch or
        // continuing to make a previous one
        if (startIntent.getStringExtra(KEY_ARRAY_INDEX) == null) {
            /*
            we are creating a new dog profile from scratch; create a new
            Profile object, and reassign our dimensionKeys and imageUris from
            the startIntent extras
             */
            newProfile = new Profile(
                    startIntent.getStringExtra(KEY_PROFILE_NAME));
            dimensionKeys =
                    startIntent.getStringArrayExtra(KEY_DIMENSION_KEYS);
            imageUris = startIntent.getStringArrayExtra(
                    KEY_DIMENSION_IMAGE_URIS);
            arrayIndex = 0;

        } else {
            /*
            we're continuing to gather dimensions for our newProfile object
             */
            arrayIndex = startIntent.getIntExtra(KEY_ARRAY_INDEX, -1);
            if (arrayIndex == -1)
                throw new InvalidParameterException("KEY_ARRAY_INDEX == " +
                        "-1. Start intent did not provide a value for the " +
                        "key: KEY_ARRAY_INDEX");

        }

        // update the GUI depending on what the start intent told us to do
        /*try {
            Class resString = R.string.class;

            int descriptionId = resString.getField(dimensionKeys[arrayIndex]
            + "Description"
            ).getInt();
            int friendlyId = resString.getField(dimensionKeys[arrayIndex] +
            "Friendle").getInt();

            getResources().getString();
        } catch(Exception e) {}*/


    }

}
