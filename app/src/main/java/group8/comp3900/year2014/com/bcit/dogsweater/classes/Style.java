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
        boolean expFound = true;

        while ( expFound ) {
            expFound = false;

            for (int i = 0; i < text.length(); i++) {

                // If we hit the end of the insert
                if (expressionGo && text.charAt(i) == ']') {
                    expressionGo = false;

                    // Replace the text with the expression return
                    double expValue = Double.parseDouble( d.parseExpression( expression ) );
                    String replaceString;

                    if ( expValue == Math.floor( expValue ) ) {
                        // If this is an integer
                        replaceString = "" + (int) expValue;
                    } else {
                        replaceString = "" + expValue;
                    }

                    text = text.replace("[" + expression + "]", replaceString);

                    // Reset the expression string
                    expression = "";

                    expFound = true;
                    break;
                }

                // Add to the expression if we are in an expression
                if (expressionGo) {
                    expression += text.charAt(i);
                }

                // If we hit a variable insert
                if (text.charAt(i) == '[') {
                    expressionGo = true;
                    startIndex = i;
                }

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
        sList.add(new Step("Using circular needles, cast on [AA] sts. Join into round, taking care " +
                "not to twist the sts. Place marker to indicate the beginning of the rounds. " +
                "Note: use a distinctive maker to later differentiate from other stitch makers " +
                "that will be used later."));
        sList.add(new Step("Next Rnd: Beginning with k1, work in 1x1 RIB until collar measures " +
                "[X]."));

        style_1.add(new Section("Collar", sList));

        // Section 2
        sList = new ArrayList<Step>(1);
        sList.add(new Step("Rnd 1: K[CC], place 2nd marker (use a regular marker that is different" +
                " than the end of the round marker), continue in 1x1 RIB following the stitch " +
                "pattern in the round below until the end of the round."));
        sList.add(new Step("Using a couple of stitch markers that can attach to the actual stitch (" +
                "similar to a safety pin), attach one of these markers to the first stitch after the " +
                "end of the round stitch marker (this stitch will be identified as MarkerA) and the " +
                "last stitch before the 2nd marker (this stitch will be identified as MarkerB)."));
        sList.add(new Step("Rnd 2 (inc): M1, k1 (MarkerA), M1, k to last stitch before MarkerB, " +
                "M1, k1 (MarkerB), M1, mm, RIB to the end of the round."));
        sList.add(new Step("Rnd 3 (inc): Knit to MarkerA, M1, k1 (MarkerA), M1, knit to MarkerB, " +
                "M1, k1 (MarkerB), M1, mm, RIB to end."));
        sList.add(new Step(" Note: As the MarkerA and MarkerB are physically " +
                "attached to the actual stitches, while knitting the rounds, transfer the markers" +
                "up to the stitch of the last round."));
        sList.add(new Step("Repeat Rnd 3 for [((BB-AA)/4)-3] more rounds to a total of [BB] sts on " +
                "the needle."));
        sList.add(new Step("Continue in pattern for 2 more rounds. Make sure the MarkerA and MarkerB " +
                "are moved up also."));

        style_1.add(new Section("Chest", sList));

        // Section 3
        sList = new ArrayList<Step>(1);
        sList.add(new Step("Rnd 1: Knit to MarkerA, cast off MarkerA and [(BB-AA)/4] sts, k[CC-2], " +
                "cast off [(BB-AA)/4] sts and MarkerB, knit to marker, mm, RIB to the end of the " +
                "round marker."));
        sList.add(new Step("Then, continue in the same direction, knit to the end of the row. Turn. " +
                "[BB-((BB-AA)/2)-2] sts total, [BB-(CC-2)-(BB-AA)/2-2] sts for top section."));
        sList.add(new Step("Place remaining [CC-2] chest stitches onto a stitch holder."));
        sList.add(new Step("Slip st at the beginning of the next row and all subsequent rows. Purl " +
                "the next row. Be sure to move the markers as they occur."));
        sList.add(new Step("Continue in stocking stitch for a total of [(BB-AA)/2+1] rows ending on " +
                "the wrong side."));
        sList.add(new Step("Place the [BB-(CC-2)-(BB-AA)/2-2] stitches onto a stitch holder."));
        sList.add(new Step("Begin working the [CC-2] sts on the chest (between the legs) portion. " +
                "From the wrong side, join yarn, begin with a purl row. Slip st at the beginning of " +
                "the next row and all subsequent rows."));
        sList.add(new Step("Continue in stocking stitch for a total of [(BB-AA)/2+1] rows ending on " +
                "the wrong side. Turn."));
        sList.add(new Step("Once the knitting is done for both sections, join them back onto the circular needles. " +
                "Do this by knitting [CC-2] sts from the chest area. Join to other section by " +
                "knitting [(BB-AA)/4] to marker, mm, knit [BB-(CC-2)-(BB-AA)-2] sts to next marker. " +
                "(this is the beginning of the round marker)."));

        style_1.add(new Section("Leg Openings", sList));

        // Section 4
        sList = new ArrayList<Step>(1);
        sList.add(new Step("This section is worked in rounds.\nRnd 1: Begin 1x1 RIB [(CC-2)+(BB-AA)/2]" +
                " sts from beginning of round marker to the 2nd marker. Make sure the chest area is " +
                "joined in the round. Knit the remaining [BB-(CC-2)-(BB-AA)-2] sts."));
        sList.add(new Step("Rnd 2: RIB, mm, knit."));
        sList.add(new Step("Continue in this pattern until sweater measures [X+Y] from the top."));
        sList.add(new Step("Cast off [(CC-2)+(BB-AA)/2] sts to the 2nd marker. Turn. [BB-(CC-2)-(BB-AA)-2] sts."));

        style_1.add(new Section("Mid-Section", sList));


        // Section 5
        sList = new ArrayList<Step>(1);
        sList.add(new Step("Working the remaining [BB-(CC-2)-(BB-AA)-2] sts back and forth, 1x1 RIB until sweater " +
                "measures [X+Z] from the top."));
        sList.add(new Step("Cast off and darn ends."));

        style_1.add(new Section("Bottom \"Tail\" Section", sList));

        // Return the section list
        return style_1;
    }

    // TODO: Get materials list for a style
}
