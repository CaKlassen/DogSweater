package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class ModifyProfile extends Activity {


    /** used by this activity to access the Profile database */
    private ProfileDataSource db;


    /** intent used to select a photo from some sort of gallery app */
    private static final int SELECT_PHOTO          = 100;

    /** intent used to take a photo from camera */
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private Profile curProfile;

    /** starting intent key to the profileId to pass on */
    public static final String KEY_PROFILE_ID = "Profile Id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        //Get Project data via the intent
        final long projId = getIntent().getExtras().getLong(KEY_PROFILE_ID);

        db = new ProfileDataSource(this);
        db.open();
        curProfile = db.getProfile(projId);
        db.close();

        TextView projName = (TextView) findViewById(R.id.projName);
        projName.setText(curProfile.getName());

        // Set fonts
        TextView title = (TextView) findViewById( R.id.dimenTitle );
        Typeface typeface = Typeface.createFromAsset( getAssets(), "GrandHotel-Regular.otf" );
        title.setTypeface( typeface );

        title = (TextView) findViewById( R.id.projImageLabel );
        title.setTypeface( typeface );

        //Set hints for all boxes
        //A
        EditText dimension;
        dimension = (EditText) findViewById(R.id.ADimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.NECK_DIAMETER).getValue());

        //B
        dimension = (EditText) findViewById(R.id.BDimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.CHEST_DIAMETER).getValue());

        //C
        dimension = (EditText) findViewById(R.id.CDimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.FRONT_LEGS_DISTANCE).getValue());

        //X
        dimension = (EditText) findViewById(R.id.XDimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.NECK_LENGTH).getValue());

        //Y
        dimension = (EditText) findViewById(R.id.YDimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.UNDERBELLY_LENGTH).getValue());

        //Z
        dimension = (EditText) findViewById(R.id.ZDimension);
        dimension.setHint(" " + curProfile.getDimensions().getDimension(Profile.CENTRE_BACK_LENGTH).getValue());

    }


    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        //Change image to the profile image
        final ImageView iv = (ImageView) findViewById(R.id.projImage);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // use a worker thread to load the image
        Uri imageUri = curProfile.getImageURI();
        if (imageUri != null) {
            ThreadManager.loadImage(

                    getApplicationContext(),            // application context
                    imageUri,                           // local uri to image file
                    ThreadManager.CropPattern.DEFAULT,   // crop pattern
                    width / 2,                                // image width

                    // what to do when success
                    new ThreadManager.OnResponseListener() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            iv.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    public void takePhoto(View v)
    {

        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void chooseExisting(View v)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }
    public boolean chooseImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    chooseImage();
                }
                break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK) {

                    db.open();
                    curProfile.setImageURI(imageReturnedIntent.getData().toString());
                    db.updateProfile(curProfile);
                    db.close();
                    Toast.makeText(this, imageReturnedIntent.getData().toString(), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    /*******************************************************
     * Saves all dimensions into the profile if the user changes
     * them. Check through all the edit texts and checks if
     * they contain new data.
     *
     ******************************************************/
    public void saveDimensions(View v)
    {
        EditText dimension;
        boolean changed = false;
        //Set hints for all boxes
        //A
        dimension = (EditText) findViewById(R.id.ADimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.NECK_DIAMETER, newVal);
        }

        //B
        dimension = (EditText) findViewById(R.id.BDimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.CHEST_DIAMETER, newVal);
        }

        //C
        dimension = (EditText) findViewById(R.id.CDimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.FRONT_LEGS_DISTANCE, newVal);
        }

        //X
        dimension = (EditText) findViewById(R.id.XDimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.NECK_LENGTH, newVal);
        }

        //Y
        dimension = (EditText) findViewById(R.id.YDimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.UNDERBELLY_LENGTH, newVal);
        }

        //Z
        dimension = (EditText) findViewById(R.id.ZDimension);
        if (dimension.getText().toString().length() > 0 )
        {
            changed = true;
            double newVal = Double.parseDouble(dimension.getText().toString());
            curProfile.getDimensions().setDimension(Profile.CENTRE_BACK_LENGTH, newVal);
        }

        if (changed)
        {
            db.open();
            db.updateProfile(curProfile);
            db.close();
            Toast.makeText(getApplicationContext(), "Profile has been updated!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No changes to save.", Toast.LENGTH_SHORT).show();
        }


    }

}
