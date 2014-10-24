package group8.comp3900.year2014.com.bcit.dogsweater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Material;

public class Materials extends Activity {
    private long projId;

    MyCustomAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);

        // Get the project id from the bundle
        projId = getIntent().getExtras().getLong("Project Id");

        //Generate list View from ArrayList
        displayListView();

    }

    private void displayListView() {
        //Array list of materials
        ArrayList<Material> materialList = new ArrayList<Material>();

        //Add Materials here!
        Material material = new Material("Yarn",false);
        materialList.add(material);
        material = new Material("Knitting Needles", false);
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
            Log.v("ConvertView", String.valueOf(position));

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
                        Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
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

            return convertView;

        }

    }
    public void startInstructions(View v)
    {
        Intent in = new Intent(this, ProjectPattern.class);
        // Put the project id into the bundle
        in.putExtra("Project Id", projId);

        startActivity(in);
    }
}
