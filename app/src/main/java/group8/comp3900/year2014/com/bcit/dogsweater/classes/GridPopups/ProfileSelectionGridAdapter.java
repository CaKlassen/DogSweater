package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.adapters.DialogableAdapter;

/****************************************************************
 * Created by Rhea on 02/10/2014.
 * Use for PROFILE grid's and popups.
 ****************************************************************/
public class ProfileSelectionGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Dialogable> dialogables = new ArrayList<Dialogable>();

    /////////////////////
    // database things //
    /////////////////////
    /**
     * database interface object used to get all profiles so we can display them
     */
    private ProfileDataSource profileDataSource;


    public ProfileSelectionGridAdapter(Context c) {
        // initialize instance members from constructor parameters
        context = c;
        profileDataSource = new ProfileDataSource(context);

        // add profiles to UI
        buildImageList();

    }

    @Override
    public int getCount() {
        return dialogables.size();
    }

    @Override
    public Object getItem(int position) {
        return dialogables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View llview = view;
        ImageView iview;
        TextView tv;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            llview = inflater.inflate(R.layout.grid_layout, parent, false);

        } else {
            llview = view;
        }

        //Get the image and text view for the square

        iview = (ImageView) llview.findViewById(R.id.gridImage);
        tv = (TextView) llview.findViewById(R.id.gridText);

        //Apply the images and layout constraints to the imageView
        iview.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
        iview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iview.setPadding(5, 5, 5, 5);
        Uri imageUri = (dialogables.get(position).getDialogueImageUri());
        try {
            // TODO: move parsing of bitmap elsewhere
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bm = BitmapFactory.decodeStream(stream);
            bm = Bitmap.createScaledBitmap(bm, 600, bm.getHeight() * 600 / bm.getWidth(), false);
            bm = Bitmap.createBitmap(bm, bm.getWidth() / 2 - 300, bm.getHeight() / 2 - 300, 600, 600);
            iview.setImageBitmap(bm);
        } catch(Exception e) {
            Log.e("trouble parsing URI", e.toString());
        }

        if (position == 0)
        {
            tv.setText("New Profile");
        }
        else
        {
            tv.setText("Temp Pup");
        }

        return llview;
    }

    //TODO: BUILD THIS ARRAY LIST DYNAMICALLY
    public void buildImageList()
    {
        // tile used to add a new profile
        dialogables.add(new DialogableAdapter() {

            @Override
            public Uri getDialogueImageUri() {
                return Uri.parse("android.resource://group8.comp3900.year2014.com.bcit.dogsweater/drawable/plus");
            }
        });

        // putting other profiles onto the gridview
        profileDataSource.open();
        List<Profile> Profiles = profileDataSource.getAllProfiles();
        for (final Profile profile: Profiles) {

            dialogables.add(new Dialogable() {
                @Override
                public String getDialogueTitle() {
                    return profile.getName();
                }

                @Override
                public String getDialogueDescription() {
                    return profile.getImageURI().toString();
                }

                @Override
                public String getDialogueButtonText() {
                    return "SELECT THIS PROFILE";
                }

                @Override
                public Uri getDialogueImageUri() {
                    return profile.getImageURI();
                }

                @Override
                public String getNextScreen() {
                    return "group8.comp3900.year2014.com.bcit.dogsweater.Yarn";
                }
            });
        }
    }

    public ArrayList<Dialogable> getImageList()
    {
        return dialogables;
    }

}