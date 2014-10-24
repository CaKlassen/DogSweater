package group8.comp3900.year2014.com.bcit.dogsweater.classes;

import java.util.ArrayList;

/**
 * Created by Chris on 2014-10-15.
 */
public class Section {
    private String name;
    private ArrayList<Step> stepList;

    public Section(String n) {
        name = n;
    }

    public Section(String n, ArrayList<Step> a) {
        name = n;
        stepList = a;
    }

    public String getName() {
        return name;
    }

    public Step getStep(int step) {
        return stepList.get(step);
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }

    public void initializeStepList(ArrayList<Step> a) {
        stepList = a;
    }
}
