package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;


public class PdfPreview extends Activity {
    private Project curProject;

    //For PDF generation
    private Intent mShareIntent;
    private OutputStream os;
    private View inflatedFirstView;
    private View inflatedViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        // Build the project from the database
        long projId = getIntent().getExtras().getLong( "Project id" );

        ProfileDataSource db = new ProfileDataSource(this);
        db.open();
        curProject = db.getProject(projId);
        db.close();


        LinearLayout root = (LinearLayout) findViewById( R.id.pdf_preview_base );


        // Loop through all sections
        for (int i = 0; i < curProject.getStyle().getSectionList().size(); i++) {

            // Create a new section
            RelativeLayout r = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(576, 792);
            layoutParams.setMargins( 0, 20, 0, 20 );
            r.setLayoutParams(layoutParams);
            r.setVisibility(View.VISIBLE);
            r.setPadding(30, 30, 30, 30);
            r.setBackgroundColor(Color.WHITE);
            r.setId(i);
            root.addView( r );

            // Draw the project name
            TextView pName = new TextView(this);
            r.addView(pName);
            pName.setText(curProject.getName());
            pName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            pName.setLayoutParams( lp );
            pName.setId( 1000 + i );
            pName.setPadding( 0, 0, 0, 10 );

            // Draw the website link
            TextView website = new TextView(this);
            website.setText( "www.knittingAstor.com" );
            website.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp3.addRule(RelativeLayout.ALIGN_PARENT_START);
            lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            website.setLayoutParams( lp3 );
            r.addView(website);

            // Draw the page number
            TextView pageNum = new TextView(this);
            pageNum.setText("Page " + (i + 1));
            pageNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            pageNum.setLayoutParams( lp2 );
            r.addView(pageNum);

            // Create the step section
            LinearLayout l = new LinearLayout(this);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(0, 0, 0, 0);
            layoutParams2.addRule(RelativeLayout.BELOW, pName.getId() );
            l.setLayoutParams(layoutParams2);
            r.addView( l );
            l.setVisibility(View.VISIBLE);
            l.setOrientation( LinearLayout.VERTICAL );
            l.setPadding(0, 0, 0, 0);
            l.setBackgroundColor(Color.WHITE);

            if (i == 0)
            {
                // Draw the logo
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.dog_silhouette_sweater);
                iv.setScaleX(0.3f);
                iv.setScaleY(0.3f);
                iv.setScaleType( ImageView.ScaleType.CENTER );
                iv.setLayoutParams( new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT ));
                l.addView(iv);
            }

            // Draw the section name
            TextView sHead = new TextView(this);
            sHead.setText(curProject.getStyle().getSection(i).getName());
            sHead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            sHead.setPadding(10, 0, 0, 6);
            l.addView(sHead);

            int stepCounter = 1;

            // Loop through the steps in the section
            for ( int j = 0; j < curProject.getStyle().getSection(i).getStepList().size(); j++ )
            {

                TextView newStep = new TextView(this);
                newStep.setText(stepCounter + ". " + curProject.getStyle().getStep(i, j,
                        curProject.getDimensions()));
                newStep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5);
                newStep.setPadding( 0, 0, 0, 5 );
                l.addView(newStep);

                stepCounter++;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pdf_preview, menu);
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

    public void sharePDF( View view ) {
        //new Thread().start();

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

        Uri uri = Uri.fromFile(pdf);
        shareDocument(uri);
    }

    private void generatePDF( PdfDocument document, int height, int width ) {

        // Create a new PDF page
        PdfDocument.PageInfo pageInfo;
        PdfDocument.Page page;

        LinearLayout root = (LinearLayout) findViewById( R.id.pdf_preview_base );

        // Loop through each page child
        for ( int i = 0; i < root.getChildCount(); i++ ) {
            pageInfo = new PdfDocument.PageInfo.Builder(width, height, i + 1).create();
            page = document.startPage(pageInfo);

            root.getChildAt( i ).draw(page.getCanvas());
            document.finishPage(page);
        }

        /*
        if ( ll.getWidth() != width && ll.getHeight() != height )
        {
            float xScale = (float) ll.getWidth() / width;
            float yScale = (float) ll.getHeight() / height;
            Log.d("Scale: ", "xScale: " + xScale);
            Log.d("Scale: ", "yScale: " + yScale);
            scaleViewAndChildren(ll, xScale, yScale, 0);

        }*/

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

        if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT || layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT )
        {
            int width  = root.getMeasuredWidth();
            layoutParams.width = width /= widthScale;
        }

        if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT || layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            int height  = root.getMeasuredHeight();
            layoutParams.height= height /= heightScale;
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
