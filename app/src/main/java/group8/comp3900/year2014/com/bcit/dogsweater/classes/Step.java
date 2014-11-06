package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Chris on 2014-10-15.
 */
public class Step {
    private String text;
    private int weight;
    private boolean checked;
    /** The photo associated with the task, taken by the user. */
    private int photoId;

    public Step(String t) {
        text = t;
        weight = 1;
    }

    public Step(String t, int w) {
        text = t;
        weight = w;
    }

    public void setChecked(boolean c) {
        checked = c;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setPhoto(int id) {
        photoId = id;
    }

    public int getPhoto() {
        return photoId;
    }

    public int getWeight() {
        return weight;
    }

    public String getText() {
        return text;
    }
}
