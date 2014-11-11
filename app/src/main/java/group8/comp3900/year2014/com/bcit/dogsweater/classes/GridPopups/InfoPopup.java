package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Rhea on 07/10/2014.
 */
public class InfoPopup extends Dialog {

    public InfoPopup(final Context c, Dialogable d) {
        this (c, d.getNextScreen(), d.getDialogueImageUri(),
                d.getDialogueButtonText(), d.getDialogueTitle(),
                d.getDialogueDescription(), d.getItemId());
    }

    public InfoPopup(final Context context, final String nextScreen,
            final Uri imageUri, String buttonText, String titleText,
            String descriptionText, final long itemId) {
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

        // set image to whatever is clicked on...use a worker thread to load the image
        final ImageView image = (ImageView) findViewById(R.id.largeView);

        if (imageUri == null)
        {
            image.setBackgroundResource(R.drawable.dog_silhouette);
        }

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
                    ProfileDataSource profileDataSource = new ProfileDataSource(context);
                    profileDataSource.open();

                    //Create new project with that profile
                    Project p = new Project(profileDataSource.getProfile(itemId));
                    profileDataSource.insertProject(p);

                    in = new Intent(context, Class.forName(nextScreen));
                    in.putExtra("projId", p.getId());

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
