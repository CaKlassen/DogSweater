package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Chris on 2014-10-15.
 */
public class Style {
    private String name;
    private int styleNumber;
    private ArrayList<Section> sectionList;

    /**
     * Author: Chris Klassen
     *
     * Constructor for a style object
     */
    public Style(String n, int sn) {
        name = n;
        styleNumber = sn;
    }

    public String getName() {
        return name;
    }

    public int getStyleNumber() {
        return styleNumber;
    }


    public Section getSection(int section) {
        return sectionList.get(section);
    }


    public ArrayList<Section> getSectionList() {
        return sectionList;
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
        String expression = "";
        boolean expressionGo = false;
        int startIndex = -1;
        int endIndex = -1;

        // TODO: dynamically insert variables into step
        for (int i = 0; i < text.length(); i++) {

            // If we hit the end of the insert
            if ( expressionGo && text.charAt(i) == ']') {
                expressionGo = false;
                endIndex = -1;

                // Replace the text with the expression return
                text = text.replace( "[" + expression + "]", d.parseExpression(expression) );

                // Reset the expression string
                expression = "";
            }

            // Add to the expression if we are in an expression
            if ( expressionGo ) {
                expression += text.charAt(i);
            }

            // If we hit a variable insert
            if (text.charAt(i) == '[') {
                expressionGo = true;
                startIndex = i;
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
     * Retrieves the name for a specific style
     *
     * @param id the id to pull the name from
     * @return the name
     */
    public static String getNameFromId(int id) {
        switch(id) {
            case 0: // Style 1
                return "Style 1";
            default:
                return "Invalid Style";
        }
    }

    /**
     * Author: Chris Klassen
     *
     * Calls the makeStyle function of a specific style and returns
     * the associated section list
     *
     * @return a list of sections
     */
    public static ArrayList<Section> makeStyle(int styleNumber) {
        switch(styleNumber) {
            case 0: // Style 1
                return makeStyle_1();
            default: // Incorrect Style
                Log.d("Incorrect Style Number", "Incorrect style number entered.");
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
        sList.add(new Step("Using circular needles, cast on [AA] sts.")); // Example of variable insertion
        sList.add(new Step("Join into round, taking care not to twist the sts."));
        sList.add(new Step("Place marker to indicate the beginning of the rounds."));
        sList.add(new Step("Next Rnd: Beginning with k1, work in 1 x 1 RIB until collar measures " +
                "[-1]."));

        style_1.add(new Section("Collar", sList));

        // Section 2
        sList = new ArrayList<Step>(1);
        sList.add(new Step("Rnd 1: K[CC], place 2nd marker (use a regular marker that is different" +
                " than the end of the round marker)."));
        sList.add(new Step("Continue in 1 x 1 RIB following the stitch pattern in the round below " +
                "until the end of the round."));
        sList.add(new Step("Using a couple of stitch markers that can attach to the actual stitch (" +
                "similar to a safety pin), attach one of these markers to the first stitch after the" +
                "end of the round stitch marker (this stitch will be identified as MarkerA) and the" +
                "last stitch before the 2nd marker (this stitch will be identified as MarkerB)."));
        sList.add(new Step("Rnd 2 (inc): M1, k1 (MarkerA)."));
        sList.add(new Step("M1, k to last stitch before MarkerB."));
        sList.add(new Step("M1, k1 (MarkerB)."));
        sList.add(new Step("M1, mm, RIB to the end of the round."));
        sList.add(new Step("Rnd 3 (inc): Knit to MarkerA."));
        sList.add(new Step("M1, k1 (MarkerA)."));
        sList.add(new Step("M1, knit to MarkerB."));
        sList.add(new Step("M1, k1 (MarkerB)."));
        sList.add(new Step("M1, mm, RIB to end. Note: As the MarkerA and MarkerB are physically " +
                "attached to the actual stitches, while knitting the rounds, transfer the markers" +
                "up to the stitch of the last round."));
        sList.add(new Step("Repeat Rnd 3 for [((BB-AA)/4)-3] more rounds to a total of [BB] sts on " +
                "the needle."));
        sList.add(new Step("Continue in pattern for 2 more rounds. Make sure the MarkerA and MarkerB " +
                "are moved up also."));
        style_1.add(new Section("Chest", sList));

        // Section 3
        sList = new ArrayList<Step>(1);
        sList.add(new Step("This is the first step."));
        sList.add(new Step("This is the second step."));
        sList.add(new Step("This is the third step."));

        style_1.add(new Section("Legs", sList));

        // Return the section list
        return style_1;
    }

    // TODO: Get materials list for a style
}
