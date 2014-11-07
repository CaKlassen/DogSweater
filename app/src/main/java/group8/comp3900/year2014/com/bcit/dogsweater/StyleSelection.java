package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.StyleGridAdapter;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.StylePopup;


public class StyleSelection extends Activity {



    //////////////////////////
    // starting intent keys //
    //////////////////////////
    /** name of the class. (i.e.: group8.comp3900...DogProfileCreation) */
    public static final String CLASS_NAME =
            StyleSelection.class.getCanonicalName() + ".";

    /**
     * starting intent key to the id of the profile that will be added to the
     *   new knitting project that will be created when enough information has
     *   been gathered.
     */
    public static final String KEY_PROFILE_ID = CLASS_NAME + "KEY_PROFILE_ID";


    //////////////////////////////
    // parsed start intent data //
    //////////////////////////////
    /**
     * id of the profile that will be added to the new knitting project that
     *   will be created when enough information has been gathered.
     */
    private long profileId;


    ////////////////////
    // GUI references //
    ////////////////////
    /**
     * GridView used on this activity to display Styles for the user to select
     *   for their new knitting project
     */
    private GridView gridview;


    //////////////////////////
    // life cycle callbacks //
    //////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        parseStartingIntent(getIntent());
        initializeGUIReferences();
        configureGUIReferences();
    }


    //////////////////////
    // system callbacks //
    //////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.style, menu);
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
    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * parses the starting intent into the activity's instance variables.
     */
    public void parseStartingIntent(Intent startIntent) {
        profileId = startIntent.getLongExtra(KEY_PROFILE_ID, -1);
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * initializes activity instance's View references
     */
    public void initializeGUIReferences() {
        gridview = (GridView) findViewById(R.id.styleGridView);
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * does one-time things to views as necessary to make them behave in a
     * certain way
     */
    public void configureGUIReferences() {

        final StyleGridAdapter gridAdapter = new StyleGridAdapter(this);

        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                StylePopup popup = new StylePopup(v.getContext());

                popup.setImageBackgroundResource(
                        gridAdapter.getImageList().get(position));
                popup.setTitleText("STYLE");
                popup.setContinueButtonText("SELECT THIS STYLE");
                popup.setContinueButtonOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            //TODO: CORRECT LOCATION IN PATTERN
                            public void onClick(View v) {
                                Context context = v.getContext();
                                Intent in = new Intent(context, YarnSelection.class);

                                // Add the project id to the bundle
                                in.putExtra(YarnSelection.KEY_PROFILE_ID, profileId);
                                in.putExtra(YarnSelection.KEY_STYLE_ID, position);
                                context.startActivity(in);

                            }
                        });
                popup.show();
            }
        });
    }
}
