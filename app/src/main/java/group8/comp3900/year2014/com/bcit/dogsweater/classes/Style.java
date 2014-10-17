package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.ArrayList;

/**
 * Created by Chris on 2014-10-15.
 */
public class Style {
    private String name;
    private ArrayList<Section> sectionList;

    /** String keys for the different variables to be inserted. */
    public static final String D_NECK = "[A]";
    public static final String D_CHEST = "[B]";
    public static final String L_NECK = "[C]";
    public static final String L_UNDERBELLY = "[D]";
    public static final String L_CENTRE_BACK = "[E]";

    public static final String STS_NECK = "[AA]";
    public static final String STS_CHEST = "[BB]";
    public static final String STS_CENTRE_BACK = "[FF]";
    public static final String STS_CHEST_AREA = "[GG]";
    public static final String STS_NECK_TO_CHEST_ = "[HH]";
    public static final String STS_FIRST_LEGHOLE = "[II]";
    public static final String STS_STOMACH = "[JJ]";
    public static final String STS_BACK_FLAP = "[KK]";

    /**
     * Author: Chris Klassen
     *
     * Constructor for a style object
     */
    public Style(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }


    public Section getSection(int section) {
        return sectionList.get(section);
    }

    /**
     * Author: Chris Klassen
     *
     * Uses a dimensions object to insert correct values
     * into a step containing variables.
     *
     * @param section the requested section
     * @param step the requested step
     * @param d the Dimensions object passed in
     * @return the modified step string
     */
    public String getStep(int section, int step, Dimensions d) {
        String text = sectionList.get(section).getStep(step).getText();

        // TODO: dynamically insert variables into step
        for (int i = 0; i < text.length(); i++) {
            // If we hit a variable insert
            if (text.charAt(i) == '[') {
                // Replace the variable with the proper variable
            }
        }

        return text;
    }

    /**
     * Author: Chris Klassen
     *
     * Links a specific style section list to the object
     */
    public void initializeSectionList(ArrayList<Section> a) {
        sectionList = a;
    }

    /**
     * Author: Chris Klassen
     *
     * Calls the makeStyle function of a specific style and returns
     * the associated section list
     *
     * @return a list of sections
     */
    public static ArrayList<Section> makeStyle(int style) {
        switch(style) {
            case 0: // Style 1
                return makeStyle_1();
            default: // Incorrect style entered
                return null;
        }
    }

    /**
     * Author: Chris Klassen
     *
     * Creates and returns a reference to a fully formed list of
     * steps divided into sections.
     *
     * @return a list of sections
     */
    public static ArrayList<Section> makeStyle_1() {
        ArrayList<Section> style_1 = new ArrayList<Section>(1);
        ArrayList<Step> sList;

        // Section 1
        sList = new ArrayList<Step>(1);
        sList.add(new Step("This is the first step."));
        sList.add(new Step("This is the second step."));
        sList.add(new Step("This is the third step."));

        style_1.add(new Section("Cowl", sList));

        // Section 2
        sList = new ArrayList<Step>(1);
        sList.add(new Step("This is the first step."));
        sList.add(new Step("This is the second step."));
        sList.add(new Step("This is the third step."));

        style_1.add(new Section("Chest", sList));

        // Return the section list
        return style_1;
    }

    // TODO: Get materials list for a style
}
