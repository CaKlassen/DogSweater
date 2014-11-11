package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        // Set the activity fonts
        Button b = (Button) findViewById( R.id.newProjectButton );
        Typeface buttonTypeface = Typeface.createFromAsset( getAssets(), "ProximaNova-Bold.otf" );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.manageProfilesButton );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.currentProjectsButton );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.informationButton );
        b.setTypeface( buttonTypeface );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void newProject(View v)
    {
        Intent in = new Intent(this, ProfileSelection.class);
        startActivity(in);

        return;

    }

    public void currentProjects(View v)
    {
        Intent in = new Intent(this, currentProjects.class);
        startActivity(in);

        return;

    }

    public void manageProfiles(View v)
    {
        Intent in = new Intent(this, ManageProfiles.class);
        startActivity(in);

        return;

    }

    public void info(View v)
    {
        Intent in = new Intent(this, Info.class);
        startActivity(in);

        return;

    }


}
