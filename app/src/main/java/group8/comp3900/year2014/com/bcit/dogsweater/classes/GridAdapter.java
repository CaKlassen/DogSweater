package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import group8.comp3900.year2014.com.bcit.dogsweater.R;

/**
 * Created by Rhea on 02/10/2014.
 */
public class GridAdapter extends BaseAdapter {
    private Context context;
    private Integer[] imageIds = {
            R.drawable.dog_silhouette_sweater,
            R.drawable.dog_silhouette,
            R.drawable.dog_diagram,
            R.drawable.sample_profie
    };

    public GridAdapter(Context c) { context = c; }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return imageIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView iview;
        if (view == null) {
            iview = new ImageView(context);
            iview.setLayoutParams(new GridView.LayoutParams(350,350 ));
            iview.setScaleType(ImageView.ScaleType.FIT_CENTER );
            iview.setBackgroundColor(Color.rgb(255, 255, 255));
            iview.setPadding(5, 5, 5, 5);
        } else {
            iview = (ImageView) view;
        }
        iview.setImageResource(imageIds[position]);
        return iview;
    }
}