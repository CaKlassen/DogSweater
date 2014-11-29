package group8.comp3900.year2014.com.bcit.dogsweater;

/**
 * Created by Rhea on 27/11/2014.
 */

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Unit;


public class AppSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // Set fonts
        TextView title = (TextView) findViewById( R.id.title );
        Typeface typeface = Typeface.createFromAsset( getAssets(), "GrandHotel-Regular.otf" );
        title.setTypeface( typeface );


        Unit curUnit = Unit.getDefaultUnit();

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
            Unit.setDefaultUnit(Unit.CENTIMETRES);
        }
        if ( !uSwitch.isChecked() )
        {
            Unit.setDefaultUnit(Unit.INCHES);
        }

    }

}
