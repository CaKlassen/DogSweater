package group8.comp3900.year2014.com.bcit.dogsweater.classes;

/****************************************************
 * Class to represent each style on the style selection
 * grid. This allows the Style class and its
 * basic information to be seperation so that the calculations
 * do not need to be mashed with the basic information.
 * Created by Rhea on 12/11/2014.
 ****************************************************/
public class StyleInfo {

    /** Style name **/
    private String name;

    /** Description of the style  **/
    private String description;

    /** Id to the style's image to be displayed  **/
    private int imageId;

    public StyleInfo(String n, String d, int i)
    {
        name = n;
        description = d;
        imageId = i;
    }


    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getImageUri() { return imageId; }
}
