package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;

/****************************************************************
 * Created by Rhea on 02/10/2014.
 * Use for PROFILE grid's and popups.
 ****************************************************************/
public class ProfileSelectionGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> imageIds = new ArrayList<Integer>();

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
        return imageIds.size();
    }

    @Override
    public Object getItem(int position) {
        return imageIds.get(position);
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
        iview.setImageResource(imageIds.get(position));

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
        imageIds.add( R.drawable.plus );

        // putting other profiles onto the gridview
        profileDataSource.open();
        List<Profile> Profiles = profileDataSource.getAllProfiles();
        for (Profile profile: Profiles) {
            imageIds.add( R.drawable.sample_profie );
        }
    }

    public ArrayList<Integer> getImageList()
    {
        return imageIds;
    }

}