package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Material;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Profile;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.Style;
import group8.comp3900.year2014.com.bcit.dogsweater.classes.database.ProfileDataSource;

public class Materials extends Activity {

    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);

        TextView matsTitle = (TextView) findViewById(R.id.materialsText);
        Typeface titleFont = Typeface.createFromAsset( getAssets(), "GrandHotel-Regular.otf" );
        matsTitle.setTypeface( titleFont );

        //Generate list View from ArrayList
        displayListView();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Create menu
        MenuHelper m = new MenuHelper(getApplicationContext(), this);

    }

    private void displayListView() {
        //Array list of materials
        ArrayList<Material> materialList = new ArrayList<Material>();

        //Add Materials here!

        //Get the yarn calculation
        // Build the project from the database

        long projId = getIntent().getExtras().getLong(ProjectPattern.KEY_PROJECT_ID);
        ProfileDataSource db = new ProfileDataSource(this);
        db.open();
        Project curProject = db.getProject(projId);

        Profile p =curProject.getProfile();
        db.close();


        Material material = new Material( Style.calculateYardage(p,
                curProject.getStyle().getStyleNumber()) + "Yards of yarn", false);
        materialList.add(material);

        material = new Material("16\" Circular Knitting Needles", false);
        materialList.add(material);

        material = new Material("Stitch markers", false);
        materialList.add(material);

        material = new Material("Stitch Holder", false);
        materialList.add(material);

        material = new Material("Darning Needle", false);
        materialList.add(material);

        //create an ArrayAdapter from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.material_info, materialList);
        ListView listView = (ListView) findViewById(R.id.listView1);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

    }

    private class MyCustomAdapter extends ArrayAdapter<Material>
    {

        private ArrayList<Material> materialList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Material> materialList)
        {
            super(context, textViewResourceId, materialList);
            this.materialList = new ArrayList<Material>();
            this.materialList.addAll(materialList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.material_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Material material = (Material) cb.getTag();
                        material.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Material material = materialList.get(position);
            holder.name.setText(material.getName());
            holder.name.setChecked(material.isSelected());
            holder.name.setTag(material);
            holder.name.setPadding( 20, 0, 0, 0 );
            holder.name.setTextSize( TypedValue.COMPLEX_UNIT_SP, 20 );

            return convertView;

        }

    }
    public void startInstructions(View v)
    {
        // transfer desired extras from starting intent to new intent
        long projId = getIntent().getExtras().getLong(ProjectPattern.KEY_PROJECT_ID);
        Intent in = new Intent(this, ProjectPattern.class);
        in.putExtra(ProjectPattern.KEY_PROJECT_ID, projId);
        startActivity(in);
    }
}
