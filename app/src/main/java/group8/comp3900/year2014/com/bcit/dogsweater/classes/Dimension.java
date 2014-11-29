package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Eric on 2014-11-05.
 */
public class Dimension {


    /////////////////////
    // class constants //
    /////////////////////

    /**
     * key used to access the mUnit used to save the dimension of a Dimension in
     * JSONObject form.
     */
    public static final String KEY_UNIT = "KEY_UNIT";

    /**
     * key used to access the value attribute of a Dimension in JSONObject form.
     */
    public static final String KEY_VALUE = "KEY_VALUE";

    /**
     * key used to access the mDefaultValueExpression attribute of a Dimension in
     * JSONObject form.
     */
    public static final String KEY_DEFAULT_VALUE_EXPRESSION =
            "KEY_DEFAULT_VALUE_EXPRESSIONS";


    ////////////////////////
    // instance variables //
    ////////////////////////

    /** mUnit used when the value was saved into this Dimension instance. */
    private final Unit mUnit;

    /** value of this Dimension instance in the specified units. */
    private final double mValue;

    /**
     * expression used to calculate the default value for this Dimension
     * instance. May contain keys of other Dimension objects, but the expression
     * must be evaluated by a Dimensions instance.
     */
    private final String mDefaultValueExpression;

    /** Dimensions object that holds this dimension. */
    private final Dimensions mDimensions;

    /** reference to the application context. */
    private final Context mContext;


    /////////////////
    // constructor //
    /////////////////

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * instantiates an Dimension instance, and initializes its instance
     * variables from the constructor parameters.
     */
    public Dimension(Context context, Unit unit, double value, String defaultValueExpression, Dimensions dimensions) {

        // validate arguments
        if (dimensions == null) {
            throw new IllegalArgumentException("parameter: \"dimensions\" cannot be null");
        }

        // pre process arguments
        if (unit == null) {
            unit = Unit.getDefaultUnit(context);
        }
        if (defaultValueExpression == null) {
            defaultValueExpression = "";
        }

        // initialize instance variables from constructor parameters
        mUnit = unit;
        mValue = value;
        mDefaultValueExpression = defaultValueExpression;
        mDimensions = dimensions;
        mContext = context;
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * instantiates an Dimension instance, and initializes its instance
     * variables from the constructor parameters.
     */
    public Dimension(Context context, JSONObject o, Dimensions dimensions) {
        try {
            mDimensions = dimensions;
            mUnit = Unit.parseIdentifier(o.getString(KEY_UNIT));
            mValue = o.getDouble(KEY_VALUE);
            mDefaultValueExpression = o.has(KEY_DEFAULT_VALUE_EXPRESSION) ?
                    o.getString(KEY_DEFAULT_VALUE_EXPRESSION) : "";
            mContext = context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    ///////////////
    // interface //
    ///////////////

    /** @see this.getValue(Unit) */
    public double getValue() {
        return getValue(Unit.getDefaultUnit(mContext));
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * returns the value associated with this Dimension instance
     *
     * @return value associated with this Dimension instance
     */
    public double getValue(Unit targetUnit) {
        return mUnit.convert(targetUnit, mValue);
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * returns the expression in the form of a String that's associated with
     *   this Dimension instance
     *
     * @return the expression in the form of a String that's associated with
     *   this Dimension instance
     *
     */
    public String getDefaultValueExpression() {
        return mDefaultValueExpression;
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     *
     * returns the result of the default value expression that's associated with
     *   this Dimension instance in the form of a String
     *
     * @return result of the default value expression that's associated with
     *   this Dimension instance in the form of a String
     *
     */
    public String getDefaultValue() {
        return mDimensions.parseExpression(getDefaultValueExpression());
    }

    /**
     * author: Eric Tsang
     * date: November 5 2014
     * revisions: none
     * @return a JSONObject that contains the attributes and corresponding
     * values of this Dimensions instance. (excluding the "key" attribute).
     *
     * returns a JSONObject that contains the attributes and corresponding
     * values of this Dimensions instance. (excluding the "key" attribute).
     */
    public JSONObject toJSONObject() {
        try {
            JSONObject o = new JSONObject();
            o.put(KEY_UNIT, mUnit.getIdentifier());
            o.put(KEY_VALUE, mValue);
            o.put(KEY_DEFAULT_VALUE_EXPRESSION, mDefaultValueExpression);
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
