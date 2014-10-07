package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.NoSuchElementException;

import org.json.JSONArray;
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
     * to the passed value
     */
    public void setDimension(String key, double value) throws JSONException
    {
        setDimension(key, value, Unit.getDefaultUnit());
    }

    /**
     * @author          Eric Tsang
     * @date            October 1 2014
     * @revisions       none
     * @param           key   key used to identify the measurement
     * @return          value of the measurement identified by the passed
     *                  identifier ( key )
     *
     * returns the value of the dimension identified by the passed key. the
     * units used will be in Unit.defaultUnits
     */
    public double getDimension(String key)
    {
        return getDimension(key, Unit.getDefaultUnit());
    }

    /**
     * @author          Eric Tsang
     * @date            October 7 2014
     * @revisions       none
     * @return          String array of all keys
     *
     * returns a string array of all keys saved in this instance
     */
    public String[] getDimensionKeys()
    {
        JSONArray names = mStorage.names(); // all measurements keys
        int len = names.length();           // used in for loop
        String[] keys = new String[len];    // will contain all measurement keys

        // populate the keys array with the keys
        for (int i = 0; i < names.length(); i++) {
            try {
                keys[i] = names.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // return...
        return keys;
    }

    /**
     * @author          Eric Tsang
     * @date            October 7 2014
     * @revisions       none
     * @param           key   key used to identify to the measurement
     * @return          void
     *
     * removes the key-value pair; throws a NoSuchElementException when the key
     * cannot be found
     */
    public void deleteDimension(String key)
    {
        try {
            mStorage.get( key );
        } catch (JSONException e) {
            throw new NoSuchElementException( "There is no measurement "
                    + "associated with the passed key: \"" + key + "\".");
        }
        mStorage.remove(key);
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
     * sets the value of the dimension identified by the passed key to the
     * passed value in the passed unit
     */
    public void setDimension(String key, double value, Unit unit)
            throws JSONException
    {
        // validate parameters
        if (key == null || unit == null)
            throw new NullPointerException("Neither key or unit parameters "
                    + "can be null!");

        // add the measurement to mStorage
        mStorage.put( key, unit.convert( Unit.INCHES, value ) );
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
     * returns the value of the dimension identified by the passed key in the
     * passed units ( unit )
     */
    public double getDimension(String key, Unit unit)
    {
        try {
            // get and return the measurement with the key that matches the
            // passed key
            double measurementValue = mStorage.getDouble( key );
            return Unit.getDefaultUnit().convert( unit, measurementValue );
        }
        catch ( JSONException e )
        {
            // exception thrown, passed key probably doesn't exist
            throw new NoSuchElementException( "There is no measurement "
                    + "associated with the passed key: \"" + key + "\"." );
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
        catch ( JSONException e )
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
            m.setDimension("Hello", 11);
            m.setDimension("No", 12);
            m.setDimension("Bye", 13);
            Dimensions m2 = new Dimensions(m.stringify());

            System.out.println( m2.getDimension("Hello") );
            System.out.println( m2.getDimension("No", Unit.CENTIMETRES) );
            System.out.println( m.getDimension("Bye", Unit.INCHES) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
