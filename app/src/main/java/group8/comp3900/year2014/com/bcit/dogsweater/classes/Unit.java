package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Eric on 2014-10-01.
 */
public enum Unit
{




    // -------------------------------------------------------------------------
    // enumeration declarations
    // -------------------------------------------------------------------------
    CENTIMETRES( 2.54 /* centimetres per inch */ ),
    INCHES( 1 /* inches per inch */ );

    /** amount of this unit is needed to make up 1 inch */
    private final double coefficient;

    /** enum constructor, initializes enum members */
    Unit( double coefficient )
    {
        this.coefficient = coefficient;
    }

    /** converts value of this unit to equivalent value in target unit */
    public double convert( Unit target, double value )
    {
        return target.coefficient * value / coefficient;
    }




    // -------------------------------------------------------------------------
    // static members
    // -------------------------------------------------------------------------
    /** default unit that all things measured is to be displayed in */
    private static Unit defaultUnits = INCHES;

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
