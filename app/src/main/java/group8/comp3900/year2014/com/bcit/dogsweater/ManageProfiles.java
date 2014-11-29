package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.ManageInfoPopup;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.ProfileManagementGridAdapter;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;


public class ManageProfiles extends Activity {


    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to the one and only GridView for this activity */
    private GridView gridview;

    /**
     * dialog box used to ask if the user wants to delete the profile as this
     *   action will force us to delete related project data from the database.
     */
    private AlertDialog deleteProfileDialog;


    ////////////////////////
    // instance variables //
    ////////////////////////
    /** used by this activity to access the Profile database */
    private ProfileDataSource profileDataSource;

    /** grid adapter for the above GridView. */
    private ProfileManagementGridAdapter gridAdapter;

    /**
     * position of the selected profile in the grid adapter that needs to be
     *   deleted.
     */
    private int profileToDeletePosition;

    /** popup that appears when a tile in the gridAdapter is clicked. */
    private ManageInfoPopup popup;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profiles);

        // Set title font
        TextView title = (TextView) findViewById( R.id.title);
        Typeface titleTypeface = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        title.setTypeface( titleTypeface );

        initializeInstanceData();
        initializeGUIReferences();
        configureGUIReferences();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        // refresh the GUI because things may have changed
        gridAdapter.buildImageList();
        gridAdapter.notifyDataSetChanged();
    }

    //////////////////////////////
    // android system callbacks //
    //////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_profiles, menu);
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

    /////////////////////
    // support methods //
    /////////////////////
    /** initializes instance variables */
    private void initializeInstanceData() {
        profileDataSource = new ProfileDataSource(this);
        gridAdapter = new ProfileManagementGridAdapter(this);

    }

    /** gets references to GUI views for this activity */
    private void initializeGUIReferences() {
        gridview = (GridView) findViewById(R.id.manageProfilesGridView);

    }

    /** one time behavioural configuration to the activity's views go here */
    private void configureGUIReferences() {
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                popup = new ManageInfoPopup(v.getContext(), (Dialogable) gridAdapter.getItem(position));

                popup.setOnDeleteButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileToDeletePosition = position;
                        getDeleteProfileDialog().show();
                    }
                });

                popup.setOnModifyButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),ModifyProfile.class);

                        Profile curProfile = ((Dialogable<Profile>)gridAdapter.getItem( position )).getItem();
                        in.putExtra( "Profile Id", curProfile.getId() );
                        startActivity(in);
                        popup.dismiss();
                    }

                });
                popup.show();

            }

        });

    }

    private AlertDialog getDeleteProfileDialog() {

        if (deleteProfileDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.delete_profile_prompt));
            builder.setPositiveButton(
                    R.string.delete,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Profile profileToDelete = ((Dialogable<Profile>) gridAdapter.getItem(profileToDeletePosition)).getItem();

                            //Delete from database
                            profileDataSource.open();
                            profileDataSource.deleteProjectsWithProfile(profileToDelete.getId());
                            profileDataSource.deleteProfile(profileToDelete);
                            profileDataSource.close();
                            gridAdapter.remove(profileToDeletePosition);
                            popup.dismiss();

                        }

                    });
            builder.setNegativeButton(
                    R.string.cancel,
                    null);

            deleteProfileDialog = builder.create();
        }

        return deleteProfileDialog;
    }

}
