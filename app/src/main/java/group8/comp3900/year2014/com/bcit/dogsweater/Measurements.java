package group8.comp3900.year2014.com.bcit.dogsweater;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by Eric on 2014-10-01.
 */
public class Measurements {

    // declare class variables
    public static enum Unit {
        CENTIMETRES,
        INCHES,
        MILLI
    };

    // declare instance variables
    private JSONObject mStorage = new JSONObject();

    public void setDimen(String key, Dimen value) {
    }

    public Dimen getDimen(String key) {
        return null;
    }

    public class Dimen {
        public String toString() {
            return null;
        }
    }

}
