package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.NoSuchElementException;

import group8.comp3900.year2014.com.bcit.dogsweater.AppSettings;

/**
 * Created by Eric on 2014-10-01.
 */
public enum Unit
{


    // -------------------------------------------------------------------------
    // enumeration declarations
    // -------------------------------------------------------------------------
    CENTIMETRES( 1 /* centimetres per centimetre */, "CENTIMETRES", "cm" ),
    INCHES( 0.393701 /* inches per centimetre */, "INCHES", "in" );

    /** amount of this unit is needed to make up 1 inch */
    private final double coefficient;

    /** value returned in the getIdentifier method */
    private final String identifier;

    /**
     * string displayed in the knitting instructions to identify what unit this
     *   is (i.e. "cm" or "in" or "ft")
     */
    private final String unit;

    /** enum constructor, initializes enum members */
    Unit( double coefficient , String identifier, String unit )
    {
        this.coefficient = coefficient;
        this.identifier = identifier;
        this.unit = unit;
    }

    /** converts value of this unit to equivalent value in target unit */
    public double convert( Unit target, double value )
    {
        return target.coefficient * value / coefficient;
    }

    /** returns the identifier of version of the unit */
    public String getIdentifier() {
        return identifier;
    }

    public String getUnitString() {
        return unit;
    }


    // -------------------------------------------------------------------------
    // static members
    // -------------------------------------------------------------------------

    /**
     * returns the unit associated with the passed (identifier) String returned
     *   by Unit.getIdentifier()
     * @param identifier String returned by Unit.getIdentifier().
     * @return a Unit enumeration that corresponds to the passed identifier.
     */
    public static Unit parseIdentifier(String identifier) {
        for (Unit unit : Unit.values()) {
            if (unit.getIdentifier().equals(identifier)) {
                return unit;
            }
        }
        throw new NoSuchElementException("the passed identifier does not mat" +
                "ch any stringified enumerations.");
    }

    /** returns the current default unit */
    public static Unit getDefaultUnit(Context c)
    {
        // read the unit preference from preferences file
        SharedPreferences preferences = c.getSharedPreferences(
                AppSettings.KEY_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String unitIdentifier = preferences.getString(
                AppSettings.KEY_DEFAULT_UNIT, Unit.CENTIMETRES.getIdentifier());
        return Unit.parseIdentifier(unitIdentifier);
    }

    /** sets the default unit, cannot be null */
    public static void setDefaultUnit( Context c, Unit defaultUnits )
    {
        if ( defaultUnits == null )
            throw new NullPointerException( "Parameter defaultUnits cannot be "
                    + "null" );

        // write the new default unit preference to preferences file
        SharedPreferences preferences = c.getSharedPreferences(
                AppSettings.KEY_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String unitIdentifier = defaultUnits.getIdentifier();
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(
                AppSettings.KEY_DEFAULT_UNIT, unitIdentifier);
        preferenceEditor.commit();
    }
}
