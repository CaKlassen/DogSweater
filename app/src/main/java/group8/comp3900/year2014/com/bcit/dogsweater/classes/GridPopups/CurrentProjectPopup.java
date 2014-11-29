package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Rhea on 07/10/2014.
 * Creates the grid_layout for the current project screen
 */
public class CurrentProjectPopup extends Dialog {

    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to ImageView */
    private ImageView image;

    /** reference to title TextView */
    private TextView tv;

    /** reference to delete button */
    private ImageButton dltButton;

    /** reference to continue button */
    private Button cntButton;

    /** reference to Modify Image button */
    private ImageButton modButton;

    private ProgressBar progressBar;



    //////////////////
    // constructors //
    //////////////////
    public CurrentProjectPopup(final Context context, Dialogable<Project> d) {
        this (context
             , d.getDialogueImageUri()
             , d.getItem().getPercentDone()
             , d.getDialogueTitle());
    }

    public CurrentProjectPopup(final Context context, final Uri imageUri, float percentDone,
                           String titleText) {
        super(context);

        // set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_current_project);
        initializeGUIReferences();

        // get current screen's height and width
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;

        // set grid_layout window
        getWindow().setLayout((w/100)*85, ( h/100)*70);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

        if (imageUri == null)
        {
            image.setImageResource(R.drawable.dog_silhouette);
        }
        // setting dialog image...use a worker thread to load the image
        ThreadManager.loadImage(
                context,                            // application context
                imageUri,                           // local uri to image file
                ThreadManager.CropPattern.SQUARE,   // crop pattern
                600,                                // image width

                // what to do when success
                new ThreadManager.OnResponseListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                });

        // setting title text
        tv.setText(titleText);
        // Set the custom font
        Typeface titleTF = Typeface.createFromAsset( context.getAssets(), "Proxima Nova Bold.otf" );
        tv.setTypeface( titleTF );
        tv.setAllCaps(true);

        // Set the custom font for the button
        Button conButton = (Button) findViewById(R.id.Continue);
        conButton.setTypeface(titleTF);

        progressBar.setMax(100);
        progressBar.setProgress((int) percentDone);

    }



    ///////////////////////
    // interface methods //
    //////////////////////////////////////////////
    /**
          * author: Eric Tsang
          * date: November 6 2014
          *
          * sets the dialog's ImageView's image to the corresponding drawable given
          *   its ID (i.e.: R.drawable.ic_launcher)
          *
          * @param drawableResourceId id that corresponds to a drawable to use as the
          *   dialog's ImageView's image
          */
    public void setImageByDrawableId(int drawableResourceId) {
        image.setBackgroundResource(drawableResourceId);
    }

    /**
          * author: Eric Tsang
          * date: November 6 2014
          *
          * gets and parses the image at the uri into a Bitmap, and sets it as the
          *   dialog's ImageView's image
          *
          * @param imageUri Uri to an image to display on the dialog on the android's
          *   local file system
          */
    public void setImageByUri(Uri imageUri) {
        if (imageUri == null)
        {
            image.setImageResource(R.drawable.dog_silhouette);
        }


        ThreadManager.loadImage(
                getContext(),                       // application context
                imageUri,                           // local uri to image file
                ThreadManager.CropPattern.DEFAULT,   // crop pattern
                getWindow().getAttributes().width,  // image width

                // what to do when success
                new ThreadManager.OnResponseListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * @author          Eric Tsang
     * @date            October 19 2014
     * @revisions       none
     * @param           onClickListener   OnClickListener for the delete button
     *
     * sets the OnClickListener for the delete button
     */
    public void setOnDeleteButtonClickListener(View.OnClickListener onClickListener) {
        dltButton.setOnClickListener(onClickListener);
    }

    /**
     * @author          Eric Tsang
     * @date            October 19 2014
     * @revisions       none
     * @param           onClickListener   OnClickListener for the continue
     *                  button
     *
     * sets the OnClickListener for the continue button
     */
    public void setOnContinueButtonClickListener(View.OnClickListener onClickListener) {
        cntButton.setOnClickListener(onClickListener);
    }


    public void setOnModifyButtonClickListener(View.OnClickListener onClickListener) {
        modButton.setOnClickListener(onClickListener);
    }


    /////////////////////
    // support methods //
    /////////////////////
    /** gets references to GUI views for this activity */
    private void initializeGUIReferences() {
        image = (ImageView) findViewById(R.id.largeView);
        tv = (TextView) findViewById(R.id.popupTitle);
        dltButton = (ImageButton) findViewById(R.id.Delete);
        cntButton = (Button) findViewById(R.id.Continue);
        modButton = (ImageButton) findViewById(R.id.Modify);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
