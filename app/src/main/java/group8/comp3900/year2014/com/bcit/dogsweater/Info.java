package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
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

    public void emailAstor( View view ) {
        Intent in = new Intent( Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "astor@knittingastor.com", null ) );
        in.putExtra( Intent.EXTRA_SUBJECT, "Dog Yarn It Help");

        startActivity( Intent.createChooser( in, "Email Astor" ) );
    }

    public void astorWebsite( View view ) {
        String url = "http://www.knittingastor.com/";
        Intent in = new Intent( Intent.ACTION_VIEW );
        in.setData( Uri.parse( url ) );

        startActivity( in );
    }

    public void astorTwitter( View view ) {
        String url = "http://twitter.com/knittingAstor/";
        Intent in = new Intent( Intent.ACTION_VIEW );
        in.setData( Uri.parse( url ) );

        startActivity( in );
    }
}
