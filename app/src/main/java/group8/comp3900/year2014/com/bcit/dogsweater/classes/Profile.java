package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.net.Uri;

/**
 * Created by Georgi on 01-Oct-14.
 */

public class Profile
{
    private Uri imageURI = null;
    private String name;
    private Dimensions dimensions;
    private Uri measurementsURI = null;

    public Profile( String name )
    {
        this( name, new Dimensions() );
    }

    public Profile( String name, Dimensions dimensions)
    {
        imageURI = null;
        setName( name );
        setDimensions(dimensions);
        measurementsURI = null;
    }

    public void setURI( Uri newImageURI )
    {
        if( newImageURI == null )
            throw new NullPointerException( "Image URI cannot be null!" );
        this.imageURI = newImageURI;
    }

    public Uri getImageURI() { return imageURI; }

    public void setName( String name )
    {
        if( name == null )
            throw new NullPointerException( "Name cannot be null!" );
        if( name.length() <= 0 )
            throw new IllegalArgumentException( "Name cannot be empty!" );
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setDimensions(Dimensions m)
    {
        if( m == null )
            throw new NullPointerException( "Cannot have null measurements!" );
        dimensions = m;
    }

    public Dimensions getDimensions()
    {
        return dimensions;
    }

    public void setMeasurementsURI( Uri newMeasurementsUri )
    {
        if( newMeasurementsUri == null )
            throw new NullPointerException( "Measurements URI cannot be null!" );
        this.measurementsURI = newMeasurementsUri;
    }

    public Uri getMeasurementsURI() { return measurementsURI; }


    //TODO: public void loadMeasurements();
    //TODO: public void saveMeasurements();
}
