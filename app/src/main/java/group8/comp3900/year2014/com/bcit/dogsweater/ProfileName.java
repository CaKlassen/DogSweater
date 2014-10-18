package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;


public class ProfileName extends Activity {



    ////////////////////////////
    // reference to GUI views //
    ////////////////////////////
    /** text input reference on activity */
    private EditText profileNameInput;



    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);
        initializeGUIReferences();

    }



    // -------------------------------------------------------------------------
    // android callbacks
    // -------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_name, menu);
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
     * goes to the next activity if user input is valid
     */
    public void dogMeasurements(View v) {
        // extract information from GUI
        String profileName = profileNameInput.getText().toString().trim();

        // validate extracted information; do we have enough information to
        // start the next activity?
        if (!profileName.isEmpty())
        {
            /*
            we have enough information to start the next activity; start
            it...our goal here is to create a new profile from scratch.
             */
            Intent in = new Intent(this, DogProfileCreation.class);
            in.putExtra(DogProfileCreation.KEY_PROFILE_NAME, profileName);
            in.putExtra(DogProfileCreation.KEY_DIMENSION_KEYS,
                    Profile.MINIMUM_DIMENSION_KEYS);
            startActivity(in);

        } else {
            /*
            not enough information to continue on with creating a new Profile
             */
            Toast.makeText(this, "please enter a profile name",
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
        profileNameInput = (EditText) findViewById(R.id.measureA);

    }

}
