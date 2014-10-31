package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.net.Uri;

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

    private Uri imageURI = null;

    /**
     * Author: Chris Klassen
     *
     * Constructor for a project object.
     */
    public Project(Profile p, Style s) {
        this( "Temp", 0, 0, p, s );
    }

    /**
     * Author Rhea Lauzon
     * @param p : profile object
     *          when the profile is known but style isnt determined yet
     */
    public Project(Profile p) {
        //TODO: FIX LATER TO PROPER DEFAULT VAL
        this( p, new Style("Style 1", 1) );
    }

    /**
     * Author: Chris Klassen
     *
     * Database constructor for a project object.
     */
    public Project(String n, float pd, int r, Profile p, Style s) {
        setName( n );
        setPercentDone( pd );
        rowCounter = r;
        profile = p;
        setStyle( s );
    }

    public Project(String n, float pd, int r, Profile p, Style s, Uri imageURI) {
        this( n, pd, r, p, s );
        setImageURI( imageURI );
    }

    public Project(String n, float pd, int r, Profile p, Style s, String imageURI) {
        this( n, pd, r, p, s );
        setImageURI( imageURI );
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

    public void decrementRowCounter() {
        if ( rowCounter > 0 ) {
            rowCounter--;
        }
    }

    public void resetRowCounter() {
        rowCounter = 0;
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

    public void setImageURI( String newImageURI ) {

        if( newImageURI == null || newImageURI.equalsIgnoreCase( "null" ) ) {

            setImageURI( (Uri) null );
        }
        else {

            setImageURI( Uri.parse( newImageURI ) );
        }

    }

    public void setImageURI( Uri newImageURI ) {

        this.imageURI = newImageURI;
    }

    public Uri getImageURI() {

        return imageURI;
    }

}
