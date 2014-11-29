package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.content.Context;
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

import group8.comp3900.year2014.com.bcit.dogsweater.R;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.StyleInfo;

/****************************************************************
 * Created by Rhea on 02/10/2014.
 * Use for STYLE grid's and popups.
 ****************************************************************/
public class StyleGridAdapter extends BaseAdapter {
    private Context context;

    //list of style info classes hard coded into the app
    private ArrayList<StyleInfo> styles = new ArrayList<StyleInfo>();
    int numStyles = 0;


    public StyleGridAdapter(Context c) {
        context = c;
        buildStyleList();
    }

    @Override
    public int getCount() {
        return styles.size();
    }

    @Override
    public Object getItem(int position) {
        return styles.get(position);
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

        iview.setImageResource(styles.get(position).getImageUri());

        tv.setText(styles.get(position).getName());

        return llview;
    }

   public void buildStyleList()
    {
        styles.add(new StyleInfo("Standard Top Down", "A simple top down sweater that wraps around the belly.", R.drawable.style01) );
        numStyles++;
        styles.add(new StyleInfo("Standard Dog Sweater", "The dog sweater seen in Hip Knitting.", R.drawable.style02) );
        numStyles++;
    }

    public ArrayList<StyleInfo> getStyleList()
    {
        return styles;
    }


}