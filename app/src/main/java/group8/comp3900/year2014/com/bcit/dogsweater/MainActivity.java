package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
