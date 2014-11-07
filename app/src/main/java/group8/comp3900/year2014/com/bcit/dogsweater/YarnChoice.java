package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class YarnChoice extends Activity {

    /////////////////////////////////
    // parsed starting intent data //
    /////////////////////////////////
    /** id of a project in this application's database */
    private long projId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarn);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);


        // Get the project id from the bundle
        projId = getIntent().getExtras().getLong("Project Id");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.yarn, menu);
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

    public void needYarn(View v ) {
        Intent in = new Intent(this, YarnSelection.class);
        // Add the project id to the bundle
        in.putExtra("Project Id", projId);

        startActivity(in);
    }


    public void haveYarn(View v ) {
        Intent in = new Intent(this, Materials.class);
        // Add the project id to the bundle
        in.putExtra("Project Id", projId);

        startActivity(in);
    }


}
