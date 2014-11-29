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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;



public class ModifyProject extends Activity {

    /** used by this activity to access the Profile database */
    private ProfileDataSource db;


    /** intent used to select a photo from some sort of gallery app */
    private static final int SELECT_PHOTO          = 100;

    /** intent used to take a photo from camera */
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private Project curProject;

    /** starting intent key to the projectId to pass on */
    public static final String KEY_PROJECT_ID = "Project Id";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_project);

        //Get Project data via the intent
        final long projId = getIntent().getExtras().getLong(KEY_PROJECT_ID);

        db = new ProfileDataSource(this);
        db.open();
        curProject = db.getProject(projId);
        db.close();

        TextView projName = (TextView) findViewById(R.id.projName);
        projName.setText(curProject.getName());

        //Change fonts
        TextView projLabel = (TextView) findViewById( R.id.projImageLabel);
        Typeface typeface = Typeface.createFromAsset( getAssets(), "GrandHotel-Regular.otf" );
        projLabel.setTypeface(typeface);

        Typeface typefaceTitle = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        projName.setTypeface(typefaceTitle);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        //Change image to the project image
        final ImageView iv = (ImageView) findViewById(R.id.projImage);

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // use a worker thread to load the image
        Uri imageUri = curProject.getImageURI();
        if (imageUri != null) {
            ThreadManager.loadImage(

                    getApplicationContext(),            // application context
                    imageUri,                           // local uri to image file
                    ThreadManager.CropPattern.DEFAULT,   // crop pattern
                    width / 2,                          // image width

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
                    curProject.setImageURI(imageReturnedIntent.getData().toString());
                    db.updateProject(curProject);
                    db.close();
                    Toast.makeText(this, imageReturnedIntent.getData().toString(), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}
