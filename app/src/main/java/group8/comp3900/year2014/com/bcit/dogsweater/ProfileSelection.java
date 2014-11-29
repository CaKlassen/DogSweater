package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.Popup;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.ProfileSelectionGridAdapter;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;


public class ProfileSelection extends Activity {


    ///////////////////
    // instance data //
    ///////////////////
    /** adapter for the activity's GridView */
    private ProfileSelectionGridAdapter mGridAdapter;


    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        // Set title font
        TextView title = (TextView) findViewById( R.id.title);
        Typeface titleTypeface = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        title.setTypeface( titleTypeface );


        GridView gridview = (GridView) findViewById(R.id.profileGridView);
        mGridAdapter = new ProfileSelectionGridAdapter(this);
        gridview.setAdapter(mGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                if (position == 0)
                {
                    Intent in = new Intent(getApplicationContext(), ProfileName.class);
                    startActivity(in);
                }
                else
                {
                    Dialogable dialogable = (Dialogable) mGridAdapter.getItem(position);
                    Popup popup = new Popup(v.getContext());
                    popup.setButtonText("SELECT THIS PROFILE");
                    popup.setDescriptionText("");
                    popup.setImageByDrawableId(R.drawable.dog_silhouette);  // in case no user defined image exists...
                    popup.setImageByUri(dialogable.getDialogueImageUri());  // try to load the user defined image
                    popup.setTitleText(dialogable.getDialogueTitle());
                    popup.setButtonOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(ProfileSelection.this,
                                    StyleSelection.class);
                            i.putExtra(StyleSelection.KEY_PROFILE_ID,
                                    mGridAdapter.getItemId(position));
                            startActivity(i);
                        }
                    });
                    popup.show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        mGridAdapter.buildImageList();
    }



    // -------------------------------------------------------------------------
    // android callbacks
    // -------------------------------------------------------------------------
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
        return super.onOptionsItemSelected(item);
    }

}

