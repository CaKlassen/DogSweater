package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.ArrayList;

/**
 * Created by Chris on 2014-10-15.
 */
public class Section {
    private String name;
    private ArrayList<Step> stepList;

    public Section(String n ) {
        name = n;
        stepList = new ArrayList<Step>(1);
    }

    public String getName() {
        return name;
    }
}
