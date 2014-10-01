package group8.comp3900.year2014.com.bcit.dogsweater;

public class Profile
{
    private String name;
    private Measurements measurements;

    public Profile( String name )
    {
        this( name, new Measurements() );
    }

    public Profile( String name, Measurements measurements )
    {
        setName( name );
        setMeasurements( measurements );
    }

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

    public void setMeasurements( Measurements m )
    {
        if( m == null )
            throw new NullPointerException( "Cannot have null measurements!" );
        measurements = m;
    }

    public Measurements getMeasurements()
    {
        return measurements;
    }
}
