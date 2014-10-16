package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.ArrayList;

/**
 * Created by Chris on 2014-10-15.
 */
public class Style {
    private String name;
    private ArrayList<Section> sectionList;

    /**
     * Author: Chris Klassen
     *
     * Constructor for a style object
     */
    public Style(String n) {
        name = n;
        sectionList = new ArrayList<Section>(1);
    }

    public String getName() {
        return name;
    }

    public void initializeSectionList() {

    }
}
