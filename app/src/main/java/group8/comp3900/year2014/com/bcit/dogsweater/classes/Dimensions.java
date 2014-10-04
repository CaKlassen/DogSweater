package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * @author          Eric Tsang
 * @date            October 1 2014
 * @revisions
 *
 * October 3 2014
 * changed the name of this class from "Measurements" to "Dimensions"
 *
 * @param           stringified   a string created with the
 *                  Measurements.stringify method
 * @return          an instance of Measurements with the exact same
 *                  measurements, and associated values as the Measurements
 *                  instance that was stringified
 *
 * instantiates an instance of Dimensions
 */
public class Dimensions
{

    // declare instance variables
    /** underlying object used to save measurement information */
    private JSONObject mStorage;




    /////////////////
    // constructor //
    /////////////////

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           stringified   a string created with the
     *                  Measurements.stringify method
     * @return          an instance of Measurements with the exact same
     *                  measurements, and associated values as the Measurements
     *                  instance that was stringified
     *
     * instantiates an instance of Measurements
     */
    public Dimensions(String stringified)
    {
        parseStringified(stringified);
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @return          an instance of Measurements
     *
     * instantiates an instance of Measurements
     */
    public Dimensions()
    {
        mStorage = new JSONObject();
    }




    ////////////////////////////
    // get & set measurements //
    ////////////////////////////

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           key   key used to identify to the measurement
     * @param           value   new value of the measurement in
     *                          Unit.defaultUnits
     * @return          void
     *
     * sets the value of the measurement with key associated with the passed key
     *     to the passed value
     */
    public void setMeasurement( String key, double value ) throws JSONException
    {
        setMeasurement( key, value, Unit.getDefaultUnit() );
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           key   key used to identify to the measurement
     * @param           value   value of the measurement in the passed units
     *                          ( unit )
     * @param           unit   units that value is passed in
     * @return          void
     *
     * sets the value of the measurement with key associated with the passed key
     *     to the passed value in the passed units ( unit )
     */
    public void setMeasurement( String key, double value, Unit unit )
            throws JSONException
    {
        if (key == null || unit == null)
            throw new NullPointerException("Neither key or unit parameters "
                    + "can be null!");
        JSONObject o = new JSONObject();
        o.put( Measurement.KEY_VALUE, unit.convert( Unit.INCHES, value ) );
        mStorage.put( key, o );
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           key   key used to identify the measurement
     * @return          value of the measurement identified by the passed
     *                  identifier ( key )
     *
     * returns the value of the measurement identified by the passed key
     */
    public double getMeasurement( String key )
    {
        return getMeasurement( key, Unit.getDefaultUnit() );
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           key   key used to identify the measurement
     * @param           unit   units that the returned value should be returned
     *                         in
     * @return          value of the measurement identified by the passed
     *                  identifier ( key ) in the passed units ( unit )
     *
     * returns the value of the measurement identified by the passed key in the
     * passed units ( unit )
     */
    public double getMeasurement( String key, Unit unit )
    {
        try {
            JSONObject o = mStorage.getJSONObject( key );
            double measurementValue = o.getDouble( Measurement.KEY_VALUE );
            return Unit.getDefaultUnit().convert( unit, measurementValue );
        }
        catch ( JSONException e )
        {
            throw new NoSuchElementException("There is no measurement "
                    + "associated with the passed key: \"" + key + "\".");
        }
    }




    /////////////////////////////
    // other interface methods //
    /////////////////////////////

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @return          returns a string that can be used in a constructor of
     *                  the measurements class to build a measurements class
     *                  with the same state as it had when it was stringified.
     *
     * returns the value of the measurement identified by the passed key in the
     * passed units ( unit )
     */
    public String stringify()
    {
        return mStorage.toString();
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           stringified   a string returned by the
     *                  Measurements.stringify method. this sets the state of
     *                  this instance to the same state as the instance that was
     *                  stringified, at the time that it was stringified.
     * @return          void
     *
     * returns the value of the measurement identified by the passed key in the
     * passed units ( unit )
     */
    public void parseStringified( String stringified )
    {
        try
        {
            mStorage = new JSONObject( stringified );
        }
        catch (JSONException e)
        {
            throw new IllegalArgumentException("Parameter stringified was not "
                    + "formatted correctly.");
        }
    }




    ///////////////
    // unit test //
    ///////////////

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           args   unused command line arguments
     * @return          void
     *
     * demonstrates how to use the measurements class
     */
    public static void main( String[] args )
    {
        Dimensions m = new Dimensions();

        try
        {
            m.setMeasurement( "Hello", 11 );
            m.setMeasurement( "No", 12 );
            m.setMeasurement( "Bye", 13 );
            Dimensions m2 = new Dimensions(m.stringify());

            System.out.println( m2.getMeasurement( "Hello" ) );
            //System.out.println( m.getMeasurement( "Helloo" ) );
            System.out.println( m2.getMeasurement( "No", Unit.CENTIMETRES ) );
            System.out.println( m.getMeasurement( "Bye", Unit.INCHES ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static class Measurement
    {
        // class variables
        /** key to value of the measurement in inches */
        public static final String KEY_VALUE = "KEY_VALUE";
    }

}
