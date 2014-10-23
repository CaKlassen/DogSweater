package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Chris on 2014-10-15.
 */
public class Project {
    //for databasing
    private long id;

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
        name = "Temp";
        percentDone = 0;
        rowCounter = 0;
    }

    /**
     * Author Rhea Lauzon
     * @param p : profile object
     *          when the profile is known but style isnt determined yet
     */
    public Project(Profile p) {
        profile = p;
        name = "Temp";
        percentDone = 0;
        rowCounter = 0;
        //TODO: FIX LATER TO PROPER DEFAULT VAL
        style = new Style("Style 1", 1);
    }

    /**
     * Author: Chris Klassen
     *
     * Database constructor for a project object.
     */
    public Project(String n, float pd, int r, Profile p, Style s) {
        name = n;
        percentDone = pd;
        rowCounter = r;
        profile = p;
        style = s;
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

    public void incrementRowCounter() {
        rowCounter++;
    }

    public int getRowCounter() {
        return rowCounter;
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

    public long getId() {

        return id;
    }

    public void setId( long id ) {

        this.id = id;
    }

    public void setStyle(Style s){
        this.style = s;
    }

}
