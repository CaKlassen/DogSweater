package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Chris on 2014-10-15.
 */
public class Project {
    private String name;
    private float percentDone;
    private int rowCounter;
    private Profile profile;
    private Style style;

    /**
     * Author: Chris Klassen
     *
     * Constructor for a project object.
     */
    public Project(Profile p, Style s) {
        profile = p;
        style = s;

        percentDone = 0;
        rowCounter = 0;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public void setPercentDone(float p) {
        percentDone = p;
    }

    public void addPercent(float p) {
        percentDone += p;
    }

    public float getPercentDone() {
        return percentDone;
    }

    public Profile getProfile() {
        return profile;
    }

    public Style getStyle() {
        return style;
    }
}
