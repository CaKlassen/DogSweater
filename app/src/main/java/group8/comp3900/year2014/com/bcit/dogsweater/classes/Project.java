package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.net.Uri;

/**
 * Created by Chris on 2014-10-15.
 */
public class Project {
    //for databasing
    private long id;


    //////////////////////////////////////////
    // Dimensions returned by getDimensions //
    //////////////////////////////////////////

    // dimensions keys of the Dimensions object returned through getDimensions
    public static final String KEY_GAUGE = "GAUGE";
    public static final String KEY_STS_AA = "AA";
    public static final String KEY_STS_BB = "BB";
    public static final String KEY_STS_CC = "CC";

    // expressions used to calculate dimension values of the Dimensions object
    //   returned through getDimensions
    /**
     * AA is estimated by GUAGE*A/4 then rounded up to the closest number that
     *   is both a multiple of 3 and 4 Possible AA’s : 12, 24, 36, 48, 60, 72,
     *   84, 96, 108, 120, etc – basically a multiple of 12
     */
    public static final String STS_AA_EXPRESSION =
            "ceil(" + KEY_GAUGE + "*" + Profile.NECK_DIAMETER + "/12)*12";

    /** BB is GUAGE*B/4 rounded up to the closest multiple of 4 */
    public static final String STS_BB_EXPRESSION =
            "ceil(" + KEY_GAUGE + "*" + Profile.CHEST_DIAMETER + "/4/4)*4";

    /** CC is GUAGE*C/4 rounded up to the nearest odd number */
    public static final String STS_CC_EXPRESSION =
            "round(" + KEY_GAUGE + "*" + Profile.FRONT_LEGS_DISTANCE + "/2)*2+1";

    /**
     * array of mandatory of keys that should be in the Dimensions object
     *   returned through getDimensions
     */
    public static final String[] MIN_DIMENSION_KEYS = new String[] {
            KEY_STS_AA,
            KEY_STS_BB,
            KEY_STS_CC
    };

    /**
     * array of expressions used to each dimension in the Dimensions object
     *   returned through getDimensions
     */
    public static final String[] MIN_DIMENSION_EXPRESSIONS = new String[] {
            STS_AA_EXPRESSION,
            STS_BB_EXPRESSION,
            STS_CC_EXPRESSION
    };

    /**
     * array of mandatory dimensions keys that should be saved in the Dimensions
     * object returned by getDimensions
     */

    private String name;
    private float percentDone;
    private int rowCounter;
    private int curSection;
    private Profile profile;
    private Style style;

    private Uri imageURI = null;

    /** thickness of the yarn used for this project */
    private double gauge;

    /**
     * Author: Chris Klassen
     *
     * Constructor for a project object.
     */
    public Project(Profile p, Style s) {
        this( "Temp", 0, 0, p, s, 0 );
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
    public Project(String n, float pd, int r, Profile p, Style s, int cs) {
        setName( n );
        setPercentDone( pd );
        rowCounter = r;
        profile = p;
        setStyle( s );
        curSection = cs;
    }

    public Project(String n, float pd, int r, Profile p, Style s, Uri imageURI, int cs) {
        this( n, pd, r, p, s, cs );
        setImageURI( imageURI );
    }

    public Project(String n, float pd, int r, Profile p, Style s, String imageURI, int cs) {
        this( n, pd, r, p, s, cs );
        setImageURI( imageURI );
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public void setSection(int s) {
        curSection = s;
    }

    public int getSection() {
        return curSection;
    }

    public void setPercentDone(float p) {
        percentDone = p;
    }

    public void setGauge(double newGauge) {
        gauge = newGauge;
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

    public Dimensions getDimensions() {

        // create a copy of the Profile's Dimensions object because we want to
        // access its dimensions like A, B, C and so on.
        String stringifiedDimensions = getProfile().getDimensions().stringify();
        Dimensions dimensions = new Dimensions(stringifiedDimensions);

        // calculate and add project dimensions into Dimensions object & return
        dimensions.setDimension(KEY_GAUGE, getGauge());
        for (int i = 0; i < MIN_DIMENSION_KEYS.length; i++) {
            dimensions.setDimension(MIN_DIMENSION_KEYS[i],
                    Double.valueOf(
                            dimensions.parseExpression(
                                    MIN_DIMENSION_EXPRESSIONS[i])));
        }
        return dimensions;
    }

    public long getId() {

        return id;
    }

    public double getGauge() {
        return gauge;
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

    @Override
    public String toString()
    {
        return "Project[id:"      + id
                + ",name:"        + name
                + ",imageURI:"    + imageURI
                + ",percentDone:" + percentDone
                + ",rowCounter:"  + rowCounter
                + ",curSection:"  + curSection
                + ",profile:"     + profile
                + ",style:"       + style  + "]";
    }
}
