package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.ThreadManager;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/****************************************************************
 * Created by Rhea on 02/10/2014.
 * Use for PROJECTS grid's and popups.
 ****************************************************************/
public class ProjectGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Dialogable> dialogables = new ArrayList<Dialogable>();

    /////////////////////
    // database things //
    /////////////////////
    /**
     * database interface object used to get all profiles so we can display them
     */
    private ProfileDataSource profileDataSource;

    public ProjectGridAdapter(Context c) {
        context = c;

        profileDataSource = new ProfileDataSource(context);
        //Add profiles to UI
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
        final ImageView iview;
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
        ThreadManager.mInstance.loadImage(
                context,                            // application context
                imageUri,                           // local uri to image file
                ThreadManager.CropPattern.SQUARE,   // crop pattern
                600,                                // image width

                // what to do when success
                new ThreadManager.OnResponseListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                       // iview.setImageBitmap(bitmap);
                        iview.setBackgroundResource(R.drawable.dog_silhouette);

                    }
                });

        tv.setText(dialogables.get(position).getDialogueTitle());

        return llview;
    }

    public void remove(int position) {
        dialogables.remove(position);
        notifyDataSetChanged();

    }

    //TODO: IMAGE URI STUFF FOR PROJECTS
    public void buildImageList()
    {

        // putting other profiles onto the gridview
        profileDataSource.open();
        List<Project> Projects = profileDataSource.getAllProjects();
        for (final Project project: Projects) {

            dialogables.add(new Dialogable() {
                @Override
                public long getItemId() { return project.getId(); }

                @Override
                public String getDialogueTitle() {
                    return project.getName();
                }

                @Override
                public String getDialogueDescription() {
                    return "The notes of the project go here.";
                }

                @Override
                public String getDialogueButtonText() {
                    return "";
                }

                @Override
                public Uri getDialogueImageUri() {
                    return null;
                }

                @Override
                public String getNextScreen() {
                    return "group8.comp3900.year2014.com.bcit.dogsweater.ProjectPattern";
                }
            });
        }
    }

    public ArrayList<Dialogable> getImageList()
    {
        return dialogables;
    }

}