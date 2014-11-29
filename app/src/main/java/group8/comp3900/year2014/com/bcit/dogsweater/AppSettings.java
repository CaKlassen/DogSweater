package group8.comp3900.year2014.com.bcit.dogsweater;

/**
 * Created by Rhea on 27/11/2014.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Unit;


public class AppSettings extends Activity {

    /**
     * canonical name of this class (i.e.: group8.comp3900...classes.Unit)
     *   that's used to build strings that are unique to this class.
     */
    public static final String CLASS_NAME =
            AppSettings.class.getCanonicalName() + ".";

    /////////////////////////////////////////
    // preference file & app settings keys //
    /////////////////////////////////////////
    /** key to the preferences file that's used to save this app's settings. */
    public static final String KEY_PREFERENCE_FILE =
            CLASS_NAME + "KEY_PREFERENCE_FILE";

    public static final String KEY_DEFAULT_UNIT =
            CLASS_NAME + "KEY_DEFAULT_UNIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Unit curUnit = Unit.getDefaultUnit(this);

        Switch uSwitch = (Switch) findViewById(R.id.unitSwitch);

        if ( curUnit == Unit.CENTIMETRES )
        {
            uSwitch.toggle();
        }

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


    @Override
    protected void onResume()
    {
        super.onResume();
        MenuHelper m = new MenuHelper(getApplicationContext(), this);
    }

    public void changeUnit(View v)
    {
        Switch uSwitch = (Switch) v;

        if ( uSwitch.isChecked() )
        {
            Unit.setDefaultUnit(this, Unit.CENTIMETRES);
        }
        if ( !uSwitch.isChecked() )
        {
            Unit.setDefaultUnit(this, Unit.INCHES);
        }

    }

}
