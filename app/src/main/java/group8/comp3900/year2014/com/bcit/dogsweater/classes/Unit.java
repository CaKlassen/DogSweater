package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.NoSuchElementException;

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
    /** default unit that all things measured is to be displayed in */
    private static Unit defaultUnits = CENTIMETRES;

    /** returns the unit associated with the passed String (stringified) */
    public static Unit parseStringified(String stringified) {
        for (Unit unit : Unit.values()) {
            if (unit.getIdentifier().equals(stringified)) {
                return unit;
            }
        }
        throw new NoSuchElementException("the passed identifier does not mat" +
                "ch any stringified enumerations.");
    }

    /** returns the current default unit */
    public static Unit getDefaultUnit()
    {
        return defaultUnits;
    }

    /** sets the default unit, cannot be null */
    public static void setDefaultUnit( Unit defaultUnits )
    {
        if ( defaultUnits == null )
            throw new NullPointerException( "Parameter defaultUnits cannot be "
                    + "null!" );
        Unit.defaultUnits = defaultUnits;
    }
}
