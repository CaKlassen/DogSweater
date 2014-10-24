package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.StyleGridAdapter;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups.StylePopup;


public class StyleSelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        // Retrieve the project id from the Bundle
        final long projId;
        projId = getIntent().getExtras().getLong("projId");

        GridView gridview = (GridView) findViewById(R.id.styleGridView);
        final StyleGridAdapter gridAdapter = new StyleGridAdapter(this);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO: modify below to instantiate an InfoPopup the new way because Eric changed it
               StylePopup popup = new StylePopup(v.getContext(), gridAdapter.getImageList(), position , "group8.comp3900.year2014.com.bcit.dogsweater.Materials", "STYLE", projId);
            }
        });
    }


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
