package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Dimensions;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Step;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class ProjectPattern extends Activity {
    private Project curProject;
    private int curSection;
    private Dimensions dimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pattern);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

        // Retrieve the project id from the bundle
        final long projId = getIntent().getExtras().getLong("Project Id");

        // Retrieve the current section from the bundle
        if (getIntent().getExtras().containsKey("Current Section")) {
            curSection = getIntent().getExtras().getInt("Current Section");
        } else {
            // This is our first section
            curSection = 0;
        }

        // Build the project from the database
        ProfileDataSource db = new ProfileDataSource(this);
        db.open();
        curProject = db.getProject(projId);
        db.close();

        // Populate the title based on the active project
        TextView title = (TextView) findViewById(R.id.patternTitle);
        title.setText( curProject.getStyle().getSection(curSection).getName());

        // Populate the steps based on the active project
        LinearLayout taskList = (LinearLayout) findViewById(R.id.patternTaskList);

        // Retrieve Dimensions object
        dimension = curProject.getProfile().getDimensions();

        // Create a step number counter
        int stepNum = 0;

        for(Step step : curProject.getStyle().getSection(curSection).getStepList()) {

            // Create and add a step to the screen
            LinearLayout llStep = new LinearLayout( this );
            makeStep( llStep, stepNum );

            taskList.addView(llStep);

            // Increment the step number counter
            stepNum++;
        }

        // Set the path of the 'next' button
        if (curSection < curProject.getStyle().getSectionList().size() - 1) {
            // We still have sections left
            Button b = (Button) findViewById(R.id.patternNextButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), ProjectPattern.class);

                    // Continue forward
                    in.putExtra("Project Id", projId);
                    in.putExtra("Current Section", curSection + 1);

                    startActivity(in);
                }
            });
        } else {
            // The project is complete
            Button b = (Button) findViewById(R.id.patternNextButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);

                    // Return to the menu
                    startActivity(in);
                }
            });
        }

        // Update the row counter with its current value
        TextView t = (TextView) findViewById( R.id.patternRowCounter );
        t.setText( "" + curProject.getRowCounter() );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_pattern, menu);
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

    public void makeStep( LinearLayout step, int stepNum ) {

        // Assign text to the step
        TextView text = new TextView( this );
        text.setText(curProject.getStyle().getStep(curSection, stepNum, dimension));
        text.setLayoutParams( new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.9f ) );

        step.addView( text );

        // Create the checkbox and give it a unique ID
        CheckBox checkbox = new CheckBox( this );
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.1f));
        checkbox.setId( stepNum );

        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton b, boolean checked) {
                ProfileDataSource db = new ProfileDataSource(getApplicationContext());
                db.open();

                if ( checked ) {
                    //db.saveStepState( curProject, curSection, b.getId(), true );
                } else {
                    //db.saveStepState( curProject, curSection, b.getId(), false );
                }

                db.close();
            }
        });

        step.addView( checkbox );

        // Set up the step layout parameters
        step.setLayoutParams( new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT ) );
    }

    public void decrementRow( View view ) {
        curProject.decrementRowCounter();

        TextView t = (TextView) findViewById( R.id.patternRowCounter );
        t.setText( "" + curProject.getRowCounter() );
    }

    public void incrementRow( View view ) {
        curProject.incrementRowCounter();

        TextView t = (TextView) findViewById( R.id.patternRowCounter );
        t.setText( "" + curProject.getRowCounter() );
    }
}
