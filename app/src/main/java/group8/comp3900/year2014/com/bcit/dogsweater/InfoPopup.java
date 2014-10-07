package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Rhea on 07/10/2014.
 */
public class InfoPopup extends Dialog {

    Context context;
    public InfoPopup(Context context,String imageurl) {
        super(context);

        this.context = context;

//Set custom dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_popup);

//get current screen h/w
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        int w = d.getWidth();
        int h = d.getHeight();

//Set popup window h/w full screen
        getWindow().setLayout((int)( w/100)*50, (int)( h/100)*50);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

//now get imageview inside custome dialog layout
        ImageView large = (ImageView) findViewById(R.id.largeView);

//Show dialog
        show();

    }
}
