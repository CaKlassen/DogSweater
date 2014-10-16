package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import group8.comp3900.year2014.com.bcit.dogsweater.R;

/****************************************************************
 * Created by Rhea on 02/10/2014.
 * Use for STYLE grid's and popups.
 ****************************************************************/
public class StyleGridAdapter extends BaseAdapter {
    private Context context;
    //list of image resource ID's for population of the grid
    private ArrayList<Integer> imageIds = new ArrayList<Integer>();
    int numImages = 0;


    public StyleGridAdapter(Context c) {
        context = c;
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
        ImageView iview;
        if (view == null) {
            //add the image view to the grid
            iview = new ImageView(context);
            iview.setLayoutParams(new GridView.LayoutParams(350,350 ));
            iview.setScaleType(ImageView.ScaleType.FIT_CENTER );
            iview.setBackgroundColor(Color.rgb(255, 255, 255));
            iview.setPadding(5, 5, 5, 5);
        } else {
            iview = (ImageView) view;
        }
        iview.setImageResource(imageIds.get(position));
        return iview;
    }

    //TODO: BUILD THIS ARRAY LIST DYNAMICALLY
    public void buildImageList()
    {
        imageIds.add(numImages, R.drawable.dog_silhouette_sweater );
        numImages++;
    }

    public ArrayList<Integer> getImageList()
    {
        return imageIds;
    }


}