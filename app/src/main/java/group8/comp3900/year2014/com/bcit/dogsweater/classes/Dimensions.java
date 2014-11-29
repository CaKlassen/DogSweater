package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import group8.comp3900.year2014.com.bcit.dogsweater.R;

/**
 * author: Eric Tsang
 * date: October 1 2014
 * revision: October 3 2014 - changed the name of this class from
 * "Measurements" to "Dimensions"
 *
 * mStorage JSON schema:
 *
 * {
 *     "dimensionKey1":{
 *         "value":"5",
 *         "unit":"CENTIMETRES",
 *         "defaultValueExpression":""
 *     }
 *     "dimensionKey2":{
 *         "value":"19",
 *         "unit":"INCHES",
 *         "defaultValueExpression":"dimensionKey1 + 5"
 *     }
 * }
 *
 */
public class Dimensions
{
    //////////////////////
    // static variables //
    //////////////////////
    /** evaluator object used to evaluate expressions. */
    private static Evaluator sEvaluator = new Evaluator();


    ////////////////////////
    // instance variables //
    ////////////////////////
    /** underlying object used to save measurement information. */
    private final JSONObject mStorage;

    /** reference to the application context. */
    private final Context mContext;


    /////////////////
    // constructor //
    /////////////////
    /**
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
     *
     * instantiates an instance of Measurements with the exact same
     * measurements, and associated values as the Measurements instance that was
     * stringified.
     *
     * @param stringified a string created with the Measurements.stringify
     *   method
     * @param context reference to the application's context
     */
    public Dimensions(Context context, String stringified)
    {
        if (context == null)
            throw new NullPointerException("context cannot be null");

        mContext = context;
        try
        {
            mStorage = new JSONObject(stringified);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
     *
     * instantiates an instance of Measurements
     *
     * @param context reference to the application's context
     */
    public Dimensions(Context context)
    {
        if (context == null)
            throw new NullPointerException("context cannot be null");

        mContext = context;
        mStorage = new JSONObject();
    }



    /////////////
    // getters //
    /////////////

    /** returns the context that was passed into the instance. */
    public Context getContext()
    {
        return mContext;
    }

    /**
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
     * @param           key   key used to identify the measurement
     * @return          value of the measurement identified by the passed
     *                  identifier ( key ) in the passed units ( unit )
     *
     * returns the value of the dimension identified by the passed key in the
     * passed units ( unit )
     */
    public Dimension getDimension(String key)
    {
        try {
            // get and return the measurement with the key that matches the
            // passed key
            return new Dimension(mContext, mStorage.getJSONObject(key), this);
        }
        catch ( JSONException e )
        {
            // exception thrown, passed key probably doesn't exist
            throw new NoSuchElementException( "There is no measurement "
                    + "associated with the passed key: \"" + key + "\"." );
        }
    }

    /**
     * author: Eric Tsang
     * date: October 7 2014
     * revisions: none
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
        for (int i = 0; i < len; ++i) {
            try {
                keys[i] = names.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // return...
        Arrays.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
        return keys;
    }



    /////////////
    // setters //
    /////////////

    /** @see this.setDimension(String, String, double) */
    public void setDimension(String key, double value)
    {
        setDimension(key, value, null);
    }

    /** @see this.setDimension(String, double, String, Unit) */
    public void setDimension(String key, double value, String defaultValueExpression)
    {
        setDimension(key, value, defaultValueExpression, Unit.getDefaultUnit(mContext));
    }

    /**
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
     * @param           key   key used to identify to the measurement
     * @param           value   value of the measurement in the passed units
     *                          ( unit )
     * @param           defaultValueExpression   expression used to calculate
     *                  the default value for this dimension
     * @param           unit   units that value is passed in
     *
     * sets the value of the dimension identified by the passed key to the
     * passed value in the passed unit
     */
    public void setDimension(String key, double value, String defaultValueExpression, Unit unit)
    {
        // validate parameters
        if (key == null || unit == null)
            throw new NullPointerException("Neither key or unit parameters "
                    + "can be null!");

        // add the measurement to mStorage
        try
        {
            Dimension dimension = new Dimension(
                    mContext,
                    unit,
                    value,
                    defaultValueExpression,
                    this);
            mStorage.put(key, dimension.toJSONObject());
        }
        catch( Exception e )
        {
            throw new RuntimeException("JSONException: " + e.toString());
        }
    }



    /////////////////////////////
    // other interface methods //
    /////////////////////////////
    /**
     * author: Eric Tsang
     * date: October 7 2014
     * revisions: none
     * @param           key   key used to identify to the measurement
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
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
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
     * author: Chris Klassen
     * date: October 17 2014
     * revisions: none
     * @param expression a string passed in from an Archetype
     *                               step that must be calculated and returned.
     * @return  String       the calculated expression result
     *
     * returns the result of an expression passed in containing dimension
     *   variables. the units used in the calculation will be the default unit
     *   for the app.
     */
    public String parseExpression( String expression )
    {
        return parseExpression(expression, Unit.getDefaultUnit(mContext));
    }

    /**
     * author: Chris Klassen
     * date: October 17 2014
     * revisions: none
     * @param expression a string passed in from an Archetype step that must be
     *   calculated and returned.
     * @param expressionBaseUnit the kind of unit to use in the calculation.
     * @return  String       the calculated expression result
     *
     * returns the result of an expression passed in containing dimension
     * variables.
     */
    public String parseExpression( String expression, Unit expressionBaseUnit )
    {
        Log.d(" input: ", expression);
        String result;

        // replace variables with values
        Log.d("expression: ", expression);
        String[] keys = getDimensionKeys();
        for (String key : keys) {
            expression = expression.replaceAll(key,
                    String.valueOf(getDimension(key).getValue(
                            expressionBaseUnit)));
        }

        // evaluate the expression
        Log.d("parses expression: ", expression);
        try {
            Log.d("expression: ", expression);
            result = sEvaluator.evaluate(expression);
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        // return...
        Log.d("output: ", result);
        return result;
    }



    ////////////////////
    // helper methods //
    ////////////////////
    /**
     * author: Rhea Lauzon
     * date: November 21 2014
     * revisions: none
     * @param key key String of the dimension we're getting the
     *                  information name for
     * @param appContext application context...
     * @param suffix suffix appended to passed key, used to get the resource.
     * @return String resource associated with passed key
     *
     * returns the String associated with passed key; null if it is not defined.
     *
     * to define a hint it must be added to the R.string class. to do
     * this, it must be added into strings.xml with the name format:
     * "[key][suffix]".
     */
    private static String getDimensionString(Context appContext, String key, String suffix) {
        String ret;


        try {
            int friendlyId = R.string.class.getField(key + suffix).getInt(null);
            ret = appContext.getResources().getString(friendlyId);

        } catch(Exception e) {
            ret = null;

        }

        return ret;

    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param           key   key String of the dimension we're getting the
     *                  information name for
     * @param           appContext   application context...
     * @return          user friendly dimension name String associated with
     *                  passed key
     *
     * returns a user friendly dimension name String associated with passed key;
     * null if it is not defined.
     *
     * to define a friendly, it must be added to the R.string class. to do
     * this, it must be added into strings.xml with the name format:
     * "[key]Friendly".
     */
    public static String getFriendly(Context appContext, String key) {

        return getDimensionString( appContext, key, "Friendly");
    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param           key   key String of the dimension we're getting the
     *                  information name for
     * @param           appContext   application context...
     * @return          user friendly description String associated with passed
     *                  key
     *
     * returns a description dimension String associated with passed key; null
     * if it is not defined.
     *
     * to define a description, it must be added to the R.string class. to do
     * this, it must be added into strings.xml with the name format:
     * "[key]Description".
     */
    public static String getDescription(Context appContext, String key) {

        return getDimensionString( appContext, key, "Description");

    }

    /**
     * author: Rhea Lauzon
     * date: November 21 2014
     * revisions: none
     * @param           key   key String of the dimension we're getting the
     *                  information name for
     * @param           appContext   application context...
     * @return          user friendly hint String associated with passed
     *                  key
     *
     * returns a hint String associated with passed key; null
     * if it is not defined.
     *
     * to define a hint it must be added to the R.string class. to do
     * this, it must be added into strings.xml with the name format:
     * "[key]Description".
     */
    public static String getHint(Context appContext, String key) {

        return getDimensionString( appContext, key, "Hint");

    }

    /**
     * author: Eric Tsang
     * date: October 18 2014
     * revisions: none
     * @param           key   key String of the dimension we're getting the
     *                  information name for
     * @param           appContext   application context...
     * @return          drawable associated with this dimension; a picture of a
     *                  dog, showing what the dimension is a measurement of
     *
     * returns a drawable associated with passed key; null if it is not defined.
     *
     * to define a drawable, it must be added to the R.drawable class. to do
     * this, the image must be added into the drawable folder with the name
     * format: "[key]Drawable".
     */
    public static Drawable getDrawable(Context appContext, String key) {
        Drawable ret;

        try {
            int descriptionId = R.drawable.class.getField(
                    key.toLowerCase() + "_drawable").getInt(null);
            ret = appContext.getResources().getDrawable(descriptionId);

        } catch(Exception e) {
            ret = null;

        }

        return ret;
    }



    ///////////////
    // unit test //
    ///////////////
    /**
     * author: Eric Tsang
     * date: October 1 2014
     * revisions: none
     * @param           args   unused command line arguments
     *
     * demonstrates how to use the measurements class
     */
    public static void main( String[] args )
    {
        Dimensions m = new Dimensions(null);

        try
        {
            m.setDimension("Hello", 11, null);
            m.setDimension("No", 12, null);
            m.setDimension("Bye", 13, null);
            m.setDimension("DefaultValue", 13, "Hello + No");
            System.out.println(m.parseExpression("4 - 5"));
            System.out.println(m.parseExpression("floor(Hello - Bye + No + No + 0.5"));
            Dimensions m2 = new Dimensions(null, m.stringify());

            System.out.println( m2.getDimension("Hello").toJSONObject().toString() );
            System.out.println( m2.getDimension("DefaultValue").getDefaultValue() );
            System.out.println( m2.getDimension("No").getValue(Unit.CENTIMETRES) );
            System.out.println( m.getDimension("Bye").getValue(Unit.INCHES) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
