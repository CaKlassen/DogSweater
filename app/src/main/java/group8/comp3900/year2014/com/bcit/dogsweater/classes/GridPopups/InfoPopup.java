package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.InputStream;
import java.util.ArrayList;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Rhea on 07/10/2014.
 */
public class InfoPopup extends Dialog {

    public InfoPopup(final Context c, Dialogable d) {
        this (c, d.getNextScreen(), d.getDialogueImageUri(),
                d.getDialogueButtonText(), d.getDialogueTitle(),
                d.getDialogueDescription());
    }

    public InfoPopup(final Context context, final String nextScreen,
            final Uri imageUri, String buttonText, String titleText,
            String descriptionText) {
        super(context);

        //Set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_popup);

        //get current screen's height and width
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;

        //Set grid_layout window
        getWindow().setLayout((int)( w/100)*75, (int)( h/100)*75);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

        //Set image to whatever is clicked on
        try {
            // TODO: move parsing of bitmap elsewhere
            ImageView image = (ImageView) findViewById(R.id.largeView);
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bm = BitmapFactory.decodeStream(stream);
            bm = Bitmap.createScaledBitmap(bm, 600, bm.getHeight() * 600 / bm.getWidth(), false);
            bm = Bitmap.createBitmap(bm, bm.getWidth() / 2 - 300, bm.getHeight() / 2 - 300, 600, 600);
            image.setImageBitmap(bm);
        } catch(Exception e) {
            Log.e("trouble parsing URI", e.toString());
        }

        //Set description text
        TextView description = (TextView) findViewById(R.id.descriptionText);
        description.setText(descriptionText);

        //Set title to correct
        TextView title =  (TextView) findViewById(R.id.popupTitle);
        title.setText(titleText);

        //Set onclick listener to next screen
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setText(buttonText);
        nextButton.setOnClickListener(new View.OnClickListener(){
            Intent in;
            @Override
            public void onClick(View v) {
                try {
                    in = new Intent(context, Class.forName(nextScreen));
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                context.startActivity(in);
            }
            });

        //Show dialog
        show();

    }
}
