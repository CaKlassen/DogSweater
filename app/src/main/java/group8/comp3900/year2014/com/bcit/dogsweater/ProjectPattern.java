package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Dimensions;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Step;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class ProjectPattern extends Activity {
    private Project curProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pattern);

        // Retrieve the project id from the bundle
        final long projId = getIntent().getExtras().getLong("Project Id");
        Log.d("FUCK", "" + projId);

        // Retrieve the current section from the bundle
        final int curSection;

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
        Dimensions d = curProject.getProfile().getDimensions();

        // Create a step number counter
        int stepNum = 0;

        for(Step step : curProject.getStyle().getSection(curSection).getStepList()) {

            // Create and add a step to the screen
            Button b = new Button(this);
            b.setText(curProject.getStyle().getStep(curSection, stepNum, d));

            taskList.addView(b);

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
}
