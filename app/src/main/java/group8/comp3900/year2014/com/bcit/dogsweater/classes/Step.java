package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Chris on 2014-10-15.
 */
public class Step {
    private String text;
    private int weight;

    public Step(String t, int w) {
        text = t;
        weight = w;
    }

    public int getWeight() {
        return weight;
    }

    public String getText() {
        return text;
    }
}
