package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.content.Context;
import android.net.Uri;

/**
 * Created by Georgi on 01-Oct-14.
 */

public class Profile
{
    //for databasing
    private long id;


    ////////////////////
    // Dimension keys //
    ////////////////////
    /** key for diameter around the neck in the collar area dimension */
    public static final String NECK_DIAMETER = "A";

    /** key for diameter around the chest dimension */
    public static final String CHEST_DIAMETER = "B";

    /** key for width measured between the two front legs dimension */
    public static final String FRONT_LEGS_DISTANCE = "C";

    /** key for length of neck from chin to collar dimension */
    public static final String NECK_LENGTH = "X";

    /**
     * key for length of underbelly, from collar to the end of the ribs
     * dimension
     */
    public static final String UNDERBELLY_LENGTH = "Y";

    /**
     * key for length of center back, from collar to base of the tail dimension
     */
    public static final String CENTRE_BACK_LENGTH = "Z";


    /////////////////////////////////////////
    // Dimension default value expressions //
    /////////////////////////////////////////
    /**
     * default value expression for the distance between the two front legs
     * dimension
     */
    public static final String FRONT_LEGS_DISTANCE_EXPRESSION =
            NECK_DIAMETER + "/4";

    ////////////////////////////
    // Misc profile constants //
    ////////////////////////////
    /**
     * array of mandatory dimension keys that should be saved into a profile's
     * Dimensions object.
     */
    public static final String[] MIN_DIMENSION_KEYS = new String[] {
            NECK_DIAMETER,
            CHEST_DIAMETER,
            FRONT_LEGS_DISTANCE,
            NECK_LENGTH,
            UNDERBELLY_LENGTH,
            CENTRE_BACK_LENGTH
            };

    /**
     * array of mandatory default value expressions that should be saved into a
     * profile's Dimensions object. length of this array should be the same as
     * the length of the MIN_DIMENSION_KEYS array
     */
    public static final String[] MIN_DIMENSION_EXPRESSIONS = new String[] {
            null,
            null,
            FRONT_LEGS_DISTANCE_EXPRESSION,
            null,
            null,
            null
            };

    /** Uri to an image file of this profile on the device's local memory */
    private Uri imageURI = null;

    /** name of the profile (i.e.: Sparky) */
    private String name;

    /**
     * Dimensions object associated with the profile object; measurements of the
     * subject of this profile
     */
    private Dimensions dimensions;

    public Profile( String name, Context context ) {

        this( name, new Dimensions(context), (Uri)null);
    }

    public Profile( String name, Dimensions dimensions ) {

        this( name, dimensions, (Uri)null );
    }

    public Profile( String name, String dimensions, Context context ) {

        this( name, dimensions, context, (Uri)null );
    }

    public Profile( String name, String dimensions, Context context, Uri imageURI ) {

        this( name, new Dimensions( context, dimensions ), imageURI );
    }

    public Profile( String name, Dimensions dimensions, String imageURI ) {

        this( name, dimensions, Uri.parse( imageURI ) );
    }

    public Profile( String name, String dimensions, Context context, String imageURI ) {

        this( name, new Dimensions( context, dimensions ), imageURI );
    }

    public Profile( String name, Dimensions dimensions, Uri imageURI ) {

        setName( name );
        setDimensions( dimensions );
        setImageURI( imageURI );
    }

    public long getId() {

        return id;
    }

    public void setId( long id ) {

        this.id = id;
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

    public void setName( String name ) {

        if( name == null ) {

            throw new NullPointerException( "Name cannot be null!" );
        }
        if( name.length() <= 0 ) {

            throw new IllegalArgumentException( "Name cannot be empty!" );
        }
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setDimensions( Dimensions m ) {

        if( m == null ) {

            throw new NullPointerException( "Cannot have null measurements!" );
        }
        dimensions = m;
    }

    public Dimensions getDimensions() {

       return dimensions;
    }

    @Override
    public String toString()
    {
        return "Profile[id:"        + id
                    + ",name:"      + name
                    + ",imageURI:"  + imageURI
                    + ",dimensions" + dimensions + "]";
    }
}
