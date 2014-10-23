package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.net.Uri;

/**
 * Created by Georgi on 01-Oct-14.
 */

public class Profile
{
    //for databasing
    private long id;

    // TODO: modify MINIMUM_DIMENSION_KEYS so that they are the actual minimum dimension keys
    /** minimum dimension keys needed to create a profile */
    public static final String[] MINIMUM_DIMENSION_KEYS = new String[] {
            "AA",
            "BB",
            "CC",
            "DD"
    };

    private Uri imageURI = null;
    private String name;
    private Dimensions dimensions;

    public Profile( String name ) {

        this( name, new Dimensions() );
    }

    public Profile( String name, Dimensions dimensions ) {

        this( name, dimensions, (Uri)null );
    }

    public Profile( String name, String dimensions ) {

        this( name, dimensions, (Uri)null );
    }

    public Profile( String name, String dimensions, Uri imageURI ) {

        this( name, new Dimensions( dimensions ), imageURI );
    }

    public Profile( String name, Dimensions dimensions, String imageURI ) {

        this( name, dimensions, Uri.parse( imageURI ) );
    }

    public Profile( String name, String dimensions, String imageURI ) {

        this( name, new Dimensions( dimensions ), imageURI );
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
}
