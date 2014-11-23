package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Congratulations extends Activity {

    private TextView congratulationTextView;
    private ImageView shareImage;
    private ImageButton addShareImage;

    private static final Uri DEFAULT_IMAGE = Uri.parse("android.resource://group8.comp3900.year2014.com.bcit.dogsweater/drawable/dog_silhouette_sweater");
    private Uri shareImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        initializeGUIReferences();
        configureGUIReferences();

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
        congratulationTextView = (TextView)    findViewById(R.id.congratulationTextView);
        shareImage             = (ImageView)   findViewById(R.id.shareImage);
        addShareImage          = (ImageButton) findViewById(R.id.addShareImage);
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
                    shareImageUri = imageReturnedIntent.getData();
                    shareImage.setImageURI( shareImageUri );
                }
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        /*
        figure out which context menu we're supposed to open by checking which
        view invoked this method, and open the right context menu.
         */
        switch (v.getId()) {

            case R.id.addShareImage:
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

    public void getShareImageUri(View v) {
        Log.d("Take share image button","clicked");
        openContextMenu(addShareImage);
    }

    private void configureGUIReferences() {
        registerForContextMenu(addShareImage);

    }

    public void shareOnFacebook( View v ) {
        if( shareImageUri == null )
        {
            Toast.makeText(this,"Please take an image", Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(getApplicationContext(), FacebookShare.class);
        i.putExtra("ImageURI", ( shareImageUri != null ? shareImageUri : DEFAULT_IMAGE ).toString());
        startActivity(i);
        printKeyHash(this);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {

            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);

            }
        } catch (NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public void GeneralShare(View v) {
        if( shareImageUri == null )
        {
            Toast.makeText(this,"Please take an image", Toast.LENGTH_LONG).show();
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_caption));
        sendIntent.putExtra(Intent.EXTRA_STREAM, ( shareImageUri != null ? shareImageUri : DEFAULT_IMAGE ));
        startActivity(Intent.createChooser(sendIntent,"Share with"));
    }
}
