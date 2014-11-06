package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Eric Tsang on 07/10/14.
 */
public class Yarn {

    /** contains all the dimensions about this instance */
    private final Dimensions mDimensions;

    ////////////////////
    // dimension keys //
    ////////////////////
    // these keys are used to save things in an instances mDimension object
    /** canonical name of the class (i.e.: group8.comp3900...classes.Yarn) */
    private static final String CANONICAL_NAME = Yarn.class.getCanonicalName();

    /** key for the weight (thickness) of the yarn instance */
    public static final String KEY_WEIGHT = CANONICAL_NAME + ".KEY_WEIGHT";




    //////////////////
    // constructors //
    //////////////////
    public Yarn() {
        mDimensions = new Dimensions();
    }

    ///////////////////////
    // interface methods //
    ///////////////////////
    public Dimensions getDimensions() {
        return mDimensions;
    }
}
