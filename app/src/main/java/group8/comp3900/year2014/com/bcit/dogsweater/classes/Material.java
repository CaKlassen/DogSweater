package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/**
 * Created by Rhea on 02/10/2014.
 */
public class Material {

    String name = null;
    boolean selected = false;

    public Material(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}