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
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Rhea on 07/10/2014.
 */
public class ManageInfoPopup extends Dialog {

    private ProfileDataSource pds;

    public ManageInfoPopup(final Context c, Dialogable d, final int  p) {
        this (c, d.getDialogueImageUri(),  d.getDialogueTitle(),
                d.getDialogueDescription(), p);
    }

    public ManageInfoPopup(final Context context, final Uri imageUri,
                           String titleText,  String descriptionText, final  int position) {
        super(context);

        //Set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_popup_managable);

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

        TextView tv = (TextView) findViewById(R.id.popupTitle);
        tv.setText(titleText);


        Button dltButton = (Button) findViewById(R.id.Delete);
        dltButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Delete from database
                pds = new ProfileDataSource(context);
                pds.open();
               List<Profile> profiles =   pds.getAllProfiles();

                if (profiles.size() > 0) {
                     pds.deleteProfile((profiles.get(position)));

                     Toast.makeText(context, "Item has been deleted.", Toast.LENGTH_SHORT).show();

                     profiles.remove(position);

                     //TODO: REFRESH BACKGROUND ACTIVITY

                    pds.close();
                }

            }
            });


        //TODO: MODIFICATION OF PROFILES
        Button modButton = (Button) findViewById(R.id.Modify);
        modButton.setOnClickListener(new View.OnClickListener(){
            Intent in;
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Modifying item!", Toast.LENGTH_SHORT).show();

            }
        });

        //Show dialog
        show();

    }
}
