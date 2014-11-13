package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.R;

/**
 * Created by Rhea on 07/10/2014.
 * Creates the grid_layout for the current project screen
 */
public class StylePopup extends Dialog {


    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to the continue button on the dialog */
    private Button continueBtn;

    /** reference to the main ImageView on the dialog */
    private ImageView image;

    /** reference to the title TextView on the dialog */
    private TextView titleText;

    private Context c;


    /////////////////
    // constructor //
    /////////////////
    public StylePopup(Context context) {
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
    public void setImageBackgroundResource(int drawableResourceId) {
        image.setBackgroundResource(drawableResourceId);
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

        // Set the custom font
        Typeface titleTF = Typeface.createFromAsset( c.getAssets(), "GrandHotel-Regular.otf" );
        titleText.setTypeface( titleTF );
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the text displayed on the continue button of the dialog to the
     * passed String (text)
     *
     * @param text text to display on the continue button
     */
    public void setContinueButtonText(String text) {
        continueBtn.setText(text);

        // Set the custom font
        Typeface buttonTF = Typeface.createFromAsset( c.getAssets(), "Proxima Nova Bold.otf" );
        continueBtn.setTypeface( buttonTF );
    }

    /**
     * author: Eric Tsang
     * date: November 6 2014
     *
     * sets the onClickListener for the continue button of this dialog
     *
     * @param listener onClickListener to use for the dialog's continue button
     */
    public void setContinueButtonOnClickListener(View.OnClickListener listener) {
        continueBtn.setOnClickListener(listener);
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
        image = (ImageView) findViewById(R.id.largeView);
        titleText =  (TextView) findViewById(R.id.popupTitle);
        continueBtn = (Button) findViewById(R.id.nextButton);
    }
}
