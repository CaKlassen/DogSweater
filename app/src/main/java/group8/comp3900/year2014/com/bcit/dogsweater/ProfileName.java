package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button addImageButton;



    ////////////////////////
    // user inputted data //
    ////////////////////////
    /** URI to image chosen by user for the profile that's being created */
    // TODO: make a default image uri. it's not as simple as a drawable i think, because drawables don't have URIs
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

        initializeGUIReferences();
        configureGUIReferences();

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();

        /*
        figure out which context menu we're supposed to open by checking which
        view invoked this method, and open the right context menu.
         */
        switch (v.getId()) {

            case R.id.addImage:
                inflater.inflate(R.menu.add_image_list, menu);
                break;

            default:
                super.onCreateContextMenu(menu, v, menuInfo);
                break;
        }
    }

    public boolean takeImage() {

        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        return true;
    }

    public boolean choseImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.menu_item_take_picture:
                return takeImage();

            case R.id.menu_item_choose_image:
                return choseImage();

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
                    choseImage();
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
            in.putExtra(DogProfileCreation.KEY_PROFILE_IMAGE_URI,
                    profileImageUri);
            startActivity(in);

        } else {
            /*
            not enough information to continue on with creating a new Profile
             */
            Toast.makeText(this, "please enter a profile name",
                    Toast.LENGTH_SHORT).show();

        }

    }

    public void getImageUri(View v) {
        openContextMenu(addImageButton);
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
        addImageButton = (Button) findViewById(R.id.addImage);

    }

    /**
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     *
     * does one-time things to views as necessary to make them behave in a
     * certain way
     */
    private void configureGUIReferences() {
        registerForContextMenu(addImageButton);

    }

}
