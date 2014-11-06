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
    CENTIMETRES( 1 /* centimetres per centimetre */, "CENTIMETRES" ),
    INCHES( 0.393701 /* inches per centimetre */, "INCHES" );

    /** amount of this unit is needed to make up 1 inch */
    private final double coefficient;

    /** value returned in the toString method */
    private final String string;

    /** enum constructor, initializes enum members */
    Unit( double coefficient , String string )
    {
        this.coefficient = coefficient;
        this.string = string;
    }

    /** converts value of this unit to equivalent value in target unit */
    public double convert( Unit target, double value )
    {
        return target.coefficient * value / coefficient;
    }

    /** returns the string of version of the unit */
    public String stringify() {
        return string;
    }




    // -------------------------------------------------------------------------
    // static members
    // -------------------------------------------------------------------------
    /** default unit that all things measured is to be displayed in */
    private static Unit defaultUnits = CENTIMETRES;

    /** returns the unit associated with the passed String (stringified) */
    public static Unit parseStringified(String stringified) {
        for (Unit unit : Unit.values()) {
            if (unit.stringify().equals(stringified)) {
                return unit;
            }
        }
        throw new NoSuchElementException("the passed string does not match " +
                "any stringified enumerations.");
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
