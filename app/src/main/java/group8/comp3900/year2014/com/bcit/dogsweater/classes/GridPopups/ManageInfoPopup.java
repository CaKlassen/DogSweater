package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.drm.DrmManagerClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.InputStream;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Rhea on 07/10/2014.
 */
public class ManageInfoPopup extends Dialog {


    ////////////////////
    // GUI references //
    ////////////////////
    /** reference to ImageView */
    private ImageView image;

    /** reference to title TextView */
    private TextView tv;

    /** reference to delete button */
    private Button dltButton;

    /** reference to modify button */
    private Button modButton;


    //////////////////
    // constructors //
    //////////////////
    public ManageInfoPopup(final Context context, Dialogable d) {
        this (context, d.getDialogueImageUri(),  d.getDialogueTitle());
    }

    public ManageInfoPopup(final Context context, final Uri imageUri,
                           String titleText) {
        super(context);

        // set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_popup_managable);
        initializeGUIReferences();

        // get current screen's height and width
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;

        // set grid_layout window
        getWindow().setLayout(( w/100)*75, ( h/100)*75);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

        // setting dialog image...use a worker thread to load the image
        ThreadManager.mInstance.loadImage(
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

    }



    ///////////////////////
    // interface methods //
    ///////////////////////
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
     * @param           onClickListener   OnClickListener for the delete button
     *
     * sets the OnClickListener for the delete button
     */
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
        dltButton = (Button) findViewById(R.id.Delete);
        modButton = (Button) findViewById(R.id.Modify);
    }

}
