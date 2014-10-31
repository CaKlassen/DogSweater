package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


/**
 * Created by Rhea on 30/10/2014.
 */
public class MenuHelper extends Activity  implements group8.comp3900.year2014.com.bcit.dogsweater.SlideMenuInterface.OnSlideMenuItemClickListener{

    public static group8.comp3900.year2014.com.bcit.dogsweater.SlideMenu slidemenu;
    private final static int MYITEMID = 42;
    private final Context context;
    public static  Activity a;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    MenuHelper(Context c, Activity act)
    {
        context = c;
        a = act;

        showActionBar();

        /*
         * There are two ways to add the slide menu:
         * From code or to inflate it from XML (then you have to declare it in the activities layout XML)
         */
        // this is from code. no XML declaration necessary, but you won't get state restored after rotation.
        //slidemenu = new SlideMenu(this, R.menu.slide, this, 333);
        // this inflates the menu from XML. open/closed state will be restored after rotation, but you'll have to call init.
        slidemenu = (SlideMenu) a.findViewById(R.id.slideMenu);

            slidemenu.init(a, R.menu.slide_menu, this, 100);


        // this can set the menu to initially shown instead of hidden
        //slidemenu.setAsShown();

        // set optional header image
        slidemenu.setHeaderImage(a.getResources().getDrawable(R.drawable.yarn));

        // this demonstrates how to dynamically add menu items
            /*
            SlideMenuItem item = new SlideMenuItem();
            item.id = MYITEMID;
            item.icon = getResources().getDrawable(R.drawable.ic_launcher);
            item.label = "Dynamically added item";
            slidemenu.addMenuItem(item);
            */

        // connect the fallback button in case there is no ActionBar
        ImageButton b = (ImageButton) a.findViewById(R.id.menu);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidemenu.show();
            }
        });

        // Gesture detection
        gestureDetector = new GestureDetector(a, new MyGestureDetector(slidemenu));
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        View rl = b.getRootView();
        rl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                slidemenu.show();
            }
        });
        rl.setOnTouchListener(gestureListener);

    }



    @Override
    public void onSlideMenuItemClick(int itemId) {

        Intent in;
        switch(itemId) {
            case R.id.item_home:
                in = new Intent(a, MainActivity.class);
                a.startActivity(in);
                break;
            case R.id.item_newProj:
                in = new Intent(a, ProfileSelection.class);
                a.startActivity(in);
                break;
            case R.id.item_currentProj:
                in = new Intent(a, currentProjects.class);
                a.startActivity(in);
                break;
            case R.id.item_managProf:
                in = new Intent(a, ManageProfiles.class);
                a.startActivity(in);
                break;
            case R.id.item_information:
                in = new Intent(a, Info.class);
                a.startActivity(in);
                break;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu:
                slidemenu.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater)
                a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar = a.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }


}