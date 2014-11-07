package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    //For PDF generation
    private Intent mShareIntent;
    private OutputStream os;
    private View inflatedFirstView;
    private View inflatedViewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pattern);

        //Create the menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

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
        TextView title = (TextView) findViewById(R.id.patternTitle);
        title.setText(curProject.getStyle().getSection(curSection).getName());

        // Populate the steps based on the active project
        LinearLayout taskList = (LinearLayout) findViewById(R.id.patternTaskList);

        // Retrieve Dimensions object
        dimension = curProject.getProfile().getDimensions();

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
            Button b = (Button) findViewById(R.id.patternNextButton);
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
        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        t.setText("" + curProject.getRowCounter());

        inflatedFirstView = getLayoutInflater().inflate(R.layout.pdf_page_first, null);
        inflatedViewContent = getLayoutInflater().inflate(R.layout.pdf_page_body, null);
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

    public void makeStep(LinearLayout step, int stepNum) {

        // Assign text to the step
        TextView text = new TextView(this);
        text.setText(curProject.getStyle().getStep(curSection, stepNum, dimension));
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.9f));

        // Add the text field to the step
        step.addView(text);

        // Create the checkbox and give it a unique ID
        CheckBox checkbox = new CheckBox(this);
        checkbox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,
                0.1f));
        checkbox.setId(stepNum);

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

        // Set up the step layout parameters
        step.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
    }

    public void decrementRow(View view) {
        curProject.decrementRowCounter();

        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        t.setText("" + curProject.getRowCounter());
    }

    public void incrementRow(View view) {
        curProject.incrementRowCounter();

        TextView t = (TextView) findViewById(R.id.patternRowCounter);
        t.setText("" + curProject.getRowCounter());
    }

    public void createPattern(View v) {
        new Thread().start();

        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setResolution(new PrintAttributes.Resolution("DogYarnIt", PRINT_SERVICE, 450, 700)).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();

        // Generate the PDF based on the given project
        PdfDocument document = new PrintedPdfDocument(this, printAttrs);
        int pageHeight = printAttrs.getMediaSize().getHeightMils() / 1000 * 72;
        int pageWidth = printAttrs.getMediaSize().getWidthMils() / 1000 * 72;

        generatePDF(document, pageHeight, pageWidth);

        File pdf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "Pattern.pdf");
        try {

            //Write to phone
            os = new FileOutputStream(pdf);
            document.writeTo(os);
            document.close();
            os.close();

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }

    }

    public void shareButton(View v)
    {
        File pdf = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "Pattern.pdf");
        Uri uri = Uri.fromFile(pdf);
        shareDocument(uri);
    }

    private void generatePDF(PdfDocument pdf, int height, int width) {
        PdfDocument.PageInfo pageInfo;
        PdfDocument.Page page;

        // Loop through all sections
        for (int i = 0; i < curProject.getStyle().getSectionList().size(); i++) {

            pageInfo = new PdfDocument.PageInfo.Builder(width, height, i + 1).create();
            page = pdf.startPage(pageInfo);

            if (i == 0) {
                // Inflate the page

                // First page of the PDF
                View content = inflatedFirstView.findViewById(R.id.PDF_first_root);

                // Dynamically insert data into the page
                TextView pName = (TextView) inflatedFirstView.findViewById(R.id.PDF_first_projName);
                pName.setText(curProject.getName());

                TextView sHead = (TextView) inflatedFirstView.findViewById(R.id.PDF_first_sectionHeader);
                sHead.setText(curProject.getStyle().getSection(i).getName());

                LinearLayout section = (LinearLayout) inflatedFirstView.findViewById(R.id.PDF_first_section);

                // Loop through the steps in the section
                for (int j = 0; j < curProject.getStyle().getSection(i).getStepList().size(); j++) {
                    TextView newStep = new TextView(this);
                    newStep.setText(curProject.getStyle().getSection(i).getStep(j).getText());
                    newStep.setTextSize(15);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT);

                    newStep.setLayoutParams(lp);

                    section.addView(newStep);
                }

                TextView pageNum = (TextView)  inflatedFirstView.findViewById(R.id.PDF_first_pageNum);
                pageNum.setText("Page " + (i + 1));

                // Append the inflated view to the current page
                LinearLayout ll = (LinearLayout) findViewById(R.id.PDF_append_location);
                // TODO: Fix this awful memory leak yo
                // ll.removeAllViews();
                ll.addView(content);


                if ( ll.getWidth() != width && ll.getHeight() != height )
                {
                    float xScale = (float) ll.getWidth() / width;
                    float yScale = (float) ll.getHeight() / height;
                    Log.d("Scale: ", "xScale: " + xScale);
                    Log.d("Scale: ", "yScale: " + yScale);
                    scaleViewAndChildren(ll, xScale, yScale, 0);

                }

                // Adding the layout to the PDF page
                ll.draw(page.getCanvas());
                pdf.finishPage(page);
            }


            else {
                View content = inflatedViewContent.findViewById(R.id.PDF_root);

                // Dynamically insert data into the page
                TextView pName = (TextView) inflatedViewContent.findViewById(R.id.PDF_projName);
                pName.setText(curProject.getName());

                TextView sHead = (TextView) inflatedViewContent.findViewById(R.id.PDF_sectionHeader);
                sHead.setText(curProject.getStyle().getSection(i).getName());

                LinearLayout section = (LinearLayout) inflatedViewContent.findViewById(R.id.PDF_section);

                // Loop through the steps in the section
                for (int j = 0; j < curProject.getStyle().getSection(i).getStepList().size(); j++) {
                    TextView newStep = new TextView(this);
                    newStep.setText(curProject.getStyle().getSection(i).getStep(j).getText());
                    newStep.setTextSize(15);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT);

                    newStep.setLayoutParams(lp);

                    section.addView(newStep);
                }

                TextView pageNum = (TextView)  inflatedViewContent.findViewById(R.id.PDF_pageNum);
                pageNum.setText("Page " + (i + 1));

                // Append the inflated view to the current page
                LinearLayout ll = (LinearLayout) findViewById(R.id.PDF_append_location);
                // TODO: Fix this awful memory leak yo
                ll.removeAllViews();
                ll.addView(content);

                View pageContent = findViewById(R.id.PDF_append_location);

                // Adding the layout to the PDF page
                pageContent.draw(page.getCanvas());
                pdf.finishPage(page);
            }
        }

    }

    private void shareDocument(Uri uri) {
        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("application/pdf");
        // Assuming it may go via eMail:
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from Dog Yarn it.");
        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(mShareIntent);

        return;
    }

    public static void scaleViewAndChildren(View root, float widthScale, float heightScale, int canary) {
            // Retrieve the view's layout information
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();

            // Scale the View itself
            if (layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams.width /= widthScale;
            }
            if (layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams.height /= heightScale;
            }



            // If it's a TextView, scale the font size

            if(root instanceof TextView) {
            TextView tv = (TextView)root;
            tv.setTextSize(tv.getTextSize() / heightScale);
            }

            // If it's a ViewGroup, recurse!
            if (root instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                scaleViewAndChildren(vg.getChildAt(i), widthScale, heightScale, canary + 1);
            }
        }
    }
}
