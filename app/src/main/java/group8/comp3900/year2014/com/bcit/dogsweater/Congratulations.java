package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class Congratulations extends Activity {

    private TextView congratulationTextView;
    private ImageView shareImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        initializeGUIReferences();

        new MenuHelper(getApplicationContext(), this);

        Typeface textFont = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        congratulationTextView.setTypeface( textFont );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.congratulations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeGUIReferences() {
        congratulationTextView = (TextView)  findViewById(R.id.congratulationTextView);
        shareImage             = (ImageView) findViewById(R.id.shareImage);
    }

     ///////////////////
    // Image Capture //
   ///////////////////

    /** intent used to select a photo from some sort of gallery app */
    private static final int SELECT_PHOTO          = 100;

    /** intent used to take a photo from camera */
    private static final int REQUEST_IMAGE_CAPTURE = 101;

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
                    shareImage.setImageURI( imageReturnedIntent.getData() );
                }
                break;
        }
    }
}
