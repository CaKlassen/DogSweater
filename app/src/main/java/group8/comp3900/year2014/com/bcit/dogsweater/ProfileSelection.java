package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.InfoPopup;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.ProfileSelectionGridAdapter;


public class ProfileSelection extends Activity {





    // -------------------------------------------------------------------------
    // activity lifecycle callbacks
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        GridView gridview = (GridView) findViewById(R.id.profileGridView);
        final ProfileSelectionGridAdapter gridAdapter = new ProfileSelectionGridAdapter(this);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (position == 0)
                {
                    Intent in = new Intent(getApplicationContext(), ProfileName.class);
                    startActivity(in);
                }
                else
                {
                    InfoPopup popup = new InfoPopup(v.getContext(), gridAdapter.getImageList(), position , "group8.comp3900.year2014.com.bcit.dogsweater.Yarn", "PROFILE" );
                }
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

