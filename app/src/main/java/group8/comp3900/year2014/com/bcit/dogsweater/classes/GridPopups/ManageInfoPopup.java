package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group8.comp3900.year2014.com.bcit.dogsweater.R;

/**
 * Created by Rhea on 07/10/2014.
 */
public class ManageInfoPopup extends Dialog {

    //Position in the grid that was clicked
    private int p;
    private ArrayList<Integer> imageList;

    Context context;
    public ManageInfoPopup(final Context context, ArrayList<Integer> il, int position, final String nextScreen, String Title) {
        super(context);
        this.context = context;
        p = position;
        imageList = il;


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
        ImageView image = (ImageView) findViewById(R.id.largeView);
        image.setBackgroundResource(imageList.get(position));

        //Set title to correct
        TextView title =  (TextView) findViewById(R.id.popupTitle);
        title.setText(Title);


        //TODO: DELETE BUTTON DELETES FROM QUERY
        Button dltButton = (Button) findViewById(R.id.Delete);
        dltButton.setOnClickListener(new View.OnClickListener(){
            Intent in;
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleting item!", Toast.LENGTH_SHORT).show();
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
