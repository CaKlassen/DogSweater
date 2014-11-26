package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;

/**
 * Created by Rhea on 07/10/2014.
 * Creates the grid_layout for the current project screen
 */
public class Popup extends Dialog {


    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to the title TextView on the dialog */
    private TextView titleText;

    /** reference to the main ImageView on the dialog */
    private ImageView image;

    /** reference to the description TextView on the dialog */
    private TextView descriptionText;

    /** reference to the button on the dialog */
    private Button button;

    private Context c;

    /////////////////
    // constructor //
    /////////////////
    public Popup(Context context) {
        super(context);
        c = context;

        //Set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_popup);
        initializeGUIReferences();

        //get current screen's height and width
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;

        //Set grid_layout window
        getWindow().setLayout((w/100)*85, ( h/100)*70);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);
    }


    ///////////////////////
    // interface methods //
    ///////////////////////
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
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the title of the dialog to the passed String (text)
     *
     * @param text text to display as the dialog's title
     */
    public void setTitleText(String text) {
        titleText.setText(text);

        // Set the custom font for the title
        Typeface titleTF = Typeface.createFromAsset( c.getAssets(), "Proxima Nova Bold.otf" );
        titleText.setTypeface( titleTF );
        titleText.setTextColor( c.getResources().getColor( R.color.black ) );
        titleText.setTextSize( TypedValue.COMPLEX_UNIT_SP, 20 );
        titleText.setAllCaps( true );
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the description of the dialog to the passed String (text)
     *
     * @param text text to display as the dialog's description
     */
    public void setDescriptionText(String text) {
        descriptionText.setText(text);
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the text displayed on the button of the dialog to the
     * passed String (text)
     *
     * @param text text to display on the button
     */
    public void setButtonText(String text) {
        button.setText(text);

        // Set the custom font for the title
        Typeface titleTF = Typeface.createFromAsset( c.getAssets(), "Proxima Nova Bold.otf" );
        button.setTypeface( titleTF );
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the onClickListener for the button of this dialog
     *
     * @param listener onClickListener to use for the dialog's button
     */
    public void setButtonOnClickListener(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }


    /////////////////////
    // support methods //
    /////////////////////
    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * initializes dialog's View references
     */
    private void initializeGUIReferences() {
        titleText = (TextView) findViewById(R.id.popupTitle);
        image = (ImageView) findViewById(R.id.largeView);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        button = (Button) findViewById(R.id.nextButton);
    }
}
