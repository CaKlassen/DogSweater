package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private ArrayList<Dialogable<Project>> dialogables = new ArrayList<Dialogable<Project>>();

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

        //Get Device sizes
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //Apply the images and layout constraints to the imageView
        //NOTE LEAVE UNCOMMENTED UNLESS FUTURE ISSUES
        //iview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height/3));
        //iview.setScaleType(ImageView.ScaleType.FIT_CENTER);


        // setting dialog image...use a worker thread to load the image
        Uri imageUri = (dialogables.get(position).getDialogueImageUri());
        if (imageUri == null)
        {
            iview.setImageResource(R.drawable.dog_silhouette);
        }

        ThreadManager.loadImage(
                context,                            // application context
                imageUri,                           // local uri to image file
                ThreadManager.CropPattern.SQUARE,   // crop pattern
                width/2,                                // image width

                // what to do when success
                new ThreadManager.OnResponseListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        iview.setImageBitmap(bitmap);
                    }
                });

        tv.setText(dialogables.get(position).getDialogueTitle());

        return llview;
    }

    public void remove(int position) {
        dialogables.remove(position);
        notifyDataSetChanged();

    }

    public void buildImageList()
    {
        dialogables.clear();

        // putting other profiles onto the gridview
        profileDataSource.open();
        List<Project> Projects = profileDataSource.getAllProjects();
        for (final Project project: Projects) {

            dialogables.add( new Dialogable<Project>() {

                @Override
                public Project getItem(){ return project; }

                public Project getProject() {
                    return project;
                }

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
                public Uri getDialogueImageUri() { return project.getImageURI(); }

                @Override
                public String getNextScreen() {
                    return "group8.comp3900.year2014.com.bcit.dogsweater.ProjectPattern";
                }
            } );
        }
        profileDataSource.close();
    }

    public ArrayList<Dialogable<Project>> getImageList()
    {
        return dialogables;
    }

}