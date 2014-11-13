package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;


public class ProfileName extends Activity {



    ////////////////////////////
    // android system intents //
    ////////////////////////////
    /** intent used to select a photo from some sort of gallery app */
    private static final int SELECT_PHOTO          = 100;

    /** intent used to take a photo from camera */
    private static final int REQUEST_IMAGE_CAPTURE = 101;



    ////////////////////////////
    // reference to GUI views //
    ////////////////////////////
    /** text input reference on activity */
    private EditText profileNameInput;

    /** button used to add an image to the being created profile */
    private ImageButton addImageButton;




    ////////////////
    // class data //
    ////////////////
    /**
     * true if a new profile is being created; the user is entering information
     * about a profile, but it's not saved into the database yet
     *
     * this is set to true whenever a new instance of this activity is created,
     * and set to false when the last DogProfileCreation is left.
     */
    private static boolean profileCreationInProgress;



    ///////////////////
    // instance data //
    ///////////////////
    /**
     * URI to image chosen by user for the profile that's being created. value
     * it is initialized with is a uri to a default image if the user doesn't
     * pick an image themselves.
     */
    private String profileImageUri = "android.resource://group8.comp3900.year2014.com.bcit.dogsweater/drawable/dog_silhouette";




    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        // set this to true whenever this activity is created from nothing
        // because we are creating a new Profile
        profileCreationInProgress = true;

        initializeGUIReferences();
        configureGUIReferences();

        TextView nameDog = (TextView) findViewById(R.id.nameDog);
        TextView takePhoto = (TextView) findViewById(R.id.takePhoto);

        Typeface textFont = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        nameDog.setTypeface( textFont );
        takePhoto.setTypeface( textFont );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // when we come back to this activity; no profile creation is in
        // progress, finish
        if (!profileCreationInProgress) {
            finish();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        /*
        figure out which context menu we're supposed to open by checking which
        view invoked this method, and open the right context menu.
         */
        switch (v.getId()) {

            case R.id.addImage:
                inflater.inflate(R.menu.add_image_list, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.menu_item_take_picture:
                // TODO on Eric's nexus 7, this doesn't seem to start the right startActivityForResult thing.
                return takeImage();

            case R.id.menu_item_choose_image:
                return chooseImage();

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    // TODO: this doesn't seem to work; after taking a picture, and coming back to this page, the chooseImage doesn't launch the choose image thing on Eric's nexus 7
                    chooseImage();
                }
                break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK) {
                    profileImageUri = imageReturnedIntent.getData().toString();
                }
                break;
        }
    }



    // -------------------------------------------------------------------------
    // interface methods
    // -------------------------------------------------------------------------
    /**
     * sets profileCreationInProgress to false to signal that no more profile
     * creation is in progress. only instances of the DogProfileCreation should
     * be invoking this method because that activity is the last activity that's
     * used in the profile creation process.
     *
     * @param caller Activity that is calling thia method
     */
    public static void finishProfileCreation(Activity caller) {
        try {
            DogProfileCreation activity = (DogProfileCreation) caller;
            profileCreationInProgress = false;
        } catch(ClassCastException e) {
            throw new RuntimeException("the activity invokes this method must be DogProfileCreation");
        }
    }

    /**
     * returns true if a new profile is currently being created; false
     * otherwise.
     *
     * @return true if a new profile is being created; false otherwise.
     */
    public static boolean isProfileCreationInProgress() {
        return profileCreationInProgress;
    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param v used by the android system
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
            in.putExtra(
                    DogProfileCreation.KEY_PROFILE_NAME,
                    profileName);
            in.putExtra(
                    DogProfileCreation.KEY_PROFILE_IMAGE_URI,
                    profileImageUri);
            in.putExtra(
                    DogProfileCreation.KEY_DIMENSION_KEYS,
                    Profile.MIN_DIMENSION_KEYS);
            in.putExtra(
                    DogProfileCreation.KEY_DEFAULT_VALUE_EXPRESSIONS,
                    Profile.MIN_DIMENSION_EXPRESSIONS);
            startActivity(in);

        } else {
            /*
            not enough information to continue on with creating a new Profile
             */
            Toast.makeText(this, "please enter a profile name",
                    Toast.LENGTH_SHORT).show();

        }

    }

    public boolean takeImage() {

        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        return true;
    }

    public boolean chooseImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        return true;
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     *
     * opens a context menu that provides different options that can be used to
     * associate a URI to an image to the profile that's being created.
     *
     * @param v used by the android system
     */
    public void getImageUri(View v) {
        openContextMenu(addImageButton);
    }



    // -------------------------------------------------------------------------
    // support methods
    // -------------------------------------------------------------------------
    /**
     * author: Eric Tsang
     * date: October 18 2014
     *
     * initializes activity instance's View references
     */
    private void initializeGUIReferences() {
        profileNameInput = (EditText) findViewById(R.id.measureA);
        addImageButton = (ImageButton) findViewById(R.id.addImage);

    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     *
     * does one-time things to views as necessary to make them behave in a
     * certain way
     */
    private void configureGUIReferences() {
        registerForContextMenu(addImageButton);

    }

}
