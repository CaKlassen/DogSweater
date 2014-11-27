package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

        // Override the default font
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "Proxima Nova Reg.otf");

        // Set the home button fonts
        Button b = (Button) findViewById( R.id.newProjectButton );
        Typeface buttonTypeface = Typeface.createFromAsset( getAssets(), "Proxima Nova Bold.otf" );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.manageProfilesButton );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.currentProjectsButton );
        b.setTypeface( buttonTypeface );

        b = (Button) findViewById( R.id.informationButton );
        b.setTypeface( buttonTypeface );


        //TOS Popup
        boolean TOS = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("tos", true);

        //first run
        if (TOS){

            //Create the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Terms Of Service Agreement");
            builder.setMessage("Dog Yarn It Terms of  Service (\"Agreement\")\n" +
                    "\n" +
                    "This Agreement was last modified on November 26th, 2014.\n" +
                    "\n" +
                    "Please read these Terms of Service completely using http://www.knittingastor.com/ which is owned and operated by Knitting Astor. This Agreement documents the legally binding terms and conditions attached to the use of the Site at http://www.knittingastor.com/.\n" +
                    "\n" +
                    "By using or accessing the app in any way, viewing or browsing the app, or adding your own content to the app, you are agreeing to be bound by these Terms of Service.\n" +
                    "\n" +
                    "Intellectual Property\n" +
                    "\n" +
                    "The App and all of its original content are the sole property of Your Nowhere Town and are, as such, fully protected by the appropriate international copyright and other intellectual property rights laws.\n" +
                    "\n" +
                    "Termination\n" +
                    "\n" +
                    "Your Nowhere Town reserves the right to terminate your access to the App, without any advance notice.\n" +
                    "\n" +
                    "Links to Other Websites\n" +
                    "\n" +
                    "Our App does contain a number of links to other websites and online resources that are not owned or controlled by Your Nowhere Town.\n" +
                    "\n" +
                    "Dog Yarn It has no control over, and therefore cannot assume responsibility for, the content or general practices of any of these third party apps and/or services. Therefore, we strongly advise you to read the entire terms and conditions and privacy policy of any site that you visit as a result of following a link that is posted on our App.\n" +
                    "\n" +
                    "Governing Law\n" +
                    "\n" +
                    "This Agreement is governed in accordance with the laws of British Columbia, Canada.\n" +
                    "\n" +
                    "Changes to This Agreement\n" +
                    "\n" +
                    "Dog Yarn it reserves the right to modify these Terms of Service at any time. We do so by posting and drawing attention to the updated terms on the App. Your decision to continue to visit and make use of the App after such changes have been made constitutes your formal acceptance of the new Terms of Service.\n" +
                    "\n" +
                    "Therefore, we ask that you check and review this Agreement for such changes on an occasional basis. Should you not agree to any provision of this Agreement or any changes we make to this Agreement, we ask and advise that you do not use or continue to access the Dog Yarn It app immediately.\n" +
                    "\n" +
                    "Contact Us\n" +
                    "\n" +
                    "If you have any questions about this Agreement, please feel free to contact us at http://www.knittingastor.com/\n" +
                    "\n");

            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Save the state to never show TOS again
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("tos", false)
                            .commit();
                }
            });

            AlertDialog infoDialog = builder.create();
            infoDialog.show();

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MenuHelper m = new MenuHelper(getApplicationContext(), this);
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
        Intent in = new Intent(this, About.class);
        startActivity(in);

        return;

    }


}
