package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.CurrentProjectPopup;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.ProjectGridAdapter;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

public class currentProjects extends Activity {
    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to the one and only GridView for this activity */
    private GridView gridview;


    ////////////////////////
    // instance variables //
    ////////////////////////
    /** used by this activity to access the Profile database */
    private ProfileDataSource profileDataSource;

    /** grid adapter for the above GridView */
    private ProjectGridAdapter gridAdapter;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_projects);

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        //Initalize the GUI
        initializeInstanceData();
        initializeGUIReferences();
        configureGUIReferences();
    }


    //////////////////////////////
    // android system callbacks //
    //////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.current_projects, menu);
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


    /////////////////////
    // support methods //
    /////////////////////
    /** initializes instance variables */
    private void initializeInstanceData() {
        profileDataSource = new ProfileDataSource(this);
        gridAdapter = new ProjectGridAdapter(this);

    }

    /** gets references to GUI views for this activity */
    private void initializeGUIReferences() {
        gridview = (GridView) findViewById(R.id.projectGridView);

    }

    private int curProjectPosition = -1;

    /** one time behavioural configuration to the activity's views go here */
    private void configureGUIReferences() {

        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                final CurrentProjectPopup popup;
                popup = new CurrentProjectPopup(v.getContext(), (Dialogable) gridAdapter.getItem(position));
                popup.setOnDeleteButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Delete from database
                        profileDataSource.open();
                        List<Project> projects = profileDataSource.getAllProjects();
                        profileDataSource.deleteProject( ((Dialogable<Project>)gridAdapter.getItem( position )).getItem() );
                        profileDataSource.close();
                        gridAdapter.remove(position);
                        popup.dismiss();

                    }

                });

                popup.setOnContinueButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(), ProjectPattern.class);

                        profileDataSource.open();
                        List<Project> projects = profileDataSource.getAllProjects();
                        Project curProject = ((Dialogable<Project>)gridAdapter.getItem( position )).getItem();
                        profileDataSource.close();


                        in.putExtra( "Project Id", curProject.getId() );
                        in.putExtra( "Current Section", curProject.getSection() );
                        startActivity(in);
                    }

                });

                popup.setOnTakePhotoButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        takeImage();
                        Log.d( "Take image position", "" + position );
                        curProjectPosition = position;
                    }

                });

                popup.setOnChooseImageButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        chooseImage();
                        Log.d( "Choose image position", "" + position );
                        curProjectPosition = position;
                    }

                });
            popup.show();

            }

        });

    }

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
                    chooseImage();
                }
                break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK) {

                    profileDataSource.open();
                    Project curProject = ((Dialogable<Project>)gridAdapter.getItem( curProjectPosition )).getItem();
                    curProject.setImageURI( imageReturnedIntent.getData().toString() );
                    profileDataSource.updateProject( curProject );
                    profileDataSource.close();

                    Log.d( "Project ID", curProject.toString() );

                    Toast.makeText(this, imageReturnedIntent.getData().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}