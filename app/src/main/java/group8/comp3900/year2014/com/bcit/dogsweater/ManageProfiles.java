package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

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


    ////////////////////////
    // instance variables //
    ////////////////////////
    /** used by this activity to access the Profile database */
    private ProfileDataSource profileDataSource;

    /** grid adapter for the above GridView */
    private ProfileManagementGridAdapter gridAdapter;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profiles);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

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
        getMenuInflater().inflate(R.menu.manage_profiles, menu);
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
                final ManageInfoPopup popup;
                popup = new ManageInfoPopup(v.getContext(), (Dialogable) gridAdapter.getItem(position));
                popup.setOnDeleteButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Delete from database
                        profileDataSource.open();
                        List<Profile> profiles = profileDataSource.getAllProfiles();
                        profileDataSource.deleteProfile((profiles.get(position)));
                        profileDataSource.close();
                        gridAdapter.remove(position);
                        popup.dismiss();

                    }

                });
                //TODO: MODIFICATION OF PROFILES
                popup.setOnModifyButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ManageProfiles.this, "Modifying item!", Toast.LENGTH_SHORT).show();
                    }

                });
                popup.show();

            }

        });

    }

}
