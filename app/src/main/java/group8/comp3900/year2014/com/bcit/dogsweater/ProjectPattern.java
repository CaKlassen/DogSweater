package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Dimensions;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Step;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class ProjectPattern extends Activity {



    //////////////////////////
    // starting intent keys //
    //////////////////////////
    /** starting intent key to the projectId to pass on */
    public static final String KEY_PROJECT_ID = "Project Id";

    private Project curProject;
    private int curSection;
    private Dimensions dimension;

    /**
     * author: Chris Klassen
     *
     * Sets up the pattern page, loading project data from the database and
     * displaying it on the page based on the current section.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pattern);


        // Retrieve the project id from the bundle
        final long projId = getIntent().getExtras().getLong(KEY_PROJECT_ID);

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

        // Save the current section to the project
        curProject.setSection(curSection);
        db.open();
        db.updateProject(curProject);
        db.close();

        // Populate the title based on the active project
        TextView title = (TextView) findViewById(R.id.projectName);
        title.setText(curProject.getStyle().getSection(curSection).getName());

        // Populate the title based on the active project
        TextView projName = (TextView) findViewById(R.id.patternTitle);
        projName.setText(curProject.getName());

        // Populate the steps based on the active project
        LinearLayout taskList = (LinearLayout) findViewById(R.id.patternTaskList);

        // Retrieve Dimensions object
        dimension = curProject.getDimensions();

        // Create a step number counter
        int stepNum = 0;

        for (Step step : curProject.getStyle().getSection(curSection).getStepList()) {

            // Create and add a step to the screen
            LinearLayout llStep = new LinearLayout(this);
            makeStep(llStep, stepNum);

            taskList.addView(llStep);

            // Increment the step number counter
            stepNum++;
        }

        // Set the path of the 'next' button
        if (curSection < curProject.getStyle().getSectionList().size() - 1) {
            // We still have sections left
            ImageButton b = (ImageButton) findViewById(R.id.patternNextButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), ProjectPattern.class);

                    // Continue forward
                    in.putExtra(KEY_PROJECT_ID, projId);
                    in.putExtra("Current Section", curSection + 1);

                    startActivity(in);
                }
            });
        } else {
            // The project is complete
            ImageButton b = (ImageButton) findViewById(R.id.patternNextButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), Congratulations.class);

                    // Return to the menu
                    startActivity(in);
                }
            });
        }

        // Set the path of the 'back' button
        if (curSection > 0) {
            // This is not the first section
            Button b = (Button) findViewById(R.id.patternBackButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), ProjectPattern.class);

                    // Continue forward
                    in.putExtra(KEY_PROJECT_ID, projId);
                    in.putExtra("Current Section", curSection - 1);

                    startActivity(in);
                }
            });
        } else {
            // This is the first section
            Button b = (Button) findViewById(R.id.patternBackButton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), currentProjects.class);

                    // Return to the menu
                    startActivity(in);
                }
            });
        }

        // Update the row counter with its current value
        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        t.setText("" + curProject.getRowCounter());

    }


    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

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
        if (id == R.id.share_pdf) {
            Intent in = new Intent( this, PdfPreview.class );
            in.putExtra( "Project id", curProject.getId() );
            startActivity( in );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * author: Chris Klassen
     *
     * @param step the layout to append data too
     * @param stepNum the current step number
     *
     * Creates and fills a step based on passed in data.
     */
    public void makeStep(LinearLayout step, int stepNum) {

        step.setId( stepNum );


        // Create the checkbox and give it a unique ID
        CheckBox checkbox = new CheckBox(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.1f);
        params.setMargins(0, 0, 0, 30);

        checkbox.setLayoutParams(params);
        checkbox.setButtonDrawable( R.drawable.checkbox_selector );
        checkbox.setId(stepNum);

        // Assign text to the step
        TextView text = new TextView(this);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        text.setText(curProject.getStyle().getStep(curSection, stepNum, curProject.getDimensions()));
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.9f));


        // Check if the checkbox has been saved to the database already
        ProfileDataSource db = new ProfileDataSource(getApplicationContext());
        int state;
        db.open();

        if ((state = db.getStepState(curProject.getId(), curSection, checkbox.getId())) == -1) {
            // Save the initial step state
            db.saveStepState(curProject.getId(), curSection, checkbox.getId(), false);
            db.close();
        } else {
            // Set the step's state
            checkbox.setChecked((state != 0));
        }

        // Define the functionality of checking/unchecking a checkbox
        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton b, boolean checked) {
                ProfileDataSource db = new ProfileDataSource(getApplicationContext());
                db.open();

                if (checked) {
                    db.saveStepState(curProject.getId(), curSection, b.getId(), true);
                } else {
                    db.saveStepState(curProject.getId(), curSection, b.getId(), false);
                }

                db.close();
            }
        });

        // Add the checkbox to the step
        step.addView(checkbox);
        // Add the text field to the step
        step.addView(text);

        // Define the functionality of the step layout
        step.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileDataSource db = new ProfileDataSource(getApplicationContext());
                db.open();

                int checked = db.getStepState(curProject.getId(), curSection, view.getId());

                if (checked != 1) {
                    db.saveStepState(curProject.getId(), curSection, view.getId(), true);
                    LinearLayout s = (LinearLayout) view;
                    CheckBox cb = (CheckBox) s.getChildAt(0);
                    cb.setChecked(true);
                } else {
                    db.saveStepState(curProject.getId(), curSection, view.getId(), false);
                    LinearLayout s = (LinearLayout) view;
                    CheckBox cb = (CheckBox) s.getChildAt(0);
                    cb.setChecked(false);
                }

                db.close();
            }
        });

        // Set up the step layout parameters
        step.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
    }

    public void decrementRow(View view) {
        curProject.decrementRowCounter();

        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        ProfileDataSource db = new ProfileDataSource(getApplicationContext());
        db.open();
        db.updateProject(curProject);
        db.close();
        t.setText("" + curProject.getRowCounter());
    }

    public void incrementRow(View view) {
        curProject.incrementRowCounter();

        ProfileDataSource db = new ProfileDataSource(getApplicationContext());
        db.open();
        db.updateProject(curProject);
        db.close();

        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        t.setText("" + curProject.getRowCounter());
    }
}
