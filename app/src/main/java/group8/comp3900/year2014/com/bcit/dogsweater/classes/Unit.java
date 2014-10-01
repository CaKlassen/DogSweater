package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Eric on 2014-10-01.
 */
public enum Unit {

    // declare enumerations
    CENTIMETRES,
    INCHES;

    /** default unit that all things measured is to be displayed in */
    private static Unit mDefaultUnits;

    public static Unit getDefaultUnit() {
        return mDefaultUnits;
    }

    public static void setDefaultUnit(Unit defaultUnits) {
        mDefaultUnits = defaultUnits;
    }
}
